package bgu.spl.net.srv.Commands;

import bgu.spl.net.sharedData.User;
import bgu.spl.net.sharedData.UsersSharedData;

public class Follow {
    private Integer userID;
    private int isFollow;
    private String[] usernames;
    private UsersSharedData usersSharedData;

    public Follow (Integer userID, int isFollow, String[] usernames, UsersSharedData usersSharedData){
        this.userID = userID;
        this.isFollow = isFollow;
        this.usernames = usernames;
        this.usersSharedData = usersSharedData;
    }

    public String act (){
        int errorCounter = 0;
        String followSuccessful = "";
        if (!usersSharedData.isLoggedIn(userID))
            return "ERROR 4";
        User u = usersSharedData.getUser(usersSharedData.getUser(userID));
        if (isFollow == 0) {
            for (String username : usernames) {
                if (!usersSharedData.userExist(username) || u.getFollowing().contains(username))
                    errorCounter++;
                else {
                    u.follow(username);
                    usersSharedData.getUser(username).getFollowers().add(u.getUsername());
                    followSuccessful = followSuccessful + username + " ";
                }
            }
            if (errorCounter == usernames.length)
                return "ERROR 4";
            else
                return "ACK "+ (byte)4 +" "+(usernames.length-errorCounter) +" "+ followSuccessful;
        }
        else {
            for (String username : usernames) {
                if (!usersSharedData.userExist(username) || !u.getFollowing().contains(username))
                    errorCounter++;
                else {
                    u.unfollow(username);
                    usersSharedData.getUser(username).getFollowers().remove(u.getUsername());
                    followSuccessful = followSuccessful + username + " ";
                }
            }
            if (errorCounter == usernames.length)
                return "ERROR 4";
            else {
                byte b = (byte)4;
                return "ACK " + (byte) 4 + " " + (usernames.length - errorCounter) + " " + followSuccessful;
            }
        }
    }
}
