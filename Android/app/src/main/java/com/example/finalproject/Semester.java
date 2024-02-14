package com.example.finalproject;

public class Semester {
    private String semesterID;
    private String term;
    private String year;
    private String sectionID;

    public Semester() {

    }

    public Semester(String semesterID, String term, String year, String sectionID) {
        this.semesterID = semesterID;
        this.term = term;
        this.year = year;
        this.sectionID = sectionID;
    }

    public String getSemesterID() {
        return semesterID;
    }

    public void setSemesterID(String semesterID) {
        this.semesterID = semesterID;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getSectionID() {
        return sectionID;
    }

    public void setSectionID(String sectionID) {
        this.sectionID = sectionID;
    }
}
