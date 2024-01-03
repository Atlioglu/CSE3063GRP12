class CourseEnrollment:
    def __init__(self, id, selectedCourseList, student_id, approvalState):
        self.id = id
        self.selectedCourseList = selectedCourseList  # list of Course instances
        self.approved_course_list = []  
        self.rejected_course_list = [] 
        self.student_id = student_id
        self.approvalState = approvalState  # instance of ApprovalState enum

