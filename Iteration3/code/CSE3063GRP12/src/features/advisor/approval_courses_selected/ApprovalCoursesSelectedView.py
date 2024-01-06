class ApprovalCoursesSelectedView:

    def show_selected_courses(self,selected_course_list):
        print("====================================================|| Registration Management ||====================================================")
        print("###======List of Student Selected Courses:")
        print("=====================================================================================================================================")
        print("{:<3s}\t{:<20s} {:<50s} {:<8s} {:<10s}  {:<8s}".format("No.", "Course Code", "Course Name", "Quota", "Semester No.", "ECTS"))
        print("=====================================================================================================================================")

        for i, current_course in enumerate(selected_course_list):
            print(" [{:<1d}]\t{:<20s} {:<52s} {:<12d} {:<8d} {:<8d}".format(
                i + 1, current_course['courseCode'], current_course['name'], 
                current_course['quota'], current_course['semester'], current_course['credit']))

        print("=====================================================================================================================================")

    
    def show_success_message():
        print("You have successfully approved the course list")

  
    def show_error_message(self,exception):
        print("Error: " + str(exception))