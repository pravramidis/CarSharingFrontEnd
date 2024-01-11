package com.CharaProdromos.carsharing.ui.profile;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.CharaProdromos.carsharing.GlobalVariables;
import com.CharaProdromos.carsharing.History;
import com.CharaProdromos.carsharing.R;
import com.CharaProdromos.carsharing.Vehicle;
import com.CharaProdromos.carsharing.databinding.FragmentHistoryBinding;
import com.CharaProdromos.carsharing.ui.search.ResultsFragment;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class HistoryFragment extends Fragment {
    private FragmentHistoryBinding binding;

    private List<History> bookings = new ArrayList<>();

    private CardView[] cardViews;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        httpRequestUsersHistory(root);

        return root;
    }

    private void httpRequestUsersHistory(View root) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("Username", GlobalVariables.getInstance().getUsername());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("Get history request");
        System.out.println(jsonBody);

        String serverAddress = this.getString(R.string.serverAddress);
        System.out.println(jsonBody.toString());
        String url = serverAddress + "/histories/getHistory";
        System.out.println(url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray historyArray = response.getJSONArray("rows");
//                            table = createTable(carsArray, root);
                            createHistoryTable(historyArray, root);
                            cardViews = displayTable(root);
                        } catch (JSONException e) {
                            System.out.println("Failed to get history");
                            e.printStackTrace();
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
        System.out.println("Return com.CharaProdromos.carsharing.History");


    }

    private  void createHistoryTable(JSONArray jsonArray, View root) {
        int arrayLen = jsonArray.length();
        JSONObject jsonObject;
        String duration;
        String date;
        String plate;
        String type;
        String username;
        double price;


        try {
            for (int i = 0; i < arrayLen; i++) {
                jsonObject = jsonArray.getJSONObject(i);
                plate = jsonObject.getString("FK2_Plate_number");
                price = jsonObject.getDouble("Price");
                date = jsonObject.getString("Payment_date");
                duration = jsonObject.getString("Trip_duration");
                type =  jsonObject.getString("FK3_Type");
                username = jsonObject.getString("FK1_Username");


                History tempHistory = new History(username, plate, String.valueOf(price), date, duration, type);

                bookings.add(tempHistory);
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


    private CardView[] displayTable(View root) {
        CardView[] cardViews = new CardView[bookings.size()+1];
        TableRow[] tableRow = new TableRow[bookings.size()+1];
        TableLayout rowContainer = root.findViewById(R.id.tableLayout);


        //int i=0;
        for (int i=0; i<bookings.size()+1; i++) {

            tableRow[i] = new TableRow(requireContext());

            TextView date = new TextView(requireContext());
            String tempDate;
            if(i==0) {
                tempDate = "Payment Date";
                date.setTextSize(18);

            }
            else {
                tempDate = bookings.get(bookings.size()-i).getDate();
                String[] fullDate = tempDate.split(" ");
                tempDate = fullDate[2] + "/" + monthDecode(fullDate[1]) + "/" + fullDate[4];
            }
            date.setText(tempDate);
            date.setTextColor(Color.WHITE);
            date.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT,
                    1.0f
            ));
            date.setGravity(Gravity.CENTER_VERTICAL);
            date.setGravity(Gravity.CENTER_HORIZONTAL);
            date.setPadding(8, 8, 8, 8);
            tableRow[i].addView(date);

            TextView plate = new TextView(requireContext());
            if(i==0) {
                plate.setText("Plate Number");
                plate.setTextSize(18);
            }
            else {
                plate.setText(bookings.get(bookings.size()-i).getPlate());
            }

            plate.setTextColor(Color.WHITE);
            plate.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT,
                    1.0f
            ));
            plate.setGravity(Gravity.CENTER_VERTICAL);
            plate.setGravity(Gravity.CENTER_HORIZONTAL);
            plate.setPadding(8, 8, 8, 8);
            tableRow[i].addView(plate);

            TextView duration = new TextView(requireContext());
            if(i==0) {
                duration.setText("Trip Duration");
                duration.setTextSize(18);
            }
            else {
                duration.setText(bookings.get(bookings.size()-i).getDuration());
            }
            duration.setTextColor(Color.WHITE);
            duration.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT,
                    1.0f
            ));
            duration.setGravity(Gravity.CENTER_VERTICAL);
            duration.setGravity(Gravity.CENTER_HORIZONTAL);
            duration.setPadding(8, 8, 8, 8);
            tableRow[i].addView(duration);

            TextView price = new TextView(requireContext());
            if(i==0) {
                price.setText("Total Price");
                price.setTextSize(18);
            }
            else {
                price.setText(bookings.get(bookings.size()-i).getPrice() + "€");
            }
            price.setTextColor(Color.WHITE);
            price.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT,
                    1.0f
            ));
            price.setGravity(Gravity.CENTER_VERTICAL);
            price.setGravity(Gravity.CENTER_HORIZONTAL);
            price.setPadding(8, 8, 8, 8);
            tableRow[i].addView(price);



            CardView cardView = new CardView(requireContext());
            cardView.addView(tableRow[i]);
            cardView.setCardElevation(4);
            cardView.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.myThird)); // adjust background color
            cardView.setRadius(42);


            TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);

            layoutParams.setMargins(8, 30, 8, 30); // adjust margins
            cardView.setLayoutParams(layoutParams);

            cardViews[i] = cardView;
            rowContainer.addView(cardView);

            rowContainer.invalidate();
            rowContainer.requestLayout();
        }
        return cardViews;
    }

    private String monthDecode(String monthName) {
        System.out.println(monthName);
        switch (monthName) {
            case "Jan": {
                return "1";
            }
            case "Feb": {
                return "2";
            }
            case "Mar": {
                return "3";
            }
            case "Apr": {
                return "4";
            }
            case "May": {
                return "5";
            }
            case "Jun": {
                return "6";
            }
            case "Jul": {
                return "7";
            }
            case "Aug": {
                return "8";
            }
            case "Sep": {
                return "9";
            }
            case "Oct": {
                return "10";
            }
            case "Nov": {
                return "11";
            }
            case "Dec": {
                return "12";
            }
        }
        //never gets here
        return "-1";
    }

}
