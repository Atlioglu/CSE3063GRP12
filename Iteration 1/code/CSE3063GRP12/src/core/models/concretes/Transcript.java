package core.models.concretes;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

public class Transcript {
    private String id;
    private ArrayList<Semester> listOfSemester;
    private int gano;
    private int totalCreditTaken;
    private int totalCreditCompleted;
    private int currentSemester;

    public Transcript(String id, ArrayList<Semester> listOfSemester, int gano,
            int totalCreditTaken, int totalCreditCompleted, String studentId, int currentSemester) {
        this.id = id;
        this.listOfSemester = listOfSemester;
        this.gano = gano;
        this.totalCreditTaken = totalCreditTaken;
        this.totalCreditCompleted = totalCreditCompleted;
        this.currentSemester = currentSemester;
    }

    @SuppressWarnings("unchecked")
    public Transcript(Map<String, Object> attributes) {
        this.id = attributes.get("id") != null ? (String) attributes.get("id") : null;
        this.gano = attributes.get("gano") != null ? (Integer) attributes.get("gano") : 0;
        this.totalCreditTaken = attributes.get("totalCreditTaken") != null
                ? (Integer) attributes.get("totalCreditTaken")
                : 0;
        this.totalCreditCompleted = attributes.get("totalCreditCompleted") != null
                ? (Integer) attributes.get("totalCreditCompleted")
                : 0;
        this.currentSemester = attributes.get("currentSemester") != null
                ? (Integer) attributes.get("currentSemester")
                : 0;

        this.listOfSemester = (attributes.get("listOfSemester") != null
                && attributes.get("listOfSemester") instanceof ArrayList)
                        ? ((ArrayList<Map<String, Object>>) attributes.get("listOfSemester")).stream()
                                .map(semesterData -> new Semester(semesterData))
                                .collect(Collectors.toCollection(ArrayList::new))
                        : new ArrayList<>();
    }

    public Map<String, Object> toJson() {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("id", this.id);
        jsonMap.put("gano", this.gano);
        jsonMap.put("totalCreditTaken", this.totalCreditTaken);
        jsonMap.put("totalCreditCompleted", this.totalCreditCompleted);
        jsonMap.put("currentSemester", this.currentSemester);
        if (this.listOfSemester != null) {
            jsonMap.put("listOfSemester", this.listOfSemester.stream()
                    .map(Semester::toJson)
                    .collect(Collectors.toList()));
        } else {
            jsonMap.put("listOfSemester", null);
        }
        return jsonMap;
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

    // TODO: implement toString()
    @Override
    public String toString() {
        // Return string representation
        return ""; // Placeholder
    }
}
