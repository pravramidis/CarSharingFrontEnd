package com.CharaProdromos.carsharing.ui.search;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.annotation.NonNull;

import com.CharaProdromos.carsharing.R;
import com.CharaProdromos.carsharing.databinding.FragmentCardInfoBinding;
import com.CharaProdromos.carsharing.ui.home.HomeFragment;
import com.google.android.material.button.MaterialButton;


public class CardInfoFragment extends Fragment{
    private FragmentCardInfoBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentCardInfoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        EditText expiryDate = root.findViewById(R.id.expiry_date);
        EditText cvv = root.findViewById(R.id.cvv);
        EditText cardNumber = root.findViewById(R.id.card_number);
        EditText name = root.findViewById(R.id.cardholder_name);
        expiryDate.addTextChangedListener(new TextWatcher() {
            private String lastInput = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                if (input.length() == 2 && !lastInput.endsWith("/")) {
                    expiryDate.setText(new StringBuilder(input).append("/").toString());
                    expiryDate.setSelection(expiryDate.getText().length());
                } else if (input.length() == 2 && lastInput.endsWith("/")) {
                    expiryDate.setText(input.substring(0, 1));
                    expiryDate.setSelection(1);
                } else if (input.length() > 5) {
                    expiryDate.setText(lastInput);
                    expiryDate.setSelection(lastInput.length());
                }
                lastInput = expiryDate.getText().toString();
            }
        });
        MaterialButton payButton = root.findViewById(R.id.buttonPay);
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(expiryDate.getText().toString().length() != 5 || cvv.getText().toString().isEmpty() || cardNumber.getText().toString().isEmpty() || name.getText().toString().isEmpty()) {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Please fill all fields", Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }

                Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Successful payment", Toast.LENGTH_LONG);
                toast.show();
                HomeFragment homeFragment = new HomeFragment();
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, homeFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return root;
    }

}
