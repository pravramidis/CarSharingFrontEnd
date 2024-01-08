package com.CharaProdromos.carsharing;

public class History {
    String username;
    String plate;
    String price;
    String date;
    String duration;
    String type;


    public History(String username, String plate, String price, String date, String duration, String type) {
        this.username=username;
        this.plate=plate;
        this.price=price;
        this.date=date;
        this.duration=duration;
        this.type=type;
    }

    public String getUsername() { return this.username; }
    public String getPlate() { return this.plate; }
    public String getDate() { return this.date; }
    public String getDuration() { return this.duration; }
    public String getType() { return this.type; }
    public String getPrice() { return this.price;}
}
