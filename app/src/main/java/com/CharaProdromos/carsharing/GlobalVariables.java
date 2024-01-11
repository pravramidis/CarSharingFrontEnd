package com.CharaProdromos.carsharing;

import org.json.JSONArray;
import org.json.JSONObject;

public class GlobalVariables {

    private static GlobalVariables instance;
    private String username;
    private String password;
    private String name;
    private String phoneNumber;
    private String id;
    private String birthday;
    private String email;
    private String plateNumber;

    private Double topLeftLimit_X;
    private Double topLeftLimit_Y;
    private Double bottomRightLimit_X;
    private Double bottomRightLimit_Y;

    private Double penalty;

    private JSONObject filters = null;

    private GlobalVariables() {}

    public static synchronized GlobalVariables getInstance() {
        if (instance == null) {
            instance = new GlobalVariables();
        }
        return instance;
    }

    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getName() {
        return name;
    }
    public String getId() {
        return id;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public String getEmail() {
        return email;
    }
    public String getBirthday() {
        return birthday;
    }

    public String getPlateNumber() { return plateNumber;}

    public Double getTopLeftLimit_X() {
        return  topLeftLimit_X;
    }
    public  Double getTopLeftLimit_Y() {
        return topLeftLimit_Y;
    }
    public Double getBottomRightLimit_X() {
        return bottomRightLimit_X;
    }

    public Double getBottomRightLimit_Y() {
        return bottomRightLimit_Y;
    }

    public Double getPenalty() {
        return penalty;
    }


    public void setMapLimits(Double penalty, Double topLeftLimit_X, Double topLeftLimit_Y, Double bottomRightLimit_X, Double bottomRightLimit_Y) {
        this.topLeftLimit_X = topLeftLimit_X;
        this.topLeftLimit_Y = topLeftLimit_Y;
        this.bottomRightLimit_X = bottomRightLimit_X;
        this.bottomRightLimit_Y = bottomRightLimit_Y;
        this.penalty = penalty;
    }

    public void setUsername(String someGlobalVar) {
        this.username = someGlobalVar;
    }
    public void setPassword(String someGlobalVar) {
        this.password = someGlobalVar;
    }
    public void setName(String someGlobalVar) {
        this.name = someGlobalVar;
    }
    public void setId(String someGlobalVar) {
        this.id = someGlobalVar;
    }
    public void setPhoneNumber(String someGlobalVar) {
        this.phoneNumber = someGlobalVar;
    }
    public void setEmail(String someGlobalVar) {
        this.email = someGlobalVar;
    }
    public void setBirthday(String someGlobalVar) {
        this.birthday = someGlobalVar;
    }

    public void setPlateNumber(String someGlobalVar) {this.plateNumber = someGlobalVar;}

    public JSONObject getFilters() {return filters;}

    public void initFilters() {
        this.filters = new JSONObject();
        JSONArray initArray = new JSONArray();
        try {
            filters.put("Type", initArray);
            filters.put("Fuel Type", initArray);
            filters.put("Model", initArray);
            filters.put("Color", initArray);
            filters.put("Brand", initArray);
            filters.put("Gearbox", initArray);
            filters.put("Capacity", initArray);
        }
        catch (Exception ex) {
            System.out.println("Failed to init json");
        }

    }

    public  void editFilters(String tag, JSONArray array) {
        try {
            this.filters.remove(tag);
            this.filters.put(tag, array);
        }
        catch (Exception ex) {
            System.out.println("Failed to edit json");
        }
    }




    public void clearFilters() {
        initFilters();
    }



}
