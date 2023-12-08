package core.models.concretes;

import java.util.ArrayList;

public class Transcript {
    private String id;
    private ArrayList<Semester> listOfSemester;
    private double gano;
    private int totalCreditTaken;
    private int totalCreditCompleted;
    private int currentSemester;

    public Transcript(String id, ArrayList<Semester> listOfSemester, double gano,
            int totalCreditTaken, int totalCreditCompleted, int currentSemester) {
        this.id = id;
        this.listOfSemester = listOfSemester;
        this.gano = gano;
        this.totalCreditTaken = totalCreditTaken;
        this.totalCreditCompleted = totalCreditCompleted;
        this.currentSemester = currentSemester;
    }

    public ArrayList<Semester> getListOfSemester() {
        return listOfSemester;
    }

    public double getGano() {
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

    public void setListOfSemester(ArrayList<Semester> listOfSemester) {
        this.listOfSemester = listOfSemester;
    }

    public void setGano(double gano) {
        this.gano = gano;
    }

    public void setTotalCreditTaken(int totalCreditTaken) {
        this.totalCreditTaken = totalCreditTaken;
    }

    public void setTotalCreditCompleted(int totalCreditCompleted) {
        this.totalCreditCompleted = totalCreditCompleted;
    }

    public void setCurrentSemester(int currentSemester) {
        this.currentSemester = currentSemester;
    }

      public String toString() {
        // Return string representation
        return ""; // Placeholder
    }
}
