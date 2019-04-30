package e.iantm.recommendationapplication;

//import packages
import android.app.Activity;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

/************************************************************
 Author - Ian McManus
 Version - 1.0.0
 Date - 30/04/2019
 Description - Fragment for the map showing business location

 ************************************************************/

public class BusinessMapFragment extends SupportMapFragment implements OnMapReadyCallback {

    GoogleMap mMap;
    String places, updateLocation, userName, title;
    Double longitude, latitude, businessLat, businessLong;

    RequestQueue requestQueue;
    Resources res;

    private static final String TAG = MainActivity.class.getSimpleName();

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;

    // A default location and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(53.7544, -2.3666);
    private static final int DEFAULT_ZOOM = 11;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;

    @Override
    public void onResume() {
        super.onResume();

        setUpMapIfNeeded();
    }// on map resume method

    private void setUpMapIfNeeded() {

        if (mMap == null) {
            getMapAsync(this);

        }
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();
    }//end map set up function

    @Override
    public void onMapReady(GoogleMap googleMap)
    {

        Bundle bundle = getArguments();
        if(bundle != null) {
            userName = String.valueOf(bundle.get("userName"));
            businessLat = bundle.getDouble("latitude");
            businessLong = bundle.getDouble("longitude");
            title = String.valueOf(bundle.get("title"));
        }
        mMap = googleMap;

        // Use a custom info window adapter to handle multiple lines of text in the
        // info window contents.

        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();


    }//end onMapReady method

    public void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();

                            //gets users location
                            if(mLastKnownLocation != null) {
                                latitude = mLastKnownLocation.getLatitude();
                                longitude = mLastKnownLocation.getLongitude();

                                //creates volley request
                                requestQueue = Volley.newRequestQueue(getContext());
                                res = getResources();
                                updateLocation = String.format(res.getString(R.string.updateLocation), res.getString(R.string.url));
                                places = String.format(res.getString(R.string.recommendations), res.getString(R.string.url));

                                LatLng current = new LatLng(latitude, longitude);

                                //add location marker to the map
                                mMap.addMarker(new MarkerOptions()
                                        .position(current)
                                        .title("Current Position")
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                                mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(businessLat, businessLong))
                                        .title(title)
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));

                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        current, DEFAULT_ZOOM));

                            }
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }//end get device location method


    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions((Activity) getContext(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }//end get location permission method

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }//end method for when location permission is given

    /**
     * Updates the locationUI
     */
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }//end update location UI method
}//end class
