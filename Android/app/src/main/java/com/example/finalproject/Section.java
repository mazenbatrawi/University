package com.example.finalproject;

public class Section {
    private int sectionID;
    private String instructorEmail;
    private int courseID;
    private int maxTrainees;
    private String startTime, endTime;
    private String days;
    private String room, startDate, endDate;


    public Section() {

    }

    public Section(int sectionID, String instructorEmail, int courseID, int maxTrainees,
                   String startTime, String endTime, String days, String room, String startDate, String endDate) {
        this.sectionID = sectionID;
        this.instructorEmail = instructorEmail;
        this.courseID = courseID;
        this.maxTrainees = maxTrainees;
        this.startTime = startTime;
        this.endTime = endTime;
        this.days = days;
        this.room = room;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getSectionID() {
        return sectionID;
    }

    public void setSectionID(int sectionID) {
        this.sectionID = sectionID;
    }

    public String getInstructorEmail() {
        return instructorEmail;
    }

    public void setInstructorEmail(String instructorEmail) {
        this.instructorEmail = instructorEmail;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public int getMaxTrainees() {
        return maxTrainees;
    }

    public void setMaxTrainees(int maxTrainees) {
        this.maxTrainees = maxTrainees;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return
                "Section ID = " + sectionID +
                "\nInstructor Email = '" + instructorEmail + '\'' +
                "\nCourse ID = " + courseID +
                "\nMax Trainees = " + maxTrainees +
                "\nStart Time = '" + startTime + '\'' +
                "\nEnd Time = '" + endTime + '\'' +
                "\nDays = '" + days + '\'' +
                "\nRoom = '" + room + '\'' +
                "\nStart Date = '" + startDate + '\'' +
                "\nEnd Date = '" + endDate + '\'';
    }
}
