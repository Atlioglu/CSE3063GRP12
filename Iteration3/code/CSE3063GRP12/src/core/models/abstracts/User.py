from abc import ABC, abstractmethod

class User(ABC):
    def __init__(self, id=None, firstName=None, lastName=None, userName=None, password=None):
        self.id = id
        self.firstName = firstName
        self.lastName = lastName
        self.userName = userName
        self.password = password

    def get_user_type(self):
        # TODO: this method should be tested whether it is used?
        pass

    def __eq__(self, other):
        if not isinstance(other, User):
            return False
        return self.userName == other.userName and self.password == other.password

    def __hash__(self):
        return hash((self.user_userNamename, self.password))