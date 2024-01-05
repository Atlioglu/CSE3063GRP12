import json
import os
from pathlib import Path

from core.general_providers.InstanceManager import InstanceManager
from core.general_providers.AppConstant import AppConstant
from core.models.concretes.Course import Course

class CourseRepository:
    def __init__(self):
        self.database_manager = InstanceManager().get_database_instance()
        self.path = os.getcwd() + AppConstant.getInstance().get_base_path() + "course/"
    
    def get_courses_by_semester(self, semester_id):
        folder_path = os.path.join(self.path, str(semester_id))
        return self.read_all_json_files_in_folder(folder_path)   
    
    def create_course(self, course):
        try:
            course_path = os.path.join(self.path, str(course['semester']), f"{course['course_code']}.json")
            self.database_manager.write(course_path, course)
        except Exception as e:
            print(f"An error occurred while creating a course: {e}")

    def read_all_json_files_in_folder(self, folder_path):
        try:
            courses = []
            path = Path(folder_path)
            if path.is_dir():
                for file in path.glob('*.json'):
                    course = self.database_manager.read(file,Course)
                    if course:
                        courses.append(course)
            return courses
        except Exception as e:
            print(f"An error occurred while reading all json files in folder: {e}")
            return None
    
    def findCourseByCode(self, courseCode):
        # walking through the files
        for root, dirs, files in os.walk(self.path):
            for file in files:
                if file.endswith('.json') and courseCode in file:
                    file_path = os.path.join(root, file)
                    try:
                        return self.database_manager.read(file_path, Course)
                    except Exception as e:
                        print(f"Course with {courseCode} is coulnd't found. Error: {e}")
                        return None
        return None
    
    def getQuota(self, courseCode):
        course = self.findCourseByCode(courseCode)
        if course is not None:
            return course.quota
        else:
            raise ValueError(f"Course with code {courseCode} not found.")

    def getCurrentQuota(self, courseCode):
        course = self.findCourseByCode(courseCode)
        if course is not None:
            return course.currentQuota
        else:
            raise ValueError(f"Course with code {courseCode} not found.")
        
    def findCoursesWithCourseIds(self, ids):
        matched_courses = []
        # Walk through the files in the specified path
        for root, dirs, files in os.walk(self.path):
            for file in files:
                # Check if file is a JSON file
                if file.endswith('.json'):
                    # Check if file name contains any of the ids
                    if any(id in file for id in ids):
                        file_path = os.path.join(root, file)
                        try:
                            # Read the course information from the file
                            course = self.database_manager.read(file_path, Course)
                            matched_courses.append(course)
                        except Exception as e:
                            print(e)
        return matched_courses
    
    def updateCurrentQuota(self, courseEnrollment):
        if courseEnrollment is None:
            print("CourseEnrollment is null.")
            return

        approved_courses = courseEnrollment.approved_course_list
        if approved_courses is None or not approved_courses:
            print("No approved courses found for the student.")
            return

        for selected_course in approved_courses:
            course_code = selected_course.courseCode  # Assuming courseCode is an attribute
            print(f"Processing course with code: {course_code}")

            # Find the corresponding course in the repository
            repository_course = self.findCourseByCode(course_code)
            if repository_course is not None:
                # Update the currentQuota for the course in the repository
                repository_course.currentQuota += 1

                # Save the updated course back to the repository
                try:
                    file_path = f"{self.path}/{repository_course.semester}/{repository_course.courseCode}.json"
                    self.database_manager.write(file_path, repository_course)
                    print(f"Quota updated successfully for course: {course_code}")
                except Exception as e:
                    print(f"Error updating quota for {course_code}: {e}")
            else:
                print(f"Course with code {course_code} not found in the repository.")