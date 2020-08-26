package bgu.spl.net.srv.Commands;

import bgu.spl.net.sharedData.UsersSharedData;

import java.util.concurrent.ConcurrentLinkedQueue;

public class UserList {
    private String userName;
    private UsersSharedData usersSharedData;

    public UserList(String userName, UsersSharedData usersSharedData){
        this.userName = userName;
        this.usersSharedData = usersSharedData;
    }

    public String act (){
        if (usersSharedData.isLoggedIn(userName) == -1)
            return "ERROR 7";
        ConcurrentLinkedQueue <String> register = usersSharedData.getRegistrationOrder();
        String output = "";
        for (String aRegister : register) {
            output = output + " " + aRegister;
        }
        return "ACK 7 " + register.size() + output;
    }
}
