from core.models.concretes.Lecturer import Lecturer

class Advisor(Lecturer):
    def __init__(self, id=None, firstName=None, lastName=None, userName=None, password=None, listOfCoursesGiven=None, listOfStudentIds=None):
        super().__init__(id, firstName, lastName, userName, password, listOfCoursesGiven)
        self.listOfStudentIds = listOfStudentIds if listOfStudentIds else []