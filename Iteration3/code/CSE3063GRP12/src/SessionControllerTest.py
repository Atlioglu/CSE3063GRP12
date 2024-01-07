import unittest
from core.enums.UserType import UserType
from core.models.concretes.Student import Student
from core.general_providers.SessionController import SessionController
from core.models.concretes.Advisor import Advisor

class SessionControllerTest(unittest.TestCase):

    def test_user_type(self):
        session_controller = SessionController.getInstance()

        student = Student()
        advisor = Advisor()
        session_controller.set_current_user(student)

        user_type = session_controller.get_user_type()

        self.assertEqual(user_type, UserType.Student, "User type should be STUDENT")

    def test_set_current_user(self):
        student = Student()
        student.userName = "test"
        session_controller = SessionController.getInstance()
        session_controller.set_current_user(student)
        
        self.assertEqual(session_controller.get_current_user(), student, "Current user not set correctly")


if __name__ == '__main__':
    unittest.main()
    