from core.enums.CourseSlot import CourseSlot

class WeeklyScheduleView:
    def show_weekly_schedule(self, courses):
        print("-" * 118)
        print(f"\t| {'Course Name':<60} | {'Course Day':<15} | {'Course Slot':<15} |")
        print("-" * 118)

        for course in courses:
            for course_day, course_slots in course.session.get("courseSessions").items():
                course_name = course.name

                for slot in course_slots:
                    # Accessing the value of the enum member
                    member_name = slot.upper()
                    slot_value = getattr(CourseSlot, member_name).value
                    print(f"\t| {course_name:<60} | {course_day:<15} | {slot_value:<15} |")
            
            print("-" * 118)

