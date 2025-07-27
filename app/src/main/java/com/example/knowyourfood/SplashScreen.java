package com.example.knowyourfood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Space;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SplashScreen extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        WindowCompat.setDecorFitsSystemWindows(getWindow(),false);

        // Database reference for SQLite Database
        SQLDBHelper sqldbHelper = new SQLDBHelper(this);

        // know whether user has previously logged in or not
        Intent loadingScreen = new Intent(SplashScreen.this, LoadingScreen.class);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                firebaseAuth = FirebaseAuth.getInstance();
                DatabaseReference db = FirebaseDatabase.getInstance().getReference();

                if (firebaseAuth.getCurrentUser() == null) {
                    // there exists a user logged in
                    if(sqldbHelper.checkEmpty()) {
                        // first login for the user
                        Intent intent = new Intent(SplashScreen.this, LoadingScreen.class);
                        intent.putExtra("info", "to_greet_screen");
                        startActivity(intent);
                        finish();
                    }
                    else {
                        // there exists user logins
                        Intent intent = new Intent(SplashScreen.this, LoadingScreen.class);
                        intent.putExtra("info", "to_login_screen");
                        startActivity(intent);
                        finish();
                    }
                }
                else {
                    Intent intent = new Intent(SplashScreen.this, LoadingScreen.class);
                    intent.putExtra("info", "to_main_activity");
                    startActivity(intent);
                    finish();
                }
            }
        },2000);

    }


}