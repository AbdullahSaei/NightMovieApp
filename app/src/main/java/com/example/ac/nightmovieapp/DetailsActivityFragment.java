package com.example.ac.nightmovieapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmQuery;

import static com.example.ac.nightmovieapp.MainActivity.is2pane;
import static com.example.ac.nightmovieapp.MainActivity.realmListener;
import static com.example.ac.nightmovieapp.MainActivityFragment.pop_or_top;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends Fragment {

    private static final String LOGTAG = DetailsActivityFragment.class.getSimpleName();
    Realm realm = Realm.getDefaultInstance();
    ScrollView Mixdata;
    ListView ListDetail;
    Button xBtn;
    RelativeLayout MoreDetails;

    public DetailsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_details, container, false);

        Mixdata = (ScrollView) v.findViewById(R.id.viewscroll);
        ListDetail = (ListView) v.findViewById(R.id.datalist);
        xBtn = (Button) v.findViewById(R.id.x_btn);
        MoreDetails = (RelativeLayout) v.findViewById(R.id.Moredetail);
        final ToggleButton btn_fav = (ToggleButton) v.findViewById(R.id.btn_fav);

        ImageView iv_fullscreen = (ImageView) v.findViewById(R.id.fullscreen_content);
        ImageView iv_detail = (ImageView) v.findViewById(R.id.iv_detail);
        TextView tv_title = (TextView) v.findViewById(R.id.tv_title);
        TextView tv_detail = (TextView) v.findViewById(R.id.tv_details);
        TextView tv_review = (TextView) v.findViewById(R.id.tv_review);
        TextView tv_date = (TextView) v.findViewById(R.id.tv_date);
        Button btn_trailer = (Button) v.findViewById(R.id.btn_trailer);
        Button btn_review = (Button) v.findViewById(R.id.btn_review);

        Bundle sentBundle = getArguments();
        //Receive the sent  String
        final Movie film = sentBundle.getParcelable("film");

        Log.v(LOGTAG, "film is" + film.getId() + film.getTitle());
        if (new myReciever().isConnected(getContext())) {
            getTrailers(film);
            getReviews(film);
        } else {
            Toast.makeText(getContext(), "Internet Connection is Needed", Toast.LENGTH_SHORT).show();
        }

        if (!CheckifExists(film.getId())) {
            btn_fav.setChecked(false);
            btn_fav.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.star_gray));
        } else {
            btn_fav.setChecked(true);
            btn_fav.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.star_yellow));
        }

        tv_title.setText(film.getTitle());
        tv_detail.setText(tv_detail.getText() + film.getSummary());
        tv_review.setText(tv_review.getText() + film.getReview());
        tv_date.setText(tv_date.getText() + film.getDate());

        Picasso.with(getContext()).load(film.getPoster()).into(iv_fullscreen);
        Picasso.with(getContext()).load(film.getPoster()).into(iv_detail);

        btn_trailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new myReciever().isConnected(getContext())) {
                    String token = film.getVideos();
                    if (token == null) {
                        Toast.makeText(getContext(), "No Trailer available for this movie", Toast.LENGTH_SHORT).show();
                    } else {
                        token = token.substring(7);
                        final String[] see = token.split("/./");
                        for (int i = 0; i < see.length; i++) {
                            see[i] = "https://www.youtube.com/watch?v=" + see[i];
                        }
                        Mixdata.setVisibility(View.GONE);
                        MoreDetails.setVisibility(View.VISIBLE);
                        xBtn.setVisibility(View.VISIBLE);
                        List<String> strvids = Arrays.asList(see);
                        ArrayAdapter<String> listAdp = new ArrayAdapter<String>(getContext(), R.layout.vidlist, R.id.vid_list, strvids);
                        ListDetail.setAdapter(listAdp);
                        ListDetail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(see[position])));
                                Log.i("Video", "Video Playing....");
                            }
                        });
                        xBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Mixdata.setVisibility(View.VISIBLE);
                                MoreDetails.setVisibility(View.GONE);
                                xBtn.setVisibility(View.GONE);
                            }
                        });
                    }
                } else {
                    Toast.makeText(getContext(), "Internet Connection is needed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new myReciever().isConnected(getContext())) {
                    String tok = film.getComments();
                    if (tok == null) {
                        Toast.makeText(getContext(), "No Reviews available for this movie", Toast.LENGTH_SHORT).show();
                    } else {
                        tok = tok.substring(7);
                        String[] see = tok.split("/./");
                        Mixdata.setVisibility(View.GONE);
                        MoreDetails.setVisibility(View.VISIBLE);
                        xBtn.setVisibility(View.VISIBLE);
                        List<String> revstr = Arrays.asList(see);
                        ArrayAdapter<String> listAdp = new ArrayAdapter<String>(getContext(), R.layout.txtlist, R.id.txt_list, revstr);
                        ListDetail.setAdapter(listAdp);
                        ListDetail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Log.d("CLICK", "onItemClick: test");
                            }
                        });
                        xBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Mixdata.setVisibility(View.VISIBLE);
                                MoreDetails.setVisibility(View.GONE);
                                xBtn.setVisibility(View.GONE);
                            }
                        });
                    }
                } else {
                    Toast.makeText(getContext(), "Internet Connection is Needed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_fav.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btn_fav.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.star_yellow));
                } else {
                    btn_fav.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.star_gray));
                }
                boolean isRmvng = toFav(film);
                if ((Objects.equals(pop_or_top, "Favorite")) && (isRmvng) && (is2pane)) {
                    ((FrameLayout) getActivity().findViewById(R.id.flDetails)).setVisibility(View.INVISIBLE);
                }
            }
        });

        return v;
    }

    boolean toFav(Movie film) {
        boolean flag = false;
        realm.beginTransaction();
        if (!CheckifExists(film.getId())) {
            realm.copyToRealm(film);
            Toast.makeText(getContext(), "Added to your Favorite List", Toast.LENGTH_SHORT).show();
        } else {
            flag = true;
            realm.where(Movie.class).equalTo("Id", film.getId()).findAll().deleteAllFromRealm();
            Toast.makeText(getContext(), "Removed from your Favorite list", Toast.LENGTH_SHORT).show();
        }
        realm.commitTransaction();
        return flag;
    }


    Boolean CheckifExists(String id) {
        RealmQuery<Movie> query = realm.where(Movie.class)
                .equalTo("Id", id);
        return query.count() != 0;
    }


    private void getTrailers(final Movie film) {

        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "http://api.themoviedb.org/3/movie/" + film.getId() + "/videos" + "?api_key=" + BuildConfig.API_KEY;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            public void onResponse(String response) {
                {
                    try {
                        JSONObject jobjects = new JSONObject(response);
                        JSONArray jVideos = jobjects.getJSONArray("results");
                        String[] vids = new String[jVideos.length()];
                        for (int i = 0; i < jVideos.length(); i++) {
                            JSONObject build = jVideos.getJSONObject(i);
                            vids[i] = build.getString("key");
                        }
                        realm.removeAllChangeListeners();
                        realm.beginTransaction();
                        film.setVideos(vids);
                        realm.commitTransaction();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.v(LOGTAG, "vidsURI:error");
                    }
                }
                ;
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Log.v(LOGTAG, "things error1");
            }
        });
        queue.add(stringRequest);

    }

    private void getReviews(final Movie film) {
        // http://api.themoviedb.org/3/movie/157336/reviews?api_key=752d6e9b3f25817a8f33a3a57d372d78
        // http://api.themoviedb.org/3/movie/157336/videos?api_key=752d6e9b3f25817a8f33a3a57d372d78

        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "http://api.themoviedb.org/3/movie/" + film.getId() + "/reviews" + "?api_key=" + BuildConfig.API_KEY;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            public void onResponse(String response) {
                {
                    try {
                        JSONObject jobjects = new JSONObject(response);
                        JSONArray jReviews = jobjects.getJSONArray("results");
                        String[] revs = new String[jReviews.length()];
                        for (int i = 0; i < jReviews.length(); i++) {
                            JSONObject build = jReviews.getJSONObject(i);
                            revs[i] = build.getString("content");
                        }
                        realm.removeAllChangeListeners();
                        realm.beginTransaction();
                        film.setComments(revs);
                        realm.commitTransaction();
                        realm.addChangeListener(realmListener);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.v(LOGTAG, "revsURI:error");
                    }
                }
                ;
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Log.v(LOGTAG, "things error2");
            }
        });
        queue.add(stringRequest);
    }

}
