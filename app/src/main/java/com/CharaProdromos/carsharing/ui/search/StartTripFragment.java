package com.CharaProdromos.carsharing.ui.search;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.CharaProdromos.carsharing.GlobalVariables;
import com.CharaProdromos.carsharing.R;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.CharaProdromos.carsharing.Vehicle;
import com.CharaProdromos.carsharing.databinding.FragmentProfileBinding;
import com.CharaProdromos.carsharing.databinding.FragmentStartTripBinding;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

public class StartTripFragment extends Fragment {
    Vehicle car;
    TextView code;
    TextView timer;

    private FragmentStartTripBinding binding;
    private CountDownTimer countDownTimer;
    private boolean isTimerRunning = false;

    public StartTripFragment(Vehicle car) {
        this.car = car;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentStartTripBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        String carPlate = GlobalVariables.getInstance().getPlateNumber();
        MaterialButton startButton = root.findViewById(R.id.buttonStart);
        code = root.findViewById(R.id.codeText);
        timer = root.findViewById(R.id.timerText);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startButton.getText().equals("START TRIP")) {
                    Random random = new Random();
                    Integer fourDigitCode = 1000 + random.nextInt(9000);
                    code.setText(fourDigitCode.toString());
                    startButton.setText("END TRIP");
                    startTimer();
                    httpRequestAvailabity(car,"0");

                }
                else {
                    if (isTimerRunning) {
                        countDownTimer.cancel();
                        isTimerRunning = false;
                    }
                    car.setTime(timer.getText().toString());
                    PaymentFragment payFragment = new PaymentFragment(car);
                    FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.container, payFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }

        });

        return root;
    }

    private void httpRequestAvailabity(Vehicle car, String value) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("plate", car.getPlate());
            jsonBody.put("value", value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String serverAddress = this.getString(R.string.serverAddress);
        System.out.println(jsonBody.toString());
        String url = serverAddress + "/vehicles/availability";
        System.out.println(url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {



                        } catch (Exception exception) {

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String text = "Fail"+ error.toString();
                        Toast toast = Toast.makeText(getActivity().getApplicationContext(),text, Toast.LENGTH_LONG);
                        System.out.println("Fail"+ error.toString());
                        toast.show();
                    }
                });
        Volley.newRequestQueue(getActivity().getApplicationContext()).add(jsonObjectRequest);

    }



    private void startTimer() {
        countDownTimer = new CountDownTimer(Long.MAX_VALUE, 1000) {
            long timeElapsed = 0;

            @Override
            public void onTick(long millisUntilFinished) {
                timeElapsed += 1000;
                String hms = String.format(Locale.getDefault(), "%02d:%02d:%02d",
                        TimeUnit.MILLISECONDS.toHours(timeElapsed),
                        TimeUnit.MILLISECONDS.toMinutes(timeElapsed) % 60,
                        TimeUnit.MILLISECONDS.toSeconds(timeElapsed) % 60);
                timer.setText(hms);

            }

            @Override
            public void onFinish() {

            }
        }.start();
        isTimerRunning = true;
    }
}
