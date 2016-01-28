package uk.ac.cam.november.message;

public class Message<T> { 
    private String timestamp;
    private String prio;
    private String src;
    private String dst;
    private String pgn;
    private String description;
    private T fields;
    
    public String getTimestamp() {
        return timestamp;
    }
    public String getPrio() {
        return prio;
    }
    public String getSrc() {
        return src;
    }
    public String getDst() {
        return dst;
    }
    public String getPgn() {
        return pgn;
    }
    public String getDescription() {
        return description;
    }
    public T getFields() {
        return fields;
    }
}
