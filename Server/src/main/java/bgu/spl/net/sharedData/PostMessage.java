package bgu.spl.net.sharedData;

import java.util.ArrayList;

public class PostMessage {
    private ArrayList<String> users;
    private String content;

    public PostMessage(ArrayList<String> reciever, String content){
        this.users=reciever;
        this.content=content;
    }

}
