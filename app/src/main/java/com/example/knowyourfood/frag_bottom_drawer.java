package com.example.knowyourfood;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class frag_bottom_drawer extends Fragment {

    private FragBottomDrawerViewModel mViewModel;
    private Activity parentActivity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            parentActivity = (Activity) context;
        }
    }

    public static frag_bottom_drawer newInstance() {
        return new frag_bottom_drawer();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
//         Inflate the layout
        View view = inflater.inflate(R.layout.fragment_frag_bottom_drawer, container, false);

        ImageView homepage = view.findViewById(R.id.home);
        ImageView search = view.findViewById(R.id.search);

        ImageView account = view.findViewById(R.id.account);

        if (parentActivity instanceof MainActivity) {
            homepage.setImageResource(R.mipmap.home_filled); // Use active icon for home
        } else if (parentActivity instanceof search_activity) {
            search.setImageResource(R.mipmap.search_filled); // Use active search for home

        }
        else if(parentActivity instanceof account_page){
            account.setImageResource(R.mipmap.account_filled); // Use active search for home
        }

        else {
            // Use default icon for other activities
        }

        homepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to home activity
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);

            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to search activity
                Intent intent = new Intent(getActivity(), search_activity.class);
                startActivity(intent);
            }
        });

        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to search activity
                Intent intent = new Intent(getActivity(), account_page.class);
                startActivity(intent);
            }
        });

        // Return the configured view, not a new instance of the layout
        return view;
    }




}