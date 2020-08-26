#include <stdlib.h>
#include <boost/locale.hpp>
#include <iostream>
#include <thread>
#include "../include/connectionHandler.h"


/**
* This code assumes that the server replies the exact text the client sent it (as opposed to the practical session example)
*/
int readerFunc (ConnectionHandler* connectionHandler){
    while (1){
        std::string answer;
        bool ans = connectionHandler->getLine(answer);
        if (!ans) {
            break;
        }
        std::cout << answer << std::endl;
        if (answer == "ACK 3") {
            break;
        }
    }
    std::cout << "Ready to exit" << std::endl;
    return 0;
}

int clientInput (ConnectionHandler* connectionHandler) {
    while (1){
        const short bufsize = 1024;
        char buf[bufsize];
        std::cin.getline(buf, bufsize);
        std::string line (buf);
        int len = line.length();
        bool ans = connectionHandler->sendLine(line);
        if ((!ans) || (len == 0))
            break;

    }
    return 0;
}

int main (int argc, char *argv[]) {
    if (argc < 3) {
        std::cerr << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
        return -1;
    }
    std::string host = argv[1];
    short port = atoi(argv[2]);
    
    ConnectionHandler connectionHandler(host, port);
    if (!connectionHandler.connect()) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }
    std::thread readerThread(readerFunc, &connectionHandler);
    std::thread writerThread(clientInput, &connectionHandler);
    readerThread.join();
    writerThread.join();

    return 0;
}
