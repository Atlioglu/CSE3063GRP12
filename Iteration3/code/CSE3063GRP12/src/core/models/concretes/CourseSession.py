

class CourseSession:
    def __init__(self, id, courseSessions, lecturer):
        self.id = id
        self.courseSessions = courseSessions  # dictionary mapping CourseDay to a list of CourseSlot
        from core.models.concretes.Lecturer import Lecturer
        self.lecturer = Lecturer(**lecturer) 
        self.name = None
    
    def to_dict(self):
        return {
            "id": self.id,
            "courseSessions": {courseDay.value: [courseSlot.to_dict() if hasattr(courseSlot, 'to_dict') else courseSlot for courseSlot in courseSlots] for courseDay, courseSlots in self.courseSessions.items()},
            "lecturer": self.lecturer.to_dict() if hasattr(self.lecturer, 'to_dict') else self.lecturer,
            "name": self.name
        }

