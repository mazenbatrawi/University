package com.example.finalproject;

public class Schedule {
    private String scheduleID;
    private String sectionID;
    private String day;
    private String startTime;
    private String endTime;
    private String roomID;

    public Schedule() {

    }

    public Schedule(String scheduleID, String sectionID, String day, String startTime, String endTime, String roomID) {
        this.scheduleID = scheduleID;
        this.sectionID = sectionID;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.roomID = roomID;
    }

    public String getScheduleID() {
        return scheduleID;
    }

    public void setScheduleID(String scheduleID) {
        this.scheduleID = scheduleID;
    }

    public String getSectionID() {
        return sectionID;
    }

    public void setSectionID(String sectionID) {
        this.sectionID = sectionID;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
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

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }
}
