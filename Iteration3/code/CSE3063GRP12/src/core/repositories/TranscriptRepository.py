import os

from src.core.general_providers.AppConstant import AppConstant
from src.core.general_providers.InstanceManager import InstanceManager
from src.core.repositories.UserRepository import UserRepository
from src.core.models.concretes.Transcript import Transcript
from src.core.enums.CourseGrade import CourseGrade
from src.core.models.concretes.Semester import Semester

class TranscriptRepository:
    def __init__(self):
        self.database_manager = InstanceManager().get_database_instance()
        self.user_repository = UserRepository()
        self.path = os.getcwd() + AppConstant.getInstance().get_base_path() + "transcript/"

    def get_transcript(self, student_id):
        try:
            student = self.user_repository.get_user(student_id)
            current_semester = student.transcript.current_semester
            transcript_path = os.path.join(self.path, str(current_semester), f"{student_id}.json")
            
            transcript = self.database_manager.read(transcript_path, Transcript)
            return transcript
        except Exception as e:
            print(f"An error occurred while getting transcript: {e}")
    
    def update_transcript(self, course_enrollment):
        try:
            transcript = self.get_transcript(course_enrollment.student_id)
            current_semester = transcript.current_semester

            if transcript.listOfSemester == None or current_semester > len(transcript.listOfSemester):
                new_course_list = {}

                if transcript.listOfSemester == None:
                    transcript.listOfSemester = {}
                
                for course in course_enrollment.selectedCourseList:
                    new_course_list[course.courseCode] = CourseGrade.NON

                total_credit_taken = sum(course.credit for course in course_enrollment.selectedCourseList)
                semester = Semester("0", new_course_list, total_credit_taken, 0, transcript.currentSemester)
                
                transcript.listOfSemester[current_semester] = semester

                self.database_manager.write(os.path.join(self.path, str(current_semester), f"{course_enrollment.studentId}.json"), transcript)
            else:
                new_course_list = {}
                for course in course_enrollment.selectedCourseList:
                    new_course_list[course.courseCode] = CourseGrade.NON

                total_credit_taken = sum(course.credit for course in course_enrollment.selectedCourseList)    

                semester = Semester("0", new_course_list, total_credit_taken, 0, transcript.current_semester)
                transcript.listOfSemester[current_semester] = semester

                self.database_manager.write(os.path.join(self.path, str(current_semester), f"{course_enrollment.studentId}.json"), transcript)
    
        except Exception as e:
            print(f"An error occurred while updating transcript: {e}")
            raise e