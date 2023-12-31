package com.CharaProdromos.carsharing.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.annotation.NonNull;

import com.CharaProdromos.carsharing.databinding.FragmentShowCarBinding;

public class ShowCarFragment extends Fragment{

    private FragmentShowCarBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentShowCarBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }
}
