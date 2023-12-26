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
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ResultsFragment extends Fragment {
    private FragmentResultsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentResultsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


//        LinearLayout checkboxContainer = root.findViewById(R.id.carButtonContainer);
//        CheckBox checkBox = new CheckBox(requireContext());
//        checkboxContainer.addView(checkBox);

        JSONObject cars = httpRequestCars();
        try {
            JSONArray carsArray = cars.getJSONArray("Cars");
            TableRow[] table = createTable(carsArray, root);
        } catch (JSONException e) {
            System.out.println("Failed to get cars");
        }

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


    private JSONObject httpRequestCars() {

        JSONObject response = new JSONObject();
        System.out.println(response.toString());
        JSONArray jsonArray = new JSONArray();

        try {
            JSONObject value1 = new JSONObject();
            JSONObject value2 = new JSONObject();
            JSONObject value3 = new JSONObject();
            JSONObject value4 = new JSONObject();
            JSONObject value5 = new JSONObject();
            value1.put("Plate", "ION123");
            value1.put("Model", "Captur");
            value1.put("Brand", "Renault");
            value2.put("Plate", "NOI321");
            value2.put("Model", "Hurracan");
            value2.put("Brand", "Lamborghini");
            value3.put("Plate", "NOI321");
            value3.put("Model", "Hurracan");
            value3.put("Brand", "Lamborghini");
            value4.put("Plate", "NOI321");
            value4.put("Model", "Hurracan");
            value4.put("Brand", "Lamborghini");
            value5.put("Plate", "NOI321");
            value5.put("Model", "Hurracan");
            value5.put("Brand", "Lamborghini");

            jsonArray.put(value1);
            jsonArray.put(value2);
            jsonArray.put(value3);
            jsonArray.put(value4);
            jsonArray.put(value5);
            response.put("Cars", jsonArray);
        }
        catch (Exception ex) {
            System.out.println("Failed to put objects in the json");
        }
        return response;
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


        try {
            for (int i = 0; i < arrayLen; i++) {
                jsonObject = jsonArray.getJSONObject(i);
                model = jsonObject.getString("Model");
                brand = jsonObject.getString("Brand");
                plate = jsonObject.getString("Plate");
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
                tableRow[i].addView(textBrand);

                TextView textPrice = new TextView(requireContext());
                textPrice.setText("2$/min");
                textPrice.setGravity(Gravity.CENTER_VERTICAL);
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
