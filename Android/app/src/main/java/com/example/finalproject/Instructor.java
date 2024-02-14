package com.example.finalproject;

public class Instructor {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String mobileNumber;
    private String address;
    private String specialization;
    private String degree;
    private String canTeach;
    private byte[] image;

    public Instructor() {

    }

    public Instructor(String email, String password, String firstName, String lastName, String mobileNumber,
                      String address, String specialization, String degree, String canTeach, byte[] image) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobileNumber = mobileNumber;
        this.address = address;
        this.specialization = specialization;
        this.degree = degree;
        this.canTeach = canTeach;
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getCanTeach() {
        return canTeach;
    }

    public void setCanTeach(String canTeach) {
        this.canTeach = canTeach;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Email: " + email + "\n" +
                "Password: " + password + "\n" +
                "First Name: " + firstName + "\n" +
                "Last Name: " + lastName + "\n" +
                "Mobile Number: " + mobileNumber + "\n" +
                "Address: " + address + "\n" +
                "Specialization: " + specialization + "\n" +
                "Degree: " + degree + "\n" +
                "Can Teach: " + canTeach + "\n";
    }
}
