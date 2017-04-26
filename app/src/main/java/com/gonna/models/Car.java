package com.gonna.models;

import com.gonna.firebase.JSONRepresentable;
import com.google.android.gms.maps.model.LatLng;

import java.util.Map;

/**
 * Created by Maxim on 4/6/2017.
 */

public class Car extends JSONRepresentable {
    @Override public void fromJSON(Map<String, Object> json) {

    }

    @Override public Map<String, Object> toJSON() {
        return null;
    }


    public static double getCurrentLongitude() {
        return 0.0;
    }

    public static double getCurrentLatitude() {
        return 0.0;
    }
}
