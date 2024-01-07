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

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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
                        System.out.println("response to users history");
                        System.out.println(response);
                        try {
                            JSONArray historyArray = response.getJSONArray("rows");
//                            table = createTable(carsArray, root);
                            createHistoryTable(historyArray, root);
                            cardViews = displayTable(root);
                        } catch (JSONException e) {
                            System.out.println("Failed to get history");
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
            System.out.println("Got in create");
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
                System.out.println("Got out of create");
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
        System.out.println("Got in display");
        CardView[] cardViews = new CardView[bookings.size()];
        TableRow[] tableRow = new TableRow[bookings.size()];
        TableLayout rowContainer = root.findViewById(R.id.tableLayout);


        int i=0;
        for (History booking: bookings) {

            tableRow[i] = new TableRow(requireContext());
            // existing code to setup tableRow with ImageView and TextViews

            TextView date = new TextView(requireContext());
            date.setText(booking.getDate());
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

            // Create a CardView and add the TableRow to it
            CardView cardView = new CardView(requireContext());
            cardView.addView(tableRow[i]);
            cardView.setCardElevation(4); // adjust elevation
            cardView.setCardBackgroundColor(Color.BLACK); // adjust background color
            cardView.setRadius(8); // adjust corner radius
            ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(
                    ViewGroup.MarginLayoutParams.MATCH_PARENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(8, 8, 8, 8); // adjust margins
            cardView.setLayoutParams(layoutParams);

            cardViews[i] = cardView;
            rowContainer.addView(cardView);
        }
        return cardViews;
    }

}
