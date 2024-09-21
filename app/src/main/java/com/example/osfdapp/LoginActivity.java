package com.example.osfdapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.osfdapp.Comman.NetworkChangeListener;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {

    TextView tvTitle,tvNewUser,tvForgetPassword;
    EditText etUsername,etPassword;
    CheckBox cbShowHide;
    AppCompatButton btnLogin;
    ProgressDialog progressDialog;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient googleSignInClient;
    AppCompatButton btnSignInWithGoogle;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        editor = preferences.edit();

        if(preferences.getBoolean("isLogin",false))
        {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
        }

        tvTitle = findViewById(R.id.tvLoginTitle);
        tvNewUser = findViewById(R.id.tvLoginNewUser);
        cbShowHide = findViewById(R.id.cbLoginShowHidePassword);
        etUsername = findViewById(R.id.etLoginUsername);
        etPassword = findViewById(R.id.etLoginPassword);
        btnLogin = findViewById(R.id.btnLoginLogin);
        btnSignInWithGoogle = findViewById(R.id.acbtnLoginSignInWithGoogle);
        tvForgetPassword = findViewById(R.id.tvLoginForgetPassword);

        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(LoginActivity.this,googleSignInOptions);

        btnSignInWithGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

    cbShowHide.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etUsername.getText().toString().isEmpty()) {
                    etUsername.setError("Please Enter Your Username");
                } else if (etUsername.getText().toString().length() < 8) {
                    etUsername.setError("Please Enter Username greater than 8");
                } else if (etPassword.getText().toString().isEmpty()) {
                    etPassword.setError("Please Enter Your Password");
                } else if (etPassword.getText().toString().length() < 8) {
                    etPassword.setError("Please Enter Password greater than 8");
                }else{
                    progressDialog = new ProgressDialog(LoginActivity.this);
                    progressDialog.setTitle("Please Wait");
                    progressDialog.setMessage("Login Under Process");
                    progressDialog.show();

                    userLogin();

                    Toast.makeText(LoginActivity.this, "Login Successfully Done", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);

                }
            }
        });

        tvForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,ConfirmRegisterMobileNoActivity.class);
                startActivity(intent);
            }
        });

        tvNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }

    private void signIn() {
        Intent intent = googleSignInClient.getSignInIntent();
        startActivityForResult(intent,999);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 999)
        {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                task.getResult(ApiException.class);
                Intent intent = new Intent(LoginActivity.this, MyProfileActivity.class);
                startActivity(intent);
                finish();
            } catch (ApiException e) {
                Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);


        registerReceiver(networkChangeListener,intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(networkChangeListener);
    }

    private void userLogin() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("username",etUsername.getText().toString());
        params.put("password",etPassword.getText().toString());

        client.post("http://192.168.25.38:80/ProjectAPI/userLogin.php",params,new JsonHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);

                        progressDialog.dismiss();

                        try {
                            String status = response.getString("success");
                            if (status.equals("1")){

                                Toast.makeText(LoginActivity.this,"Login Successfully Done",
                                        Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                                startActivity(intent);
                                finish();

                            }
                            else {

                                Toast.makeText(LoginActivity.this,"Invalid Username or Password",
                                        Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        progressDialog.dismiss();

                        Toast.makeText(LoginActivity.this,"Server Error",Toast.LENGTH_SHORT).show();
                    }
                }
        );

    }
}