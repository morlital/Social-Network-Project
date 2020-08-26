package bgu.spl.net.srv.Commands;


import bgu.spl.net.sharedData.UsersSharedData;

public class Logout {
    private Integer userID;
    private UsersSharedData usersSharedData;

    public Logout (Integer userID, UsersSharedData usersSharedData){
        this.userID = userID;
        this.usersSharedData = usersSharedData;
    }
    public String act() {
        if (!usersSharedData.isLoggedIn(userID))
            return "ERROR 3";
        usersSharedData.logout(userID);
        return "ACK 3";
    }
}
