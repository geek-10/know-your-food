package com.example.knowyourfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class account_page extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    TextView user_name;
    TextView user_email;
    String username;
    String useremail;
    TextView userInitial;
    Button trigger_logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_page);


        firebaseAuth = FirebaseAuth.getInstance();
        String userUid = firebaseAuth.getCurrentUser().getUid();
        Log.d("UserUid", userUid);
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("users").child(userUid);

        user_name = (TextView) findViewById(R.id.write_username);
        user_email = (TextView) findViewById(R.id.write_useremail);
        trigger_logout = (Button) findViewById(R.id.account_page_trigger_logout);
        userInitial = (TextView) findViewById(R.id.userIcon);

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.d("Child Count", String.valueOf(snapshot.getChildrenCount()));
                    username = snapshot.child("Name").getValue().toString();
                    user_name.setText(username);
                    userInitial.setText(username.toUpperCase().charAt(0));
                    Log.d("Username", username);
                    useremail = snapshot.child("Email").getValue().toString();
                    user_email.setText(useremail);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        HashMap<String, Object> recipe = new HashMap<>();
        recipe.put("recipeTitle", "Creamy Mango Chia Pudding ");
        recipe.put("recipeDescription", "A delightful and refreshing sweet treat");
        recipe.put("recipeDuration", "10m");
        recipe.put("recipeRating", "2.0");
        recipe.put("recipeFlag", true);
        recipe.put("recipeCalories", "298kcal");


        recipe.put("recipeAllergen", "{}");

        recipe.put("recipeIngredients", "{'1':'Coconut Milk', '2':'Ripe Mangoes', '3':'Honey', '4':'Chia Seeds', '5':'Cinnamon', '6':'Flaked Almond'}");
        recipe.put("imageUrl", "https://www.kitchenathoskins.com/wp-content/uploads/2022/06/mango-chia-pudding-3.jpg");


        for(int i=0;i<4;i++){
            FirebaseRecipeFetcher loaddata = new FirebaseRecipeFetcher();

            loaddata.getRandomRecipe(new OnDataReceiveCallback() {
                @Override
                public void onDataReceived(ArrayList<HashMap<String, Object>> data) {
                    if (data != null && !data.isEmpty()) {

                        recipe_card_fragement recipeCardFragment = recipe_card_fragement.newInstance(data);
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.add(R.id.saved_viewer_container, recipeCardFragment, "RECIPE_CARD_TAG");
                        transaction.commit();
                    }
                }
            });
        }
        trigger_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(account_page.this, LoadingScreen.class);
                intent.putExtra("info","to_login_screen");
                startActivity(intent);
                finish();
            }
        });

    }
}