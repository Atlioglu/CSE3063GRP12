from core.models.abstracts.User import User

class Student(User):
    def __init__(self, id=None, firstName=None, lastName=None, userName=None, password=None, advisor=None, listOfLectureSessions=None, transcript=None):
        super().__init__(id, firstName, lastName, userName, password)
        self.advisor = advisor
        self.listOfLectureSessions = listOfLectureSessions if listOfLectureSessions else []
        self.transcript = transcript