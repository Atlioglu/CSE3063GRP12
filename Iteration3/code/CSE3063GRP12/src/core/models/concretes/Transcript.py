class Transcript:
    def __init__(self, id, listOfSemester, gano, totalCreditTaken, totalCreditCompleted, currentSemester):
        self.id = id
        self.listOfSemester = listOfSemester  # dictionary mapping integer (semester number) to Semester instances
        self.gano = gano
        self.totalCreditTaken = totalCreditTaken
        self.totalCreditCompleted = totalCreditCompleted
        self.currentSemester = currentSemester

  
