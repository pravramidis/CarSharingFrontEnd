package com.CharaProdromos.carsharing.ui.search;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.annotation.NonNull;

import com.CharaProdromos.carsharing.GlobalVariables;
import com.CharaProdromos.carsharing.R;
import com.CharaProdromos.carsharing.Vehicle;
import com.CharaProdromos.carsharing.databinding.FragmentCardInfoBinding;
import com.CharaProdromos.carsharing.ui.home.HomeFragment;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.Date;


public class CardInfoFragment extends Fragment{

    Vehicle car;
    private FragmentCardInfoBinding binding;

    public CardInfoFragment(Vehicle car) { this.car =car; }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentCardInfoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        EditText expiryDate = root.findViewById(R.id.expiry_date);
        EditText cvv = root.findViewById(R.id.cvv);
        EditText cardNumber = root.findViewById(R.id.card_number);
        EditText name = root.findViewById(R.id.cardholder_name);
        expiryDate.addTextChangedListener(new TextWatcher() {
            private String lastInput = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                if (input.length() == 2 && !lastInput.endsWith("/")) {
                    expiryDate.setText(new StringBuilder(input).append("/").toString());
                    expiryDate.setSelection(expiryDate.getText().length());
                } else if (input.length() == 2 && lastInput.endsWith("/")) {
                    expiryDate.setText(input.substring(0, 1));
                    expiryDate.setSelection(1);
                } else if (input.length() > 5) {
                    expiryDate.setText(lastInput);
                    expiryDate.setSelection(lastInput.length());
                }
                lastInput = expiryDate.getText().toString();
            }
        });
        MaterialButton payButton = root.findViewById(R.id.buttonPay);
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                System.out.println("month: "+expiryDate.getText().toString().substring(0,2));
//                System.out.println("year: "+expiryDate.getText().toString().substring(3,4));

                if(expiryDate.getText().toString().isEmpty() || cvv.getText().toString().isEmpty() || cardNumber.getText().toString().isEmpty() || name.getText().toString().isEmpty()) {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Please fill all fields", Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }

                if(expiryDate.getText().length() !=5 ||Integer.parseInt(expiryDate.getText().toString().substring(0,2)) > 12 || Integer.parseInt(expiryDate.getText().toString().substring(3,5)) < 24 ||
                        Integer.parseInt(expiryDate.getText().toString().substring(3,5)) == 24 && Integer.parseInt(expiryDate.getText().toString().substring(0,2)) < 2) {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Incorrect expiration date", Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }

                if(cvv.getText().length()<3) {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Incorrect CVV", Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }

                if(cardNumber.getText().length()<16) {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Incorrect card number", Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }
                if(expiryDate.getText().toString().length() != 5 || cvv.getText().toString().isEmpty() || cardNumber.getText().toString().isEmpty() || name.getText().toString().isEmpty()) {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Please fill all fields", Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }


                httpRequestAddToHistory(car);
                httpRequestAvailabity(car,"1");

                Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Successful payment", Toast.LENGTH_LONG);
                toast.show();
                HomeFragment homeFragment = new HomeFragment();
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, homeFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return root;
    }
    private void httpRequestAvailabity(Vehicle car, String value) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("plate", car.getPlate());
            jsonBody.put("value", value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String serverAddress = this.getString(R.string.serverAddress);
        System.out.println(jsonBody.toString());
        String url = serverAddress + "/vehicles/availability";
        System.out.println(url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {



                        } catch (Exception exception) {

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

    private void httpRequestAddToHistory(Vehicle car) {
        JSONObject jsonBody = new JSONObject();
        String user = GlobalVariables.getInstance().getUsername();
        Date currentDate = new Date();
        String[] date = currentDate.toString().split(" ");
        String payDate = date[0] +" "+ date[1]+" " +date[2]+" " +date[3]+" "+date[5];
        String type = car.getRate();
        String[] temp = type.split("/");
        type = temp[1];
        try {
            jsonBody.put("PlateNum", car.getPlate());
            jsonBody.put("Type",type);
            jsonBody.put("Price", car.getFinalPrice());
            jsonBody.put("Duration", car.getTime());
            jsonBody.put("Date", payDate );
            jsonBody.put("Username",user);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String serverAddress = this.getString(R.string.serverAddress);
        System.out.println(jsonBody.toString());
        String url = serverAddress + "/histories/booking";
        System.out.println(url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {



                        } catch (Exception exception) {

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

}
