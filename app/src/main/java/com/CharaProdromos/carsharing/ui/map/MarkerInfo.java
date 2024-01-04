package com.CharaProdromos.carsharing.ui.map;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.Fragment;

import com.CharaProdromos.carsharing.GlobalVariables;
import com.CharaProdromos.carsharing.R;
import com.CharaProdromos.carsharing.ui.search.ShowCarFragment;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow;

import java.lang.ref.WeakReference;

public class MarkerInfo extends MarkerInfoWindow {
    String plate;
    Double price;
    String brand;
    String model;
    View mapView;


    public MarkerInfo(Marker marker, MapView mapView, String plate, Double price, String brand,String model) {
        super(R.layout.map_pin, mapView);
        System.out.println("Got before create");
        this.brand = brand;
        this.price = price;
        this.brand = brand;
        this. model = model;
        this.plate = plate;
        this.mapView = mapView;
    }

    @Override
    public void onOpen(Object item) {
        System.out.println("Got here");

        TextView titleTextView = mView.findViewById(R.id.titleTextView);
        TextView subtitleTextView = mView.findViewById(R.id.subtitleTextView);
        Button customButton = mView.findViewById(R.id.getNowButton);

        titleTextView.setText(brand + " " + model);
        subtitleTextView.setText("Price: " + price+"€/min");

        customButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mView.getContext(), "Button Clicked", Toast.LENGTH_SHORT).show();
                GlobalVariables.getInstance().setPlateNumber(plate);
                ShowCarFragment showCarFragment = new ShowCarFragment(plate);
                AppCompatActivity activity = (AppCompatActivity) mapView.getContext();
                FragmentTransaction transaction =  activity.getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, showCarFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }
}


