package com.CharaProdromos.carsharing.ui.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.CharaProdromos.carsharing.GlobalVariables;
import com.CharaProdromos.carsharing.R;
import com.CharaProdromos.carsharing.Vehicle;
import com.CharaProdromos.carsharing.databinding.FragmentMapBinding;
import com.CharaProdromos.carsharing.databinding.FragmentSearchBinding;
import com.CharaProdromos.carsharing.ui.search.ResultsFragment;
import com.CharaProdromos.carsharing.ui.search.ShowCarFragment;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;

import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

import java.util.Collections;

public class MapFragment extends Fragment implements MapEventsReceiver {
    private MapView map;
    private FragmentMapBinding binding;
    private IMapController mapController;

    private static final int PERMISSION_ID = 123;
    private double userXCoordinates;
    private double userYCoordinates;

    FusedLocationProviderClient mFusedLocationClient;

    private boolean userAsCenter;

    private static final String TAG = "OsmActivity";
    private String plate;


    private static final int PERMISSION_REQUEST_CODE = 1;
    public MapFragment(boolean userAsCenter) {
        this.userAsCenter = userAsCenter;
    }

    public MapFragment(boolean userAsCenter, String plate) {
        this.userAsCenter = userAsCenter;
        this.plate = plate;
    }




    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState){

        Context ctx = getActivity().getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        //inflate and create the map
        binding = FragmentMapBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        map = root.findViewById(R.id.mapView);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        mapController = map.getController();
        mapController.setZoom(15);

        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(this.getContext(), this);
        map.getOverlays().add(0, mapEventsOverlay);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this.getActivity());

        getLastLocation();
        httpRequestCars(root);



        return root;
    }


    private void httpRequestCars(View root) {
        JSONObject jsonBody = new JSONObject();
        System.out.println(jsonBody);

        String serverAddress = this.getString(R.string.serverAddress);
        String url = serverAddress + "/vehicles/coordinates";
        System.out.println(url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("response to cars coords");
                        System.out.println(response);

                        try {
                            JSONArray carsArray = response.getJSONArray("cars");
                            System.out.println("Response ");
                            System.out.println(carsArray);
                            createVehiclePoints(carsArray, root);
                        } catch (JSONException e) {
                            System.out.println("Failed to get cars");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("why error");
                        String text = "Fail "+ error.toString();
                        System.out.println(text);
                    }
                });


        // Add the request to the RequestQueue
        Volley.newRequestQueue(root.getContext()).add(jsonObjectRequest);
        System.out.println("Return filters");
    }


    private  void createVehiclePoints(JSONArray jsonArray, View root) {
        int arrayLen = jsonArray.length();
        JSONObject jsonObject;
        String model;
        String brand;
        String plate;
        String color;
        double xCoordinates;
        double yCoordinates;
        double price;


        try {
            for (int i = 0; i < arrayLen; i++) {
                jsonObject = jsonArray.getJSONObject(i);
                plate = jsonObject.getString("Plate_number");
                price = jsonObject.getDouble("Price");
                brand = jsonObject.getString("Brand");
                model = jsonObject.getString("Model");
                color = jsonObject.getString("Color");

                xCoordinates = jsonObject.getDouble("X_Coordinates");
                yCoordinates = jsonObject.getDouble("Y_Coordinates");

                GeoPoint carPoint = new GeoPoint(yCoordinates, xCoordinates);

                if (plate.equals(this.plate) && userAsCenter == false) {
                    mapController.setZoom(18.0);
                    mapController.setCenter(carPoint);
                }

                View markerView = getLayoutInflater().inflate(R.layout.map_pin, null);

//                TextView titleTextView = markerView.findViewById(R.id.titleTextView);
//                TextView subtitleTextView = markerView.findViewById(R.id.subtitleTextView);
//                MaterialButton customButton = markerView.findViewById(R.id.customButton);
//
//                titleTextView.setText(brand + " " + model);
//                subtitleTextView.setText("Price: " + price+"€/min");

                Marker marker = new Marker(map);
                marker.setPosition(carPoint);
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                int iconResource = R.drawable.baseline_directions_car_24;
                marker.setIcon(getResources().getDrawable(iconResource));

                System.out.println("Got before create");
                MarkerInfo markerInfo = new MarkerInfo(marker, map, plate, price, brand, model, color, userXCoordinates, userYCoordinates);
                marker.setInfoWindow(markerInfo);

                map.getOverlays().add(marker);

            }
        } catch (Exception ex) {
            System.out.println("json exception");
            ex.printStackTrace();
        }
    }



    public void onResume() {
        super.onResume();
        if (map != null)
            map.onResume();
    }

    public void onPause() {
        super.onPause();
        if (map != null)
            map.onPause();
    }


    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {
                Log.v(TAG, "Permission is revoked");
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                return false;
            }
        } else { // permission is automatically granted on SDK < 23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
            //resume tasks needing this permission
        }
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {

                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            userXCoordinates =location.getLongitude();
                            userYCoordinates =location.getLatitude();
                            GeoPoint startPoint = new GeoPoint(userYCoordinates, userXCoordinates);
                            if (userAsCenter) {
                                mapController.setCenter(startPoint);
                            }

                            Marker marker = new Marker(map);
                            marker.setPosition(startPoint);
                            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                            int iconResource = R.drawable.profile_icon;
                            marker.setIcon(getResources().getDrawable(iconResource));
                            map.getOverlays().add(marker);
                            System.out.println(userXCoordinates);
                            System.out.println((userYCoordinates));
                        }
                    }
                });
            } else {
                Toast.makeText(this.getContext(), "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this.getContext());
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
//            latitudeTextView.setText("Latitude: " + mLastLocation.getLatitude() + "");
//            longitTextView.setText("Longitude: " + mLastLocation.getLongitude() + "");
            userYCoordinates = mLastLocation.getLatitude();
            userXCoordinates = mLastLocation.getLongitude();
            System.out.println(userXCoordinates);
            System.out.println((userYCoordinates));

        }
    };

    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission
                (this.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission
                (this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
//         ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    // method to request for permissions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this.getActivity(), new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    // method to check
    // if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override public boolean singleTapConfirmedHelper(GeoPoint p) {
        InfoWindow.closeAllInfoWindowsOn(map);
        return true;
    }

    @Override public boolean longPressHelper(GeoPoint p) {
        //DO NOTHING FOR NOW:
        return false;
    }


}
