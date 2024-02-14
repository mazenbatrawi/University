package com.example.finalproject;

public class Notification {
    private int notID;
    private String notText;
    private int status;
    private String traineeEmail;

    public Notification(int notID, String notText, int status, String traineeEmail) {
        this.notID = notID;
        this.notText = notText;
        this.status = status;
        this.traineeEmail = traineeEmail;
    }

    public Notification() {
    }

    public int getNotID() {
        return notID;
    }

    public void setNotID(int notID) {
        this.notID = notID;
    }

    public String getNotText() {
        return notText;
    }

    public void setNotText(String notText) {
        this.notText = notText;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTraineeEmail() {
        return traineeEmail;
    }

    public void setTraineeEmail(String traineeEmail) {
        this.traineeEmail = traineeEmail;
    }

}
