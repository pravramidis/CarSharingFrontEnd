package com.CharaProdromos.carsharing;

import org.json.JSONArray;
import org.json.JSONObject;

public class GlobalVariables {

    private static GlobalVariables instance;
    private String username;

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

    public void setUsername(String someGlobalVar) {
        this.username = someGlobalVar;
    }

    public JSONObject getFilters() {return filters;}

    public void initFilters() {
        this.filters = new JSONObject();
        JSONArray initArray = new JSONArray();
        try {
            filters.put("TYPE", initArray);
            filters.put("FUEL_TYPE", initArray);
            filters.put("MODEL", initArray);
            filters.put("COLOR", initArray);
            filters.put("BRAND", initArray);
            filters.put("GEARBOX", initArray);
            filters.put("CAPACITY", initArray);
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
