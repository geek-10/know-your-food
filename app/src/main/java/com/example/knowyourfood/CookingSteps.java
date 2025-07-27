package com.example.knowyourfood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class CookingSteps extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cooking_steps);

        Intent intent = getIntent();
        String cookingsteps_string = intent.getStringExtra("steps");
        ArrayList<String> finalStepsList = new ArrayList<>();
//        cookingsteps_string = cookingsteps_string.replaceAll("\\{","[");
//        cookingsteps_string = cookingsteps_string.replaceAll("\\}","]");

        JSONObject js = new JSONObject();
        try {
            js.put("Hello","hello");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        Log.d("sjfsjgjghdf", String.valueOf(js));
        LinearLayout backButton = findViewById(R.id.steps_screen_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        try {

            JSONObject jsonObject = new JSONObject(cookingsteps_string);
            // Iterate over the keys and add values to the ArrayList
            Iterator<String> keys = jsonObject.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                Object value = jsonObject.get(key);
                finalStepsList.add(String.valueOf(value));
                Log.d("sdjjfsfjkghdkjfghkfjhgkfgh",(String.valueOf(value)));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
//        try {
//
//            JSONObject obj = new JSONObject(cookingsteps_string);
//            for(int i=0;i<obj.length();i++){
//                finalStepsList.add(obj.getString(String.valueOf(i)));
//            }
//            for(int i=0;i<finalStepsList.size();i++){
//                Log.d(String.valueOf(i),finalStepsList.get(i));
//            }
//
//
//        } catch (Throwable t) {
//            Log.e("My App", "Could not parse malformed JSON: \"" + cookingsteps_string + "\"");
//        }



    }

}