package com.example.knowyourfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ForgotPasswordScreen extends AppCompatActivity {

    EditText email;
    Button send_button;
    TextInputLayout email_field;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_screen);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        email = (EditText) findViewById(R.id.forgot_password_screen_input_for_email);
        send_button = (Button) findViewById(R.id.forgot_password_screen_trigger_send);
        email_field = (TextInputLayout) findViewById(R.id.forgot_password_screen_email_field);

        // Authentication instance for firebase authentication
        firebaseAuth = FirebaseAuth.getInstance();

        // Validating email field
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ^([a-z0-9\.]+)@([a-z]+)\.([a-z\.]{2,5})$
                if(!s.toString().isEmpty() && !s.toString().matches("^([a-z0-9\\.]+)@([a-z]+)\\.([a-z\\.]{2,5})$")) {
                    email_field.setError("Please input correct Email");
                }
                else {
                    email_field.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_email = email.getText().toString();
                firebaseAuth.sendPasswordResetEmail(user_email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Intent intent = new Intent(ForgotPasswordScreen.this, ForgotPasswordSuccessScreen.class);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(ForgotPasswordScreen.this, "Please enter a registered email", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}