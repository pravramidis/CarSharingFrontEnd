package com.CharaProdromos.carsharing.ui.search;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.annotation.NonNull;

import com.CharaProdromos.carsharing.GlobalVariables;
import com.CharaProdromos.carsharing.R;

import com.CharaProdromos.carsharing.Vehicle;
import com.CharaProdromos.carsharing.databinding.FragmentPaymentBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;

public class PaymentFragment extends Fragment {
    Vehicle car;
    FusedLocationProviderClient mFusedLocationClient;
    private static final int PERMISSION_ID = 123;
    TextView finalPrice;
    TextView penaltyText;
    Double penalty;

    private FragmentPaymentBinding binding;

    public PaymentFragment(Vehicle car) {
        this.car = car;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentPaymentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        MaterialButton payButton = root.findViewById(R.id.buttonPay);

        TextView duration = root.findViewById(R.id.durationText);
        TextView rate = root.findViewById(R.id.rateText);
        TextView plate = root.findViewById(R.id.plateText);
        TextView brand = root.findViewById(R.id.brandText);
        TextView model = root.findViewById(R.id.modelText);
        finalPrice = root.findViewById(R.id.finalPrice);
        penaltyText = root.findViewById(R.id.penaltyText);


        rate.setText(car.getRate());
        duration.setText(car.getTime());
        plate.setText(car.getPlate());
        brand.setText(car.getBrand());
        model.setText(car.getModel());

        getLastLocationAndUpdateVehicle(car);

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CardInfoFragment cardFragment = new CardInfoFragment(car);
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, cardFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });





        return root;
    }

    private String getTotalPrice(String [] time, String rate) {
        String finalPrice,  rateType, ratePrice;
        int days, hours, minutes, seconds;
        double price;
        String[] rates;
        hours = Integer.parseInt(time[0]);
        minutes = Integer.parseInt(time[1]);
        seconds = Integer.parseInt(time[2]);

        System.out.println("hours: "+hours);
        System.out.println("mins: "+minutes);
        System.out.println("seconds: "+seconds);


        rates = rate.split("/",2);
        ratePrice = rates[0].substring(0, rates[0].length() - 2);
        price = Double.parseDouble(ratePrice);
        System.out.println("Price: "+price);
        rateType = rates[1];

        if(rateType.equals("day")) {
            days = hours % 24;
            if (minutes != 0 || seconds !=0) {
                days++;
            }
            price = days*price;
            finalPrice = String.valueOf(price);
            return finalPrice;
        }
        if(rateType.equals("hour")) {
            days = hours % 24;
            if(days != 0 ) {
                hours = hours +(days*24);
            }
            if(minutes !=0 || seconds != 0) {
                hours++;
            }
            price = hours*price;
            finalPrice = String.valueOf(price);
            return finalPrice;
        }
        days = hours % 24;
        if(days != 0) {
            minutes = minutes +(days *24*60);
        }
        if (seconds != 0) {
            minutes++;
        }
        System.out.println(minutes);
        price = (minutes*price) ;
        System.out.println(penalty);
        if (penalty != 0) {
            System.out.println(penalty);
            price += penalty;
        }
        finalPrice = String.valueOf(price);

        return finalPrice;
    }
    @SuppressLint("MissingPermission")
    private void getLastLocationAndUpdateVehicle(Vehicle car) {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this.getContext());
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            Double userXCoordinates =location.getLongitude();
                            Double userYCoordinates =location.getLatitude();
                            penalty = 0.0;
                            //Checks if user is outside of city limits
                            if (userXCoordinates > GlobalVariables.getInstance().getBottomRightLimit_X()
                                    || userXCoordinates < GlobalVariables.getInstance().getTopLeftLimit_X()
                                    || userYCoordinates < GlobalVariables.getInstance().getBottomRightLimit_Y()
                                    || userYCoordinates > GlobalVariables.getInstance().getTopLeftLimit_Y()) {

                                penalty = GlobalVariables.getInstance().getPenalty();
                            }

                            penaltyText.setText(penalty.toString() + " €" );

                            String[] time = car.getTime().split(":",3);
                            String totalPrice = getTotalPrice(time, car.getRate());
                            Double doublePrice = Double.parseDouble(totalPrice);
                            String formatedPrice = String.format("%.2f", doublePrice);

                            car.setFinalPrice(formatedPrice);
                            totalPrice = "  "+formatedPrice +" €  ";
                            finalPrice.setText(totalPrice);

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

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this.getContext());
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
        }
    };

    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission
                (this.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission
                (this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    // method to request for permissions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this.getActivity(), new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
}

