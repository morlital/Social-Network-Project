package bgu.spl.net.srv.Commands;

import bgu.spl.net.sharedData.UsersSharedData;
import bgu.spl.net.api.bidi.Connections;

public class PM {
    private String userName;
    private String recievingUser;
    private String content;
    private UsersSharedData usersSharedData;
    private Connections<String> connections;

    public PM (Connections<String> connections, String userName, String recievingUser, String content, UsersSharedData usersSharedData){
        this.connections = connections;
        this.userName = userName;
        this.recievingUser = recievingUser;
        this.content = content;
        this.usersSharedData = usersSharedData;
    }

    public String act() {
        if (usersSharedData.isLoggedIn(userName) == -1)
            return "ERROR 6";
        if (!usersSharedData.userExist(recievingUser))
            return "ERROR 6";
        if (usersSharedData.isLoggedIn(recievingUser) != -1)
            connections.send(usersSharedData.isLoggedIn(recievingUser), "NOTIFICATION 0"+"\0" + userName + "\0" + content+"\0");
        else
            usersSharedData.getUser(recievingUser).newNotification("NOTIFICATION 0"+"\0" + userName + "\0" + content+"\0");
        return "ACK 6";
    }
}
