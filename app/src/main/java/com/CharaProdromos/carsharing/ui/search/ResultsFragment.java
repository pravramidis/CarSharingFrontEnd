package com.CharaProdromos.carsharing.ui.search;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.CharaProdromos.carsharing.GlobalVariables;
import com.CharaProdromos.carsharing.R;
import com.CharaProdromos.carsharing.databinding.FragmentResultsBinding;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ResultsFragment extends Fragment {
    private FragmentResultsBinding binding;
    private TableRow[] table;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentResultsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


//        LinearLayout checkboxContainer = root.findViewById(R.id.carButtonContainer);
//        CheckBox checkBox = new CheckBox(requireContext());
//        checkboxContainer.addView(checkBox);

        httpRequestCars(root);
//        try {
//            JSONArray carsArray = cars.getJSONArray("Cars");
//            TableRow[] table = createTable(carsArray, root);
//        } catch (JSONException e) {
//            System.out.println("Failed to get cars");
//        }

        MaterialButton back= root.findViewById(R.id.backToFilters);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                Fragment searchFragment =fragmentManager.findFragmentByTag("searchFragment");
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.container, searchFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });




        return root;
    }

    private void carListener(TableRow row, String plate) {
        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("User wants this car: plate:"+ plate);

            }
        });
    }


    private void httpRequestCars(View root) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("request", GlobalVariables.getInstance().getFilters());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("Search request");
        System.out.println(jsonBody);

        String serverAddress = this.getString(R.string.serverAddress);
        System.out.println(jsonBody.toString());
        String url = serverAddress + "/vehicles/search";
        System.out.println(url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("response to cars with filters");
                        System.out.println(response);
                        try {
                            JSONArray carsArray = response.getJSONArray("cars");
                            table = createTable(carsArray, root);
                        } catch (JSONException e) {
                            System.out.println("Failed to get cars");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("why error");
                        String text = "Fail "+ error.toString();
                        System.out.println(text);
                    }
                });


        // Add the request to the RequestQueue
        Volley.newRequestQueue(root.getContext()).add(jsonObjectRequest);
        System.out.println("Return filters");
    }


    private TableRow[] createTable(JSONArray jsonArray, View root) {
        int arrayLen = jsonArray.length();
        TableRow[] tableRow = new TableRow[arrayLen];
        TableLayout rowContainer = root.findViewById(R.id.tableLayout);
        JSONObject jsonObject;
        String type;
        String model;
        String color;
        String brand;
        int capacity;
        String fuel;
        String plate;
        double xCoordinates;
        double yCoordinates;
        double price;


        try {
            for (int i = 0; i < arrayLen; i++) {
                jsonObject = jsonArray.getJSONObject(i);
                model = jsonObject.getString("Model");
                brand = jsonObject.getString("Brand");
                plate = jsonObject.getString("Plate_number");
                price = jsonObject.getDouble("Price");
//                type = jsonObject.getString("Type");
//                color = jsonObject.getString("Color");
//                capacity = jsonObject.getInt("Capacity");
//                fuel = jsonObject.getString("Fuel Type");
//                xCoordinates = jsonObject.getDouble("XCoords");
//                yCoordinates = jsonObject.getDouble("YCoords");

                tableRow[i] = new TableRow(requireContext());
                carListener(tableRow[i], plate);
                tableRow[i].setGravity(Gravity.CENTER_VERTICAL);
                ImageView imageView = new ImageView(requireContext());
                imageView.setImageResource(R.drawable.lambo);
                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT,
                        1.0f
                );
                layoutParams.width = 200;
                layoutParams.height = 200;
                layoutParams.gravity = Gravity.CENTER;
                imageView.setLayoutParams(layoutParams);
                imageView.setMaxWidth(20);
                imageView.setPadding(8, 8, 8, 8);
                tableRow[i].addView(imageView);
                tableRow[i].setClickable(true);

                TextView textModel = new TextView(requireContext());
                textModel.setText(model);
                textModel.setTextColor(Color.BLACK);
                textModel.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT,
                        1.0f
                ));
                textModel.setGravity(Gravity.CENTER_VERTICAL);
                textModel.setGravity(Gravity.CENTER_HORIZONTAL);
                textModel.setPadding(8, 8, 8, 8);
                tableRow[i].addView(textModel);

                TextView textBrand = new TextView(requireContext());
                textBrand.setText(brand);
                textBrand.setTextColor(Color.BLACK);
                textBrand.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT,
                        1.0f
                ));
                textBrand.setPadding(8, 8, 8, 8);
                textBrand.setGravity(Gravity.CENTER_VERTICAL);
                textBrand.setGravity(Gravity.CENTER_HORIZONTAL);
                tableRow[i].addView(textBrand);

                TextView textPrice = new TextView(requireContext());
                textPrice.setText(price+"$/min");
                textPrice.setGravity(Gravity.CENTER_VERTICAL);
                textPrice.setGravity(Gravity.CENTER_HORIZONTAL);
                textPrice.setTextColor(Color.BLACK);
                textPrice.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT,
                1.0f
                ));
                textPrice.setPadding(8, 8, 8, 8);
                tableRow[i].addView(textPrice);

                TextView textDistance = new TextView(requireContext());
                textDistance.setGravity(Gravity.CENTER_VERTICAL);
                textDistance.setGravity(Gravity.CENTER_HORIZONTAL);
                textDistance.setTextColor(Color.BLACK);
                textDistance.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT,
                        1.0f
                ));
                textDistance.setText("1km");
                textDistance.setPadding(8, 8, 8, 8);
                tableRow[i].addView(textDistance);

                rowContainer.addView(tableRow[i]);
            }
        }
        catch (Exception ex) {
            System.out.println("json exception");
            ex.printStackTrace();
        }

        return tableRow;

    }
}
