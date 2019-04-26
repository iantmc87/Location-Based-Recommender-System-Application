package e.iantm.recommendationapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static java.lang.System.err;

/************************************************************
 Author - Ian McManus
 Version - 1.0.0
 Date - 30/04/2019
 Description - Fragment for the recommended restaurant locations

 ************************************************************/

public class MapFragment extends SupportMapFragment implements OnMapReadyCallback {

    GoogleMap mMap;
    String places, updateLocation, userName, location;
    RequestQueue requestQueue;
    Double longitude, latitude;
    Resources res;
    SharedPreferences locationText;

    private static final String TAG = MainActivity.class.getSimpleName();

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 11;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    private static final int LOC_PERM_REQ_CODE = 1;
    private static final int GEOFENCE_RADIUS = 1000;
    private static final int GEOFENCE_EXPIRATION = 6000;

    private GeofencingClient geofencingClient ;
    private Context mContext;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;

    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onResume() {
        super.onResume();

        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {

        if (mMap == null) {
            getMapAsync(this);

        }
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        SharedPreferences locationFirst = getContext().getSharedPreferences("locationFirst", 0);
        location = locationFirst.getString("first", null);

        SharedPreferences preferences = getContext().getSharedPreferences("account", 0);
        userName = preferences.getString("user", null);

        mMap = googleMap;


        if(location.equals("true")) {
            AlertDialog.Builder account = new AlertDialog.Builder(getActivity());
            account.setTitle("Permission");
            account.setMessage("The app needs permission to access your location to generate " +
                    "recommendations please accept on the next screen");

            account.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    locationText = getContext().getSharedPreferences("locationFirst", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = locationText.edit();

                    editor.putString("first", "false");
                    editor.commit();
                    getLocationPermission();
                    updateLocationUI();
                }
            });
            account.show();
        } else {
            updateLocationUI();
        }

        // Use a custom info window adapter to handle multiple lines of text in the
        // info window contents.

        geofencingClient = LocationServices.getGeofencingClient(mContext);
        addLocationAlert(53.7816207, -2.3882367);

        // Prompt the user for permission.

        // Turn on the My Location layer and the related control on the map.
        //updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();


    }

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

                            if(mLastKnownLocation != null) {
                                latitude = mLastKnownLocation.getLatitude();
                                longitude = mLastKnownLocation.getLongitude();

                                SharedPreferences pref = getContext().getSharedPreferences("location", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();

                                editor.putString("latitude", latitude.toString());
                                editor.putString("longitude", longitude.toString());
                                editor.commit();
                                requestQueue = Volley.newRequestQueue(getContext());
                                res = getResources();
                                updateLocation = String.format(res.getString(R.string.updateLocation), res.getString(R.string.url));
                                places = String.format(res.getString(R.string.recommendations), res.getString(R.string.url));

                                LatLng current = new LatLng(latitude, longitude);

                                mMap.addMarker(new MarkerOptions()
                                        .position(current)
                                        .title("Current Position")
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        current, DEFAULT_ZOOM));

                                Request request = new StringRequest(Request.Method.POST, updateLocation, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                }) {
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> parameters = new HashMap<String, String>();
                                        parameters.put("longitude", longitude.toString());
                                        parameters.put("latitude", latitude.toString());
                                        parameters.put("user_name", userName);

                                        return parameters;
                                    }
                                };
                                requestQueue.add(request);

                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, places, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response.toString());
                                            JSONArray recommendations = jsonObject.getJSONArray("recommendations");
                                            int length = recommendations.length();
                                            String[] name = new String[length];
                                            Double[] longitude = new Double[length];
                                            Double[] latitude = new Double[length];
                                            Marker[] m = new Marker[length];

                                            for (int i = 0; i < length; i++) {
                                                JSONObject obj = recommendations.getJSONObject(i);
                                                name[i] = obj.getString("name");
                                                longitude[i] = obj.getDouble("longitude");
                                                latitude[i] = obj.getDouble("latitude");
                                                if (i == 0) {
                                                    m[i] = mMap.addMarker(new MarkerOptions()
                                                            .position(new LatLng(latitude[i], longitude[i]))
                                                            .title(name[i])
                                                            .snippet("Ranking: " + (i + 1))
                                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
                                                } else {
                                                    m[i] = mMap.addMarker(new MarkerOptions()
                                                            .position(new LatLng(latitude[i], longitude[i]))
                                                            .title(name[i])
                                                            .snippet("Ranking: " + (i + 1))
                                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
                                                }
                                            }


                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                }) {
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> parameters = new HashMap<String, String>();
                                        parameters.put("user_name", userName);
                                        parameters.put("longitude", longitude.toString());
                                        parameters.put("latitude", latitude.toString());

                                        return parameters;
                                    }
                                };
                                requestQueue.add(stringRequest);
                                }
                            }, 10000);
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
    }


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
    }

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
                else {

                }
            }
        }
        updateLocationUI();
    }
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
    }

    @SuppressLint("MissingPermission")
    private void addLocationAlert(double lat, double lng) {
        if(mLocationPermissionGranted) {
            getLocationPermission();
        } else {
            String key = "geofence";
            Geofence geofence = getGeofence(lat, lng, key);
            geofencingClient.addGeofences(getGeofencingRequest(geofence),
                    getGeofencePendingIntent())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(getContext(), "Location alters have been added", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "Location alters could not be added", Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "" + err);
                            }
                        }
                    });
        }
    }

    private void removeLocationAlert() {
        if(mLocationPermissionGranted) {
            getLocationPermission();
        } else {
            geofencingClient.removeGeofences(getGeofencePendingIntent())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(),
                                        "Location alters have been removed",
                                        Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getContext(),
                                        "Location alters could not be removed",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private PendingIntent getGeofencePendingIntent() {
        Intent intent = new Intent(getContext(), LocationAlertIntentService.class);
        return PendingIntent.getService(getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private GeofencingRequest getGeofencingRequest(Geofence geofence) {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_DWELL);
        builder.addGeofence(geofence);
        return builder.build();
    }

    private Geofence getGeofence(double lat, double lang, String key) {
        return new Geofence.Builder()
                .setRequestId(key)
                .setCircularRegion(lat, lang, GEOFENCE_RADIUS)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_DWELL)
                .setLoiteringDelay(60000)
                .build();
    }

}
