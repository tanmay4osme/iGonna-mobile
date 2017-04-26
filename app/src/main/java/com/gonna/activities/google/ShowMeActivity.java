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
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.gonna.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;

public class ShowMeActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = ShowMeActivity.class.getSimpleName();
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private static int UPDATE_INTERVAL = 10000;
    private static int FATEST_INTERVAL = 5000;
    private static int DISPLACEMENT = 10;
    private GoogleMap mMap;
    private Location lastLocation;
    private boolean mRequestLocationUpdates;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_me);


        if (checkPlayServices()) {
            buildGoogleApiClient();
            createLocationRequest();
        }
        //        buildLocationSettingsRequest();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    //    private void updateValuesFromBundle(Bundle savedInstanceState) {
    //        if (savedInstanceState != null) {
    //            // Update the value of mRequestingLocationUpdates from the Bundle, and make sure that
    //            // the Start Updates and Stop Updates buttons are correctly enabled or disabled.
    //            if (savedInstanceState.keySet().contains("requesting-location-updates")) {
    //                mRequestingLocationUpdates = savedInstanceState.getBoolean(KEY_REQUESTING_LOCATION_UPDATES);
    //            }
    //
    //            // Update the value of mCurrentLocation from the Bundle and update the UI to show the
    //            // correct latitude and longitude.
    //            if (savedInstanceState.keySet().contains(KEY_LOCATION)) {
    //                // Since KEY_LOCATION was found in the Bundle, we can be sure that mCurrentLocation
    //                // is not null.
    //                mCurrentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
    //            }
    //
    //            // Update the value of mLastUpdateTime from the Bundle and update the UI.
    //            if (savedInstanceState.keySet().contains("last-updated-time-string")) {
    //                mLastUpdateTime = savedInstanceState.getString("last-updated-time-string");
    //            }
    //            updateUI();
    //        }
    //    }

    private synchronized void buildGoogleApiClient() {
        Log.i(TAG, "Building GoogleApiClient");
        googleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(new GoogleApiClient
                .ConnectionCallbacks() {
            @Override public void onConnected(@Nullable Bundle bundle) {

            }

            @Override public void onConnectionSuspended(int i) {

            }
        }).addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
            @Override public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

            }
        }).addApi(LocationServices.API).build();
    }

    protected void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FATEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setSmallestDisplacement(DISPLACEMENT);
        Log.d("LocationRequest", " is created");
    }

    @Override public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //        LatLng sydney = new LatLng(-34, 151);
        //        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager
                .PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission
                .ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        LatLng me = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
        mMap.addMarker(new MarkerOptions().position(me));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(me));
    }

    @Override protected void onStart() {
        super.onStart();
        if (googleApiClient != null) {
            googleApiClient.connect();
            displayLocation();
            Log.d("onStart ", "mGoogleApiClient is built");
        }
    }


    @Override protected void onResume() {
        super.onResume();
        checkPlayServices();
        if (googleApiClient.isConnected() && mRequestLocationUpdates) {
            startLocationUpdates();
        }
    }


    @Override protected void onStop() {
        super.onStop();
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    @Override protected void onPause() {
        super.onPause();
        //stopLocationUpdates();
    }


    private void displayLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager
                            .PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
        } else {
            //Do Your Stuff

        }
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        Log.d("LastLocation ", "found");
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }


    protected void startLocationUpdates() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager
                            .PERMISSION_GRANTED) {
                return;
            }
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, new
                LocationListener() {
            @Override public void onLocationChanged(Location location) {

            }
        });
        Log.d("Location was ", "updates");
    }

    //    protected void stopLocationUpdates() {
    //        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    //    }

    //    @Override public void onConnected(@Nullable Bundle bundle) {
    //        displayLocation();
    //        if (mRequestLocationUpdates) {
    ////            startLocationUpdates();
    //        }
    //    }

    //    @Override public void onConnectionSuspended(int i) {
    //        mGoogleApiClient.connect();
    //    }


    //    @Override public void onLocationChanged(Location location) {
    //        mLastLocation = location;
    //
    //        Toast.makeText(getApplicationContext(), "Location changed!", Toast.LENGTH_SHORT).show();
    //
    //        displayLocation();
    //    }

    //    @Override public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    //        Log.i(TAG, "Connection failed: " + connectionResult.getErrorCode());
    //    }

    public static void start(Context context) {
        Intent starter = new Intent(context, ShowMeActivity.class);
        context.startActivity(starter);
        starter.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
    }
}