package oNode;

import java.util.List;
import java.util.Map;
import java.util.AbstractMap.SimpleEntry;
import java.io.File;
import java.util.ArrayList;

public class DijkstrasAlgorithm {
    private List<String> visited;
    private List<String> unvisited;
    private List<DijkstrasTable> dijkstrasTable;
    private Delay delay;

    public DijkstrasAlgorithm(){
        this.visited = new ArrayList<>();
        this.unvisited = new ArrayList<>();
        this.dijkstrasTable = new ArrayList<>();
        this.delay = new Delay();
    }

    public List<DijkstrasTable> algorithm(String start, File f, Delay d){
        List<String> lst = new ArrayList<>();
        Map<String, List<String>> config = ConfigFileParser.readFile(f); // os vizinhos estao aqui
        config.forEach((k,v) -> lst.add(k));
        this.delay = d;

        setUnvisited(lst);
        startDistances(lst, start);
                
        while(!this.unvisited.isEmpty()){
            DijkstrasTable min = minRow();
            addVisited(min.getVertex());
            removeUnvisited(min.getVertex());
            
            List<String> neighbours = config.get(min.getVertex());
            for(String node : neighbours){
                if(!this.visited.contains(node)){
                    SimpleEntry<Float, Integer> delayInfo = delay.getDelay(new ArrayList<>(){{add(min.getVertex()); add(node);}});
                    float newDelay = min.getDistance() + delayInfo.getKey();
                    int neighbourIdRow = getIndexDijkstrasTable(node);
                    DijkstrasTable newRow = this.dijkstrasTable.get(neighbourIdRow);
                    
                    if( newDelay < newRow.getDistance() ||
                    (newDelay == newRow.getDistance() && delayInfo.getValue() < newRow.getJumps())){
                        newRow.setDistance(newDelay);
                        newRow.setJumps(delayInfo.getValue());// errado, corrigir
                        newRow.setPrevious(min.getVertex());

                        this.dijkstrasTable.set(neighbourIdRow, newRow);
                    }
                }
            }
        }
        return this.dijkstrasTable;
    }

    private void startDistances(List<String> lst, String start){
        for(String node : lst){
            if(node.equals(start))
                this.addDijkstrasTable(new DijkstrasTable(node, 0, 0, null));
            else
                this.addDijkstrasTable(new DijkstrasTable(node, Integer.MAX_VALUE, 0, null));
        }
    }

    private DijkstrasTable minRow(){
        List<DijkstrasTable> dijkstrasTable = new ArrayList<>(this.dijkstrasTable);
        for(DijkstrasTable dt : dijkstrasTable){
            if(!this.unvisited.contains(dt.getVertex())){
                dijkstrasTable.remove(dt);
            }
        }
        DijkstrasTable min = dijkstrasTable.get(0);
        for(DijkstrasTable dt : dijkstrasTable){
            if(dt.getDistance() < min.getDistance()){
                min = dt;
            }
        }
        return min;
    }

    public void addVisited(String node){
        this.visited.add(node);
    }

    public void removeVisited(String node){
        this.visited.remove(node);
    }

    public void addUnvisited(String node){
        this.unvisited.add(node);
    }

    public void removeUnvisited(String node){
        this.unvisited.remove(node);
    }

    public void addDijkstrasTable(DijkstrasTable dt){
        this.dijkstrasTable.add(dt);
    }

    public List<String> getVisited(){
        return this.visited;
    }

    public List<String> getUnvisited(){
        return this.unvisited;
    }

    public List<DijkstrasTable> getDijkstrasTable(){
        return this.dijkstrasTable;
    }

    private int getIndexDijkstrasTable(String node){
        for(DijkstrasTable dt : this.dijkstrasTable){
            if(dt.getVertex().equals(node)){
                return this.dijkstrasTable.indexOf(dt);
            }
        }
        return -1;
    }

    public void setVisited(List<String> visited){
        this.visited = visited;
    }

    public void setUnvisited(List<String> unvisited){
        this.unvisited = unvisited;
    }

    public void setDijkstrasTable(List<DijkstrasTable> dijkstrasTable){
        this.dijkstrasTable = dijkstrasTable;
    }



}
