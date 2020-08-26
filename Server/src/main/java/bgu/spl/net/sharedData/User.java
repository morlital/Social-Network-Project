package bgu.spl.net.sharedData;

import java.util.ArrayList;

public class User {
    private String username;
    private String password;
    private ArrayList<String > following;
    private ArrayList<String> followers;
    private ArrayList<PMMessages> pms;
    private ArrayList<PostMessage> postMessages;
    private ArrayList<String> awaitingNotifications;

    public User (String username, String password){
        this.username=username;
        this.password = password;
        following = new ArrayList<>();
        pms= new ArrayList<>();
        postMessages = new ArrayList<>();
        awaitingNotifications = new ArrayList<>();
        followers = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<String> getFollowing(){
        return following;
    }

    public ArrayList<String> getFollowers() { return followers; }

    public ArrayList<PostMessage> getPosts () { return postMessages; }

    public void follow (String username){
        following.add(username);
    }

    public void unfollow(String username){
        following.remove(username);
    }

    public void newNotification (String notification) {
        awaitingNotifications.add(notification);
    }

    public void addPost (PostMessage postMessage){
        postMessages.add(postMessage);
    }

    public ArrayList<String> getAwaitingNotifications() {
        return awaitingNotifications;
    }
}
