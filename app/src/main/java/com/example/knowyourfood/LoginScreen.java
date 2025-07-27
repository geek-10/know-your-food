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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class LoginScreen extends AppCompatActivity {

    EditText email;
    EditText password;
    Button signin_button;
    Button google_login_button;
    TextView trigger_forgot_password;
    TextView trigger_signup;

    TextInputLayout email_field;
    TextInputLayout password_field;

    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient googleSignInClient;

    private FirebaseAuth firebaseAuth;
    String user_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(),false);
        setContentView(R.layout.activity_login_screen);

        // Authentication instance for firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();

        // Database reference for firebase database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        // Database reference for SQLite database
        SQLDBHelper DBHelper = new SQLDBHelper(this);

        email = (EditText) findViewById(R.id.login_screen_input_for_email);
        password = (EditText) findViewById(R.id.login_screen_input_for_password);
        signin_button = (Button) findViewById(R.id.login_screen_trigger_signin);
        google_login_button = (Button) findViewById(R.id.login_screen_trigger_google);
        trigger_forgot_password = (TextView) findViewById(R.id.login_screen_trigger_forgot_password);
        trigger_signup = (TextView) findViewById(R.id.login_screen_trigger_signup);
        email_field = (TextInputLayout) findViewById(R.id.login_screen_email_field);
        password_field = (TextInputLayout) findViewById(R.id.login_screen_password_field);

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ^([a-z0-9\\.]+)@([a-z]+)\\.([a-z\\.]{2,5})$")
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

        signin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_email = email.getText().toString();
                String user_password = password.getText().toString();


                if(!user_email.isEmpty() && !user_password.isEmpty()) {
                    if(user_password.length() < 8) {
                        password_field.setError("Password should be atleast 8 characters long");
                    }
                    else{
                        password_field.setError(null);
                        // validate login from firebase Authentication
                        firebaseAuth.signInWithEmailAndPassword(user_email, user_password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                // login success
                                String userUid = firebaseAuth.getUid();
                                Log.d("User UID", userUid);

                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(userUid);

                                // updating the password everytime if user has ever resetted the password in firebase realtime database
                                ref.child("Password").setValue(user_password);
                                ref.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for(DataSnapshot data : snapshot.getChildren()) {
                                            user_name = snapshot.child("Name").getValue().toString();
                                            Log.d("Value of User name", user_name);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                                if (!DBHelper.checkUser(userUid)) {
                                    DBHelper.addUser(user_name,user_email,user_password,userUid);
                                } else {
                                    // if the user already exists in the SQLite Database update the password
                                    UserContentModel userContentModel = new UserContentModel();
                                    userContentModel.userUid = userUid;
                                    userContentModel.password = user_password;

                                    DBHelper.updatePassword(userContentModel);
                                }

//                                if(user_email != null) {
//                                    Toast.makeText(LoginScreen.this, "Logged in as: " + user_name,Toast.LENGTH_SHORT).show();
//                                }

                                Intent intent = new Intent(LoginScreen.this, LoadingScreen.class);
                                intent.putExtra("info", "to_success_screen");
                                startActivity(intent);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginScreen.this, "Invalid email or password",Toast.LENGTH_SHORT).show();
                                email_field.setFocusable(true);
                            }
                        });
                    }
                } else {
                    Toast.makeText(LoginScreen.this, "Please input all the fields",Toast.LENGTH_SHORT).show();
                }
            }
        });

        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions);
        GoogleSignInAccount accountInfo = GoogleSignIn.getLastSignedInAccount(this);

        if(accountInfo != null) {

                Intent intent = new Intent(LoginScreen.this, LoadingScreen.class);
                intent.putExtra("info", "to_main_activity");
                startActivity(intent);
                finish();
        }
        google_login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signin();
            }
        });

        trigger_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginScreen.this, LoadingScreen.class);
                intent.putExtra("info", "to_signup_screen");
                startActivity(intent);
                finish();
            }
        });

        trigger_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginScreen.this, LoadingScreen.class);
                intent.putExtra("info", "to_forgot_password_screen");
                startActivity(intent);
                finish();
            }
        });
    }

    public void signin() {
        Intent signInActivityIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInActivityIntent, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try{
                task.getResult(ApiException.class);

                GoogleSignInAccount accountInfo = GoogleSignIn.getLastSignedInAccount(this);
                String accountusername = accountInfo.getDisplayName();
                String accountuseremail = accountInfo.getEmail();
                String accountuserpassword = "google_login";
                SQLDBHelper DBHelper = new SQLDBHelper(this);

                firebaseAuth.createUserWithEmailAndPassword(accountuseremail,accountuserpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                    @Override

                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // checking if email address was not registered in firebase Authentication
                        if(task.isSuccessful()) {
                            String userUid = firebaseAuth.getUid();

                            // storing data in firebase realtime database
                            HashMap<String, Object> user = new HashMap<>();
                            user.put("Name", accountusername);
                            user.put("Email", accountuseremail);
                            user.put("Password", accountuserpassword);

                            databaseReference.child("users").child(userUid).setValue(user);

                            // storing data in SQLite Database
                            if(accountusername != null) {
                                Toast.makeText(LoginScreen.this,"Logged in as: " + accountusername,Toast.LENGTH_SHORT).show();
                            }
                            DBHelper.addUser(accountusername,accountuseremail,accountuserpassword,userUid);


                            Intent intent = new Intent(LoginScreen.this, LoadingScreen.class);
                            intent.putExtra("info", "to_success_screen");
                            startActivity(intent);
                            finish();

                        } else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            // email already exists in firebase authentication
                            String userUid = firebaseAuth.getUid();

                            if (!DBHelper.checkUser(userUid)) {
                                // user details not present inside SQLite Database
                                DBHelper.addUser(accountusername, accountuseremail, accountuserpassword, userUid);
                            }


                            Toast.makeText(LoginScreen.this,"Logged in as: " + accountusername,Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginScreen.this, LoadingScreen.class);
                            intent.putExtra("info", "to_success_screen");
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(LoginScreen.this,"Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } catch (ApiException e) {
                Toast.makeText(LoginScreen.this,"Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }

}