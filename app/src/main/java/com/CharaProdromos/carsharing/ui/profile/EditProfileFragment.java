package com.CharaProdromos.carsharing.ui.profile;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.CharaProdromos.carsharing.GlobalVariables;
import com.CharaProdromos.carsharing.R;
import com.CharaProdromos.carsharing.databinding.FragmentEditProfileBinding;
import com.CharaProdromos.carsharing.databinding.FragmentProfileBinding;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;


public class EditProfileFragment extends Fragment{
    ImageView profilePic;
    EditText username;
    EditText password;
    EditText name;
    EditText license;
    EditText birthday;
    EditText phoneNumber;
    EditText email;
    MaterialButton updateButton;




    private FragmentEditProfileBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentEditProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        profilePic = root.findViewById(R.id.imageView);
        username = root.findViewById(R.id.edit_username);
        password = root.findViewById(R.id.edit_password);
        name = root.findViewById(R.id.edit_name);
        license = root.findViewById(R.id.edit_licence);
        birthday = root.findViewById(R.id.edit_dob);
        phoneNumber = root.findViewById(R.id.edit_phone_number);
        email = root.findViewById(R.id.edit_email);
        updateButton = root.findViewById(R.id.buttonUpdate);

        String user = GlobalVariables.getInstance().getUsername();
        httpRequestGetUserInfo(user);
        username.setText(user);

        updateButton.setOnClickListener((v -> {
            updateButtonClick();

        }));
        return root;
    }

    private void updateButtonClick() {
        String newUsername = username.getText().toString();
        String newPassword = password.getText().toString();
        String newName = name.getText().toString();
        String newDate = birthday.getText().toString();
        String newLicense = license.getText().toString();
        String newPhoneNumber = phoneNumber.getText().toString();
        String newEmail = email.getText().toString();

        if (newUsername.isEmpty() || newPassword.isEmpty() || newName.isEmpty() || newDate.isEmpty() || newLicense.isEmpty() || newPhoneNumber.isEmpty()) {
            Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Please fill all fields", Toast.LENGTH_LONG);
            toast.show();
            return;
        }



        httpRequestUpdateInfo(newUsername, newPassword, newName, newDate, newLicense, newPhoneNumber, newEmail);

    }

    private void httpRequestUpdateInfo(String newUsername, String newPassword, String newName, String newDate, String newLicense, String newPhoneNumber, String newEmail) {
        JSONObject jsonBody = new JSONObject();
        String user = GlobalVariables.getInstance().getUsername();
        try {
            jsonBody.put("oldUsername", user);
            jsonBody.put("Username", newUsername);
            jsonBody.put("Password", newPassword);
            jsonBody.put("name", newName);
            jsonBody.put("date", newDate);
            jsonBody.put("licenseId", newLicense);
            jsonBody.put("phoneNumber", newPhoneNumber);
            jsonBody.put("email", newEmail);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String serverAddress = this.getString(R.string.serverAddress);
        System.out.println(jsonBody.toString());
        String url = serverAddress + "/users/update";
        System.out.println(url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if (response.getString("message").equals("User already exists")) {
                                String text = "Username already exists";
                                Toast toast = Toast.makeText(getActivity().getApplicationContext(), text, Toast.LENGTH_LONG);
                                System.out.println("Success: " + response.toString());
                                toast.show();

                                username.setText(user);
                            }
                            else {
                                GlobalVariables.getInstance().setUsername(newUsername);
                                username.setText(newUsername);
                                name.setText(newName);
                                email.setText(newEmail);
                                license.setText(newLicense);
                                phoneNumber.setText(newPhoneNumber);
                                birthday.setText(newDate);
                                password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                password.setText(newPassword);
                                Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Successful profile update", Toast.LENGTH_LONG);
                                toast.show();

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
                        Toast toast = Toast.makeText(getActivity().getApplicationContext(),text, Toast.LENGTH_LONG);
                        System.out.println("Fail"+ error.toString());
                        toast.show();
                    }
                });


        // Add the request to the RequestQueue
        Volley.newRequestQueue(getActivity().getApplicationContext()).add(jsonObjectRequest);

    }


    private void httpRequestGetUserInfo(String username) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("Username", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String serverAddress = this.getString(R.string.serverAddress);
        System.out.println(jsonBody.toString());
        String url = serverAddress + "/users/profile";
        System.out.println(url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response.toString());


                        try {
                            String currName = response.getString("Full_Name");
                            String currEmail = response.getString("Email");
                            String currId = response.getString("License_id");
                            String currPhoneNum = response.getString("Phone_number");
                            String currBirthday = response.getString("Date_of_birth");
                            String[] parts = currBirthday.split("T");
                            currBirthday = parts[0];

                            String currPassword = response.getString("Password");

                            name.setText(currName);
                            email.setText(currEmail);
                            license.setText(currId);
                            phoneNumber.setText(currPhoneNum);
                            birthday.setText(currBirthday);
                            password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            password.setText(currPassword);

                        }
                        catch (Exception exception) {

                            System.out.println("Error in profile info");
                            exception.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                        // You can log the error or show a message to the user

                        String text = "Fail"+ error.toString();
                        Toast toast = Toast.makeText(getActivity().getApplicationContext(),text, Toast.LENGTH_LONG);
                        System.out.println("Fail"+ error.toString());
                        toast.show();
                    }
                });
        Volley.newRequestQueue(getActivity().getApplicationContext()).add(jsonObjectRequest);
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



}
