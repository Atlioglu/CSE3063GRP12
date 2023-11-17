package features.weekly_schedule;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import core.models.concretes.Course;
import core.models.concretes.CourseSession;
import core.repositories.CourseRepository;
import core.enums.CourseDay;
import core.enums.CourseSlot;
import core.general_providers.InstanceManager;
import core.general_providers.SessionController;
import core.general_providers.TerminalManager;
import features.main_menu.MenuController;
import core.models.concretes.Student;
import core.models.abstracts.User;


public class WeeklyScheduleController {

    private WeeklyScheduleView weeklyScheduleView;
    private CourseRepository courseRepository;

    public WeeklyScheduleController(){
        this.weeklyScheduleView = new WeeklyScheduleView();
        this.courseRepository = new CourseRepository();
		handleWeeklySchedule();
    }

    private ArrayList<Course> fetchCourses() {
        /*
        UNCOMMENT:
        Student currentStudent = (Student)(SessionController.getInstance().getCurrentUser());
        int currentSemester = currentStudent.getTranscript().getCurrentSemester();
        return courseRepository.getCoursesBySemester(currentSemester);*/
        
        Course course1 = new Course("id1","cse22","cse3063",3,createSessions(),null,10);
        Course course2 = new Course("id2","cse23","cse3064",3,createSessions2(),null,10);
        Course course3 = new Course("id3","cse24","cse3065",3,createSessions3(),null,10);
        Course course4 = new Course("id4","cse25","cse3066",3,createSessions4(),null,10);
        return new ArrayList<>(Arrays.asList(course1,course2,course3,course4));
	}

    private CourseSession createSessions() {
        Map<CourseDay, ArrayList<CourseSlot>> schedule = new HashMap<>();
        ArrayList<CourseSlot> mondaySlots = new ArrayList<>();
        mondaySlots.add(CourseSlot.one);
        mondaySlots.add(CourseSlot.two);
        schedule.put(CourseDay.MONDAY, mondaySlots);
        CourseSession session = new CourseSession("id1", schedule, null);
        return session;
    }
     private CourseSession createSessions2() {
        Map<CourseDay, ArrayList<CourseSlot>> schedule = new HashMap<>();
        ArrayList<CourseSlot> mondaySlots = new ArrayList<>();
        mondaySlots.add(CourseSlot.seven);
        mondaySlots.add(CourseSlot.two);
        schedule.put(CourseDay.FRIDAY, mondaySlots);
        CourseSession session = new CourseSession("id1", schedule, null);
        return session;
    }
     private CourseSession createSessions3() {
        Map<CourseDay, ArrayList<CourseSlot>> schedule = new HashMap<>();
        ArrayList<CourseSlot> mondaySlots = new ArrayList<>();
        mondaySlots.add(CourseSlot.one);
        mondaySlots.add(CourseSlot.two);
        schedule.put(CourseDay.WEDNESDAY, mondaySlots);
        CourseSession session = new CourseSession("id1", schedule, null);
        return session;
    }
      private CourseSession createSessions4() {
        Map<CourseDay, ArrayList<CourseSlot>> schedule = new HashMap<>();
        ArrayList<CourseSlot> mondaySlots = new ArrayList<>();
        mondaySlots.add(CourseSlot.one);
        mondaySlots.add(CourseSlot.two);
        schedule.put(CourseDay.THURSDAY, mondaySlots);
        CourseSession session = new CourseSession("id1", schedule, null);
        return session;
    }

    private void navigateToMenu() {
        new MenuController();
    }
    
    private String getUserInput() {
       weeklyScheduleView.showQuitMessage();
       return TerminalManager.getInstance().read();
    }
    
    private void handleWeeklySchedule() {
        ArrayList<Course> currentCourses = fetchCourses();
        weeklyScheduleView.showWeeklySchedule(currentCourses);
        if(getUserInput().equals("q"))
            navigateToMenu();
    }
}
