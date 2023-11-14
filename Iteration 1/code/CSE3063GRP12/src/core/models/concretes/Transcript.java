package core.models.concretes;

import java.util.ArrayList;
import java.util.Map;

public class Transcript {
    private String id;
    private ArrayList<Semester> listOfSemester;
    private int gano;
    private int totalCreditTaken;
    private int totalCreditCompleted;
    private int currentSemester;

    public Transcript(String id, ArrayList<Semester> listOfSemester, int gano, 
                      int totalCreditTaken, String studentId, int currentSemester) {
        this.id = id;
        this.listOfSemester = listOfSemester;
        this.gano = gano;
        this.totalCreditTaken = totalCreditTaken;
        this.totalCreditCompleted = totalCreditCompleted;
        this.currentSemester = currentSemester;
    }

    public Transcript(Map<String, Object> attributes) {
        this.id = (String) attributes.get("id");
        this.listOfSemester = (ArrayList<Semester>) attributes.get("listOfSemester");
        this.gano = (int) attributes.get("gano");
        this.totalCreditTaken = (int) attributes.get("totalCreditTaken");
        this.totalCreditCompleted = (int) attributes.get("totalCreditCompleted");
        this.currentSemester = (int) attributes.get("currentSemester");
    }

    public Map<String, Object> toJson() {
        // Convert to JSON map
        return null; // Placeholder
    }

    public ArrayList<Semester> getListOfSemester() {
        return listOfSemester;
    }

    public int getGano() {
        return gano;
    }

    public int getTotalCreditTaken() {
        return totalCreditTaken;
    }

    public int getTotalCreditCompleted() {
        return totalCreditCompleted;
    }

    public int getCurrentSemester() {
        return currentSemester;
    }

    @Override
    public String toString() {
        // Return string representation
        return ""; // Placeholder
    }
}
