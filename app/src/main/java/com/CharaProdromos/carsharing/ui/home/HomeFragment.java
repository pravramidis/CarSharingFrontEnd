package com.CharaProdromos.carsharing.ui.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.CharaProdromos.carsharing.GlobalVariables;
import com.CharaProdromos.carsharing.R;
import com.CharaProdromos.carsharing.UserLogin;
import com.CharaProdromos.carsharing.UserRegistration;
import com.CharaProdromos.carsharing.Vehicle;
import com.CharaProdromos.carsharing.databinding.FragmentHomeBinding;
import com.CharaProdromos.carsharing.ui.search.ResultsFragment;
import com.CharaProdromos.carsharing.ui.search.SearchFragment;
import com.CharaProdromos.carsharing.ui.search.ShowCarFragment;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomeFragment extends Fragment {
    private List<Vehicle> cars = new ArrayList<>();
    private static final int PERMISSION_ID = 123;
    Double userYCoordinates;
    Double userXCoordinates;
    FusedLocationProviderClient mFusedLocationClient;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        String user = GlobalVariables.getInstance().getUsername();

        // Find the TextView by its ID
        TextView welcomeTextView = root.findViewById(R.id.welcomeText);

        // Set the welcome message with the username
        String welcomeString = "Welcome, " + user + "!";
        welcomeTextView.setText(welcomeString);

        httpRequestGetCoordinates(root);

        Collections.sort(cars, new distanceComparator());
        System.out.println(cars);

        MaterialButton findMoreButton = root.findViewById(R.id.buttonSearch);

        findMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                Fragment searchFragment = fragmentManager.findFragmentByTag("searchFragment");

                if (searchFragment == null) {
                    searchFragment = new SearchFragment();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.container, searchFragment, "searchFragment");
                    transaction.addToBackStack(null);
                    transaction.commit();
                    BottomNavigationView navView = requireActivity().findViewById(R.id.nav_view);
                    navView.setSelectedItemId(R.id.navigation_search);
                    return;
                }
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                transaction.replace(R.id.container, searchFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                BottomNavigationView navView = requireActivity().findViewById(R.id.nav_view);
                navView.setSelectedItemId(R.id.navigation_search);


            }

        });


        return root;
    }

    private void httpRequestGetCoordinates(View root) {

        String serverAddress = this.getString(R.string.serverAddress);
        String url = serverAddress + "/vehicles/coordinates";
        System.out.println(url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray vehiclesArray = response.getJSONArray("cars");
                            createVehicleTable(vehiclesArray);
                            System.out.println(cars);
                            int i = 0;
                            GridLayout grid = root.findViewById(R.id.homeContainer);
                            for(Vehicle car: cars) {
                                if (i == 3) {
                                    break;
                                }
                                ImageView imageView = new ImageView(requireContext());
                                String iconString =
                                        car.getBrand().toLowerCase() + "_" + car.getModel().toLowerCase() + "_" + car.getColor().toLowerCase();
                                int drawableResourceId = getResources().getIdentifier(iconString, "drawable", root.getContext().getPackageName());
                                imageView.setImageResource(drawableResourceId);
                                GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
                                layoutParams.height = GridLayout.LayoutParams.MATCH_PARENT;
                                layoutParams.columnSpec = GridLayout.spec(i, 1.0f);  // Column weight
                                layoutParams.rowSpec = GridLayout.spec(0, 1.0f);  // Row weight
                                layoutParams.width = 300;
                                layoutParams.height = 300;
                                layoutParams.bottomMargin = -70;
                                layoutParams.setGravity(Gravity.CENTER);
                                imageView.setLayoutParams(layoutParams);

                                grid.addView(imageView);

                                TextView costText = new TextView(requireContext());
                                costText.setText(Double.toString(car.getCostMinute()) + "€/min");
                                GridLayout.LayoutParams costTextParams = new GridLayout.LayoutParams();
                                costTextParams.width = 300;
                                costTextParams.height = GridLayout.LayoutParams.WRAP_CONTENT;
                                costTextParams.columnSpec = GridLayout.spec(i, 1.0f);
                                costTextParams.rowSpec = GridLayout.spec(1, GridLayout.CENTER);
                                layoutParams.setGravity(Gravity.CENTER);
                                costText.setLayoutParams(costTextParams);
                                costText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                int primaryColor = ContextCompat.getColor(requireContext(), R.color.myPrimary);
                                costText.setTextColor(primaryColor);

                                carListener(costText, imageView, car.getPlate());

                                grid.addView(costText);
                                i++;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        Volley.newRequestQueue(root.getContext()).add(jsonObjectRequest);
    }

    private void createVehicleTable(JSONArray jsonArray) {
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
                Vehicle tempVehicle = new Vehicle(plate, xCoordinates, yCoordinates, price, brand, model, color, "doesn't matter");
                cars.add(tempVehicle);

            }
        } catch (Exception ex) {
            System.out.println("json exception");
            ex.printStackTrace();
        }
    }

    private void carListener(TextView text, ImageView image, String plate) {


        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("User wants this car: plate:"+ plate);
                ShowCarFragment showCarFragment = new ShowCarFragment(plate, userXCoordinates, userYCoordinates);
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, showCarFragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("User wants this car: plate:"+ plate);
                ShowCarFragment showCarFragment = new ShowCarFragment(plate, userXCoordinates, userYCoordinates);
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, showCarFragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });
    }


    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (checkPermissions()) {
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



        @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    class distanceComparator implements Comparator<Vehicle> {
        @Override
        public int compare(Vehicle v1, Vehicle v2) {
            return Double.compare(v1.getDistance(), v2.getDistance());
        }
    }
}