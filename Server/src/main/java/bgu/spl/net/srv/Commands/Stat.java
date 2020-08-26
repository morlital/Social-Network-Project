package bgu.spl.net.srv.Commands;


import bgu.spl.net.sharedData.UsersSharedData;

public class Stat {
    private String userName;
    private int connectionID;
    private UsersSharedData usersSharedData;

    public Stat (String userName,int connectionID, UsersSharedData usersSharedData){
        this.userName = userName;
        this.usersSharedData = usersSharedData;
        this.connectionID = connectionID;
    }

    public String act (){
        if (!usersSharedData.isLoggedIn(connectionID))
            return "ERROR 8";
        if(!usersSharedData.userExist(userName))
            return "ERROR 8";
        return "ACK 8 " + usersSharedData.getUser(userName).getPosts().size() + " " + usersSharedData.getUser(userName).getFollowers().size() + " " + usersSharedData.getUser(userName).getFollowing().size();
    }
}
