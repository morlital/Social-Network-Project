//
// Created by gleity@wincs.cs.bgu.ac.il on 1/1/19.
//

#include <iostream>
#include <iterator>
#include <sstream>
#include "../include/EncoderDecoder.h"

EncoderDecoder::EncoderDecoder():msg() {}

std::vector<byte> EncoderDecoder::encode(std::string message) {
    std::istringstream iss(message);
    std::vector<std::string> messageVector((std::istream_iterator<std::string>(iss)),
                                     std::istream_iterator<std::string>());
    msg.push_back(0);
    if (messageVector[0]=="REGISTER") {
        msg.push_back(1);
        message.erase(0, 9);
        for (int i = 0; i < (signed)message.length(); ++i) {
            if (message[i] != ' ')
                msg.push_back(static_cast<byte>(message[i]));
            else
                msg.push_back(0);
        }
    } else if (messageVector[0]=="LOGIN") {
        msg.push_back(2);
        message.erase(0, 6);
        for (int i = 0; i < (signed)message.length(); ++i) {
            if (message[i] != ' ')
                msg.push_back(static_cast<byte>(message[i]));
            else
                msg.push_back(0);
        }
    } else if (messageVector[0]=="LOGOUT") {
        msg.push_back(3);
    } else if (messageVector[0]=="FOLLOW") {
        msg.push_back(4);
        message.erase(0, 7);
        if (messageVector[1]=="0")
            msg.push_back(0);
        else
            msg.push_back(1);
        message.erase(0,2);
        int numOfUsers = std::stoi(messageVector[2]);
        char byteArr[2] = {'0','0'};
        shortToBytes((short)numOfUsers,byteArr);
        msg.push_back(byteArr[0]);
        msg.push_back(byteArr[1]);
        message.erase(0,2);
        std::cout<<byteArr[0]<<byteArr[1]<<std::endl;
        for (int i = 0; i < (signed)message.length(); ++i) {
            if (message[i] != ' ') {
                msg.push_back(static_cast<byte>(message[i]));
            }
            else
                msg.push_back(0);
        }
    } else if (messageVector[0]=="POST") {
        msg.push_back(5);
        message.erase(0, 5);
        for (int i = 0; i < (signed)message.length(); ++i)
            msg.push_back(static_cast<byte>(message[i]));
    } else if (messageVector[0]=="PM") {
        msg.push_back(6);
        message.erase(0, 3);
        bool first = false;
        for (int i = 0; i < (signed)message.length(); ++i) {
            if (!first) {
                if (message[i] != ' ')
                    msg.push_back(static_cast<byte>(message[i]));
                else {
                    msg.push_back(0);
                    first = true;
                }
            } else
                msg.push_back(static_cast<byte>(message[i]));
        }
    } else if (messageVector[0]=="USERLIST") {
        msg.push_back(7);
    } else if (messageVector[0]=="STAT") {
        msg.push_back(8);
        message.erase(0, 5);
        for (int i = 0; i < (signed)message.length(); ++i) {
            msg.push_back(static_cast<byte>(message[i]));
        }
    }
    return msg;
}

void EncoderDecoder::shortToBytes(short num, char *bytesArr) {
    bytesArr[0] = ((num >> 8) & 0xFF);
    bytesArr[1] = (num & 0xFF);
}

