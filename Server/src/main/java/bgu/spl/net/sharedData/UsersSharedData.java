package bgu.spl.net.sharedData;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.*;

public class UsersSharedData {
    private ConcurrentHashMap<String, User> registeredUsers;
    private ConcurrentHashMap<Integer, String> loggedUsers;
    private ConcurrentLinkedQueue<String> registrationOrder;

    //TODO:check if needed to implement as singleton
    public UsersSharedData() {
        registeredUsers = new ConcurrentHashMap<>();
        loggedUsers = new ConcurrentHashMap<>();
        registrationOrder = new ConcurrentLinkedQueue<>();
    }

    public void registerUser(User u) {
        registeredUsers.put(u.getUsername(), u);
        registrationOrder.add(u.getUsername());
    }

    public ConcurrentLinkedQueue<String> getRegistrationOrder(){
        return registrationOrder;
    }

    public ArrayList<String> login(User u, Integer clientID) {
        ArrayList<String> arrayList=u.getAwaitingNotifications();
        loggedUsers.put(clientID, u.getUsername());
        return arrayList;
    }

    public boolean userExist(String username) {
        return registeredUsers.containsKey(username);
    }

    public User getUser(String username) {
        return registeredUsers.get(username);
    }

    public String getUser(Integer userID) {
        return loggedUsers.get(userID);
    }

    public Integer isLoggedIn(String username) {
        Integer userID = loggedUsers.search(1, (k, v) -> {
            if (v.equals(username)) {
                return k;
            }
            return null;
        });
        if (userID == null)
            return -1;
        return userID;
    }

    public boolean isLoggedIn(Integer userID) {
        return loggedUsers.containsKey(userID);
    }

    public void logout(Integer userID) {
        loggedUsers.remove(userID);
    }

}
