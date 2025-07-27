package com.example.knowyourfood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.airbnb.lottie.LottieAnimationView;

public class GreetScreen extends AppCompatActivity {
    LottieAnimationView greet_screen_animation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greet_screen);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        Intent welcomeScreen = new Intent(GreetScreen.this, WelcomeScreen.class);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(welcomeScreen);
                finish();
            }
        },2000);

        greet_screen_animation = (LottieAnimationView) findViewById(R.id.greet_screen_animation);
        greet_screen_animation.setSpeed(2);
    }
}