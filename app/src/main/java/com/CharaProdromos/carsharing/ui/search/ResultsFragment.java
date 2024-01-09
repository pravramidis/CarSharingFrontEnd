package com.CharaProdromos.carsharing.ui.search;
import static androidx.core.content.ContextCompat.getSystemService;

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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.CharaProdromos.carsharing.GlobalVariables;
import com.CharaProdromos.*;
import com.CharaProdromos.carsharing.R;
import com.CharaProdromos.carsharing.Vehicle;
import com.CharaProdromos.carsharing.databinding.FragmentResultsBinding;
import com.CharaProdromos.carsharing.ui.search.ShowCarFragment;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;

import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.content.ContextCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class ResultsFragment extends Fragment {
    private FragmentResultsBinding binding;
    private TableRow[] table;
    private CardView[] cardViews;

    private boolean priceFlag = true;
    private boolean distanceFlag = true;
    private static final int PERMISSION_ID = 123;
    private double userXCoordinates;
    private double userYCoordinates;

    FusedLocationProviderClient mFusedLocationClient;

    private List<Vehicle> cars = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentResultsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        httpRequestCars(root);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this.getActivity());
        getLastLocation();

        MaterialButton back= root.findViewById(R.id.backToFilters);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                Fragment searchFragment =fragmentManager.findFragmentByTag("searchFragment");
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.container, searchFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        TextView price = root.findViewById(R.id.resultPrice);

        price.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                if (priceFlag == true) {
                    priceFlag = false;
                    Collections.sort(cars, new priceComparator());
                    cardViews = displayTable(root);
                }
                else {
                    priceFlag = true;
                    Collections.sort(cars, new priceComparatorOpposite());
                    cardViews = displayTable(root);
                }

            }
        });

        TextView distance = root.findViewById(R.id.resultDistance);

        distance.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                if (distanceFlag == true) {
                    distanceFlag = false;
                    Collections.sort(cars, new distanceComparator());
                    cardViews = displayTable(root);
                }
                else {
                    distanceFlag = true;
                    Collections.sort(cars, new distanceComparatorOpposite());
                    cardViews = displayTable(root);
                }

            }
        });




        return root;
    }

    private void carListener(TableRow row, String plate) {


        row.setOnClickListener(new View.OnClickListener() {
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


   private void httpRequestCars(View root) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("request", GlobalVariables.getInstance().getFilters());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("Search request");
        System.out.println(jsonBody);

        String serverAddress = this.getString(R.string.serverAddress);
        System.out.println(jsonBody.toString());
        String url = serverAddress + "/vehicles/search";
        System.out.println(url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("response to cars with filters");
                        System.out.println(response);
                        try {
                            JSONArray carsArray = response.getJSONArray("cars");
//                            table = createTable(carsArray, root);
                            createVehicleTable(carsArray, root);
                            Collections.sort(cars, new priceComparator());
                            cardViews = displayTable(root);
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

    public  void createVehicleTable(JSONArray jsonArray, View root) {
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
                color =  jsonObject.getString("Color");

                xCoordinates = jsonObject.getDouble("X_Coordinates");
                yCoordinates = jsonObject.getDouble("Y_Coordinates");
                getLastLocation();
                Vehicle tempVehicle = new Vehicle(plate, xCoordinates, yCoordinates, price, brand, model, color, "doesn't matter");

                tempVehicle.setDistanceFromUser(userXCoordinates, userYCoordinates);
                cars.add(tempVehicle);
            }
        } catch (Exception ex) {
            System.out.println("json exception");
            ex.printStackTrace();
        }
    }

    private void clearTable(View root) {
        System.out.println("Clear results table");
        TableLayout rowContainer = root.findViewById(R.id.tableLayout);
        View firstView = rowContainer.getChildAt(0);
        rowContainer.removeAllViews();

        if (firstView != null) {
            rowContainer.addView(firstView);
        }
    }

    private CardView[] displayTable(View root) {
        System.out.println("Got in display");
        CardView[] cardViews = new CardView[cars.size()];
        TableRow[] tableRow = new TableRow[cars.size()];
        TableLayout rowContainer = root.findViewById(R.id.tableLayout);
        clearTable(root);

        int i = 0;
        for (Vehicle car: cars){
            tableRow[i] = new TableRow(requireContext());
            carListener(tableRow[i], car.getPlate());
            String iconString =
                    car.getBrand().toLowerCase() + "_" + car.getModel().toLowerCase() + "_" + car.getColor().toLowerCase();
//            tableRow[i].setGravity(Gravity.CENTER_VERTICAL);
            ImageView imageView = new ImageView(requireContext());
//            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            int drawableResourceId = getResources().getIdentifier(iconString, "drawable", root.getContext().getPackageName());
            imageView.setImageResource(drawableResourceId);
//            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
//                    TableRow.LayoutParams.WRAP_CONTENT,
//                    TableRow.LayoutParams.WRAP_CONTENT,
//                    Gravity.CENTER
//            );
//            layoutParams.height = 100;
//            imageView.setMaxWidth(100);
//            layoutParams.gravity = Gravity.CENTER;
//            imageView.setLayoutParams(layoutParams);

            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(200, 200);
            layoutParams.gravity = Gravity.CENTER;
            imageView.setLayoutParams(layoutParams);
            imageView.setPadding(16, 8, 8, 8);
            tableRow[i].addView(imageView);
            tableRow[i].setClickable(true);



            TextView textBrand = new TextView(requireContext());
            textBrand.setText(car.getBrand());
            textBrand.setTextColor(ContextCompat.getColor(getContext(), R.color.myThird));
            textBrand.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER
            ));
            textBrand.setPadding(8, 70, 8, 8);
            textBrand.setGravity(Gravity.CENTER_VERTICAL);
            textBrand.setGravity(Gravity.CENTER_HORIZONTAL);
            tableRow[i].addView(textBrand);

            TextView textModel = new TextView(requireContext());
            textModel.setText(car.getModel());
            textModel.setTextColor(ContextCompat.getColor(getContext(), R.color.myThird));
            textModel.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER
            ));
            //textModel.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

            textModel.setGravity(Gravity.CENTER_VERTICAL);
            textModel.setGravity(Gravity.CENTER_HORIZONTAL);
            textModel.setPadding(8, 8, 8, 8);
            tableRow[i].addView(textModel);

            TextView textPrice = new TextView(requireContext());
            textPrice.setText(car.getCostMinute()+"€");
            textPrice.setGravity(Gravity.CENTER_VERTICAL);
            textPrice.setGravity(Gravity.CENTER_HORIZONTAL);
            textPrice.setTextColor(ContextCompat.getColor(getContext(), R.color.myThird));
            textPrice.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER
            ));
            textPrice.setPadding(8, 8, 8, 8);
            tableRow[i].addView(textPrice);

            TextView textDistance = new TextView(requireContext());
            Double dist = car.getDistance();
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            decimalFormat.setRoundingMode(java.math.RoundingMode.HALF_UP);
            String formatDist = decimalFormat.format(dist);
            textDistance.setText(formatDist+"km");
            textDistance.setGravity(Gravity.CENTER_VERTICAL);
            textDistance.setGravity(Gravity.CENTER_HORIZONTAL);
            textDistance.setTextColor(ContextCompat.getColor(getContext(), R.color.myThird));
            textDistance.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER
            ));
            textDistance.setPadding(8, 8, 8, 8);

            tableRow[i].addView(textDistance);


            // Create a CardView and add the TableRow to it
            CardView cardView = new CardView(requireContext());
            cardView.addView(tableRow[i]);
            cardView.setCardElevation(4); // adjust elevation
            cardView.setCardBackgroundColor(Color.WHITE); // adjust background color
            cardView.setRadius(42); // adjust corner radius


//            TableLayout.LayoutParams layoutParamsCard = new TableLayout.LayoutParams(
//                    TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);

            TableLayout.LayoutParams layoutParamsCard = new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);

        //            ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(
        //                    ViewGroup.MarginLayoutParams.MATCH_PARENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT);
            layoutParamsCard.setMargins(8, 30, 8, 30); // adjust margins
            cardView.setLayoutParams(layoutParamsCard);

            cardViews[i] = cardView;
            rowContainer.addView(cardView);
            i++;

            rowContainer.invalidate();
            rowContainer.requestLayout();
        }
        return cardViews;

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
//                            latitudeTextView.setText(location.getLatitude() + "");
//                            longitTextView.setText(location.getLongitude() + "");
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

    // If everything is alright then
    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }
    }


    class priceComparator implements Comparator<Vehicle> {
        @Override
        public int compare(Vehicle v1, Vehicle v2) {
            return Double.compare(v1.getCostMinute(), v2.getCostMinute());
        }
    }

    class priceComparatorOpposite implements Comparator<Vehicle> {
        @Override
        public int compare(Vehicle v1, Vehicle v2) {
            return -Double.compare(v1.getCostMinute(), v2.getCostMinute());
        }
    }

    class distanceComparator implements Comparator<Vehicle> {
        @Override
        public int compare(Vehicle v1, Vehicle v2) {
            return Double.compare(v1.getDistance(), v2.getDistance());
        }
    }

    class distanceComparatorOpposite implements Comparator<Vehicle> {
        @Override
        public int compare(Vehicle v1, Vehicle v2) {
            return -Double.compare(v1.getDistance(), v2.getDistance());
        }
    }

}
