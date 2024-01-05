from core.models.concretes.Lecturer import Lecturer

class Advisor(Lecturer):
    def __init__(self, id=None, firstName=None, lastName=None, userName=None, password=None, listOfCoursesGiven=None, listOfStudentIds=None):
        super().__init__(id, firstName, lastName, userName, password, listOfCoursesGiven)
        self.listOfStudentIds = listOfStudentIds if listOfStudentIds else []
    
    def to_dict(self):
        return {
            "id": self.id,
            "firstName": self.firstName,
            "lastName": self.lastName,
            "userName": self.userName,
            "password": self.password,
            "listOfCoursesGiven": [course.to_dict() if hasattr(course, 'to_dict') else course for course in self.listOfCoursesGiven],
            "listOfStudentIds": self.listOfStudentIds
        }