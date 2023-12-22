package com.CharaProdromos.carsharing.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.CharaProdromos.carsharing.R;
import com.CharaProdromos.carsharing.databinding.FragmentSearchBinding;
import com.google.android.material.button.MaterialButton;


public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        NotificationsViewModel notificationsViewModel =
//                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        MaterialButton searchButton = root.findViewById(R.id.searchButton);
        LinearLayout checkboxContainer = root.findViewById(R.id.checkboxContainer);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox = new CheckBox(requireContext());
                checkboxContainer.addView(checkBox);
                inflater.inflate(R.layout.search_filters, container, false);

            }

        });
//        final TextView textView = binding.;
//        notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}