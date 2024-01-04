import json
from core.database.abstracts.DatabaseManager import DatabaseManager

class JsonManager(DatabaseManager):
    def read(self, path, class_of_t):
        try:
            with open(path, 'r') as file:
                data = json.load(file)
                return class_of_t(**data) 
        except Exception as e:
            return None

    def write(self, path, object):
        try:
            with open(path, 'w') as file:
                json.dump(object.__dict__, file)  # Convert the object's attributes to JSON
        except Exception as e:
            pass