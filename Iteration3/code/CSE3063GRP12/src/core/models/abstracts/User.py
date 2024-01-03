from abc import ABC, abstractmethod

class User(ABC):
    def __init__(self, id=None, first_name=None, last_name=None, user_name=None, password=None):
        self.id = id
        self.first_name = first_name
        self.last_name = last_name
        self.user_name = user_name
        self.password = password

    def get_user_type(self):
        # TODO: this method should be tested whether it is used?
        pass

    def __eq__(self, other):
        if not isinstance(other, User):
            return False
        return self.user_name == other.user_name and self.password == other.password

    def __hash__(self):
        return hash((self.user_name, self.password))