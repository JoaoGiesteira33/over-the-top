package oNode;

import java.util.HashMap;
import java.util.Map;


public class RoutingTable {
    Map<String, String> routingTable;
    
    public RoutingTable(){
        this.routingTable = new HashMap<>();
    }

    public void addEntry(String key, String value){
        this.routingTable.put(key, value);
    }

    public String getEntry(String key){
        return this.routingTable.get(key);
    }

    public void removeEntry(String key){
        this.routingTable.remove(key);
    }

    public void updateEntry(String key, String value){
        this.routingTable.replace(key, value);
    }
}
