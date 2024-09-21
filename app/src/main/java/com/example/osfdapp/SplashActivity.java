package com.example.osfdapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SplashActivity extends AppCompatActivity {

    ImageView ivLogo;
    TextView tvTitle,tvSlogan;
    Animation animation;
    Handler handler;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ivLogo = findViewById(R.id.ivSplashLogo);
        tvTitle = findViewById(R.id.tvSplashTitle);
        tvSlogan = findViewById(R.id.tvSplashSlogan);

        animation = AnimationUtils.loadAnimation(SplashActivity.this,R.anim.toptobottomtranslate);
        ivLogo.startAnimation(animation);

        animation = AnimationUtils.loadAnimation(SplashActivity.this,R.anim.anim1);
        tvTitle.startAnimation(animation);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(intent);

            }
        },4000);

    }
}