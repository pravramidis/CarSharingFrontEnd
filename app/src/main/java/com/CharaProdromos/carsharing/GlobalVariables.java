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
