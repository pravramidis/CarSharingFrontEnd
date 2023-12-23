package com.CharaProdromos.carsharing.ui.search;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.CharaProdromos.carsharing.R;
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

public class FiltersFragment extends Fragment {

    private FragmentFiltersBinding binding;
    private String request;

    public void setRequestType(String request) {
        this.request = request;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentFiltersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        TextView text = root.findViewById(R.id.FilterOption);
        text.setText(request);

        JSONArray request;
        request =  httpFiltersRequest("color");
        createBoxes(request, root);

        MaterialButton apply = root.findViewById(R.id.buttonApply);

        apply.setOnClickListener(new View.OnClickListener() {
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

    private JSONArray httpFiltersRequest(String request) {
        JSONArray jsonArray = new JSONArray();

        jsonArray.put("Yellow");
        jsonArray.put("Grey");

        return jsonArray;
    }

    private void createBoxes(JSONArray jsonArray, View root) {
        int arrayLen = jsonArray.length();
        CheckBox checkBoxArray[] = new CheckBox[arrayLen];
        LinearLayout checkboxContainer = root.findViewById(R.id.checkboxContainer);

        try {
            for (int i = 0; i < arrayLen; i++) {
                String text = jsonArray.getString(i);
                checkBoxArray[i] = new CheckBox(requireContext());
                checkBoxArray[i].setText(text);
                checkboxContainer.addView(checkBoxArray[i]);
            }
        }
        catch (Exception ex) {
            System.out.println("json exception");
        }


    }
}

