package com.CharaProdromos.carsharing;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.CharaProdromos.carsharing.ui.home.HomeFragment;
import com.CharaProdromos.carsharing.ui.map.MapFragment;
import com.CharaProdromos.carsharing.ui.profile.ProfileFragment;
import com.CharaProdromos.carsharing.ui.search.ResultsFragment;
import com.CharaProdromos.carsharing.ui.search.SearchFragment;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.navigation.NavigationBarView;

import com.CharaProdromos.carsharing.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        httpRequestMapLimits(binding.getRoot());


        BottomNavigationView navView = findViewById(R.id.nav_view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_search, R.id.navigation_map, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);


        System.out.println("init fragments");
        String tagSearch = "searchFragment";

        Fragment profileFragment = new ProfileFragment();
        String tagProfile = "profileFragment";



        navView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment searchFragment =fragmentManager.findFragmentByTag("searchFragment");

                if (item.getItemId() == R.id.navigation_home) {
                    Fragment homeFragment = new HomeFragment();
                    String tagHome = "homeFragment";
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.container, homeFragment, tagHome);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    return true;
                }
                else if (item.getItemId() == R.id.navigation_search) {
                    if (searchFragment == null) {
                        searchFragment = new SearchFragment();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.container, searchFragment, tagSearch);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        return true;
                    }
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.container, searchFragment, tagSearch);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    return true;
                }
                else if (item.getItemId() == R.id.navigation_profile) {
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.container, profileFragment, tagProfile);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    return true;
                }
                else if (item.getItemId() == R.id.navigation_map) {
                    Fragment mapFragment = new MapFragment(true);
                    String tagMap = "mapFragment";
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.container, mapFragment, tagMap);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    return true;
                }

                return false;
            }
        });
    }
    //request happens at the start of the program because the limits don't change
    private void httpRequestMapLimits(View root) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("City", "Volos");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(jsonBody);

        String serverAddress = this.getString(R.string.serverAddress);
        System.out.println(jsonBody.toString());
        String url = serverAddress + "/vehicles/mapLimits";
        System.out.println(url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("response to map limits");
                        System.out.println(response);
                        try {
                            JSONObject city = response.getJSONObject("response");
                            Double penalty = city.getDouble("Penalty");
                            Double Y_Coordinates_Bottom_Right = Double.valueOf(city.getString("Y_Coordinates_Bottom_Right"));
                            Double X_Coordinates_Bottom_Right = Double.valueOf(city.getString("X_Coordinates_Bottom_Right"));
                            Double X_Coordinates_Top_Left = Double.valueOf(city.getString("X_Coordinates_Top_Left"));
                            Double Y_Coordinates_Top_Left = Double.valueOf(city.getString("Y_Coordinates_Top_Left"));
                            GlobalVariables.getInstance().setMapLimits(penalty,X_Coordinates_Top_Left, Y_Coordinates_Top_Left, X_Coordinates_Bottom_Right, Y_Coordinates_Bottom_Right);

                        } catch (JSONException ex) {
                            ex.printStackTrace();
                            System.out.println("Failed to get map limits");

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String text = "Fail "+ error.toString();
                        System.out.println(text);
                    }
                });


        // Add the request to the RequestQueue
        Volley.newRequestQueue(root.getContext()).add(jsonObjectRequest);
        System.out.println("Return filters");
    }

}