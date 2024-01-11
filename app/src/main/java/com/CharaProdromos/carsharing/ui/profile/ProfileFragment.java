package com.CharaProdromos.carsharing.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import com.CharaProdromos.carsharing.GlobalVariables;
import com.CharaProdromos.carsharing.History;
import com.CharaProdromos.carsharing.MainActivity;
import com.CharaProdromos.carsharing.R;
import com.CharaProdromos.carsharing.UserLogin;
import com.CharaProdromos.carsharing.UserRegistration;
import com.CharaProdromos.carsharing.databinding.FragmentProfileBinding;
import com.CharaProdromos.carsharing.ui.search.ResultsFragment;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        String user = GlobalVariables.getInstance().getUsername();

        TextView hiTextView = root.findViewById(R.id.textView);

        String welcomeString = "Hi " + user + "!";
        hiTextView.setText(welcomeString);

        MaterialButton history = root.findViewById(R.id.buttonHistory);
        MaterialButton edit = root.findViewById(R.id.buttonEdit);
        MaterialButton logout = root.findViewById(R.id.buttonLogout);
        MaterialButton delete = root.findViewById(R.id.buttonDelete);
        CardView deleteMsg = root.findViewById(R.id.card_view);
        MaterialButton yes = root.findViewById(R.id.buttonYes);
        MaterialButton no = root.findViewById(R.id.buttonNo);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditProfileFragment editFragment = new EditProfileFragment();
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, editFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }

        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserLogin.class);
                startActivity(intent);
            }

        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HistoryFragment historyFragment = new HistoryFragment();
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, historyFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete.setVisibility(View.GONE);
                history.setVisibility(View.GONE);
                logout.setVisibility(View.GONE);
                edit.setVisibility(View.GONE);
                deleteMsg.setVisibility(View.VISIBLE);


            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteMsg.setVisibility(View.GONE);
                delete.setVisibility(View.VISIBLE);
                history.setVisibility(View.VISIBLE);
                logout.setVisibility(View.VISIBLE);
                edit.setVisibility(View.VISIBLE);
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteMsg.setVisibility(View.GONE);
                delete.setVisibility(View.VISIBLE);
                history.setVisibility(View.VISIBLE);
                logout.setVisibility(View.VISIBLE);
                edit.setVisibility(View.VISIBLE);
                httpRequestDeleteAcc();

            }

        });



        return root;
    }

    private void httpRequestDeleteAcc() {
        JSONObject jsonBody = new JSONObject();
        String user = GlobalVariables.getInstance().getUsername();

        try {
            jsonBody.put("Username",user);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String serverAddress = this.getString(R.string.serverAddress);
        System.out.println(jsonBody.toString());
        String url = serverAddress + "/users/delete";
        System.out.println(url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Deleted user account");
                        try {
                            if (response.getString("message").equals("User deleted successfully.")) {
                                String text = "Deleted successfully";
                                Toast toast = Toast.makeText(getActivity().getApplicationContext(), text, Toast.LENGTH_LONG);
                                toast.show();
                                Intent intent = new Intent(getActivity(), UserLogin.class);
                                startActivity(intent);
                            }
                        }
                        catch (Exception ex){

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String text = "Fail"+ error.toString();
                    }
                });
        Volley.newRequestQueue(getActivity().getApplicationContext()).add(jsonObjectRequest);


    }
}


