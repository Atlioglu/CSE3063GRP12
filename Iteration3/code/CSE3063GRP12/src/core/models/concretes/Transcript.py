class Transcript:
    def __init__(self,studentId, transcriptId, listOfSemesters, gano, totalCreditTaken, totalCreditCompleted, currentSemester):
        self.studentId = studentId
        self.transcriptId = transcriptId
        self.listOfSemesters = listOfSemesters  # dictionary mapping integer (semester number) to Semester instances
        self.gano = gano
        self.totalCreditTaken = totalCreditTaken
        self.totalCreditCompleted = totalCreditCompleted
        self.currentSemester = currentSemester
    
    def to_dict(self):
        dict = {
            "studentId": self.studentId,
            "transcriptId": self.transcriptId,
            "listOfSemesters": {semesterNo: semester.to_dict() if hasattr(semester, 'to_dict') else semester for semesterNo, semester in self.listOfSemesters.items()},
            "gano": self.gano,
            "totalCreditTaken": self.totalCreditTaken,
            "totalCreditCompleted": self.totalCreditCompleted,
            "currentSemester": self.currentSemester
        }
        return dict

  
