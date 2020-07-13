package com.example.ac.nightmovieapp;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import java.util.Objects;

/**
 * Created by AC on 11/11/2016.
 */

public class menuFragment extends DialogFragment {
    public menuFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_fragment, container, false);
        RadioButton rbtntop = (RadioButton) view.findViewById(R.id.rBtn_top);
        RadioButton rbtnpop = (RadioButton) view.findViewById(R.id.rBtn_pop);
        RadioButton rbtnfav = (RadioButton) view.findViewById(R.id.rBtn_myfav);

        if (Objects.equals(MainActivityFragment.getPop_or_top(), "Favorite")) {
            rbtnfav.setChecked(true);
        } else if (Objects.equals(MainActivityFragment.getPop_or_top(), "/popular")) {
            rbtnpop.setChecked(true);
        } else if (Objects.equals(MainActivityFragment.getPop_or_top(), "/top_rated")){
            rbtntop.setChecked(true);
        }
        return view;
    }




}
