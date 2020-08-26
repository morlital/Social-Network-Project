package bgu.spl.net.srv.Commands;


import bgu.spl.net.sharedData.User;
import bgu.spl.net.sharedData.UsersSharedData;
import bgu.spl.net.api.bidi.Connections;

import java.util.ArrayList;

public class Login {
    private String username,password;
    private Integer userID;
    private UsersSharedData usersSharedData;
    private Connections<String> connectionsTCP;

    public Login (String username, String password, Integer userID, UsersSharedData usersSharedData, Connections<String> connectionsTCP){
        this.username = username;
        this.password = password;
        this.userID = userID;
        this.usersSharedData = usersSharedData;
        this.connectionsTCP = connectionsTCP;
    }

    public String act(){
        User user = usersSharedData.getUser(username);
        if (user == null)
            return "ERROR 2";
        else if (!user.getPassword().equals(password)){
            return "ERROR 2";
        }
        else if (usersSharedData.isLoggedIn(username) != -1){
            return "ERROR 2";
        }
        else{
            ArrayList<String> oldNotifications = usersSharedData.login(user,userID);
            if (oldNotifications != null) {
                while(!oldNotifications.isEmpty()) {
                    connectionsTCP.send(userID, oldNotifications.get(0));
                    oldNotifications.remove(0);
                }
            }
            return "ACK 2";
        }
    }
}
