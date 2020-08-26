package bgu.spl.net.api.bidi;

import bgu.spl.net.sharedData.UsersSharedData;
import bgu.spl.net.srv.Commands.*;

public class BidiMessagingProtocolImpl implements BidiMessagingProtocol<String> {
    private int connectionID;
    private Connections<String> connections;
    private UsersSharedData usersSharedData;
    private boolean shouldTerminate;

    public BidiMessagingProtocolImpl(UsersSharedData usersSharedData){
        this.usersSharedData = usersSharedData;
        this.shouldTerminate = false;
    }

    @Override
    public void start(int connectionId, Connections<String> connections) {
        this.connectionID = connectionId;
        this.connections = connections;
    }

    @Override
    public void process(String message) {
        String originalMSG = message;
        message = switchCommand(message, connections);
        connections.send(connectionID,message);
        if (originalMSG.equals("LOGOUT"))
            if (message.contains("ACK")){
                shouldTerminate = true;
                connections.disconnect(connectionID);
            }
    }

    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }

    private String switchCommand (String msg, Connections<String> connections){
        String output = "";
        byte opCode = msg.getBytes()[1];
        String originalMsg=msg;
        msg = msg.substring(2);
        String[] arrStr = msg.split("\0");
        switch (opCode){
            case 1: { //REGISTER
                    String username = arrStr[0];
                    String password = arrStr[1];
                    Register reg = new Register(username,password,connectionID,usersSharedData);
                    output = reg.act();
                }
                break;
            case 2: {//LOGIN
                    String username = arrStr[0];
                    String password = arrStr[1];
                    Login log = new Login(username,password,connectionID,usersSharedData,connections);
                    output = log.act();
                }
                break;
            case 3: { //LOGOUT
                    Logout out = new Logout(connectionID,usersSharedData);
                    output = out.act();
                }
                break;
            case 4: {//FOLLOW
                    int ifFollow =originalMsg.charAt(2);
                    int numOfUsers = originalMsg.substring(3,5).getBytes()[0]*10 + originalMsg.substring(3,5).getBytes()[1];
                    String[] usernames = new String [numOfUsers];
                    originalMsg=originalMsg.substring(5);
                    arrStr = originalMsg.split("\0");
                    System.arraycopy(arrStr, 0, usernames, 0, usernames.length);
                    Follow follow = new Follow(connectionID,ifFollow, usernames,usersSharedData);
                    output = follow.act();
                }
                break;
            case 5: { //POST
                    String userName = usersSharedData.getUser(connectionID);
                    String m = arrStr[0];
                    Post post = new Post(userName, m, usersSharedData, connections);
                    output = post.act();
                }
                break;
            case 6: { //PM
                    String userName = usersSharedData.getUser(connectionID);
                    String receivingUser = arrStr[0];
                   String content = arrStr[1];
                   PM pm = new PM(connections, userName, receivingUser, content, usersSharedData);
                   output = pm.act();
                }
             break;
            case 7: { //USERLIST
                    String userName = usersSharedData.getUser(connectionID);
                    UserList userList = new UserList(userName, usersSharedData);
                    output = userList.act();
                }
                break;
            case 8: { //STAT
                    String userName = arrStr[0];
                    Stat stat = new Stat (userName, connectionID, usersSharedData);
                    output = stat.act();
                }
                break;
            case 9: { //NOTIFICATION
                    String notiType = arrStr[0];
                    String postingUser = arrStr[1];
                    String content = arrStr[2];
                    Notification noti = new Notification(notiType, postingUser, content);
                    output = noti.act();
                }

        }
        return output;
    }
}
