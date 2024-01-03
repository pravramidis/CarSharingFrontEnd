package com.CharaProdromos.carsharing.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.annotation.NonNull;

import com.CharaProdromos.carsharing.databinding.FragmentCardInfoBinding;



public class CardInfoFragment extends Fragment{
    private FragmentCardInfoBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentCardInfoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

}
