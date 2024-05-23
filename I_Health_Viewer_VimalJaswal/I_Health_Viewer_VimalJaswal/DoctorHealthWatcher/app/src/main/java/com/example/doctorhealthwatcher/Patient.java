package com.example.doctorhealthwatcher;

public class Patient {
    private String userName;
    private String heartRate;
    private String email;

    public Patient(){
        //empty constructor needed
    }

    public Patient(String userName, String email, String heartRate){
        this.userName = userName;
        this.email = email;
        this.heartRate =heartRate;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setHeartRate(String heartRate) {
        this.heartRate = heartRate;
    }

    public String getHeartRate() {
        return heartRate;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

}
