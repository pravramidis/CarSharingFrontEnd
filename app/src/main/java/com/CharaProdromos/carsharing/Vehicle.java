package com.CharaProdromos.carsharing;

public class Vehicle {
    String brand;
    String color;
    String model;
    String type;
    String plate;
    String Fuel_Type;
    double x_coordinates;
    double y_coordinates;
    int Capacity;
    double costMinute;

    public Vehicle(String plate, double x_coordinates, double y_coordinates) {
        this.plate = plate;
        this.x_coordinates = x_coordinates;
        this.y_coordinates = y_coordinates;

        this.costMinute = httpGetCost(plate);
    }

    public static double httpGetCost(String plate) {

       return 0.2;
    }

}
