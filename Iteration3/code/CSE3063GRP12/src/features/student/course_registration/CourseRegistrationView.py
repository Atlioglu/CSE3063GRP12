class CourseRegistrationView:

    def show_course_list(self, course_list, approval_state=None):
        if approval_state is None:
            print(f"{'No.':<3}\t{'Course Code':<20} {'Course Name':<50} {'Quota':<8} {'Current Quota':<15} {'Semester No.':<8} {'ECTS':<8}")
            print("=" * 128)
            for i, course in enumerate(course_list):
                print(f"[{i + 1:<1}]\t{course.courseCode:<20} {course.name:<50} {course.quota:<8} {course.currentQuota:<15} {course.semester:<8} {course.credit:<8}")
            print("=" * 128)
        else:
            print("Your list of courses for this semester:")
            for i, course in enumerate(course_list):
                print(f"{i + 1}. {course.get('name')}")
            print(f"Your registration status is: {approval_state} \n\n")

    def show_success_message(self):
        print("Your enrollment has been saved successfully")
    
    def show_error_message(self, exception):
        print(f"An error occurred while saving your enrollment: {exception}")