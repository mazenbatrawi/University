package com.example.finalproject;

public class CanTeach {
    private String canTeachID;
    private String courseID;
    private String email;

    public CanTeach(){

    }

    public CanTeach(String canTeachID, String courseID, String email) {
        this.canTeachID = canTeachID;
        this.courseID = courseID;
        this.email = email;
    }

    public String getCanTeachID() {
        return canTeachID;
    }

    public void setCanTeachID(String canTeachID) {
        this.canTeachID = canTeachID;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
