package features.student.weekly_schedule;

import java.util.ArrayList;
import java.util.Map;

import core.enums.CourseDay;
import core.enums.CourseSlot;
import core.models.concretes.Course;

public class WeeklyScheduleView {
    public void showWeeklySchedule(ArrayList<Course> courses) {
        System.out.println("--------------------------------------------------------------------------------------------------------------------");
        System.out.printf("\t| %-60s | %-15s | %-15s |\n", "Course Name", "Course Day", "Course Slot");
        System.out.println("--------------------------------------------------------------------------------------------------------------------");
    
        for (Course course : courses) {
            for (Map.Entry<CourseDay, ArrayList<CourseSlot>> entry : course.getSession().getCourseSessions().entrySet()) {
                String courseName = course.getName();
                CourseDay courseDay = entry.getKey();
    
                for (CourseSlot slot : entry.getValue()) {
                    String courseSlot = slot.getSlot();
                    System.out.printf("\t| %-60s | %-15s | %-15s |\n", courseName, courseDay, courseSlot);
                }
            }
            System.out.println("--------------------------------------------------------------------------------------------------------------------");
        }
    }
    

    public void showErrorMessage(Exception e) {
        System.out.println("Error: " + e.getMessage());
    }

    public void showQuitMessage() {
        System.out.print("Press q to return Main Menu: ");
    }
}
