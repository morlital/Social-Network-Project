package bgu.spl.net.sharedData;

public class PMMessages {
    private User reciever;
    private String content;

    public PMMessages(User reciever, String content){
        this.reciever=reciever;
        this.content=content;
    }

}
