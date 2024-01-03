from abc import ABC, abstractmethod

class Menu(ABC):

    @abstractmethod
    def get_item_message(self):
        pass

    @abstractmethod
    def navigate(self):
        pass

    @abstractmethod
    def is_notification(self):
        pass