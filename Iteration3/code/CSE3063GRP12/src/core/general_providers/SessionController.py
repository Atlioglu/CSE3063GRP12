from core.models.concretes.Advisor import Advisor
from core.models.concretes.Student import Student

class SessionController:
    _instance = None
    _current_user = None

    def __new__(cls):
        if not cls._instance:
            cls._instance = super(SessionController, cls).__new__(cls)
        return cls._instance

    @classmethod
    def getInstance(cls):
        if cls._instance is None:
            cls._instance = SessionController()
        return cls._instance

    def get_current_user(cls):
        return cls._current_user


    def set_current_user(cls, user):
        cls._current_user = user

    def get_user_type(cls):
        if isinstance(cls._current_user, Student):
            return "Student"
        elif isinstance(cls._current_user, Advisor):
            return "Advisor"
        else:
            return None