package com.gonna.models;

import android.location.Location;

import com.gonna.firebase.JSONRepresentable;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User extends JSONRepresentable {
    public String id; //generated with firebase
    public String firstname;
    public String lastname;
    private double latitude;
    private double longitude;

    public User(String id, String firstname, String lastname, double latitude, double longitude) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public User(Map<String, Object> json) {
        super(json);
    }

    public String getFullname() {
        return String.format("%s %s", firstname, lastname);
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override public void fromJSON(Map<String, Object> json) {
        this.id = (String) json.get("id");
        this.firstname = (String) json.get("first_name");
        this.lastname = (String) json.get("last_name");
        String la = json.get("latitude") + "";
        this.latitude = Double.parseDouble(la);
        String lo = json.get("longitude") + "";
        this.longitude = Double.parseDouble(lo);
    }

    @Override public Map<String, Object> toJSON() {
        Map<String, Object> json = new HashMap<>();
        json.put("id", id);
        json.put("first_name", firstname);
        json.put("last_name", lastname);
        json.put("latitude", latitude);
        json.put("longitude", longitude);

        return json;
    }
}