package com.example.knowyourfood;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListUtil;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class FirebaseRecipeFetcher  {

    final HashMap<String,Object>[] temp = new HashMap[1];
    private TextView textview;


        ArrayList<HashMap<String, Object>> listofrecipes = new ArrayList<>();
        String saved_recipes_list = "";
    protected HashMap<String,Object> getRandomRecipe(OnDataReceiveCallback callback){

        DatabaseReference db = FirebaseDatabase.getInstance().getReference("RecipeList");


        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot recipeSnapshot : dataSnapshot.getChildren()) {
                        HashMap<String, Object> recipeMap = (HashMap<String, Object>) recipeSnapshot.getValue();
                        if (recipeSnapshot.getValue() != null) {
                            // Add the recipe to your ArrayList
                            listofrecipes.add(recipeMap);
                        }
                    }


                    // send callback to datarecievedinterface after data has been fetched

                    // Now, listofrecipes ArrayList contains all the Recipe objects from Firebase Database
                    callback.onDataReceived(listofrecipes);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Someerror","firebase error");
            }
        });


        return temp[0];
    }

    protected HashMap<String,Object> getSavedData(OnDataReceiveCallback callback){
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("savedRecipe");

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot recipeSnapshot : snapshot.getChildren()) {
                        String savedMap = (String) recipeSnapshot.getValue();
                        if (recipeSnapshot.getValue() != null) {
                            // Add the recipe to your ArrayList
                            saved_recipes_list.concat(savedMap);
                        }
                    }

//                    callback.onSavedReceived(saved_recipes_list);;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("VeryBIgError","raaaaaaaaaaaaahhhhhhhhhhh");
            }
        });

        return temp[0];
    }

}
