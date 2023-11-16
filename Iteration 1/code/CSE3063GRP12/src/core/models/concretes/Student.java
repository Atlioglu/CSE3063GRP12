package core.models.concretes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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

    @SuppressWarnings("unchecked")
    public Student(Map<String, Object> attributes) {
        this.id = attributes.get("id") != null ? (String) attributes.get("id") : null;
        this.firstName = attributes.get("firstName") != null ? (String) attributes.get("firstName") : null;
        this.lastName = attributes.get("lastName") != null ? (String) attributes.get("lastName") : null;
        this.userName = attributes.get("userName") != null ? (String) attributes.get("userName") : null;
        this.password = attributes.get("password") != null ? (String) attributes.get("password") : null;
        this.advisor = attributes.get("advisor") != null ? new Advisor((Map<String, Object>) attributes.get("advisor"))
                : null;
        this.listOfLectureSessions = ((ArrayList<Map<String, Object>>) attributes.get("listOfLectureSessions")).stream()
                .map(sessionData -> new CourseSession(sessionData))
                .collect(Collectors.toCollection(ArrayList::new));
        System.out.println(attributes.get("transcript"));
        this.transcript = attributes.get("transcript") != null
                ? new Transcript((Map<String, Object>) attributes.get("transcript"))
                : null;
        
    }

    @Override
    public Map<String, Object> toJson() {
        Map<String, Object> jsonMap = new HashMap<>();

        jsonMap.put("id", this.id);
        jsonMap.put("firstName", this.firstName);
        jsonMap.put("lastName", this.lastName);
        jsonMap.put("userName", this.userName);
        jsonMap.put("password", this.password);

        jsonMap.put("advisor", this.advisor != null ? this.advisor.toJson() : null);

        if (this.listOfLectureSessions != null) {
            jsonMap.put("listOfLectureSessions", this.listOfLectureSessions.stream()
                    .map(CourseSession::toJson)
                    .collect(Collectors.toList()));

        } else {
            jsonMap.put("listOfLectureSessions", null);
        }

        // Transcript nesnesi için toJson metodunu çağır
        jsonMap.put("transcript", this.transcript != null ? this.transcript.toJson() : null);

        return jsonMap;
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
}
