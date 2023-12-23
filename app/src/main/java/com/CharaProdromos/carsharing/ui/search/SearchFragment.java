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
import com.CharaProdromos.carsharing.databinding.FragmentSearchBinding;
import com.google.android.material.button.MaterialButton;


public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        MaterialButton searchButton = root.findViewById(R.id.searchButton);

        MaterialButton type = root.findViewById(R.id.typeButton);
        MaterialButton model = root.findViewById(R.id.modelButton);
        MaterialButton color = root.findViewById(R.id.colorButton);
        MaterialButton brand = root.findViewById(R.id.brandButton);
        MaterialButton gearbox = root.findViewById(R.id.gearboxButton);
        MaterialButton capacity = root.findViewById(R.id.capacityButton);
        clickFiltersListerner(type);
        clickFiltersListerner(model);
        clickFiltersListerner(color);
        clickFiltersListerner(brand);
        clickFiltersListerner(gearbox);
        clickFiltersListerner(capacity);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FiltersFragment filtersFragment = new FiltersFragment();
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, filtersFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }

        });
//        final TextView textView = binding.;
//        notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    private void clickFiltersListerner(MaterialButton button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FiltersFragment filtersFragment = new FiltersFragment();
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, filtersFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}