class CourseEnrollment:
    def __init__(self, id, selectedCourseList, studentId, approvalState, approved_course_list=[], rejected_course_list=[]):
        self.id = id
        self.selectedCourseList = selectedCourseList  # list of Course instances
        self.approved_course_list = approved_course_list  
        self.rejected_course_list = rejected_course_list
        self.studentId = studentId
        self.approvalState = approvalState  # instance of ApprovalState enum
    
    def to_dict(self):
        return {
            "id": self.id,
            "selectedCourseList": [course.to_dict() if hasattr(course, 'to_dict') else course for course in self.selectedCourseList],
            "approved_course_list": [course.to_dict() if hasattr(course, 'to_dict') else course for course in self.approved_course_list],
            "rejected_course_list": [course.to_dict() if hasattr(course, 'to_dict') else course for course in self.rejected_course_list],
            "studentId": self.studentId,
            "approvalState": self.approvalState.value
        }

