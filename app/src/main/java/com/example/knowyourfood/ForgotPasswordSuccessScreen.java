package com.example.knowyourfood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class ForgotPasswordSuccessScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_success_screen);
        WindowCompat.setDecorFitsSystemWindows(getWindow(),false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(ForgotPasswordSuccessScreen.this, LoginScreen.class);
                startActivity(intent);
            }
        },6000);
    }
}