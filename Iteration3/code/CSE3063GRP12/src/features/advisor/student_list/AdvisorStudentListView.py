from core.exceptions.UnexpectedInputException import UnexpectedInputException

class AdvisorStudentListView:
    def showStudentList(self, studentArrayList):
        print("======================================================================================Student List======================================================================================")
        print("%-27s%-20s%-20s" % ("[Student ID]", "[First Name]", "[Last Name]"))
        print("========================================================================================================================================================================================")
        for i, student in enumerate(studentArrayList, start=1):
            print("[%d] %-20s %-20s %-20s" % (i, student.userName, student.firstName, student.lastName))
        print("========================================================================================================================================================================================")

    def showQuitMessage(self):
        print("Press q to return Main Menu: ")

    def showErrorMessage(self):
        raise UnexpectedInputException()
