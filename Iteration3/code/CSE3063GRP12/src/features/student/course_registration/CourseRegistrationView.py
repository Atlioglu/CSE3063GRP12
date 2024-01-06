class CourseRegistrationView:

    def show_course_list(self, *args):
        # args[0] is the list of courses
        # args[1] is the approval state
        if len(args) == 1:
            print(f"{'No.':<3}\t{'Course Code':<20} {'Course Name':<50} {'Quota':<8} {'Current Quota':<15} {'Semester No.':<8} {'ECTS':<8}")
            print("=" * 128)
            for i, course in enumerate(args[0]):
                if isinstance(course, dict):
                    # Accessing dictionary keys
                    course_code = course.get('courseCode')
                    name = course.get('name')
                    quota = course.get('quota')
                    current_quota = course.get('currentQuota')
                    semester = course.get('semester')
                    credit = course.get('credit')
                else:
                    # Accessing attributes of Course object
                    course_code = course.courseCode
                    name = course.name
                    quota = course.quota
                    current_quota = course.currentQuota
                    semester = course.semester
                    credit = course.credit
                
                print(f"[{i + 1:<1}]\t{course_code:<20} {name:<50} {quota:<8} {current_quota:<15} {semester:<8} {credit:<8}")
            print("=" * 128)
        elif len(args) == 2:
            print("Your list of courses for this semester:")
            for i, course in enumerate(args[0]):
                print(f"{i + 1}. {course.get('name')}")
            print(f"Your registration status is: {args[1]} \n\n")
        else:
            print("Error: Invalid arguments passed to show_course_list()")

    def show_success_message(self):
        print("Your enrollment has been saved successfully")
    
    def show_error_message(self, exception):
        print(f"An error occurred while saving your enrollment: {exception}")