package bgu.spl.net.api.bidi;

import bgu.spl.net.srv.NonBlockingConnectionHandler;

import java.util.HashMap;
import java.util.Set;

public class ConnectionsReactor<T> implements Connections<T> {
    private HashMap<Integer, NonBlockingConnectionHandler> clientMap;

    public ConnectionsReactor() {clientMap = new HashMap<>();}

    @Override
    synchronized public boolean send (int connectionId, T msg){
        boolean output = false;
        if (clientMap.containsKey(connectionId)){
            clientMap.get(connectionId).send(msg);
            output = true;
        }
        return output;
    }

    @Override
    public void broadcast (T msg){
        //lock UserSharedData
        Set<Integer> loggedClients = clientMap.keySet();
        for (Integer clientId : loggedClients){
            clientMap.get(clientId).send(msg);
        }
    }
    //unlock

    @Override
    public void disconnect (int connectionId){
        clientMap.get(connectionId).close();
        clientMap.remove(connectionId);
    }
    synchronized public void addConnectedClient (int id, NonBlockingConnectionHandler handler){
        if (!clientMap.containsKey(id))
            clientMap.put(id, handler);
    }
}
