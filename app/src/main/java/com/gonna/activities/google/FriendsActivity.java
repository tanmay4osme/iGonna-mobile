package com.gonna.activities.google;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.gonna.R;
import com.gonna.Singleton;
import com.gonna.firebase.FirebaseService;
import com.gonna.models.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FriendsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        FirebaseService.getUser(FirebaseAuth.getInstance().getCurrentUser().getUid(), new FirebaseService
                .Callback<User>() {
            @Override public void onSuccess(User response) {
                LatLng me = new LatLng(response.getLatitude(), response.getLongitude());
                mMap.addMarker(new MarkerOptions().position(me));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(me));
            }

            @Override public void onFailure(Exception e) {
                Toast.makeText(FriendsActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT);
            }
        });
    }
}