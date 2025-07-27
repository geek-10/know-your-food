package com.example.knowyourfood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class search_activity extends AppCompatActivity {

    public int search_result = 0;
    public int shown_results = 0;
    ScrollView search_scrollview;
    LinearLayout search_results;
    HashMap<String, Object> recipe = new HashMap<>();
    final int threshold = 10; // Threshold value in pixels
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        recipe.put("recipeTitle", "White Chocolate Cookie Balls");
        recipe.put("recipeDescription", "Sweet White Chocolate Cookie Balls");
        recipe.put("recipeDuration", "30m");
        recipe.put("recipeRating", "4.0");
        recipe.put("recipeFlag", true);
        recipe.put("imageUrl", "https://insanelygoodrecipes.com/wp-content/uploads/2021/03/White-Chocolate-Oreo-Cookie-Balls-683x1024.webp");

        search_scrollview = findViewById(R.id.search_scrollview);
        search_results = findViewById(R.id.search_results);
        Button manual_override = findViewById(R.id.search_manualOverride);

        manual_override.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_result+=1;
                updateSearchResults();
            }
        });

    }

    protected void updateSearchResults(){

        if(shown_results<=search_result && shown_results<=4){
//            recipe_card_fragement recipeCardFragment = recipe_card_fragement.newInstance(recipe);
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            transaction.add(R.id.search_results, recipeCardFragment, "RECIPE_CARD_TAG"); // Replace 'R.id.fragment_container' with your container ID
////            transaction.addToBackStack(null); // Optional: Add this fragment to the back stack if needed
//            transaction.commit();
            shown_results+=1;

        } else if (shown_results<=search_result) {
            search_scrollview.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                @Override
                public void onScrollChanged() {
                    // Get the height of the scroll view's content
                    int contentViewHeight = search_scrollview.getChildAt(0).getHeight();
                    // Calculate the difference between the content height and the scroll view's height
                    int difference = contentViewHeight - search_scrollview.getHeight();

                    // Check if the scroll view is almost at the bottom within the threshold
                    if (search_scrollview.getScrollY() >= difference - threshold) {
                        // At the bottom, add more recipe cards dynamically




                        FirebaseRecipeFetcher loaddata = new FirebaseRecipeFetcher();

                        loaddata.getRandomRecipe(new OnDataReceiveCallback() {
                            @Override
                            public void onDataReceived(ArrayList<HashMap<String, Object>> data) {
                                if (data != null && !data.isEmpty()) {

                                    recipe_card_fragement recipeCardFragment = recipe_card_fragement.newInstance(data);
                                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                    transaction.add(R.id.search_results, recipeCardFragment, "RECIPE_CARD_TAG");
                                    transaction.commit();
                                }
                            }
                        });





                    }
                }
            });
        } else{
            Toast.makeText(search_activity.this, "This is my Toast message!",Toast.LENGTH_LONG).show();
        }

    }
}