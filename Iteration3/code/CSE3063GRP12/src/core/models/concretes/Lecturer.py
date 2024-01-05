from core.models.abstracts.User import User

class Lecturer(User):
    def __init__(self, id=None, firstName=None, lastName=None, userName=None, password=None, listOfCoursesGiven=None):
        super().__init__(id, firstName, lastName, userName, password)
        self.listOfCoursesGiven = listOfCoursesGiven if listOfCoursesGiven else []

    def set_list_of_courses_given(self, listOfCoursesGiven):
        self.listOfCoursesGiven = listOfCoursesGiven

    def get_list_of_courses_given(self):
        return self.listOfCoursesGiven
    
    def to_dict(self):
        return {
            "id": self.id,
            "firstName": self.firstName,
            "lastName": self.lastName,
            "userName": self.userName,
            "password": self.password,
            "listOfCoursesGiven": [course.to_dict() if hasattr(course, 'to_dict') else course for course in self.listOfCoursesGiven]
        }