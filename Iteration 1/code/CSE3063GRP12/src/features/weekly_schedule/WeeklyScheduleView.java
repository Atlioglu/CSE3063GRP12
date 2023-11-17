package features.weekly_schedule;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import core.enums.CourseDay;
import core.enums.CourseSlot;
import core.models.concretes.Course;
import core.models.concretes.CourseSession;

public class WeeklyScheduleView {
    public void showWeeklySchedule(ArrayList<Course> courses) {   
        for (Course course : courses) {
            for (Map.Entry<CourseDay, ArrayList<CourseSlot>> entry : course.getsession().getCourseSessions().entrySet()) {
                System.out.println("Course Name: " + course.getName());
                System.out.println("Course Day: " + entry.getKey());
                for (CourseSlot slot : entry.getValue()) {
                    System.out.println("Course Slot: " + slot.getSlot());
                }
            }
            System.out.println();
        }  
        
    }
    public void showErrorMessage(Exception e){
        System.out.println("Error: " + e.getMessage());
    }
    public void showQuitMessage(){
        System.out.print("Press q to return Main Menu: ");
    }
}
