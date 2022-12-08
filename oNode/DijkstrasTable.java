package oNode;

public class DijkstrasTable {
    private String vertex;
    private float distance;
    private Integer jumps;
    private String previous;

    public DijkstrasTable(String vertex, float distance, Integer jumps, String previous){
        this.vertex = vertex;
        this.distance = distance;
        this.jumps = jumps;
        this.previous = previous;
    }

    public String getVertex(){
        return this.vertex;
    }

    public float getDistance(){
        return this.distance;
    }

    public Integer getJumps(){
        return this.jumps;
    }

    public String getPrevious(){
        return this.previous;
    }

    public void setDistance(float distance){
        this.distance = distance;
    }

    public void setJumps(Integer jumps){
        this.jumps = jumps;
    }

    public void setPrevious(String previous){
        this.previous = previous;
    }

    public void setVertex(String vertex){
        this.vertex = vertex;
    }


}
