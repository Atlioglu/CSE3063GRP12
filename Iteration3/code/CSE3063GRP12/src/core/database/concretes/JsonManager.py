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
                if hasattr(object, 'to_dict'):
                        data = object.to_dict()  # Use the to_dict() method if available
                else:
                    data = object.__dict__
                json.dump(data, file)
        except Exception as e:
            pass