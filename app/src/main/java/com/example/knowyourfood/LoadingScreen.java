package com.example.knowyourfood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;


public class LoadingScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);
        WindowCompat.setDecorFitsSystemWindows(getWindow(),false);


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                    Intent intent = getIntent();
                    String decisionString = intent.getStringExtra("info");
                    if(decisionString.equals("to_login_screen")) {
                        startActivity(new Intent(LoadingScreen.this, LoginScreen.class));
                        finish();
                    } else if(decisionString.equals("to_welcome_screen")) {
                        startActivity(new Intent(LoadingScreen.this, WelcomeScreen.class));
                        finish();
                    } else if (decisionString.equals("to_greet_screen")) {
                        startActivity(new Intent(LoadingScreen.this, GreetScreen.class));
                        finish();
                    } else if (decisionString.equals("to_main_activity")) {
                        startActivity(new Intent(LoadingScreen.this, MainActivity.class));
                        finish();
                    } else if (decisionString.equals("to_signup_screen")) {
                        startActivity(new Intent(LoadingScreen.this, SignUpScreen.class));
                        finish();
                    } else if (decisionString.equals("to_success_screen")) {
                        startActivity(new Intent(LoadingScreen.this, SuccessScreen.class));
                        finish();
                    } else if (decisionString.equals("to_forgot_password_screen")) {
                        startActivity(new Intent(LoadingScreen.this, ForgotPasswordScreen.class));
                        finish();
                    }
//                choosedIntent(intentChoosed);
            }
        },1000);

    }




}