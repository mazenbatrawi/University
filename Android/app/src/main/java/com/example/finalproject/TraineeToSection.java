package com.example.finalproject;

public class TraineeToSection {
    private int traineeToSectionID;
    private int sectionID;
    private String traineeEmail;
    private int status;

    public TraineeToSection() {

    }


    public TraineeToSection(int traineeToSectionID, int sectionID, String traineeEmail, int status) {
        this.traineeToSectionID = traineeToSectionID;
        this.sectionID = sectionID;
        this.traineeEmail = traineeEmail;
        this.status = status;
    }

    public int getTraineeToSectionID() {
        return traineeToSectionID;
    }

    public void setTraineeToSectionID(int traineeToSectionID) {
        this.traineeToSectionID = traineeToSectionID;
    }

    public int getSectionID() {
        return sectionID;
    }

    public void setSectionID(int sectionID) {
        this.sectionID = sectionID;
    }

    public String getTraineeEmail() {
        return traineeEmail;
    }

    public void setTraineeEmail(String traineeEmail) {
        this.traineeEmail = traineeEmail;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "TraineeToSection{" +
                "traineeToSectionID=" + traineeToSectionID +
                ", sectionID=" + sectionID +
                ", traineeEmail='" + traineeEmail + '\'' +
                ", status=" + status +
                '}';
    }
}
