import unittest
from core.repositories.UserRepository import UserRepository  
from core.general_providers.SessionController import SessionController  
from core.exceptions.UserNotFoundException import UserNotFoundException  
from core.exceptions.WrongPasswordException import WrongPasswordException  

class UserRepositoryTest(unittest.TestCase):

    def test_login_check_successful(self):
        userRepository = UserRepository()
        userRepository.login_check("o150120060", "150120060")
        user = SessionController.getInstance().get_current_user()
        self.assertIsNotNone(user)

    def test_login_check_user_not_found(self):
        userRepository = UserRepository()
        with self.assertRaises(UserNotFoundException):
            userRepository.login_check("o1501asdasd12", "150120060")

    def test_login_check_wrong_password(self):
        userRepository = UserRepository()
        with self.assertRaises(WrongPasswordException):
            userRepository.login_check("o150120060", "5151515")

if __name__ == '__main__':
    unittest.main()
