package uk.ac.cam.november.message;

public class Message { 
    private String timestamp;
    private int prio;
    private int src;
    private int dst;
    private int pgn;
    private String description;
    private Fields fields;
    
    public void setFields(Fields field){
        fields = field;
    }
    public String getTimestamp() {
        return timestamp;
    }
    
    public String getDescription() {
        return description;
    }
    
    public Fields getFields() {
        return fields;
    }

    public int getPrio() {
        return prio;
    }

    public int getSrc() {
        return src;
    }

    public int getDst() {
        return dst;
    }

    public int getPgn() {
        return pgn;
    }
    
    @Override
    public String toString(){
        return timestamp + " Description: " + description + " Prio: " + prio + " dst: " + dst + " src: " + src + " pgn " + pgn + " fields:" + fields.getSID();
    }
     
}
