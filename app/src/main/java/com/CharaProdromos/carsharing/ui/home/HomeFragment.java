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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.CharaProdromos.carsharing.GlobalVariables;
import com.CharaProdromos.carsharing.R;
import com.CharaProdromos.carsharing.UserLogin;
import com.CharaProdromos.carsharing.UserRegistration;
import com.CharaProdromos.carsharing.databinding.FragmentHomeBinding;
import com.CharaProdromos.carsharing.ui.search.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {



        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        String user = GlobalVariables.getInstance().getUsername();

        // Find the TextView by its ID
        TextView welcomeTextView = root.findViewById(R.id.welcomeText);

        // Set the welcome message with the username
        String welcomeString = "Welcome, " + user + "!";
        welcomeTextView.setText(welcomeString);

        MaterialButton findMoreButton = root.findViewById(R.id.buttonSearch);

        findMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                Fragment searchFragment =fragmentManager.findFragmentByTag("searchFragment");

                if (searchFragment == null) {
                    searchFragment = new SearchFragment();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.container, searchFragment, "searchFragment");
                    transaction.addToBackStack(null);
                    transaction.commit();
                    BottomNavigationView navView = requireActivity().findViewById(R.id.nav_view);
                    navView.setSelectedItemId(R.id.navigation_search);
                    return;
                }
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                transaction.replace(R.id.container, searchFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                BottomNavigationView navView = requireActivity().findViewById(R.id.nav_view);
                navView.setSelectedItemId(R.id.navigation_search);


            }

        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}