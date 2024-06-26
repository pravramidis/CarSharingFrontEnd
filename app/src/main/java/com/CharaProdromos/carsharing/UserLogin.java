package com.CharaProdromos.carsharing;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

public class UserLogin extends AppCompatActivity{
    private Context context;
    private boolean flagPassword = true;


    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        Context context = this;
        EditText loginTextUsername =findViewById(R.id.loginTextUsername);
        EditText loginTextPassword =findViewById(R.id.loginTextPassword);
        Button loginButton = findViewById(R.id.loginButton);
        Button registerButton = findViewById(R.id.RegisterNowButton);
        ImageView showPassword = findViewById(R.id.passWordImage);
        loginButton.setEnabled(true);
        registerButton.setEnabled(true);

        showPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flagPassword == true) {
                    loginTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
                    flagPassword = false;
                }
                else {
                    loginTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    flagPassword = true;
                }
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String username = loginTextUsername.getText().toString();
                String password = loginTextPassword.getText().toString();
                httpRequestAuthenticateUser(username, password);

                GlobalVariables.getInstance().setUsername(username);
            }


        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(UserLogin.this, UserRegistration.class);
                startActivity(intent);

            }

        });
    }

    private void httpRequestAuthenticateUser(String username, String password) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("Username", username);
            jsonBody.put("Password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String serverAddress = this.getString(R.string.serverAddress);
        System.out.println(jsonBody.toString());
        String url = serverAddress + "/users/login";
        System.out.println(url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("user").equals("Success")) {
                                String text = "Successful login";
                                Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
                                toast.show();
                                Intent intent = new Intent(UserLogin.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                String text = "Username and password do not match";
                                Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
                                toast.show();
                            }
                        } catch (Exception ex) {

                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        String text = "Server Error"+ error.toString();
                        Toast toast = Toast.makeText(getApplicationContext(),text, Toast.LENGTH_LONG);
                        System.out.println("Server Error"+ error.toString());
                        toast.show();
                    }
                });
        Volley.newRequestQueue(getApplicationContext()).add(jsonObjectRequest);
    }
}
