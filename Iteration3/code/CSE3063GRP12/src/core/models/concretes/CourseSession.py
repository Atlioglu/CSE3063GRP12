class CourseSession:
    def __init__(self, id, courseSessions, lecturer):
        self.id = id
        self.courseSessions = courseSessions  # dictionary mapping CourseDay to a list of CourseSlot
        self.lecturer = lecturer 
        self.name = None

