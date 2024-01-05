from core.models.abstracts.User import User

class Student(User):
    def __init__(self, id=None, firstName=None, lastName=None, userName=None, password=None, advisor=None, listOfLectureSessions=None, transcript=None):
        super().__init__(id, firstName, lastName, userName, password)
        self.advisor = advisor
        self.listOfLectureSessions = listOfLectureSessions if listOfLectureSessions else []
        self.transcript = transcript
    
    def to_dict(self):
        return {
            "id": self.id,
            "firstName": self.firstName,
            "lastName": self.lastName,
            "userName": self.userName,
            "password": self.password,
            "advisor": self.advisor.to_dict() if self.advisor else None,
            "listOfLectureSessions": [lectureSession.to_dict() if hasattr(lectureSession, 'to_dict') else lectureSession for lectureSession in self.listOfLectureSessions],
            "transcript": [course.to_dict() if hasattr(course, 'to_dict') else course for course in self.transcript]
        }