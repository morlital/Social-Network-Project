package bgu.spl.net.srv.Commands;

import bgu.spl.net.sharedData.User;
import bgu.spl.net.sharedData.UsersSharedData;

public class Register {
    private String username,password;
    private UsersSharedData usersSharedData;
    private int connectionID;

    public Register (String username, String password, int connectionID, UsersSharedData usersSharedData){
        this.username = username;
        this.password = password;
        this.connectionID = connectionID;
        this.usersSharedData = usersSharedData;
    }
    public String act (){
        if (usersSharedData.userExist(username))
            return "ERROR 1";
        if (usersSharedData.isLoggedIn(connectionID))
            return "ERROR 1";
        usersSharedData.registerUser(new User(username, password));
        return "ACK 1";
    }
}
