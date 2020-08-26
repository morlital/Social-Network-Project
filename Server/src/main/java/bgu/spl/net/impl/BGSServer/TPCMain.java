package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.api.MessageEncoderDecoderImpl;
import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;
import bgu.spl.net.sharedData.UsersSharedData;
import bgu.spl.net.srv.Server;
import java.util.function.Supplier;

public class TPCMain {
    public static void main (String[] args){
        UsersSharedData usersSharedData = new UsersSharedData();
        Supplier protocol = new Supplier() {
            @Override
            public BidiMessagingProtocolImpl get() {
                return new BidiMessagingProtocolImpl(usersSharedData);
            }
        };
        Supplier enDec = new Supplier() {
            @Override
            public MessageEncoderDecoderImpl get() {
                return new MessageEncoderDecoderImpl();
            }
        };
        Server<String> bidiServer = Server.<String>threadPerClient(Integer.parseInt(args[0]),protocol,enDec);
        bidiServer.serve();
    }
}
