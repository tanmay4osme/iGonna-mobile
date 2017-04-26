package com.gonna.models;

public class RegistrationModel {
    public String firstname;
    public String lastname;
    public String email;
    public String password;
    public double latitude;
    public double longitude;

    public RegistrationModel(String firstname, String lastname, String email, String password, double latitude,
            double longitude) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}