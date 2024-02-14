# Mazen Batrawi - 1190102
# Ibrahim Nobani - 1190278

import csv
import os
from abc import ABC
from os import walk
from optparse import OptionParser

courseName = ""  # The name of the course we're working on
nonValidPRFiles = []  # A list that collects the non valid participation reports
nonValidARFiles = []  # A list that collects the non valid attendance reports

# Option parser section to give default values for the variables or take the values by the user from the terminal
parser = OptionParser()

parser.add_option("--p", "--P", type="float", dest="P", default=0.0,
                  help="Consider students who attended more than P minutes in the meeting")

parser.add_option("--tb", "--TB", type="float", dest="TB", default=-1.0,
                  help="All entries within TB minutes of the first entry will be dropped")

parser.add_option("--te", "--TE", type="float", dest="TE", default=-1.0,
                  help="All entries within TE minutes of the last entry will be dropped")

parser.add_option("--arp", "--ARP", type="string", dest="attendanceReportsPath",
                  default=r'C:\Users\mass\Desktop\project\projectt\MeetingAttendanceReports',
                  help="Attendance reports path")

parser.add_option("--prp", "--PRP", type="string", dest="participationReportsPath",
                  default=r'C:\Users\mass\Desktop\project\projectt\MeetingParticipationReports',
                  help="Participation reports path")

parser.add_option("--slp", "--SLP", type="string", dest="studentListPath",
                  default=r'C:\Users\mass\Desktop\project\projectt\StudentLists\ENCS3130-StudentList.csv',
                  help="Students list path")

parser.add_option("--orp", "--ORP", type="string", dest="outputPath",
                  default=r'C:\Users\mass\Desktop\project\projectt\OutputReports',
                  help="Output reports path")

(options, args) = parser.parse_args()  # To access the above variables by using options.nameOfVariable


# An abstract class that has the functions needed for the Course class
class abstract(ABC):
    def readStudentList(self):
        pass

    def readAttendance(self):
        pass

    def readParticipation(self):
        pass

    def setState(self):
        pass

    def fill(self):
        pass

    def printFiles(self):
        pass


# Returns the passed time in minutes
def getMinutes(time):
    minutes = 0.0
    hour = int(time[0] + time[1])
    minutes += hour * 60
    minute = int(time[3] + time[4])
    minutes += minute
    second = int(time[6] + time[7])
    minutes += second / 60.0
    return minutes


# Checks if the name of the file follows the desired style
def check(File):
    if len(File) != 5:
        return False
    month = int(File[1])
    day = int(File[2])
    global courseName
    return 1 <= month <= 12 and 1 <= day <= 31 and len(File[1]) == 2 and len(File[2]) == 2 and len(
        File[3]) == 4 and len(File) == 5 and File[0] == courseName and (File[4] == 'AR.csv' or File[4] == 'PR.txt')


# This is the class which has the methods for reading the files for a specific course and generates the output files
# It has four attributes
class Course(abstract):
    _studentList = []  # Stores the student's information, the lectures attended and their participation
    _position = {}  # To know the position of the student in the student list
    _messageCount = {}  # To count the number of sent messages by the student per lecture
    _fileDates = []  # Stores the lecture's dates

    def __init__(self):  # The course's class constructor that initializes the attributes
        self._studentList = []
        self._position = {}
        self._messageCount = {}
        self._fileDates = []

    @classmethod
    def readStudentList(cls):  # A method to read the student list from it's path
        pos = 0
        with open(options.studentListPath) as csv_file:  # Taking the path from the option parser
            path = options.studentListPath.split('\\')
            nameCourse = path[len(path) - 1].split('-')
            global courseName
            courseName = nameCourse[0]  # Storing the course's name and code in this global variable
            csv_reader = csv.reader(csv_file, delimiter=',')
            currentRow = 0
            for line in csv_reader:
                # The first line in the file is for specifying the content of the file so it's ignored
                if currentRow == 0:
                    currentRow += 1
                    continue
                line[1] = line[1].strip()  # Removing the white spaces before and after the string
                currentStudent = [Student(line[1], line[0])]
                cls._studentList.append(currentStudent)  # Adding a student object to the student list
                cls._position[line[1]] = pos  # Saving the student's position in the student list
                pos += 1

    @classmethod
    def readAttendance(cls):  # A method to read the class attendance sheets
        nonValid = []  # Receives the non valid attendance reports
        # To go through the files in the directory
        for rpath, renames, filenames in walk(options.attendanceReportsPath):
            global nonValidARFiles
            for File in filenames:  # To go through file names in the filenames list
                checkFile = File.split('-')
                # Checking if the file is in the desired form, if not, we mark it as a non valid file
                if not check(checkFile):
                    nonValidARFiles.append(File)
                    continue
                tmpFile = "MeetingAttendanceReports\\" + File
                with open(tmpFile) as csv_file:  # Opening the csv file and reading it's lines
                    csv_reader = csv.reader(csv_file, delimiter=',')
                    currentRow = 0
                    for information in csv_reader:  # Traversing the file line by line
                        # The first line in the file is for specifying the content of the file so it's ignored
                        if currentRow == 0:
                            currentRow += 1
                            continue
                        name = ""  # A variable to store the name of the current person we're at
                        for c in information[0]:
                            # Some students have their name written twice so we take the part between brackets
                            if c == '(':
                                name = ""
                            if c.isalpha() or c == ' ':  # Taking alphabets from the name
                                name += c
                        name = name.strip()  # Removing the white spaces before and after the string
                        fullName = name.split(' ')
                        found = 0  # A boolean to tell if the person was found in the student list
                        # Searching for the student in the student list by finding a subset in the full name that
                        # matches the current name
                        for person in cls._studentList:
                            cnt = 0
                            for subName in fullName:
                                if subName.lower() in person[0]._name.lower():
                                    cnt += 1
                            # The name was found so we add a lecture object to the student object in the student list
                            if cnt == len(fullName):
                                filename = checkFile[1] + '-' + checkFile[2] + '-' + checkFile[3]
                                anotherList = [
                                    Lecture(float(information[1]), filename, "", person[0]._name, person[0]._number)]
                                cls._studentList[cls._position[person[0]._name]].append(anotherList)
                                found = 1
                                break
                        if not found:  # If the student was not found, we add them to the non valid list
                            date = courseName + '-' + checkFile[1] + '-' + checkFile[2] + '-' + checkFile[3] + \
                                   '-AR-NV.csv '
                            nonValid.append([date, information])
        #  Writing the non valid information to the file
        os.chdir(options.outputPath)
        for person in nonValid:
            with open(person[0], 'a', encoding='UTF8', newline='') as File:
                writer = csv.writer(File)
                writer.writerow(person[1])

    @classmethod
    def readParticipation(cls):  # A method to read the class participation files
        # To go through the files in the directory
        for rpath, renames, filenames in walk(options.participationReportsPath):
            global nonValidPRFiles
            for File in filenames:  # To go through file names in the filenames list
                checkFile = File.split('-')
                # Checking if the file is in the desired form, if not, we mark it as a non valid file
                if not check(checkFile):
                    nonValidPRFiles.append(File)
                    continue
                # Adding the date of the class to the lecture's dates
                cls._fileDates.append(checkFile[1] + '-' + checkFile[2] + '-' + checkFile[3])
                os.chdir(options.participationReportsPath)
                writer = open(File, encoding="utf8")
                lines = writer.readlines()
                # Variables to store the time of sending of the first and last message
                firstHour, firstMinute, firstSecond, lastHour, lastMinute, lastSecond = "", "", "", "", "", ""
                currentRow = 0
                # This loop is for getting the time of the first and last message
                for line in lines:
                    time = line.split(':')
                    if len(time) == 4:  # The correct form of the message
                        if currentRow == 0:  # The first message, so we store it
                            firstHour = time[0]
                            firstMinute = time[1]
                            firstSecond = time[2][:2]
                            currentRow += 1
                        # Assuming that this message is the last
                        lastHour = time[0]
                        lastMinute = time[1]
                        lastSecond = time[2][:2]
                os.chdir(options.outputPath)
                for line in lines:
                    time = line.split(':')
                    if len(time) == 4:
                        hours = time[0]
                        minutes = time[1]
                        seconds = time[2][:2]
                        first = getMinutes(firstHour + ':' + firstMinute + ':' + firstSecond)
                        second = getMinutes(hours + ':' + minutes + ':' + seconds)
                        # Comparing the first message with the current one, if the TB condition is satisfied, then it's
                        # not added to the class score sheet
                        if second - first <= options.TB:
                            date = courseName + '-' + checkFile[1] + '-' + checkFile[2] + '-' + checkFile[3] + \
                                   '-PR-NV.txt'
                            with open(date, "a", encoding="utf-8") as writer:
                                writer.write(line)
                                writer.close()
                            continue
                        third = getMinutes(lastHour + ':' + lastMinute + ':' + lastSecond)
                        # Comparing the last message with the current one, if the TE condition is satisfied, then it's
                        # not added to the class score sheet
                        if third - second <= options.TE:
                            date = courseName + '-' + checkFile[1] + '-' + checkFile[2] + '-' + checkFile[3] + \
                                   '-PR-NV.txt'
                            with open(date, "a", encoding="utf-8") as writer:
                                writer.write(line)
                                writer.close()
                            continue
                        # This variable holds the name of the sender
                        result = line[line.find(" From ") + len(" From "): line.find(" to ")]
                        name = ""
                        # A variable to store the name of the current person we're at
                        for c in result:
                            # Some students have their name written twice so we take the part between brackets
                            if c == '(':
                                name = ""
                            if c.isalpha() or c == ' ':  # Taking the alphabets only
                                name += c
                        name = name.strip()  # Removing the white spaces before and after the string
                        fullName = name.split(' ')
                        found = 0  # A boolean to tell if the person was found in the student list
                        # Searching for the student in the student list by finding a subset in the full name that
                        # matches the current name
                        for person in cls._studentList:
                            cnt = 0
                            for subName in fullName:
                                if subName.lower() in person[0]._name.lower():
                                    cnt += 1
                            filename = checkFile[1] + '-' + checkFile[2] + '-' + checkFile[3]
                            if cnt == len(fullName):
                                if len(person) == 1:
                                    continue
                                # The name was found so we add a lecture object to the student object in the student list
                                found = 1
                                # Searching for the lecture to add a Message object to it
                                for i in range(1, len(person)):
                                    if person[i][0]._lecture == filename:
                                        person[i].append(
                                            Message(str(person[0]._name), str(hours), str(minutes), str(seconds),
                                                    str(filename), person[i][0]._timeAttended, person[i][0]._lecture,
                                                    "", person[0]._name, person[0]._number))
                                        if person[0]._name in cls._messageCount:
                                            if filename in cls._messageCount[person[0]._name]:
                                                cls._messageCount[person[0]._name][filename] += 1
                                            else:
                                                cls._messageCount[person[0]._name][filename] = 1
                                        else:
                                            cls._messageCount[person[0]._name] = {}
                                            cls._messageCount[person[0]._name][filename] = 1
                                        break
                        if not found:  # If the student was not found, we write them to the file
                            date = courseName + '-' + checkFile[1] + '-' + checkFile[2] + '-' + checkFile[3] + \
                                   '-PR-NV.txt'
                            with open(date, "a", encoding="utf-8") as writer:
                                writer.write(line)
                                writer.close()

    # A method to give a state of absence or presence for the student in all of the lectures
    @classmethod
    def setState(cls):
        for person in cls._studentList:
            if len(person) == 1:
                continue
            for i in range(1, len(person)):
                if float(person[i][0]._timeAttended) >= float(options.P):
                    person[i][0]._state = "x"
                else:
                    person[i][0]._state = "a"

    # A method to give each student the number of messages they've sent in a certain lecture
    @classmethod
    def fill(cls):
        for person in cls._messageCount:
            for date in cls._fileDates:
                if date in cls._messageCount[person]:
                    continue
                cls._messageCount[person][date] = 0

    # Printing the data to the files
    @classmethod
    def printFiles(cls):
        global courseName
        filename = courseName + '-ClassAttendanceSheet.csv'
        os.chdir(options.outputPath)
        with open(filename, 'w') as csv_file:
            writer = csv.writer(csv_file)
            field = "student ID, Student Name, " + ', '.join(cls._fileDates)
            fields = field.split(", ")
            writer.writerow(fields)
            if cls._studentList:
                for person in cls._studentList:
                    if len(person) == 1:
                        continue
                    rows = []
                    row = ""
                    row += person[0]._number + ', ' + person[0]._name + ", "
                    for date in cls._fileDates:
                        foundDate = 0
                        for i in range(1, len(person)):
                            if person[i][0]._lecture == date:
                                if cls._fileDates[0] != date:
                                    row += ', '
                                row += person[i][0]._state
                                foundDate = 1
                                break
                        if foundDate == 0:
                            if cls._fileDates[0] != date:
                                row += ', '
                            row += 'a'
                    rows.append(row.split(', '))
                    writer.writerows(rows)

        filename = courseName + '-ClassScoreSheet.csv'
        with open(filename, 'w') as csv_file:
            writer = csv.writer(csv_file)
            field = "student ID, Student Name, " + ', '.join(cls._fileDates)
            fields = field.split(", ")
            writer.writerow(fields)
            if cls._studentList:
                for person in cls._studentList:
                    if len(person) == 1:
                        continue
                    if person[0]._name in cls._messageCount:
                        row = person[0]._number + ', ' + person[0]._name + ', ' + ', '.join(
                            str(cls._messageCount[person[0]._name][files]) for files in cls._fileDates)
                        rows = [row.split(',')]
                        writer.writerows(rows)


# A class to store the student's name and id
class Student:

    def __init__(self, name, number):  # Argumentative constructor
        self._name = name
        self._number = number


# A child class of the student class to store the lecture and the time attended in it by the student
class Lecture(Student):

    def __init__(self, timeAttended, lecture, state, name, number):  # Argumentative constructor
        super().__init__(name, number)
        self._timeAttended = timeAttended
        self._lecture = lecture
        self._state = state


# A child class of the lecture class to store the information of the message
class Message(Lecture):

    def __init__(self, sender, hours, minutes, seconds, file, timeAttended, lecture, state, name,
                 number):
        super().__init__(timeAttended, lecture, state, name, number)
        self._sender = sender
        self._hours = hours
        self._minutes = minutes
        self._seconds = seconds
        self._file = file


# A child class from the Course class for the course ENCS3130
class ENCS3130(Course):

    def __init__(self):
        super().__init__()


# Creating the course and calling the functions associated with it
linux = ENCS3130()
linux.readStudentList()
linux.readAttendance()
linux.readParticipation()
linux.setState()
linux.fill()
linux.printFiles()

# Writing the non valid participation and attendance files to two files for each one of them
file1 = courseName + '-NonValidARFiles.txt'
file2 = courseName + '-NonValidPRFiles.txt'

os.chdir(options.outputPath)

for file in nonValidARFiles:
    with open(file1, "a", encoding="utf-8") as f:
        f.write(file + "\n")
    f.close()

for file in nonValidPRFiles:
    with open(file2, "a", encoding="utf-8") as f:
        f.write(file + "\n")
    f.close()
