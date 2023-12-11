package core.models.concretes;

import java.util.ArrayList;
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

    public Student() {
    }

    public ArrayList<CourseSession> getListOfLectureSessions() {
        return listOfLectureSessions;
    }

    public Advisor getAdvisor() {
        return advisor;
    }

    public Transcript getTranscript() {
        return transcript;
    }

    public void setAdvisor(Advisor advisor) {
        this.advisor = advisor;
    }

    public void setListOfLectureSessions(ArrayList<CourseSession> listOfLectureSessions) {
        this.listOfLectureSessions = listOfLectureSessions;
    }

    public void setTranscript(Transcript transcript) {
        this.transcript = transcript;
    }

}
