class Semester:
    def __init__(self, id, listOfCoursesTaken, creditsTaken, yano, gano,semesterNo):
        self.id = id
        self.listOfCoursesTaken = listOfCoursesTaken  # dictionary mapping course identifier to CourseGrade
        self.credits_taken = creditsTaken
        self.yano = yano
        self.gano = gano
        self.semesterNo = semesterNo
        
    def to_dict(self):
        dict = {
            "id": self.id,
            "listOfCoursesTaken": {courseId: courseGrade.to_dict() if hasattr(courseGrade, 'to_dict') else courseGrade for courseId, courseGrade in self.listOfCoursesTaken.items()},
            "credits_taken": self.credits_taken,
            "yano": self.yano,
            "gano": self.gano,
            "semesterNo": self.semesterNo
        }
        return dict