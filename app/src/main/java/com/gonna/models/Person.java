package com.gonna.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.*;

/**
 * Created by Maxim on 4/8/2017.
 */

public class Person implements com.google.maps.android.clustering.ClusterItem {
    public final String name;
    public final int profilePhoto;
    private final LatLng position;

    public Person(LatLng position, String name, int pictureResource) {
        this.name = name;
        profilePhoto = pictureResource;
        this.position = position;
    }

    @Override public LatLng getPosition() {
        return null;
    }

    @Override public String getTitle() {
        return null;
    }

    @Override public String getSnippet() {
        return null;
    }
}
