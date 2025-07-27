package com.example.knowyourfood;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SignUpScreen extends AppCompatActivity {

    EditText username;
    EditText email;
    EditText  password;
    Button signup_button;
    Button google_login_button;

    TextInputLayout username_field;
    TextInputLayout email_field;
    TextInputLayout password_field;

    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient googleSignInClient;

    private FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(),false);
        setContentView(R.layout.activity_sign_up_screen);

        // Authentication instance for firebase authentication
        firebaseAuth = FirebaseAuth.getInstance();

        // Database Reference for firebase Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        // Database Helper Reference for SQLite Database
        SQLDBHelper DBHelper = new SQLDBHelper(this);

        username_field = (TextInputLayout) findViewById(R.id.username_field);
        email_field = (TextInputLayout) findViewById(R.id.email_field);
        password_field = (TextInputLayout) findViewById(R.id.password_field);
        username = (EditText) findViewById(R.id.input_for_username);
        email = (EditText) findViewById(R.id.input_for_email);
        password = (EditText) findViewById(R.id.input_for_password);
        signup_button = (Button) findViewById(R.id.signup_screen_trigger_signup);
        google_login_button = (Button) findViewById(R.id.signup_screen_trigger_google);

        // validating username field
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty() && !s.toString().matches("[a-zA-z ]+")) {
                    // if the string is all character
                    username_field.setError("Only Characters Allowed");
                } else {
                    // non empty string and non - numeric string is accepted
                    username_field.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
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


        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user_name = username.getText().toString();
                String user_email = email.getText().toString();
                String user_password = password.getText().toString();


                // validating all the fields
                if(!user_name.isEmpty() && !user_email.isEmpty() && !user_password.isEmpty()) {
                    if(user_password.length() < 8) {
                        password_field.setError("Password should be atleast 8 characters long");
                    }
                    else {
                        // adding email address and password to firebase authenticate
                        firebaseAuth.createUserWithEmailAndPassword(user_email,user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if(task.isSuccessful()) {
                                    // fetching uniques UID from firebase Authentication
                                    String userUid = firebaseAuth.getUid();

                                    // sending data to firebase realtime database
                                    HashMap<String, Object> user = new HashMap<>();
                                    user.put("Name", user_name);
                                    user.put("Email", user_email);
                                    user.put("Password", user_password);
                                    databaseReference.child("users").child(userUid).setValue(user);

                                    // sending data to SQLite Database
                                    DBHelper.addUser(user_name, user_email, user_password, userUid);

                                    // toast for registration success
                                    Toast.makeText(SignUpScreen.this, "Registration Successful", Toast.LENGTH_SHORT).show();


                                    Intent intent = new Intent(SignUpScreen.this, LoadingScreen.class);
                                    intent.putExtra("info", "to_success_screen");
                                    startActivity(intent);
                                    finish();
                                }
                                // validating whether the email address already exists
                                else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                    Toast.makeText(SignUpScreen.this,"User with this Email already exists.", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(SignUpScreen.this, "Error: " + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
                else {
                    Toast.makeText(SignUpScreen.this,"Please input all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions);
        GoogleSignInAccount accountInfo = GoogleSignIn.getLastSignedInAccount(this);

        if(accountInfo!=null) {
            Intent intent = new Intent(SignUpScreen.this, LoadingScreen.class);
            intent.putExtra("info", "to_main_activity");
            startActivity(intent);
            finish();
        }

        google_login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    public void signIn() {
        Intent signInActivityIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInActivityIntent, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                task.getResult(ApiException.class);

                GoogleSignInAccount accountInfo = GoogleSignIn.getLastSignedInAccount(this);
                String accountusername = accountInfo.getDisplayName();
                String accountuseremail = accountInfo.getEmail();
                String accountuserpassword = "google_login";
                SQLDBHelper sqldbHelper = new SQLDBHelper(this);

                firebaseAuth.createUserWithEmailAndPassword(accountuseremail, accountuserpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String userUid = firebaseAuth.getUid();

                            // storing data into SQLite Database

                            sqldbHelper.addUser(accountusername, accountuseremail, accountuserpassword, userUid);

                            // storing data into firebase Realtime Database
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                            HashMap<String, Object> user = new HashMap<>();
                            user.put("Name", accountusername);
                            user.put("Email", accountusername);
                            user.put("Password", accountuserpassword);

                            databaseReference.child("users").child(userUid).setValue(user);

                            Intent intent = new Intent(SignUpScreen.this, LoadingScreen.class);
                            intent.putExtra("info", "to_success_screen");
                            startActivity(intent);
                            finish();
                        }
                        else if (task.getException() instanceof FirebaseAuthUserCollisionException) {

                            Intent intent = new Intent(SignUpScreen.this, LoadingScreen.class);
                            intent.putExtra("info", "to_main_activity");
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(SignUpScreen.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } catch (ApiException e) {
                Toast.makeText(SignUpScreen.this,"Something went wrong", Toast.LENGTH_SHORT).show();
//                Log.v(String.valueOf(e), "error description");
//                Toast.makeText(getApplicationContext(),"Something went wrong while signing in",Toast.LENGTH_SHORT).show();
            }
        }
    }


}