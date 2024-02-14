package com.example.finalproject;

import java.util.Arrays;

public class TraineeInSection {
    private String email;
    private String name;
    private String mobileNumber;
    private String address;
    private byte[] image;

    public TraineeInSection() {

    }

    public TraineeInSection(String email,String name, String mobileNumber, String address, byte[] image) {
        this.email = email;
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.address = address;
        this.image = image;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getName() {
        return name;
    }

    public void setName(String lastName) {
        this.name = lastName;
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

    public byte[] getImage() { return image;}

    public void setImage(byte[] image) { this.image = image;}

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Email: " + email + "\n" +
                "Name: " + name + "\n" +
                "Mobile Number: " + mobileNumber + "\n" +
                "Address: " + address + "\n";
    }
}
