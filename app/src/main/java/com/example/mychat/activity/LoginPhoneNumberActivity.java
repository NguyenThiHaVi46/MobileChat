package com.example.mychat.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mychat.R;
import com.hbb20.CountryCodePicker;

public class LoginPhoneNumberActivity extends AppCompatActivity {

    CountryCodePicker countryCodePicker;
    EditText phoneNumber;
    Button sendOtpBtn;
    ProgressBar progressBar;
    ImageButton backBtn;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_phone_number);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        countryCodePicker = findViewById(R.id.login_countryCode);
        phoneNumber = findViewById(R.id.login_mobile_number);
        sendOtpBtn = findViewById(R.id.send_otp_btn);
        progressBar = findViewById(R.id.login_progress_bar);
        backBtn = findViewById(R.id.login_back_phone_number);

        progressBar.setVisibility(View.GONE);
        countryCodePicker.registerCarrierNumberEditText(phoneNumber);

        backBtn.setOnClickListener(new View.OnClickListener() {   // ham tro lai
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        sendOtpBtn.setOnClickListener((v) ->{  // check sdt , va gui sdt qua loginOTPActivity bang ma "phone"
            if(!countryCodePicker.isValidFullNumber()){
                phoneNumber.setError("Phone number not valid");
                return;
            }
            Intent intent = new Intent(LoginPhoneNumberActivity.this,LoginOtpActivity.class);
            intent.putExtra("phone",countryCodePicker.getFullNumberWithPlus());
            startActivity(intent);

        });
    }
}