package bgu.spl.net.api;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public class MessageEncoderDecoderImpl implements MessageEncoderDecoder<String> {

    private byte[] bytes = new byte[1 << 10]; //start with 1k
    private int len = 0, byteCounter = 0, zeroCounter = 0, opCode, zeroCeiling;
    private boolean b=true;
    private int numOfFollowers = 0;


    @Override
    public String decodeNextByte(byte nextByte) {
        byteCounter++;
        //notice that the top 128 ascii characters have the same representation as their utf-8 counterparts
        //this allow us to do the following comparison
        if (byteCounter == 2) {
            opCode = nextByte;
            b = nextByte != (byte) 3 & nextByte != (byte) 5 & nextByte != (byte) 7 & nextByte != (byte) 8;
        }
        if (nextByte == '\0')
            zeroCounter++;
        //Messages with above opCodes have 0 zero in them
        if (!b) {
            if (zeroCounter == 2) {
                byteCounter = 0;
                zeroCounter = 0;
                return popString();
            }
        } else {
            if (opCode == 4) {//handling Follow message
                if (byteCounter == 4) {
                    if (nextByte != 0)
                        zeroCounter++;
                    numOfFollowers = nextByte * 10;
                }
                if (byteCounter == 3) {
                    if (nextByte == 1)
                        zeroCounter++;
                }
                if (byteCounter == 5) {
                    numOfFollowers = numOfFollowers + nextByte;
                    zeroCeiling = numOfFollowers + 3;
                }
            }
            // rest of opCodes have 3 zeros
            if (zeroCounter == 3 && opCode !=4) {
                byteCounter = 0;
                zeroCounter = 0;
                return popString();
            }
            // follow message reached the last zero
            if (zeroCeiling == zeroCounter && zeroCeiling>0){
                byteCounter = 0;
                zeroCounter = 0;
                numOfFollowers = 0;
                return popString();
            }
        }
        pushByte(nextByte);
        return null; //not a line yet
    }

    @Override
    public byte[] encode(String message) {
        ArrayList<Byte> outputEn = new ArrayList<>();

        if (message.contains("ERROR")){
            byte[] op =shortToBytes((short)11);
            addBytes(outputEn,op);
            message = message.substring(6);
            op = shortToBytes(Short.parseShort(message));
            addBytes(outputEn,op);
            outputEn.add((byte)'\n');
            return convertToArray(outputEn); //uses utf8 by default
        }
        else if (message.contains("ACK")){
            byte[] op =shortToBytes((short)10);
            addBytes(outputEn,op);
            message = message.substring(4);
            op = shortToBytes(Short.parseShort(message.substring(0,1)));
            addBytes(outputEn,op);
            if (Short.parseShort(message.substring(0,1))==8){
                message = message.substring(2);
                String[] temp= message.split(" ");
                op = shortToBytes(Short.parseShort(temp[0]));
                addBytes(outputEn,op);
                op = shortToBytes(Short.parseShort(temp[1]));
                addBytes(outputEn,op);
                op = shortToBytes(Short.parseShort(temp[2]));
                addBytes(outputEn,op);
            }
            if (Short.parseShort(message.substring(0,1))==4 || Short.parseShort(message.substring(0,1))==7){
                message = message.substring(2);
                short numOfUsers = Short.parseShort(message.substring(0,1));
                op = shortToBytes(numOfUsers);
                addBytes(outputEn,op);
                message = message.substring(2);
                for (int i = 0; i < message.length(); i++) {
                    if (message.charAt(i)==' ') {
                        outputEn.add((byte) '\0');
                    }
                    else {
                        outputEn.add((byte) message.charAt(i));
                    }
                }
            }
            outputEn.add((byte)'\n');
            return convertToArray(outputEn);
        }
        else {
            byte[] op =shortToBytes((short)9);
            addBytes(outputEn,op);
           message = message.substring(13);
            outputEn.add((byte)Short.parseShort(message.substring(0,1)));
           message = message.substring(2);
           String[] temp = message.split(" ");
                addBytes(outputEn,temp[0].getBytes());
            outputEn.add((byte)0);
            for (int i = 1; i < temp.length; i++) {
                addBytes(outputEn,temp[i].getBytes());
                outputEn.add((byte)' ');
            }
            outputEn.remove(outputEn.size()-1);
            outputEn.add((byte)'\n');
            return convertToArray(outputEn); //uses utf8 by default
        }

    }


    private void pushByte(byte nextByte) {
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }
        bytes[len++] = nextByte;
    }

    private String popString() {
        //notice that we explicitly requesting that the string will be decoded from UTF-8
        //this is not actually required as it is the default encoding in java.
        String result = new String(bytes, 0, len, StandardCharsets.UTF_8);
        len = 0;
        return result;
    }
    public byte[] shortToBytes(short num)
    {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }
    private void addBytes (ArrayList<Byte> endecMSG,byte[] op){
        for (byte b1 : op) {
            endecMSG.add(b1);
        }
    }
    private byte[] convertToArray (ArrayList<Byte> list){
        byte [] arr = new byte[list.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = list.get(i);
        }
        return arr;
    }
}
