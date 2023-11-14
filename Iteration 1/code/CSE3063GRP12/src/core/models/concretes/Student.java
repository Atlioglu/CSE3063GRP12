package core.models.concretes;

import java.util.ArrayList;
import java.util.Map;

import core.models.abstracts.User;

public class Student extends User {
    private Advisor advisor;
    private ArrayList<CourseSession> listOfLectureSessions;
    private Transcript transcript;

    public Student(String id, String firstName, String lastName, String userName, 
                   String password, Advisor advisor, ArrayList<CourseSession> listOfLectureSessions, 
                   Transcript transcript) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
        this.advisor = advisor;
        this.listOfLectureSessions = listOfLectureSessions;
        this.transcript = transcript;
    }
        // this is not completed yet
    public Student(Map<String, Object> attributes) {
        this.advisor = (Advisor) attributes.get("advisor");
        this.listOfLectureSessions = (ArrayList<CourseSession>) attributes.get("listOfLectureSessions");
        this.transcript = (Transcript) attributes.get("transcript");
    }

    @Override
    public Map<String, Object> toJson() {
        // Convert to JSON map
        return null; // Placeholder
    }

    public ArrayList<CourseSession> getListOfLectureSessions() {
        return listOfLectureSessions;
    }

    public Transcript getTranscript() {
        return transcript;
    }
}
