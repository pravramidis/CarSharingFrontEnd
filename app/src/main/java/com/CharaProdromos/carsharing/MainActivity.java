package com.CharaProdromos.carsharing;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.CharaProdromos.carsharing.ui.home.HomeFragment;
import com.CharaProdromos.carsharing.ui.map.MapFragment;
import com.CharaProdromos.carsharing.ui.profile.ProfileFragment;
import com.CharaProdromos.carsharing.ui.search.SearchFragment;
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

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Intent intent = new Intent(MainActivity.this, UserLogin.class);
//        startActivity(intent);
//        Intent intent = new Intent(MainActivity.this, Map.class);
//        startActivity(intent);
//        System.out.println("hremain");
//
//

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_search, R.id.navigation_map, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);


        System.out.println("init fragments");
        String tagSearch = "searchFragment";
        Fragment homeFragment = new HomeFragment();
        String tagHome = "homeFragment";
        Fragment profileFragment = new ProfileFragment();
        String tagProfile = "profileFragment";



        navView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment searchFragment =fragmentManager.findFragmentByTag("searchFragment");

                if (item.getItemId() == R.id.navigation_home) {
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
                    Fragment mapFragment = new MapFragment();
                    String tagMap = "mapFragment";
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.container, mapFragment, tagMap);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    return true;
                }

//                switch (item.getItemId()) {
//                    case R.id.navigation_home:
////                        fragment = new HomeFragment();
//                        break;
//
//                    case R.id.navigation_search:
////                        fragment = new FiltersFragment();
//                        break;
//
//                    // Add more cases for other menu items
//
//                    default:
//                        break;
//                }

//                if (fragment != null) {
//                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                    transaction.replace(R.id.container, fragment, "FragmentTag"); // Add a tag here
//                    transaction.addToBackStack(null);
//                    transaction.commit();
//                    return true;
//                }
//
                return false;
            }
        });



    }


}