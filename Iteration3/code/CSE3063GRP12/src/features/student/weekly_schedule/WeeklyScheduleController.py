from features.student.weekly_schedule.WeeklyScheduleView import WeeklyScheduleView
from core.repositories.CourseRepository import CourseRepository
from core.general_providers.SessionController import SessionController
from core.exceptions.UnexpectedInputException import UnexpectedInputException

class WeeklyScheduleController:
    def __init__(self):
        self.weekly_schedule_view = WeeklyScheduleView()
        self.course_repository = CourseRepository()
        self.handle_weekly_schedule()

    def fetch_courses(self):
        current_student = SessionController.getInstance().get_current_user()
        current_semester = current_student.transcript.get("currentSemester")
        return self.course_repository.get_courses_by_semester(current_semester)

    def navigate_to_menu(self):
        from features.main_menu.MenuController import MenuController
        MenuController()

    def handle_weekly_schedule(self):
        try:
            current_courses = self.fetch_courses()
            self.weekly_schedule_view.show_weekly_schedule(current_courses)
            input_message = input("Press q to return Main Menu: ")
            if input_message == "q":
                self.navigate_to_menu()
            else:
                raise UnexpectedInputException("Unexpected input!")
        except Exception as e:
            print(e)