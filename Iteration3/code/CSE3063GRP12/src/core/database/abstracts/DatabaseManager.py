from abc import ABC, abstractmethod

class DatabaseManager(ABC):
    @abstractmethod
    def read(self, path, class_of_t):
        """
        Reads data from the specified path and returns an instance of class_of_t.
        """
        pass

    @abstractmethod
    def write(self, path, object):
        """
        Writes an object to the specified path.
        """
        pass