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
                    print("===========================================================================================================================================================================")
                    print("# -> Year ({}): --Semester [{}]--".format(semester.semesterNo // 2 if semester.semesterNo % 2 == 0 else (semester.semesterNo + 1) // 2, semester.semesterNo))
                    print("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------")

                    course_id_list = list(semester.listOfCoursesTaken.keys())
                    courses = self.course_repository.findCoursesWithCourseIds(course_id_list)

                    print("\t{: <15}{: <80}{: <20}{: <20}".format("Course Code", "Course Name", "Course Credit", "Course Grade"))
                    print("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------")
                    print()

                    for course in courses:
                        
                        course_grade = semester.get("listOfCoursesTaken").get(course.courseCode)
                        if course_grade is None:
                            course_grade = "NA"
                        
                        print("\t{: <15}{: <85}{: <20}{: <20}".format(course.courseCode, course.name, course.credit, semester.listOfCoursesTaken[course.courseCode]))

                    print()
                    print("===========================================================================================================================================================================")
                    print(">>---> Completed Credits: {}".format(semester.credits_completed))
                    print(">>---> Taken Credits: {}".format(semester.credits_taken))
                    print(">>---> Yano: {}".format(semester.yano))
                    print(">>---> Gano: {}".format(semester.gano))
                    print()

                print("|||========================================================================================================================================================TRANSCRIPT<---<<")
                print("###===>> Completed Credits: {}".format(transcript.totalCreditCompleted))
                print("###===>> Taken Credits: {}".format(transcript.totalCreditTaken))
                print("###===>> Gano: {}".format(transcript.gano))
                print("|||========================================================================================================================================================TRANSCRIPT<---<<")
                
                while True:
                    input_message = input("Press q to return Main Menu: ")
                    if input_message == "q":
                        self.navigate_to_menu()
                        break
                    else:
                        raise UnexpectedInputException("Unexpected input!")
        except Exception as e:
            print(e)
        
