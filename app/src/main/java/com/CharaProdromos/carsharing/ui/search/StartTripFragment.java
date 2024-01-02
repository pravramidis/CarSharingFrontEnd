package com.CharaProdromos.carsharing.ui.search;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.CharaProdromos.carsharing.R;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.CharaProdromos.carsharing.databinding.FragmentProfileBinding;
import com.CharaProdromos.carsharing.databinding.FragmentStartTripBinding;
import com.google.android.material.button.MaterialButton;

public class StartTripFragment extends Fragment {
    TextView code;
    TextView timer;

    private FragmentStartTripBinding binding;
    private CountDownTimer countDownTimer;
    private boolean isTimerRunning = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentStartTripBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


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
                }
                else {
                    if (isTimerRunning) {
                        countDownTimer.cancel();
                        isTimerRunning = false;
                    }
                    PaymentFragment payFragment = new PaymentFragment();
                    FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.container, payFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }

        });

        return root;
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
                // Timer finished logic (if applicable)
            }
        }.start();
        isTimerRunning = true;
    }
}
