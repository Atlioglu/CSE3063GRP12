import unittest
from core.models.concretes.Student import Student  # Assuming your Student class is in 'your_module'
from core.models.abstracts.User import User
from core.models.concretes.Advisor import Advisor

class StudentModelTest(unittest.TestCase):

    def test_user_equal(self):
        student = Student()
        student.userName = "test"
        student.password = "test"
        student2 = Student()
        student2.userName = "test"
        student2.password = "test"

        self.assertEqual(student, student2)

    def test_user_not_equal(self):
        student = Student()
        student.userName= "test"
        student.password = "test"
        student2 = Student()
        student2.userName= "test1"
        student2.password = "test1"

        self.assertNotEqual(student, student2)

    def test_user_type(self):
        student = Student()
        self.assertIsInstance(student, User)

if __name__ == '__main__':
    unittest.main()