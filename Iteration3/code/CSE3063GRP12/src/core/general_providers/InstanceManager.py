from core.database.concretes.JsonManager import JsonManager

class InstanceManager:
    _instance = None
    _database_manager = None

    def __new__(cls):
        if not cls._instance:
            cls._instance = super(InstanceManager, cls).__new__(cls)
        return cls._instance

    def get_database_instance(self):
        if self._database_manager is None:
            self._database_manager = JsonManager()
        return self._database_manager