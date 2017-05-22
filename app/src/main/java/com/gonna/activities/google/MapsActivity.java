package com.gonna.activities.google;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.gonna.R;
import com.gonna.firebase.FirebaseService;
import com.gonna.models.User;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import butterknife.Bind;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient
        .ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String TAG = MapsActivity.class.getSimpleName();
    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    private Location mCurrentLocation;
    private LocationRequest mLocationRequest;
    private String mLastUpdateTime;
    @Bind(R.id.spinner) Spinner spinner;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // Spinner
        spinner = new Spinner(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.maps_array, android.R
                .layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setZoomControlsEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    @Override public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setSmallestDisplacement(0.15F); // quarter of 1m
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager
                .PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override public void onConnectionSuspended(int i) {
        Location location = new Location("");
        location.setLatitude(0.0d);
        location.setLongitude(0.0d);
        FirebaseService.setLocation(location, new FirebaseService.Callback<List<User>>() {
            @Override public void onSuccess(List<User> response) {
                addMarkers(response);
            }

            @Override public void onFailure(Exception e) {

            }
        });
    }

    @Override public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

        FirebaseService.setLocation(mCurrentLocation, new FirebaseService.Callback<List<User>>() {
            @Override public void onSuccess(List<User> response) {
                addMarkers(response);
            }

            @Override public void onFailure(Exception e) {

            }
        });
        // addMarker();
    }

    private void addMarker() {
        MarkerOptions options = new MarkerOptions();
        IconGenerator iconFactory = new IconGenerator(this);
        iconFactory.setStyle(IconGenerator.STYLE_ORANGE);
        options.icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(mLastUpdateTime)));
        options.anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());

        LatLng currentLatLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        options.position(currentLatLng);
        Marker mapMarker = mMap.addMarker(options);
        long atTime = mCurrentLocation.getTime();
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date(atTime));
        mapMarker.setTitle(mLastUpdateTime);
        Log.d(TAG, "Marker added.............................");
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 17));
        Log.d(TAG, "Zoom done.............................");

    }

    private void addMarkers(List<User> users) {
        MarkerOptions markerOptions = new MarkerOptions();
        IconGenerator iconFactory = new IconGenerator(this);
        iconFactory.setStyle(IconGenerator.STYLE_ORANGE);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(mLastUpdateTime)));
        markerOptions.anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());

        LatLng currentLatLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());

        long atTime = mCurrentLocation.getTime();

        mMap.clear();
        for (User user : users) {
            LatLng latLng = new LatLng(user.getLatitude(), user.getLongitude());
            if (latLng.latitude >= 1.0d) {
                markerOptions.position(latLng);
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date(atTime));
                Marker marker = mMap.addMarker(markerOptions);
                marker.setTitle(mLastUpdateTime);
            }
        }

        Log.d(TAG, "Markers added.................................................");
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 13));
        Log.d(TAG, "Zoom done.....................................................");
    }

    @Override protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        Log.d(TAG, "Location update stopped .......................");
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager
                .PERMISSION_GRANTED) {
            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                            PackageManager.PERMISSION_GRANTED) {
                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, MapsActivity.class);
        context.startActivity(starter);
        starter.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }
}