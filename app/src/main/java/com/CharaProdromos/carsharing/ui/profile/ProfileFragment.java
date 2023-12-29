package com.CharaProdromos.carsharing.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.content.Intent;
import com.CharaProdromos.carsharing.GlobalVariables;
import com.CharaProdromos.carsharing.R;
import com.CharaProdromos.carsharing.UserLogin;
import com.CharaProdromos.carsharing.UserRegistration;
import com.CharaProdromos.carsharing.databinding.FragmentProfileBinding;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileFragment extends Fragment {
    ImageView profilePic;
    EditText username;
    EditText password;
    MaterialButton updateButton;
    ProgressBar progressBar;



    private FragmentProfileBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        profilePic = root.findViewById(R.id.imageView);
        username = root.findViewById(R.id.edit_username);
        password = root.findViewById(R.id.edit_password);
        updateButton = root.findViewById(R.id.buttonUpdate);

        String user = GlobalVariables.getInstance().getUsername();
        httpRequestGetUserInfo(user);
        username.setText(user);




        return root;
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
                            String currPassword = response.getString("Password");

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