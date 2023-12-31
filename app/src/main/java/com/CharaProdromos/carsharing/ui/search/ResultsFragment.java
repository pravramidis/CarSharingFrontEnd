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
import com.CharaProdromos.carsharing.Vehicle;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ResultsFragment extends Fragment {
    private FragmentResultsBinding binding;
    private TableRow[] table;

    private boolean priceFlag = true;

    private List<Vehicle> cars = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentResultsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        httpRequestCars(root);

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

        TextView price = root.findViewById(R.id.resultPrice);

        price.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                if (priceFlag == true) {
                    priceFlag = false;
                    Collections.sort(cars, new priceComparator());
                    table = displayTable(root);
                }
                else {
                    priceFlag = true;
                    Collections.sort(cars, new priceComparatorOpposite());
                    table = displayTable(root);
                }

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
//                            table = createTable(carsArray, root);
                            createVehicleTable(carsArray);
                            Collections.sort(cars, new priceComparator());
                            table = displayTable(root);
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

    private  void createVehicleTable(JSONArray jsonArray) {
        int arrayLen = jsonArray.length();
        JSONObject jsonObject;
        String model;
        String brand;
        String plate;
        double xCoordinates;
        double yCoordinates;
        double price;


        try {
            for (int i = 0; i < arrayLen; i++) {
                jsonObject = jsonArray.getJSONObject(i);
                plate = jsonObject.getString("Plate_number");
                price = jsonObject.getDouble("Price");
                brand = jsonObject.getString("Brand");
                model = jsonObject.getString("Model");

                xCoordinates = jsonObject.getDouble("X_Coordinates");
                yCoordinates = jsonObject.getDouble("Y_Coordinates");


                cars.add(new Vehicle(plate, xCoordinates, yCoordinates, price, brand, model));
            }
        } catch (Exception ex) {
            System.out.println("json exception");
            ex.printStackTrace();
        }
    }

    private void clearTable(View root) {
        TableLayout rowContainer = root.findViewById(R.id.tableLayout);
        View firstView = rowContainer.getChildAt(0);
        rowContainer.removeAllViews();

        if (firstView != null) {
            rowContainer.addView(firstView);
        }
    }

    private TableRow[] displayTable(View root) {
        TableRow[] tableRow = new TableRow[cars.size()];
        TableLayout rowContainer = root.findViewById(R.id.tableLayout);
        clearTable(root);
        int i = 0;
        for (Vehicle car: cars){
            tableRow[i] = new TableRow(requireContext());
            carListener(tableRow[i], car.getPlate());
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
            textModel.setText(car.getModel());
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
            textBrand.setText(car.getBrand());
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
            textPrice.setText(car.getCostMinute()+"$/min");
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
            i++;

        }
        return tableRow;

    }

    class priceComparator implements Comparator<Vehicle> {
        @Override
        public int compare(Vehicle v1, Vehicle v2) {
            return Double.compare(v1.getCostMinute(), v2.getCostMinute());
        }
    }

    class priceComparatorOpposite implements Comparator<Vehicle> {
        @Override
        public int compare(Vehicle v1, Vehicle v2) {
            return -Double.compare(v1.getCostMinute(), v2.getCostMinute());
        }
    }
}
