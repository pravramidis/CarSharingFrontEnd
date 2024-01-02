package com.CharaProdromos.carsharing;

import android.content.pm.PackageManager;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;

import java.util.Comparator;


public class Vehicle {
    String brand;
    String color;
    String model;
    String type;
    String plate;
    String Fuel_Type;
    double x_coordinates;
    double y_coordinates;

    double distanceFromUser;
    int Capacity;
    double costMinute;

    String avail;

    String time;

    String finalPrice;


    public Vehicle(String plate, String brand, String model, String avail) {
        this.plate = plate;
        this.brand = brand;
        this.model = model;
        this.avail =avail;
    }

    public Vehicle(String plate, double x_coordinates, double y_coordinates, double priceMin, String brand, String model, View root) {
        this.plate = plate;
        this.x_coordinates = x_coordinates;
        this.y_coordinates = y_coordinates;
        this.brand = brand;
        this.model = model;

        this.costMinute = priceMin;

//        this.distanceFromUser = distanceFromUser(x_coordinates, y_coordinates,);
    }

    public static double distanceFromUser(double x_coordinates, double y_coordinates, double x_userLocation, double y_userLocation) {
        final int R = 6371; // Radius of the Earth in kilometers

        // Convert latitude and longitude from degrees to radians
        double dLat = Math.toRadians(y_userLocation - y_coordinates);
        double dLon = Math.toRadians(x_userLocation - x_coordinates);

        // Haversine formula
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(y_userLocation)) * Math.cos(Math.toRadians(y_coordinates)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        // Distance in kilometers
        return R * c;
    }

    public void setDistanceFromUser(double x_userLocation, double y_userLocation) {
        this.distanceFromUser = distanceFromUser(this.x_coordinates, this.y_coordinates, x_userLocation, y_userLocation);
    }


    public double getCostMinute() {
        return this.costMinute;
    }

    public String getPlate() {
        return this.plate;
    }

    public String getBrand() {
        return this.brand;
    }
    public String getModel() {
        return this.model;
    }
    public double getDistance() {return this.distanceFromUser;}

    public void setTime(String time) { this.time = time;}
}