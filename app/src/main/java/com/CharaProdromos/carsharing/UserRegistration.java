package com.CharaProdromos.carsharing;

import static android.provider.Settings.System.getString;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.google.android.material.button.MaterialButton;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UserRegistration extends AppCompatActivity{
    private Context context;


    private DatePickerDialog datePickerDialog;
    private MaterialButton dateButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_screen);
        
        initDatePicker();
        dateButton = findViewById(R.id.datePickerButton);
        dateButton.setText(getTodaysDate());
        dateButton.setEnabled(true);

        EditText editTextUsername =findViewById(R.id.editTextUsername);
        EditText editTextPassword =findViewById(R.id.editTextPassword);
        EditText editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        EditText editTextName = findViewById(R.id.editTextName);
        EditText editTextDateofBirth = editTextName;
        EditText editTextLicenseId = findViewById(R.id.editTextLicenceId);
        EditText editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        EditText editTextEmail = findViewById(R.id.editTextEmail);
        Button registerButton = findViewById(R.id.buttonRegister);

        registerButton.setEnabled(true);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();
                String confirmPassword = editTextConfirmPassword.getText().toString();
                String name = editTextName.getText().toString();
                String date = dateButton.getText().toString();
                String licenseId = editTextLicenseId.getText().toString();
                String phoneNumber = editTextPhoneNumber.getText().toString();
                String email = editTextEmail.getText().toString();



                if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || name.isEmpty() || date.isEmpty() || licenseId.isEmpty() || phoneNumber.isEmpty()) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }

                try {
                    String [] words = date.split("\\s+");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy dd MM");
                    words[0] = monthDecode(words[0]).toString();
                    date = words[2]+" "+words[0]+" "+words[1];
                    String today = getTodaysDate();
                    words = today.split("\\s+");
                    words[0] = monthDecode(words[0]).toString();
                    today = words[2]+" "+words[0]+" "+words[1];
                    System.out.println("dateofbirth " + date);
                    System.out.println("today " + getTodaysDate());
                    Date birthDate = sdf.parse(date);
                    Date currentDate = sdf.parse(today);
                    long difference = currentDate.getTime() - birthDate.getTime();
                    //years in milliseconds
                    long eighteenYears = 18L * 365 * 24 * 60 * 60 * 1000;
                    System.out.println("diff" + difference);
                    System.out.println("target"+eighteenYears);

                    if (difference <= eighteenYears) {
                        Toast toast = Toast.makeText(getApplicationContext(), "You need to be at least 18 years old", Toast.LENGTH_LONG);
                        toast.show();
                        return;
                    }
                }
                catch (Exception ex) {
                    System.out.println("error in date check");
                    ex.printStackTrace();
                }

                if (password.equals(confirmPassword) == false) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }
                httpRequestRegistration(username, password, name, date, licenseId, phoneNumber, email);
            }
        });
    }

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);

        return makeDateString(day, month, year);
    }

    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }


    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                month = month + 1;

                String date = makeDateString(dayOfMonth, month, year);
                dateButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }

    private String getMonthFormat(int month) {
        switch (month) {
            case 1: {
                return "JAN";
            }
            case 2: {
                return "FEB";
            }
            case 3: {
               return "MAR";
            }
            case 4: {
                return "APR";
            }
            case 5: {
                return "MAY";
            }
            case 6: {
                return "JUN";
            }
            case 7: {
                return "JUL";
            }
            case 8: {
                return "AUG";
            }
            case 9: {
                return "SEP";
            }
            case 10: {
                return "OCT";
            }
            case 11: {
                return "NOV";
            }
            case 12: {
                return "DEC";
            }
        }

        return "invalid month";
    }

    public void openDatePicker(View view) {
        datePickerDialog.show();
    }

    private String monthDecode(String monthName) {
        System.out.println(monthName);
        switch (monthName) {
            case "JAN": {
                return "1";
            }
            case "FEB": {
                return "2";
            }
            case "MAR": {
                return "3";
            }
            case "APR": {
                return "4";
            }
            case "MAY": {
                return "5";
            }
            case "JUN": {
                return "6";
            }
            case "JUL": {
                return "7";
            }
            case "AUG": {
                return "8";
            }
            case "SEP": {
                return "9";
            }
            case "OCT": {
                return "10";
            }
            case "NOV": {
                return "11";
            }
            case "DEC": {
                return "12";
            }
        }
        //never gets here
        return "-1";
    }


    private void httpRequestRegistration(String username, String password, String name, String date, String licenseId, String phoneNumber, String email) {
        //RequestQueue initialized
        System.out.println(date);
        String [] words = date.split("\\s+");
        System.out.println(words[0]);
        System.out.println(words[1]);
        System.out.println(words[2]);
        date = words[0]+"-"+words[1]+"-"+words[2];

        System.out.println(date);


        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("Username", username);
            jsonBody.put("Password", password);
            jsonBody.put("name", name);
            jsonBody.put("date", date);
            jsonBody.put("licenseId", licenseId);
            jsonBody.put("phoneNumber", phoneNumber);
            jsonBody.put("email", email);
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
                                Intent intent = new Intent(UserRegistration.this, UserLogin.class);
                                startActivity(intent);
                            }
                        }
                        catch (Exception exception) {

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

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