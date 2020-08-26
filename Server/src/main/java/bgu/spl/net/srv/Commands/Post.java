package bgu.spl.net.srv.Commands;

import bgu.spl.net.sharedData.PostMessage;
import bgu.spl.net.sharedData.UsersSharedData;
import bgu.spl.net.api.bidi.Connections;

import java.util.ArrayList;

public class Post {
    private String userName;
    private String message;
    private UsersSharedData usersSharedData;
    private ArrayList<String> recievers;
    private Connections<String> connections;


    public Post(String userName, String message, UsersSharedData usersSharedData, Connections<String> connections) {
        this.userName = userName;
        this.message = message;
        this.usersSharedData = usersSharedData;
        recievers = new ArrayList<>();
        this.connections = connections;
    }

    public String act() {
        if (usersSharedData.isLoggedIn(userName) == -1)
            return "ERROR 5";
        if (!usersSharedData.getUser(userName).getFollowers().isEmpty())
            recievers.addAll(usersSharedData.getUser(userName).getFollowers());
        String[] arr = message.split(" ");
        for (String anArr : arr) {
            if (anArr.startsWith("@"))
                if (!recievers.contains(anArr.substring(1)))
                    recievers.add(anArr.substring(1));
        }
        for (String reciever : recievers) {
            if (usersSharedData.userExist(reciever))
                if (usersSharedData.isLoggedIn(reciever) != -1) {
                    connections.send(usersSharedData.isLoggedIn(reciever), "NOTIFICATION 1 " + userName + " " + message);
                } else {
                    usersSharedData.getUser(reciever).newNotification("NOTIFICATION 1 " + userName + " " + message);
                }
        }
        usersSharedData.getUser(userName).addPost(new PostMessage(recievers, message));
        return "ACK 5";
    }
}
