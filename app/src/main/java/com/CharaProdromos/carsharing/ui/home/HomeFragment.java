package com.CharaProdromos.carsharing.ui.home;

import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.CharaProdromos.carsharing.GlobalVariables;
import com.CharaProdromos.carsharing.R;
import com.CharaProdromos.carsharing.UserLogin;
import com.CharaProdromos.carsharing.UserRegistration;
import com.CharaProdromos.carsharing.databinding.FragmentHomeBinding;
import com.CharaProdromos.carsharing.ui.search.SearchFragment;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

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

//        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        double latitude = location.getLatitude();
//        double longitude = location.getLongitude();


        MaterialButton findMoreButton = root.findViewById(R.id.buttonSearch);

        findMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                Fragment searchFragment =fragmentManager.findFragmentByTag("searchFragment");

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

//    private void httpRequestGetCoordinates() {
//
//        String serverAddress = this.getString(R.string.serverAddress);
//        String url = serverAddress + "/users/home";
//        System.out.println(url);
//        // Initialize a request queue (assuming you have one set up)
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            JSONArray vehiclesArray = response.getJSONArray("vehicles"); // Assuming the JSON array is named "vehicles"
//                            List<Car> cars = new ArrayList<>();
//
//                            for (int i = 0; i < vehiclesArray.length(); i++) {
//                                JSONObject vehicleObject = vehiclesArray.getJSONObject(i);
//                                String plateNumber = vehicleObject.getString("Plate_number");
//                                String brand = vehicleObject.getString("Brand");
//                                String model = vehicleObject.getString("Model");
//                                double xCoordinates = vehicleObject.getDouble("X_Coordinates");
//                                double yCoordinates = vehicleObject.getDouble("Y_Coordinates");
//
//                                // Assuming nulls are represented as JSON null, not as the string "NULL"
//                                if (!vehicleObject.isNull("Plate_number")) {
//                                    Car car = new Car(plateNumber, brand, model, xCoordinates, yCoordinates);
//                                    cars.add(car);
//                                }
//                            }
//
//                            // Now you have a list of cars, you can sort them based on distance from the user's location
//                            // ... (calculate distances and sort the list)
//
//                            // Update your UI with the sorted list of cars
//                            // ... (update UI)
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//            }
//        });
//
//        requestQueue.add(jsonObjectRequest);
//    }




        @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}