class UserNotFoundException(Exception):
    def __init__(self, message="User Not Found!"):
        super().__init__(message)