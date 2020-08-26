package bgu.spl.net.api.bidi;

import bgu.spl.net.sharedData.UsersSharedData;
import bgu.spl.net.srv.BlockingConnectionHandler;

import java.io.IOException;
import java.util.*;

public class ConnectionsTCP<T> implements Connections<T> {
    private HashMap<Integer, BlockingConnectionHandler<T>> clientMap;
    private UsersSharedData usersSharedData;

    public ConnectionsTCP(){
        clientMap = new HashMap<>();
    }

    @Override
    synchronized public boolean send(int connectionId, T msg) {
        if(clientMap.containsKey(connectionId)){
            clientMap.get(connectionId).send(msg);
            return true;
        }
        System.out.println("not here");
        return false;
    }

    @Override
    public void broadcast(T msg) {
        Collection<BlockingConnectionHandler<T>> clients = clientMap.values();
        for (BlockingConnectionHandler<T> client : clients) client.send(msg);
    }

    @Override
    public void disconnect(int connectionId) {
        try {
            clientMap.get(connectionId).close();
            clientMap.remove(connectionId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void addConnectedClient (int id, BlockingConnectionHandler<T> handler){
        //if client doesn't exist then add it to the map
        if (!clientMap.containsKey(id))
            clientMap.put(id, handler);
    }
}
