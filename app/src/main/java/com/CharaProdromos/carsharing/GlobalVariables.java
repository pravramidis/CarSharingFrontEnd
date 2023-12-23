package com.CharaProdromos.carsharing;

public class GlobalVariables {

    private static GlobalVariables instance;
    private String username;

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

}
