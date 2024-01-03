class CourseEnrollment:
    def __init__(self, id, selectedCourseList, studentId, approvalState):
        self.id = id
        self.selectedCourseList = selectedCourseList  # list of Course instances
        self.approved_course_list = []  
        self.rejected_course_list = [] 
        self.studentId = studentId
        self.approvalState = approvalState  # instance of ApprovalState enum

