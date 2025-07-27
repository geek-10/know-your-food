package com.example.knowyourfood;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class Ingredients_list_fragement extends Fragment {

    static private String ingredients;


    public Ingredients_list_fragement() {
        // Required empty public constructor
    }


    public static Ingredients_list_fragement newInstance(String ingredient) {
        Ingredients_list_fragement fragment = new Ingredients_list_fragement();
        Bundle args = new Bundle();
        args.putString("ingredient", ingredient);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ingredients_list, container, false);

        Bundle args = getArguments();
        if(args!=null){
            String newingredient = (String) args.get("ingredient");
            TextView name = view.findViewById(R.id.ingredient_name);
            name.setText("● "+newingredient);

        }
        return view;
    }
}