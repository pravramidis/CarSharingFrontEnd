package com.CharaProdromos.carsharing;

import static android.provider.Settings.System.getString;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class UserRegistration extends AppCompatActivity{
    private Context context;
//    private Button registerButton = findViewById(R.id.buttonRegister);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_screen);

        Context context = this;
        EditText editTextUsername =findViewById(R.id.editTextUsername);
        EditText editTextPassword =findViewById(R.id.editTextPassword);
        Button registerButton = findViewById(R.id.buttonRegister);
        System.out.println("hrenew");
        System.out.println("hrenew");
        registerButton.setEnabled(true);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("in dfafdaf ");
                // Get the username and password from EditText fields
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();

                // Execute AsyncTask to perform the HTTP request
                httpRequestRegistration(username, password);
            }
        });

    }


    private void httpRequestRegistration(String username, String password) {
        //RequestQueue initialized
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("Username", username);
            jsonBody.put("Password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String serverAddress = this.getString(R.string.serverAddress);
        System.out.println(jsonBody.toString());
        String url = serverAddress + "/users/register";
        System.out.println(url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if (response.getString("message").equals("User already exists")) {
                                String text = "User already exists";
                                Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
                                System.out.println("Success: " + response.toString());
                                toast.show();
                            }
                            else {
                                setContentView(R.layout.login_screen);
                            }
                        }
                        catch (Exception exception) {

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                        // You can log the error or show a message to the user

                        String text = "Fail"+ error.toString();
                        Toast toast = Toast.makeText(getApplicationContext(),text, Toast.LENGTH_LONG);
                        System.out.println("Fail"+ error.toString());
                        toast.show();
                    }
                });


        // Add the request to the RequestQueue
        Volley.newRequestQueue(getApplicationContext()).add(jsonObjectRequest);

    }
}