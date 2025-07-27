package com.example.knowyourfood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

public class recipe_screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_screen);

        TextView recipeTitle = findViewById(R.id.recipe_title);
        TextView recipeDuration = findViewById(R.id.recipe_duration_text);
        TextView recipeRating = findViewById(R.id.recipe_rating_text);
        TextView recipeCalories = findViewById(R.id.recipe_calories_text);
        TextView allergenNote = findViewById(R.id.allergens_note);
        Button start_cooking_button = findViewById(R.id.start_cooking);

        ImageView recipeImage = findViewById(R.id.recipe_screen_image);
        String ingredientList = "";
        String allergenList = "";
        ArrayList<String>  final_ingredientList = new ArrayList<>();
        ArrayList<String>  finalallergenList = new ArrayList<>();
        String cookingSteps = "";


        //Onclick Listners

        LinearLayout backButton = findViewById(R.id.recipe_screen_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });





        Intent intent = getIntent();
        if (intent != null) {
            HashMap<String, Object> receivedHashMap = (HashMap<String, Object>) intent.getSerializableExtra("dataHashMap");

            if (receivedHashMap != null) {
                recipeTitle.setText((String)receivedHashMap.get("recipeTitle"));
                recipeDuration.setText((String)receivedHashMap.get("recipeDuration"));
                recipeRating.setText((String)receivedHashMap.get("recipeRating"));
                recipeCalories.setText((String)receivedHashMap.get("recipeCalories"));
                String imageUrl1 = (String) receivedHashMap.get("imageUrl");
                cookingSteps = (String) receivedHashMap.get("recipeSteps");
                Log.d("dsfhjsfhksgh", cookingSteps);

                if((boolean)receivedHashMap.get("recipeFlag") == true){
                    //do something
                }
                else{
                    //set the recipe to non veg
                }

                ingredientList = (String) receivedHashMap.get("recipeIngredients");
                try {

                    JSONObject jsonObject = new JSONObject(ingredientList);
                    // Iterate over the keys and add values to the ArrayList
                    Iterator<String> keys = jsonObject.keys();
                    while (keys.hasNext()) {
                        String key = keys.next();
                        Object value = jsonObject.get(key);
                        final_ingredientList.add((String) value);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                for(int i=0;i<final_ingredientList.size();i++){
                    Ingredients_list_fragement list_of_ingredients = Ingredients_list_fragement.newInstance(final_ingredientList.get(i));
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.add(R.id.ingredients_frame, list_of_ingredients); // Replace 'R.id.fragment_container' with your container ID
                    transaction.commit();
                }


                allergenList = (String) receivedHashMap.get("recipeAllergen");

                try {

                    JSONObject jsonObject = new JSONObject(allergenList);
                    // Iterate over the keys and add values to the ArrayList
                    Iterator<String> keys = jsonObject.keys();
                    while (keys.hasNext()) {
                        String key = keys.next();
                        Object value = jsonObject.get(key);
                        finalallergenList.add((String) value);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(finalallergenList.size() <=1){
                    allergenNote.setText(allergenNote.getText()+"No Allergens");
                }
                else{
                    allergenNote.setText(allergenNote.getText()+"Contains ");
                    for(String i : finalallergenList){
                        allergenNote.setText(allergenNote.getText() + ", " + i);
                    }
                }
                ImageLoader loadimage = new ImageLoader(recipeImage);
                loadimage.execute(imageUrl1);

                String finalCookingSteps = cookingSteps;


                start_cooking_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(recipe_screen.this, CookingSteps.class);
                        intent.putExtra("steps", finalCookingSteps);
                        startActivity(intent);

                    }
                });

            }
        }


    }
}