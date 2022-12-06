package oNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Delay{
    private Map <List<String>, Float> connectionDelay;

    public Delay(){
        this.connectionDelay = new HashMap<>();
    }

    public void addDelay(List<String> connection, float delay){
        connection.sort(String::compareTo);
        this.connectionDelay.put(connection, delay);
    }

    public float getDelay(List<String> connection){
        connection.sort(String::compareTo);
        return this.connectionDelay.get(connection);
    }

    public void removeDelay(List<String> connection){
        connection.sort(String::compareTo);
        this.connectionDelay.remove(connection);
    }

    public Map<List<String>, Float> delayNeighbourns(String node){
        //all keys
        Map<List<String>, Float> delayNeighbourns = new HashMap<>();
        for(List<String> key : this.connectionDelay.keySet()){
            if(key.get(0).equals(node)){
                delayNeighbourns.put(key, this.connectionDelay.get(key));
            }
            else if(key.get(1).equals(node)){
                delayNeighbourns.put(key, this.connectionDelay.get(key));
            }
        }
        return delayNeighbourns;
    }

       
}
