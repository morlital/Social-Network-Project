//
// Created by gleity@wincs.cs.bgu.ac.il on 1/1/19.
//

#include <string>
#include <vector>

#ifndef CLIENT_ENCODERDECODER_H
#define CLIENT_ENCODERDECODER_H

#endif //CLIENT_ENCODERDECODER_H

using byte = unsigned char;

class EncoderDecoder {
private:
    int len = 0;
    std::vector<byte> msg;
    void shortToBytes(short num, char* bytesArr);
    short bytesToShort(char* bytesArr);

public:
    EncoderDecoder ();
    std::vector<byte> encode (std::string message);
  //  void pushByte ()
};