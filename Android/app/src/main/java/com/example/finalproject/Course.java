package com.example.finalproject;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Course {
    private int ID;
    private String courseID;
    private String courseName;
    private String prerequisites;
    private String startDate;
    private String endDate;
    private String registrationStart;
    private String registrationEnd;
    private byte[] image;

    public Course() {

    }

    public Course(int ID, String courseID, String courseName, String prerequisites, String startDate,
                  String endDate, String registrationStart, String registrationEnd, byte[] image) {
        this.ID = ID;
        this.courseID = courseID;
        this.courseName = courseName;
        this.prerequisites = prerequisites;
        this.startDate = startDate;
        this.endDate = endDate;
        this.registrationStart = registrationStart;
        this.registrationEnd = registrationEnd;
        this.image = image;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(String prerequisites) {
        this.prerequisites = prerequisites;
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

    public String getRegistrationStart() {
        return registrationStart;
    }

    public void setRegistrationStart(String registrationStart) {
        this.registrationStart = registrationStart;
    }

    public String getRegistrationEnd() {
        return registrationEnd;
    }

    public void setRegistrationEnd(String registrationEnd) {
        this.registrationEnd = registrationEnd;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    @NonNull
    @Override
    public String toString() {
        return  "ID: " + ID + "\n" +
                "Course ID: " + courseID + "\n" +
                "Course Name: " + courseName + "\n" +
                "Prerequisites: " + prerequisites + "\n" +
                "Start Date: " + startDate + "\n" +
                "End Date: " + endDate + "\n" +
                "Registration Start: " + registrationStart + "\n" +
                "Registration End: " + registrationEnd + "\n\n";
    }
}
