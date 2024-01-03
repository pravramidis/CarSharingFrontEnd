package com.CharaProdromos.carsharing.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.annotation.NonNull;
import com.CharaProdromos.carsharing.R;

import com.CharaProdromos.carsharing.Vehicle;
import com.CharaProdromos.carsharing.databinding.FragmentPaymentBinding;
import com.google.android.material.button.MaterialButton;

public class PaymentFragment extends Fragment {
    Vehicle car;

    private FragmentPaymentBinding binding;

    public PaymentFragment(Vehicle car) {
        this.car = car;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentPaymentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        MaterialButton payButton = root.findViewById(R.id.buttonPay);

        TextView duration = root.findViewById(R.id.durationText);
        TextView rate = root.findViewById(R.id.rateText);
        TextView plate = root.findViewById(R.id.plateText);
        TextView brand = root.findViewById(R.id.brandText);
        TextView model = root.findViewById(R.id.modelText);
        TextView finalPrice = root.findViewById(R.id.finalPrice);

        rate.setText(car.getRate());
        duration.setText(car.getTime());
        plate.setText(car.getPlate());
        brand.setText(car.getBrand());
        model.setText(car.getModel());

        String[] time = car.getTime().split(":",3);
        String totalPrice = getTotalPrice(time, car.getRate());
        totalPrice = totalPrice +" €";
        finalPrice.setText(totalPrice);

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CardInfoFragment cardFragment = new CardInfoFragment();
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, cardFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });





        return root;
    }

    private String getTotalPrice(String [] time, String rate) {
        String finalPrice,  rateType, ratePrice;
        int days, hours, minutes, seconds;
        double price;
        String[] rates;
        hours = Integer.parseInt(time[0]);
        minutes = Integer.parseInt(time[1]);
        seconds = Integer.parseInt(time[2]);

        System.out.println("hours: "+hours);
        System.out.println("mins: "+minutes);

        System.out.println("seconds: "+seconds);


        rates = rate.split("/",2);
        ratePrice = rates[0].substring(0, rates[0].length() - 2);
        price = Double.parseDouble(ratePrice);
        System.out.println("Price: "+price);
        rateType = rates[1];

        if(rateType.equals("day")) {
            days = hours % 24;
            if (minutes != 0 || seconds !=0) {
                days++;
            }
            price = days*price;
            finalPrice = String.valueOf(price);
            return finalPrice;
        }
        if(rateType.equals("hour")) {
            days = hours % 24;
            if(days != 0 ) {
                hours = hours +(days*24);
            }
            if(minutes !=0 || seconds != 0) {
                hours++;
            }
            price = hours*price;
            finalPrice = String.valueOf(price);
            return finalPrice;
        }
        days = hours % 24;
        if(days != 0) {
            minutes = minutes +(days *24*60);
        }
        if (seconds != 0) {
            minutes++;
        }
        System.out.println(minutes);
        price = minutes*price;
        finalPrice = String.valueOf(price);

        return finalPrice;
    }
}

