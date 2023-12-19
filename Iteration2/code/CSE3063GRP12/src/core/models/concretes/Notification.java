package core.models.concretes;

public class Notification {

    private String message;
    private java.util.Date date;
    private boolean read;


    public Notification(String message, java.util.Date date, boolean read){
        this.message = message;
        this.date = date;
        this.read = read;
    }
    public Notification(){

    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public java.util.Date getDate() {
        return date;
    }
    public void setCreatedDate(java.util.Date date) {
        this.date = date;
    }

    public boolean isRead() {
        return read;
    }
    public void setRead(boolean read) {
        this.read = read;
    }

}