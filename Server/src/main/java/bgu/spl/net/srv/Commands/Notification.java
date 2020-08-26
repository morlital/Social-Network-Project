package bgu.spl.net.srv.Commands;

public class Notification {
    private String notiType;
    private String postingUser;
    private String content;

    public Notification (String notificationType, String postingUser, String content){
        this.notiType = notificationType;
        this.postingUser = postingUser;
        this.content = content;
    }

    public String act(){
        if(notiType.equals("0"))
            return "NOTIFICATION PMMessages " + postingUser + " " + content;
        return "NOTIFICATION Public " + postingUser + " " + content;
    }
}
