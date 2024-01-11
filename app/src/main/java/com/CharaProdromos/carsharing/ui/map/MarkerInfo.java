package com.CharaProdromos.carsharing.ui.map;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.Fragment;

import com.CharaProdromos.carsharing.GlobalVariables;
import com.CharaProdromos.carsharing.R;
import com.CharaProdromos.carsharing.Vehicle;
import com.CharaProdromos.carsharing.ui.search.ShowCarFragment;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.InfoWindow;
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MarkerInfo extends MarkerInfoWindow {
    String plate;
    Double price;
    String brand;
    String model;
    MapView mapView;

    String color;
    Double userX;

    Double userY;

    public MarkerInfo(Marker marker, MapView mapView, String plate, Double price, String brand,String model, String color, Double userX, Double userY) {
        super(R.layout.map_pin, mapView);
        this.brand = brand;
        this.price = price;
        this.brand = brand;
        this. model = model;
        this.plate = plate;
        this.mapView = mapView;
        this.color = color;
        this.userX = userX;
        this.userY = userY;
    }

    @Override
    public void onOpen(Object item) {
        InfoWindow.closeAllInfoWindowsOn(mapView);

        TextView titleTextView = mView.findViewById(R.id.titleTextView);
        TextView subtitleTextView = mView.findViewById(R.id.subtitleTextView);
        Button customButton = mView.findViewById(R.id.getNowButton);
        ImageView image = mView.findViewById(R.id.carPinImage);

        titleTextView.setText(brand + " " + model);
        subtitleTextView.setText("Price: " + price+"€/min");

        String iconString = brand.toLowerCase() + "_" + model.toLowerCase() + "_" + color.toLowerCase();
        int drawableResourceId = mapView.getContext().getResources().getIdentifier(iconString, "drawable", mapView.getContext().getPackageName());
        image.setImageResource(drawableResourceId);

        customButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShowCarFragment showCarFragment = new ShowCarFragment(plate, userX, userY);
                AppCompatActivity activity = (AppCompatActivity) mapView.getContext();
                FragmentTransaction transaction =  activity.getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, showCarFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    @Override
    public void onClose() {
        super.onClose();
    }
}


