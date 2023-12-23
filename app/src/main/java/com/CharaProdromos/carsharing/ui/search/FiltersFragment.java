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

public class FiltersFragment extends Fragment {

    private FragmentFiltersBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentFiltersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        LinearLayout checkboxContainer = root.findViewById(R.id.checkboxContainer);
        CheckBox checkBox = new CheckBox(requireContext());
        checkboxContainer.addView(checkBox);

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
}

