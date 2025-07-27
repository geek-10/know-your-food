package com.example.knowyourfood;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.ShapeAppearanceModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.AsyncListUtil;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

public class recipe_card_fragement extends Fragment  {

//    private FragBottomDrawerViewModel mViewModel;
    private Activity parentActivity;

    TextView recipeTitle;
    TextView recipeDescription;
    TextView recipeDuration;

    TextView recipeRating;
    TextView recipeFlag;

    static String title;
    static String description;
    static String duration;
    static String rating;
    static boolean flag;
    static String imageUrl1;



    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }




    public static recipe_card_fragement newInstance(ArrayList<HashMap<String,Object>> recipeData) {
        //initialize parameter received from caller
        recipe_card_fragement fragment = new recipe_card_fragement();
        Bundle args = new Bundle();
        Random random = new Random();
        int randomIndex = random.nextInt(recipeData.size());
        JSONObject jsonObject = new JSONObject(recipeData.get(randomIndex));
        args.putString("CardLayout", jsonObject.toString());
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //Inflate the layout
        View view = inflater.inflate(R.layout.recipe_card, container, false);

        HashMap<String, Object> recipeData = new HashMap<>();
        ConstraintLayout recipeLayoutMain = view.findViewById(R.id.recipe_data_main);
        ImageView savedButton = view.findViewById(R.id.saved_button_recipe_card);


        //get the data stored in bundled argument sent by parent activity
        Bundle args = getArguments();
        if (args != null && args.containsKey("CardLayout")) {
            String jsonRecipeData = args.getString("CardLayout");
            try {
                JSONObject jsonObject = new JSONObject(jsonRecipeData);
                Iterator<String> keys = jsonObject.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    Object value = jsonObject.get(key);
                    // putting values from json to hashmap
                    recipeData.put(key, value);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        //EVENT Listners
        recipeLayoutMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), recipe_screen.class);
                intent.putExtra("dataHashMap", recipeData);
                startActivity(intent);
            }
        });

        savedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (savedButton.getDrawable().getConstantState() == getResources().getDrawable( R.mipmap.bookmark_outline).getConstantState())
                {
                    savedButton.setImageResource(R.mipmap.bookmark_filled);
                    // new RegisterAsyntaskNew().execute();
                }
                else
                {
                    savedButton.setImageResource(R.mipmap.bookmark_outline);
                    // new RegisterAsyntask().execute();
                }

            }
        });



        //initializing componetns
        recipeTitle = view.findViewById(R.id.recipeTitle);
        recipeDescription = view.findViewById(R.id.recipeDescription);
        recipeDuration = view.findViewById(R.id.recipe_duration);
        recipeRating = view.findViewById(R.id.recipe_rating);
        recipeFlag = view.findViewById(R.id.recipe_flag);


        //decode values
        title = String.valueOf(recipeData.get("recipeTitle"));
        description = String.valueOf(recipeData.get("recipeDescription"));
        duration = String.valueOf(recipeData.get("recipeDuration"));
        rating = String.valueOf(recipeData.get("recipeRating"));
        flag = (boolean) recipeData.get("recipeFlag");
        imageUrl1 = String.valueOf(recipeData.get("imageUrl"));



        //set their values
        recipeTitle.setText(title);
        recipeTitle.setBackground(null);
        recipeDescription.setText(description);
        recipeDuration.setText(duration);

        if(Float.valueOf(rating) >=5){
            recipeRating.setText(rating);
            recipeRating.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.roundborder_rating5));
        }else if (Float.valueOf(rating) >= 4) {
            recipeRating.setText(String.valueOf(rating));
            recipeRating.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.roundborder_rating4));
        } else if (Float.valueOf(rating) >= 3) {
            recipeRating.setText(String.valueOf(rating));
            recipeRating.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.roundborder_rating3));
        } else if (Float.valueOf(rating) >= 2) {
            recipeRating.setText(String.valueOf(rating));
            recipeRating.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.roundborder_rating2));
        } else if (Float.valueOf(rating) >= 1) {
            recipeRating.setText(String.valueOf(rating));
            recipeRating.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.roundborder_rating1));
        } else {
            // Handle other cases (if any)
        }


        if(flag == true){
            recipeFlag.setText("Veg");
            recipeFlag.setTextColor(ContextCompat.getColor(getContext(), R.color.veg_color));
        }
        else{
            recipeFlag.setText("Non-Veg");
            recipeFlag.setTextColor(ContextCompat.getColor(getContext(), R.color.non_veg_color));
        }

        ShapeableImageView recipeThumbnail = view.findViewById(R.id.recipeThumbnail);
        ShapeAppearanceModel shapeAppearanceModel = new ShapeAppearanceModel()
                .toBuilder()
                .setAllCorners(CornerFamily.ROUNDED, 20)
                .build();

        // Set the ShapeAppearanceModel to the ShapeableImageView
        recipeThumbnail.setShapeAppearanceModel(shapeAppearanceModel);

        ImageLoader loadimage = new ImageLoader(recipeThumbnail);
        loadimage.execute(imageUrl1);

        return  view;
    }
}