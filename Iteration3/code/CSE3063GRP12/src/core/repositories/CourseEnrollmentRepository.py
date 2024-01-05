import json
import os
from pathlib import Path

from core.general_providers.InstanceManager import InstanceManager
from core.general_providers.AppConstant import AppConstant
from core.models.concretes.CourseEnrollment import CourseEnrollment
from core.general_providers.SessionController import SessionController
from core.enums.ApprovalState import ApprovalState

class CourseEnrollmentRepository:
    def __init__(self):
        self.database_manager = InstanceManager().get_database_instance()
        self.path = os.getcwd() + AppConstant.getInstance().get_base_path() + "course_enrollment/"

    def get_course_enrollment_by_student_id(self, student_id):
        try:
            path = os.path.join(self.path, f"{student_id}.json")
            course_enrollment = self.database_manager.read(path, CourseEnrollment)
            return course_enrollment
        except Exception as e:
            print(f"An error occurred while getting course enrollment by student id: {e}")
            return None

    def get_pending_enrollments(self):
        advisor = SessionController.getInstance().get_current_user()
        try:
            course_enrollments = []
            for student_id in advisor.listOfStudentIds:
                try:
                    course_enrollment = self.database_manager.read(os.path.join(self.path, f"{student_id}.json"), CourseEnrollment)
                    if course_enrollment.approvalState == ApprovalState.PENDING:
                        course_enrollments.append(course_enrollment)
                except Exception as e:
                    print(f"An error occurred while getting course enrollment by student id: {e}")
                    pass
            return course_enrollments
        except Exception as e:
            print(f"An error occurred while getting pending enrollments: {e}")
            return None

    def update_enrollment(self, course_enrollment, student_id, approved_courses, rejected_courses, approval_state):
        course_enrollment.approved_course_list = approved_courses
        course_enrollment.rejected_course_list = rejected_courses
        course_enrollment.approvalState = approval_state
        self.database_manager.write(os.path.join(self.path, f"{student_id}.json"), course_enrollment)

    def create_course_enrollment(self, courses):
        user = SessionController.getInstance().get_current_user()
        course_enrollment = CourseEnrollment("1", courses, user.userName, ApprovalState.PENDING)
        self.database_manager.write(os.path.join(self.path, f"{user.userName}.json"), course_enrollment)
