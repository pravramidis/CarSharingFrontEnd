package com.CharaProdromos.carsharing.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.CharaProdromos.carsharing.GlobalVariables;
import com.CharaProdromos.carsharing.R;
import com.CharaProdromos.carsharing.UserLogin;
import com.CharaProdromos.carsharing.UserRegistration;
import com.CharaProdromos.carsharing.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {



        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button findMoreButton = root.findViewById(R.id.buttonSearch);


        String user = GlobalVariables.getInstance().getUsername();

        // Find the TextView by its ID
        TextView welcomeTextView = root.findViewById(R.id.welcomeText);

        // Set the welcome message with the username
        String welcomeString = "Welcome, " + user + "!";
        welcomeTextView.setText(welcomeString);


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}