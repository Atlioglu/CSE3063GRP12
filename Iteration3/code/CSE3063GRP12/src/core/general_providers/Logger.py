import os
import traceback
from AppConstant import AppConstant

class Logger:

    def __init__(self, path=None):
        if path is None:
            path = AppConstant.getInstance().get_base_path()+"log"+"log.txt"
        self.__filePath = path
        self.__className = ""
        self.__methodName = ""
        try:
            if not os.path.exists(self.__filePath):
                open(self.__filePath, 'w').close()
        except IOError:
            pass

    def write(self, input_text):
        try:
            stack_trace = traceback.extract_stack()

            if len(stack_trace) >= 3:
                caller = stack_trace[-3]
                self.__className = caller.filename
                self.__methodName = caller.name

            # Write content to the file
            message = f"{input_text} is user's choice in method {self.__methodName} in class {self.__className}\n"
            with open(self.__filePath, 'a') as file:
                file.write(message)

        except IOError:
            pass

    def write_exception(self, input_text):
        try:
            with open(self.__filePath, 'a') as file:
                file.write(f"{input_text}\n")
        except IOError:
            pass
