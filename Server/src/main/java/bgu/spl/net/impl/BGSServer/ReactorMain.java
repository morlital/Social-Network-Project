package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.api.MessageEncoderDecoderImpl;
import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;
import bgu.spl.net.sharedData.UsersSharedData;
import bgu.spl.net.srv.Server;

import java.util.function.Supplier;

public class ReactorMain {
    public static void main(String[] args) {
        UsersSharedData usersSharedData = new UsersSharedData();
        Supplier protocol = () -> new BidiMessagingProtocolImpl(usersSharedData);
        Supplier enDec = MessageEncoderDecoderImpl::new;
        Server.reactor(Integer.parseInt(args[1]), (Integer.parseInt(args[0])), protocol, enDec).serve();
    }
}