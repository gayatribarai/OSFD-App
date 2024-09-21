package com.example.osfdapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SetUpNewPasswordActivity extends AppCompatActivity {
    EditText etNewPassword,etConfirmPassword;
    AppCompatButton btnSetUpPassword;
    String strMobileno;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_new_password);
        etNewPassword = findViewById(R.id.etSetUpNewPasswordNewPassword);
        etConfirmPassword=findViewById(R.id.etSetUpNewPasswordConfirmPassword);
        btnSetUpPassword = findViewById(R.id.btnConfirmRegisterMobileNoSetUpNewPassword);
        strMobileno = getIntent().getStringExtra("mobileno");

        btnSetUpPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });

    }
}