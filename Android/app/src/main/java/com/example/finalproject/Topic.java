package com.example.finalproject;

import androidx.annotation.NonNull;

public class Topic {
    private int topicID;
    private String courseID;
    private String topicName;

    public Topic() {

    }

    public Topic(int topicID, String courseID, String topicName) {
        this.topicID = topicID;
        this.courseID = courseID;
        this.topicName = topicName;
    }

    public int getTopicID() {
        return topicID;
    }

    public void setTopicID(int topicID) {
        this.topicID = topicID;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    @Override
    public String toString() {
        return "Topic{" +
                "topicID=" + topicID +
                ", courseID='" + courseID + '\'' +
                ", topicName='" + topicName + '\'' +
                '}';
    }
}
