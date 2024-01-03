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

