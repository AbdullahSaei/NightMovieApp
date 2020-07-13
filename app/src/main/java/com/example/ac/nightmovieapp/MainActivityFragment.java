package com.example.ac.nightmovieapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A placeholder fragment containing a simple view.
 */

public class MainActivityFragment extends Fragment {
    private static final String TAG = MainActivityFragment.class.getSimpleName();
    protected static String pop_or_top;
    Realm realm = Realm.getDefaultInstance();
    ImgAdapter mImgAdp;
    RealmResults<Movie> mrResults;
    View mView;
    private Context globalContext = null;

    private MovieListener mListener;

    void setMovieListener(MovieListener movieListener) {
        this.mListener = movieListener;
    }

    public static void setPop_or_top(String pop_or_top) {
        MainActivityFragment.pop_or_top = pop_or_top;

    }

    public static String getPop_or_top() {
        return pop_or_top;
    }


    public MainActivityFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_activity, container, false);
        mView = view;
        globalContext = container.getContext();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("pop_or_top", Activity.MODE_PRIVATE);
        pop_or_top = sharedPreferences.getString("top_pop", "/popular");
        return view;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void label() {
        if (mView == null) {
            return;
        } else {
            View v = mView;
            TextView headertv = (TextView) v.findViewById(R.id.tv_header);
            Log.d(TAG, "label: " + pop_or_top);
            if (Objects.equals(pop_or_top, "/popular")) {
                headertv.setText("Popular Movies");
                Log.d(TAG, "label: " + "pop");
            } else if (Objects.equals(pop_or_top, "/top_rated")) {
                headertv.setText("Top Rated Movies");
                Log.d(TAG, "label: " + "top");
            } else {
                headertv.setText("Favorited Movies");
                Log.d(TAG, "label: " + "Fav");
            }
        }
    }

    public void update() {
        Log.d(TAG, "update: Called");
        label();
        if (!Objects.equals(pop_or_top, "Favorite")) {
            if (globalContext == null) {
                Log.d(TAG, "update: FooK");
            }
            if (new myReciever().isConnected(getContext())) {
                Async_task datahere = new Async_task();
                Movie[] themovies = null;
                Log.d(TAG, "update: " + pop_or_top);
                try {
                    themovies = datahere.execute(pop_or_top).get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                mImgAdp = new ImgAdapter(getContext(), themovies);
                if (mView == null) {
                    Log.d(TAG, "update: FUCK");
                }
                TextView tv = (TextView) mView.findViewById(R.id.tv_hint);
                GridView gv_main = (GridView) mView.findViewById(R.id.gv_main);
                tv.setVisibility(View.GONE);
                gv_main.setVisibility(View.VISIBLE);
                gv_main.setAdapter(mImgAdp);

                gv_main.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Movie film = (Movie) parent.getItemAtPosition(position);
                        mListener.setSelectedMovie(film);
                    }
                });
            } else {
                Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
        } else {
            mrResults = realm.where(Movie.class).findAll();
            TextView tv = (TextView) mView.findViewById(R.id.tv_hint);
            GridView gv_fav = (GridView) mView.findViewById(R.id.gv_main);
            Movie[] Data = new Movie[mrResults.size()];
            if (mrResults.isEmpty()) {
                Toast.makeText(getContext(), "No Fav Movies", Toast.LENGTH_SHORT).show();
                tv.setVisibility(View.VISIBLE);
                gv_fav.setVisibility(View.GONE);
            } else {
                for (int i = 0; i < mrResults.size(); i++) {
                    Data[i] = mrResults.get(i);
                }
                tv.setVisibility(View.GONE);
                gv_fav.setVisibility(View.VISIBLE);
                mImgAdp = new ImgAdapter(getContext(), Data);
                gv_fav.setAdapter(mImgAdp);
            }

            gv_fav.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Movie film = (Movie) parent.getItemAtPosition(position);
                    mListener.setSelectedMovie(film);
                }
            });

        }
    }


}