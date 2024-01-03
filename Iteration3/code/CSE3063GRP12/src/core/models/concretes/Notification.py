from datetime import datetime

class Notification:
    def __init__(self, message, date, read):
        self.message = message
        self.date = date if date is not None else datetime.now()  # using datetime for date handling
        self.read = read

