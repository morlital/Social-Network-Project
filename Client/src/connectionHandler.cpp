#include "../include/connectionHandler.h"
#include <iostream>

using boost::asio::ip::tcp;

using std::cin;
using std::cout;
using std::cerr;
using std::endl;
using std::string;
 
ConnectionHandler::ConnectionHandler(string host, short port): host_(host), port_(port), io_service_(), socket_(io_service_){}
    
ConnectionHandler::~ConnectionHandler() {
    close();
}
 
bool ConnectionHandler::connect() {
    std::cout << "Starting connect to " 
        << host_ << ":" << port_ << std::endl;
    try {
		tcp::endpoint endpoint(boost::asio::ip::address::from_string(host_), port_); // the server endpoint
		boost::system::error_code error;
		socket_.connect(endpoint, error);
		if (error)
			throw boost::system::system_error(error);
    }
    catch (std::exception& e) {
        std::cerr << "Connection failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}
 
bool ConnectionHandler::getBytes(char bytes[], unsigned int bytesToRead) {
    size_t tmp = 0;
	boost::system::error_code error;
    try {
        while (!error && bytesToRead > tmp ) {
			tmp += socket_.read_some(boost::asio::buffer(bytes+tmp, bytesToRead-tmp), error);			
        }
		if(error)
			throw boost::system::system_error(error);
    } catch (std::exception& e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}

bool ConnectionHandler::sendBytes(const char bytes[], int bytesToWrite) {
    int tmp = 0;
	boost::system::error_code error;

    try {
        while (!error && bytesToWrite > tmp ) {
            tmp += socket_.write_some(boost::asio::buffer(bytes + tmp, bytesToWrite - tmp), error);
        }
		if(error)
			throw boost::system::system_error(error);
    } catch (std::exception& e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}
 
bool ConnectionHandler::getLine(std::string& line) {
    return getFrameAscii(line, '\n');
}

bool ConnectionHandler::sendLine(std::string& line) {
    return sendFrameAscii(line, '\0');
}
 
bool ConnectionHandler::getFrameAscii(std::string& frame, char delimiter) {
    char ch;
    std::vector<char> bytes;
    // Stop when we encounter the null character. 
    // Notice that the null character is not appended to the frame string.
    try {
		do{
            getBytes(&ch, 1);
            bytes.push_back(ch);

        }while (delimiter != ch || bytes.size()==2);
		char op[] ={bytes[0],bytes[1]};
		short opCode = bytesToShort(op);
		if (opCode == 9) {
            frame.append("NOTIFICATION ");
            int zeroIndex = 1;
            while ((signed)bytes[zeroIndex]!=0){
                zeroIndex++;
            }
            if (bytes[2] == 1){
                frame.append("PUBLIC ");
                for (int i = zeroIndex+1; i < (signed)bytes.size()-1; ++i) {
                    if (bytes[i]=='\0') {
                        frame.append(" ");
                    } else{
                        frame.append({bytes[i]});
                    }
                }
            }
            else{
                frame.append("PM ");
                for (int i = 3; i < (signed)bytes.size()-1; ++i) {
                    if (bytes[i]=='\0') {
                        frame.append(" ");
                    } else{
                        frame.append({bytes[i]});
                    }
                }
            }


        }
        else if (opCode == 10) {
            frame.append("ACK ");
            op[0] = bytes[2];
            op[1] = bytes[3];
            opCode = bytesToShort(op);
            frame.append(std::to_string(opCode));
            if (opCode == 8){
                for (int i = 5; i < (signed)bytes.size(); i=i+2) {
                    frame.append(" ");
                    op[0] = bytes[i-1];
                    op[1] = bytes[i];
                    opCode = bytesToShort(op);
                    frame .append( std::to_string(opCode));
                }
            }
            if (opCode == 4 || opCode == 7){
                frame.append(" ");
                op[0] = bytes[4];
                op[1] = bytes[5];
                frame.append(std::to_string(bytesToShort(op)));
                frame.append(" ");
                for (int i = 6; i < (signed)bytes.size()-1; ++i) {
                    if (bytes[i]!=0) {

                        frame.append({bytes[i]});
                    }
                    else{
                        frame.append(" ");
                    }
                }
            }
        }
        else {
            frame.append( "ERROR ");
            op[0] = bytes[2];
            op[1] = bytes[3];
            opCode =bytesToShort(op);
            frame= frame + std::to_string((int)opCode);

        }
    } catch (std::exception& e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}
 
bool ConnectionHandler::sendFrameAscii(const std::string& frame, char delimiter) {
    EncoderDecoder endec;
    std::vector<byte> encodedMsg = endec.encode(frame);
    char arrMsg[encodedMsg.size()];
    std::copy(encodedMsg.begin(),encodedMsg.end(),arrMsg);
    int vSize = encodedMsg.size();
	bool result=sendBytes(arrMsg,vSize);
	if(!result) return false;
	return sendBytes(&delimiter,1);
}
 
// Close down the connection properly.
void ConnectionHandler::close() {
    try{
        socket_.close();
    } catch (...) {
        std::cout << "closing failed: connection already closed" << std::endl;
    }
}

short ConnectionHandler::bytesToShort(char* bytesArr)
{
    short result = (short)((bytesArr[0] & 0xff) << 8);
    result += (short)(bytesArr[1] & 0xff);
    return result;
}
