class WrongPasswordException(Exception):
    def __init__(self, message="Wrong Password!"):
        super().__init__(message)