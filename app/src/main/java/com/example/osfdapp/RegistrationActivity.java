package com.example.osfdapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;

public class RegistrationActivity extends AppCompatActivity {

    EditText etName,etEmail,etPhone,etUsername,etPassword,etAddress;
    CheckBox cbShowPassword;
    Button btnRegistration;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        preferences = PreferenceManager.getDefaultSharedPreferences(RegistrationActivity.this);
        editor = preferences.edit();

        etName = findViewById(R.id.etRegistrationName);
        etEmail = findViewById(R.id.etRegistrationEmail);
        etPhone = findViewById(R.id.etRegistrationPhone);
        etUsername = findViewById(R.id.etRegistrationUsername);
        etPassword = findViewById(R.id.etRegistrationPassword);
        cbShowPassword = findViewById(R.id.cbRegistrationShowPassword);
        btnRegistration = findViewById(R.id.btnRegistrationRegistration);
        etAddress = findViewById(R.id.etRegistrationAddress);


        cbShowPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked)
                {
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else {
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etName.getText().toString().isEmpty()) {
                    etName.setError("Please Enter Your Name");
                } else if (etEmail.getText().toString().isEmpty()) {
                    etEmail.setError("Please Enter Your Email");
                } else if (etPhone.getText().toString().isEmpty()) {
                    etPhone.setError("Please Enter Your Phone");
                } else if (etPhone.getText().toString().length() !=10 ) {
                    etPhone.setError("Please Enter 10 Numbers");
                } else if (etUsername.getText().toString().isEmpty()) {
                    etUsername.setError("Please Enter Your Username");
                }else if (etPassword.getText().toString().isEmpty()) {
                    etPassword.setError("Please Enter Your Password");
                } else if (etUsername.getText().toString().length()  < 8 ) {
                    etUsername.setError("Please Enter 8 Character Username");
                } else if (etPassword.getText().toString().length() < 8 ) {
                    etPassword.setError("Please Enter 8 Character Password");
                } else if (!etUsername.getText().toString().matches(".*[A-Z].*")) {
                    etUsername.setError("Please Used 1 Uppercase Letter");
                } else if (!etUsername.getText().toString().matches(".*[a-z].*")) {
                    etUsername.setError("Please Used 1 Lowercase Letter");
                } else if (!etUsername.getText().toString().matches(".*[0-9].*")) {
                    etUsername.setError("Please Used 1 number");
                } else if (!etUsername.getText().toString().matches(".*[@,#,$,&,!].*")) {
                    etUsername.setError("Please Used 1 Special Symbol");
                } else
                {
                    progressDialog = new ProgressDialog(RegistrationActivity.this);
                   progressDialog.setTitle("Please Wait...");
                   progressDialog.setMessage("Registration is in process");
                   progressDialog.setCanceledOnTouchOutside(true);
                   progressDialog.show();

                   PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + etPhone.getText().toString(),
                           60, TimeUnit.SECONDS, RegistrationActivity.this,
                           new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                               @Override
                               public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                   progressDialog.dismiss();
                                   Toast.makeText(RegistrationActivity.this,"Verification Completed",Toast.LENGTH_SHORT).show();
                               }

                               @Override
                               public void onVerificationFailed(@NonNull FirebaseException e) {
                                   progressDialog.dismiss();
                                   Toast.makeText(RegistrationActivity.this,"Verification Failed",Toast.LENGTH_SHORT).show();

                               }

                               @Override
                               public void onCodeSent(@NonNull String verificationCode, @NonNull
                               PhoneAuthProvider.ForceResendingToken forceResendingToken)
                               {
                                   super.onCodeSent(verificationCode, forceResendingToken);
                                   Intent intent = new Intent(RegistrationActivity.this,VerifyOTPActivity.class);
                                   intent.putExtra("verificationCode",verificationCode);
                                   intent.putExtra("name",etName.getText().toString());
                                   intent.putExtra("mobileno",etPhone.getText().toString());
                                   intent.putExtra("emailid",etEmail.getText().toString());
                                   intent.putExtra("username",etUsername.getText().toString());
                                   intent.putExtra("password",etPassword.getText().toString());
                                   intent.putExtra("address",etAddress.getText().toString());
                                   startActivity(intent);

                               }
                           }
                   );
                }
            }
        });
    }
    }


