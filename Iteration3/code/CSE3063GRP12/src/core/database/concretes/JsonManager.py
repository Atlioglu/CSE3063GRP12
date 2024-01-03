import json
from core.database.abstracts.DatabaseManager import DatabaseManager  # Assuming DatabaseManager is defined elsewhere

class JsonManager(DatabaseManager):
    def read(self, path, class_of_t):
        """
        Reads JSON data from the specified path and returns an instance of class_of_t.
        """
        try:
            with open(path, 'r') as file:
                data = json.load(file)
                return class_of_t(**data)  # Assuming class_of_t can be initialized with a dictionary
        except Exception as e:
            # Handle or log the exception
            return None

    def write(self, path, object):
        """
        Converts an object to JSON and writes it to the specified path.
        """
        try:
            with open(path, 'w') as file:
                json.dump(object.__dict__, file)  # Convert the object's attributes to JSON
        except Exception as e:
            # Handle or log the exception
            pass