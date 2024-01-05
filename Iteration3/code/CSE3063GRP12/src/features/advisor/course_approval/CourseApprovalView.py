class CourseApprovalView:
    def showPendingCourseEnrollments(self, courseEnrollmentArrayList):
        print("Pending Course Enrollments (enter q for exit)")
        for i, enrollment in enumerate(courseEnrollmentArrayList, start=1):
            print(f"{i}. {enrollment.getStudentId()}")

    def showErrorMessage(self, e):
        print(f"Error: {e.getMessage()}")

    def showPromptMessage(self):
        print("Please enter your selection: ")
