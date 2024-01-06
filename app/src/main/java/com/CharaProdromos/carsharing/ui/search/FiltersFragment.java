package com.CharaProdromos.carsharing.ui.search;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.CharaProdromos.carsharing.GlobalVariables;
import com.CharaProdromos.carsharing.R;
import com.CharaProdromos.carsharing.UserLogin;
import com.CharaProdromos.carsharing.UserRegistration;
import com.CharaProdromos.carsharing.databinding.FragmentFiltersBinding;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import java.util.zip.GZIPOutputStream;

public class FiltersFragment extends Fragment {

    private FragmentFiltersBinding binding;
    private String request;
    private JSONObject response = null;
    CheckBox[] checkBoxArray = new CheckBox[0];

    public void setRequestType(String request) {
        this.request = request;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentFiltersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        TextView text = root.findViewById(R.id.FilterOption);
        text.setText(request);

        JSONObject response;
        httpFiltersRequest(request, root);

        MaterialButton apply = root.findViewById(R.id.buttonApply);

        JSONObject globalResponse = GlobalVariables.getInstance().getFilters();


        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxArray != null) {
                    JSONArray array = new JSONArray();
                    for (int i = 0; i < checkBoxArray.length; i++) {
                        String text = checkBoxArray[i].getText().toString();
                        if (checkBoxArray[i].isChecked()) {
                            JSONObject jsonObject = new JSONObject();
                            try {
                                if (text.equals("Manual")) {
                                    text = "1";
                                }
                                else if (text.equals("Automatic")) {
                                    text = "0";
                                }
                                jsonObject.put(text, "True");
                                array.put(jsonObject);
                            }
                            catch (Exception exception) {
                                System.out.println("Failed to put the check in the array");
                            }
                        }
                        else {
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put(text, "False");
                                array.put(jsonObject);
                            }
                            catch (Exception exception) {
                                System.out.println("Failed to put the check in the array");
                            }
                        }
                    }
                    System.out.println("Filters after edit");
                    GlobalVariables.getInstance().editFilters(request, array);
                    System.out.println(GlobalVariables.getInstance().getFilters());
                }

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

    private JSONObject httpFiltersRequest(String request, View root) {
        System.out.println("Filter Request made");

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("request", request);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        String serverAddress = this.getString(R.string.serverAddress);
        System.out.println(jsonBody.toString());
        String url = serverAddress + "/vehicles/filters";
        System.out.println(url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        updateFilters(response);
                        JSONObject currentFilters = GlobalVariables.getInstance().getFilters();
                        System.out.println("About to be used filters");
                        System.out.println(currentFilters);
                        try {
                            System.out.println(response);
                            JSONArray array = currentFilters.getJSONArray(request);
                            checkBoxArray = createBoxes(array, root);
                        }
                        catch (Exception ex) {
                            System.out.println("Failed to get response json array");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String text = "Fail"+ error.toString();
                        System.out.println(text);
                    }
                });


        // Add the request to the RequestQueue
        Volley.newRequestQueue(root.getContext()).add(jsonObjectRequest);
        System.out.println("Return filters");
        System.out.println(response);
        return response;
    }

    private void updateFilters(JSONObject response) {
        System.out.println("Filters before update");
        JSONObject currentFilters = GlobalVariables.getInstance().getFilters();
        System.out.println(currentFilters);
        JSONObject tempObj = new JSONObject();
        String tag;

        try {
            for (int i = 0; i < response.length(); i++) {
                String text = response.names().getString(i);
                JSONArray tempArray = response.getJSONArray(text);

                if(currentFilters.has(text) == false || currentFilters.optJSONArray(text) == null) {
                    GlobalVariables.getInstance().editFilters(text,tempArray);
                   continue;
                }
                JSONArray currFilterArray = currentFilters.getJSONArray((text));
                for (int j = 0; j < tempArray.length(); j++) {
                    tempObj = tempArray.getJSONObject(j);
                    tag = tempObj.keys().next().toString();

                    if (findIfChecked(currFilterArray, tag)) {
                        tempObj.put(tag, "True");
                        tempArray.put(j,tempObj);
                    }
                }
                GlobalVariables.getInstance().editFilters(text,tempArray);
            }
        }
        catch (Exception exception) {
            System.out.println("Failed to update");
            exception.printStackTrace();
        }

        System.out.println("Updated filters");

        System.out.println(GlobalVariables.getInstance().getFilters());
    }

    private boolean findIfChecked(JSONArray array, String key) {
        JSONObject jsonObject;
        try {
            for (int i = 0; i < array.length(); i++) {
                jsonObject = array.getJSONObject(i);
                String text = jsonObject.keys().next();
                if (key.equals(text) && jsonObject.get(text) == "True") {
                    return true;
                }
            }
            return false;
        }
        catch (Exception ex) {
            System.out.println("json exception");
        }

        return false;
    }

    private CheckBox[] createBoxes(JSONArray jsonArray, View root) {
        int arrayLen = jsonArray.length();
        CheckBox checkBoxArray[] = new CheckBox[arrayLen];
        LinearLayout checkboxContainer = root.findViewById(R.id.checkboxContainer);
        JSONObject jsonObject;

        try {
            for (int i = 0; i < arrayLen; i++) {
                jsonObject = jsonArray.getJSONObject(i);
                String text = jsonObject.keys().next();
                String displayText = text;
                if (text.equals("1")) {
                    displayText = "Manual";
                }
                else if (text.equals("0")) {
                    displayText = "Automatic";
                }
                checkBoxArray[i] = new CheckBox(requireContext());
                checkBoxArray[i].setText(displayText);
                checkBoxArray[i].setTextSize(20);
                System.out.println(jsonObject.get(text));
                if (jsonObject.get(text) == "True") {
                    checkBoxArray[i].setChecked(true);
                }
                checkBoxArray[i].setTextSize(20);
                checkboxContainer.addView(checkBoxArray[i]);
            }
        }
        catch (Exception ex) {
            System.out.println("json exception");
        }

        return checkBoxArray;

    }
}

