from features.student.transcript.TranscriptView import TranscriptView
from core.repositories.TranscriptRepository import TranscriptRepository
from core.repositories.CourseRepository import CourseRepository
from core.general_providers.SessionController import SessionController
from core.exceptions.UnexpectedInputException import UnexpectedInputException

class TranscriptController:
    def __init__(self):
        self.transcript_view = TranscriptView()
        self.transcript_repository = TranscriptRepository()
        self.course_repository = CourseRepository()
        self.handle_transcript()
    
    def fetch_transcript(self, student_id):
        try:
            return self.transcript_repository.get_transcript(student_id)
        except Exception as e:
            print(f"An error occurred while fetching transcript: {e}")
            return None
    
    def navigate_to_menu(self):
        from features.main_menu.MenuController import MenuController
        MenuController()
     
    def handle_transcript(self):
        try:
            session_controller = SessionController.getInstance()
            transcript = self.fetch_transcript(session_controller.get_current_user().userName)

            semesters = transcript.listOfSemesters
            if semesters is None:
                input_message = input("Press q to return Main Menu: ")
                if input_message == "q":
                    self.navigate_to_menu()
                else:
                    raise UnexpectedInputException("Unexpected input!")
            else:
                print(">>--->TRANSCRIPT========================================================================================================================================================|||")
                print()

                for semester in semesters.values():
                    print("-" * 100)
                    course_id_list = list(semester.get("listOfCoursesTaken").keys())
                    courses = self.course_repository.findCoursesWithCourseIds(course_id_list)

                    print(f"{'Course Code':<15} {'Course Name':<60} {'Course Credit':<15} {'Course Grade':<15}")
                    print("-" * 100)
                    print()

                    for course in courses:
                        
                        course_grade = semester.get("listOfCoursesTaken").get(course.courseCode)
                        if course_grade is None:
                            course_grade = "NA"
                        
                        
                        print(f"{course.courseCode:<15} {course.name:<60} {course.credit:<15} {course_grade:<15}")
                        print()

                print("-" * 100)
                input_message = input("Press q to return Main Menu: ")
                if input_message == "q":
                    self.navigate_to_menu()
                else:
                    raise UnexpectedInputException("Unexpected input!")

                
        except Exception as e:
            print(e)
        
