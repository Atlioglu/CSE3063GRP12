class Semester:
    def __init__(self, id, listOfCoursesTaken, creditsTaken, yano, semesterNo):
        self.id = id
        self.listOfCoursesTaken = listOfCoursesTaken  # dictionary mapping course identifier to CourseGrade
        self.credits_taken = creditsTaken
        self.yano = yano 
        self.semesterNo = semesterNo