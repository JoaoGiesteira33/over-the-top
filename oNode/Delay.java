package oNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Delay{
    private Map <List<String>, Integer> connectionDelay;

    public Delay(){
        this.connectionDelay = new HashMap<>();
    }

    public void addDelay(List<String> connection, int delay){
        connection.sort(String::compareTo);
        this.connectionDelay.put(connection, delay);
    }

    public int getDelay(List<String> connection){
        connection.sort(String::compareTo);
        return this.connectionDelay.get(connection);
    }

    public void removeDelay(List<String> connection){
        connection.sort(String::compareTo);
        this.connectionDelay.remove(connection);
    }

       
}
