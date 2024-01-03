class UnexpectedInputException(Exception):
    def __init__(self, message="Unexpected Input!"):
        super().__init__(message)