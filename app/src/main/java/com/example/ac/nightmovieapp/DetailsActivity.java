package com.example.ac.nightmovieapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);

        if (null == findViewById(R.id.flMain)) {
            Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
            setSupportActionBar(toolbar);
        }
        //Receive the sent Bundle
        Intent sentIntent = getIntent();
        Bundle sentBundle = sentIntent.getExtras();
        //Inflate Details Fragment & Send the Bundle to it
        DetailsActivityFragment mDetailsFragment = new DetailsActivityFragment();
        mDetailsFragment.setArguments(sentBundle);
        getSupportFragmentManager().beginTransaction().add(R.id.flDetails, mDetailsFragment, "").commit();
    }
}
