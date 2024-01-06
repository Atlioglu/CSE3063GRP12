import os
from core.models.concretes.Advisor import Advisor
from core.models.concretes.Student import Student
from core.database.concretes.JsonManager import JsonManager
from core.exceptions.UserNotFoundException import UserNotFoundException
from core.exceptions.WrongPasswordException import WrongPasswordException
from core.general_providers.SessionController import SessionController
from core.general_providers.AppConstant import AppConstant
from core.general_providers.InstanceManager import InstanceManager
from core.enums.UserType import UserType

class UserRepository:
    def __init__(self):
        self.path = os.getcwd() + AppConstant.getInstance().get_base_path() + "user/"
        self.advisor_path = self.path + "advisor"
        self.student_path = self.path + "student"
        self.database_manager = InstanceManager().get_database_instance()

    def login_check(self, user_name, password):
        user = None
        user_type = self.get_user_type(user_name)
        if user_type == UserType.Student:
            user = self.database_manager.read(f"{self.student_path}/{user_name}.json", Student)
        elif user_type == UserType.Advisor:
            user = self.database_manager.read(f"{self.advisor_path}/{user_name}.json", Advisor)

       
        if user is None:
            raise UserNotFoundException()
        if user.password != password:
            raise WrongPasswordException()
        else:
            self.set_current_user(user)
      

    def set_current_user(self, user):
        SessionController.getInstance().set_current_user(user)


  
    def get_students_by_advisor(self, advisor):
        students = []
        for student_id in advisor.listOfStudentIds:
            student = self.database_manager.read(f"{self.student_path}/{student_id}.json", Student)
            students.append(student)
        return students


    def get_user(self, user_name):
        user = None
        user_type = self.get_user_type(user_name)
        if user_type == UserType.Student:
            user = self.database_manager.read(f"{self.student_path}/{user_name}.json", Student)
        elif user_type == UserType.Advisor:
            user = self.database_manager.read(f"{self.advisor_path}/{user_name}.json", Advisor)

        if user is None:
            raise UserNotFoundException()
     

        return user

    def get_user_type(self, user_name):
        if len(user_name) < 2:
            raise UserNotFoundException() 
        if user_name[0] == 'o':
            return UserType.Student
        elif user_name[0] == 'a':
            return UserType.Advisor
        else:
            raise UserNotFoundException()