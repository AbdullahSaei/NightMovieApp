package com.example.ac.nightmovieapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmChangeListener;

import static com.example.ac.nightmovieapp.MainActivityFragment.pop_or_top;

public class MainActivity extends AppCompatActivity implements MovieListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    static boolean is2pane = false;
    MainActivityFragment mainFrag = new MainActivityFragment();
    menuFragment dialog = new menuFragment();

    public static RealmChangeListener realmListener;
    Realm realm = Realm.getDefaultInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ///inflate the MainFragment
        //Set The Activity to be a listener to the Fragment
        mainFrag.setMovieListener(this);
        getSupportFragmentManager().beginTransaction().add(R.id.flMain, mainFrag, "").commit();
        //Check if two pane
        if (null != findViewById(R.id.flDetails)) {
            is2pane = true;
        }

        SharedPreferences sharedPreferences = getSharedPreferences("pop_or_top", Activity.MODE_PRIVATE);
        pop_or_top = sharedPreferences.getString("top_pop", "first");
        if (Objects.equals(pop_or_top, "first")) {
            pop_or_top = "/popular";
            dialog.show(getFragmentManager(), "sort");
            Toast.makeText(this, "You can change your choice easily from the menu", Toast.LENGTH_LONG).show();
        }
        realmListener = new RealmChangeListener() {
            @Override
            public void onChange(Object element) {
                Log.d(TAG, "onChange: CHANGG");
                if(Objects.equals(pop_or_top, "Favorite")) {
                    mainFrag.update();
                }
            }
        };
        realm.addChangeListener(realmListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove the listener.
        realm.removeChangeListener(realmListener);
        // Close the Realm instance.
        realm.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainFrag.update();
    }

    public void onRadioButtonClicked(View view) {
        SharedPreferences sharedPref = getSharedPreferences("pop_or_top", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        switch (view.getId()) {
            case R.id.rBtn_pop:
                MainActivityFragment.setPop_or_top("/popular");
                break;
            case R.id.rBtn_top:
                MainActivityFragment.setPop_or_top("/top_rated");
                break;
            case R.id.rBtn_myfav:
                MainActivityFragment.setPop_or_top("Favorite");
                break;
        }
        editor.putString("top_pop", MainActivityFragment.getPop_or_top());
        editor.apply();
        if (is2pane) {
            ((FrameLayout) findViewById(R.id.flDetails)).setVisibility(View.INVISIBLE);
        }
        mainFrag.update();
        dialog.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.sort_by) {
            dialog.show(getFragmentManager(), "sort");
        } else if (id == R.id.clear_db) {
            new AlertDialog.Builder(this)
                    .setTitle("Delete All Favorites")
                    .setMessage("Are you sure you want to delete all your favorite movies?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            realm.beginTransaction();
                            realm.deleteAll();
                            realm.commitTransaction();
                            if(is2pane)
                            ((FrameLayout) findViewById(R.id.flDetails)).setVisibility(View.INVISIBLE);
                            mainFrag.update();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(), "No Action", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setSelectedMovie(Movie chosen) {
        if (!is2pane) {
            Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
            intent.putExtra("film", chosen);
            startActivity(intent);
        } else {
            //  Case Two-PAne
            ((FrameLayout) findViewById(R.id.flDetails)).setVisibility(View.VISIBLE);
            DetailsActivityFragment mDetailsFragment = new DetailsActivityFragment();
            Bundle extras = new Bundle();
            extras.putParcelable("film", chosen);
            mDetailsFragment.setArguments(extras);
            getSupportFragmentManager().beginTransaction().replace(R.id.flDetails, mDetailsFragment, "").commit();
        }
    }

}