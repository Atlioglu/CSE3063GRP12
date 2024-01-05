class Course:
    def __init__(self, id, courseCode, name, credit, session, prerequisites, quota, currentQuota, semester):
        self.id = id
        self.courseCode = courseCode
        self.name = name
        self.credit = credit
        self.session = session  # instance of CourseSession class
        self.prerequisites = prerequisites  # list of Course instances
        self.quota = quota
        self.currentQuota = currentQuota
        self.semester = semester
    
    def to_dict(self):
        return {
            "id": self.id,
            "courseCode": self.courseCode,
            "name": self.name,
            "credit": self.credit,
            "session": self.session.to_dict() if hasattr(self.session, 'to_dict') else self.session,
            "prerequisites": [prerequisite.to_dict() if hasattr(prerequisite, 'to_dict') else prerequisite for prerequisite in self.prerequisites],
            "quota": self.quota,
            "currentQuota": self.currentQuota,
            "semester": self.semester.to_dict() if hasattr(self.semester, 'to_dict') else self.semester
        }

