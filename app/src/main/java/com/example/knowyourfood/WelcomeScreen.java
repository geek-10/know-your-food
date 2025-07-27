package com.example.knowyourfood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class WelcomeScreen extends AppCompatActivity {

    // initializing the view-group variables
    TextView trigger_signup;
    Button login_triggered;

    LottieAnimationView chef_animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        // extending the activity to full screen
        WindowCompat.setDecorFitsSystemWindows(getWindow(),false);

        // binding the variables with the view-group
        trigger_signup = (TextView) findViewById(R.id.welcome_screen_trigger_signup);
        login_triggered = (Button) findViewById(R.id.welcome_screen_trigger_login);
        chef_animation = (LottieAnimationView) findViewById(R.id.welcome_screen_chef_animation);

        chef_animation.setSpeed(1);
        trigger_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeScreen.this,LoadingScreen.class);
                intent.putExtra("info", "to_signup_screen");
                startActivity(intent);
                finish();
            }
        });

        login_triggered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeScreen.this,LoadingScreen.class);
                intent.putExtra("info","to_login_screen");
                startActivity(intent);
                finish();
            }
        });

    }
}