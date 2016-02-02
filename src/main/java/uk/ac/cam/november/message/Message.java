package uk.ac.cam.november.message;

import java.util.Date;

public class Message { 
    private Date timestamp;
    private int prio;
    private int src;
    private int dst;
    private int pgn;
    private String description;
    private Fields fields;
    
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
    public void setPrio(int prio) {
        this.prio = prio;
    }
    public void setSrc(int src) {
        this.src = src;
    }
    public void setDst(int dst) {
        this.dst = dst;
    }
    public void setPgn(int pgn) {
        this.pgn = pgn;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setFields(Fields fields) {
        this.fields = fields;
    }
    
    public Date getTimestamp() {
        return timestamp;
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
    
    public String getDescription() {
        return description;
    }
    public Fields getFields() {
        return fields;
    }
}
