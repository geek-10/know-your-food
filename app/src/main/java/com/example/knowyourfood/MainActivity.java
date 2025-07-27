package com.example.knowyourfood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {
    String imageUrl = "https://insanelygoodrecipes.com/wp-content/uploads/2021/03/White-Chocolate-Oreo-Cookie-Balls-683x1024.webp";
    HashMap<String, Object> recipe = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        WindowCompat.setDecorFitsSystemWindows(getWindow(),false);


        ScrollView reccomendations_scrollview = findViewById(R.id.reccomendations_scrolview);
        LinearLayout reccomendation = findViewById(R.id.reccomendations);


        // fill 4 cards by default so that user can scroll
        for(int i=0;i<4;i++){


            FirebaseRecipeFetcher loaddata = new FirebaseRecipeFetcher();

            loaddata.getRandomRecipe(new OnDataReceiveCallback() {
                @Override
                public void onDataReceived(ArrayList<HashMap<String, Object>> data) {
                    if (data != null && !data.isEmpty()) {

                        recipe_card_fragement recipeCardFragment = recipe_card_fragement.newInstance(data);
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.add(R.id.reccomendations, recipeCardFragment, "RECIPE_CARD_TAG");
                        transaction.commit();
                    }
                }
            });



        }

        final int threshold = 10; // Threshold value in pixels

        reccomendations_scrollview.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                // Get the height of the scroll view's content
                int contentViewHeight = reccomendations_scrollview.getChildAt(0).getHeight();

                // Calculate the difference between the content height and the scroll view's height
                int difference = contentViewHeight - reccomendations_scrollview.getHeight();

                // Check if the scroll view is almost at the bottom within the threshold
                if (reccomendations_scrollview.getScrollY() >= difference - threshold) {
                    // At the bottom, add more recipe cards dynamically
                    FirebaseRecipeFetcher loaddata = new FirebaseRecipeFetcher();

                    loaddata.getRandomRecipe(new OnDataReceiveCallback() {
                        @Override
                        public void onDataReceived(ArrayList<HashMap<String, Object>> data) {
                            if (data != null && !data.isEmpty()) {

                                recipe_card_fragement recipeCardFragment = recipe_card_fragement.newInstance(data);
                                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                transaction.add(R.id.reccomendations, recipeCardFragment, "RECIPE_CARD_TAG");
                                transaction.commit();
                            }
                        }
                    });

                }
            }
        });


    }
}