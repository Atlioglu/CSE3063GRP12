import os

from core.general_providers.AppConstant import AppConstant
from core.general_providers.InstanceManager import InstanceManager
from core.repositories.UserRepository import UserRepository
from core.models.concretes.Transcript import Transcript
from core.enums.CourseGrade import CourseGrade
from core.models.concretes.Semester import Semester

class TranscriptRepository:
    def __init__(self):
        self.database_manager = InstanceManager().get_database_instance()
        self.user_repository = UserRepository()
        self.path = os.getcwd() + AppConstant.getInstance().get_base_path() + "transcript/"

    def get_transcript(self, student_id):
        try:
            student = self.user_repository.get_user(student_id)
            current_semester = student.transcript["currentSemester"]
            transcript_path = os.path.join(self.path, str(current_semester), f"{student_id}.json")
            
            transcript = self.database_manager.read(transcript_path, Transcript)
            return transcript
        except Exception as e:
            print(f"An error occurred while getting transcript: {e}")
    
    def update_transcript(self, course_enrollment):
        try:
            transcript = self.get_transcript(course_enrollment.studentId)
            current_semester = transcript.currentSemester

            if transcript.listOfSemesters == None or current_semester > len(transcript.listOfSemesters):
                new_course_list = {}

                if transcript.listOfSemesters == None:
                    transcript.listOfSemesters = {}
                
                for course in course_enrollment.approved_course_list:
                    new_course_list[course.get('courseCode')] = CourseGrade.NON

                total_credit_taken = sum(course.get('credit') for course in course_enrollment.approved_course_list)
                semester = Semester("0", new_course_list, total_credit_taken, 0, transcript.currentSemester)
                
                transcript.listOfSemesters[str(current_semester)] = semester.to_dict()

                # {'listOfCoursesTaken': {'MBG1201': 'AA', 'CSE1200': 'BB', 'CSE1241': 'CC', 'MATH1001': 'DD', 'PHYS1101': 'BA', 'PHYS1103': 'CC'}, 'creditsTaken': 27, 'creditsCompleted': 27, 'yano': 3.7, 'gano': 3.7, 'semesterNo': 1}
                # {'id': '0', 'listOfCoursesTaken': {'CSE3044': <CourseGrade.FF: 0.0>, 'CSE3264': <CourseGrade.FF: 0.0>, 'IE3235': <CourseGrade.FF: 0.0>}, 'credits_taken': 16, 'yano': 0, 'semesterNo': 6}
                self.database_manager.write(os.path.join(self.path, str(current_semester), f"{course_enrollment.studentId}.json"), transcript)
            else:
                new_course_list = {}
                for course in course_enrollment.selectedCourseList:
                    new_course_list[course.get('courseCode')] = CourseGrade.NON

                total_credit_taken = sum(course.get('credit') for course in course_enrollment.selectedCourseList)    

                semester = Semester("0", new_course_list, total_credit_taken, 0, transcript.currentSemester)
                transcript.listOfSemesters[current_semester] = semester

                self.database_manager.write(os.path.join(self.path, str(current_semester), f"{course_enrollment.studentId}.json"), transcript)
    
        except Exception as e:
            print(f"An error occurred while updating transcript: {e}")
            raise e