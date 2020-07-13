package com.example.ac.nightmovieapp;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.Arrays;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


/**
 * Created by AC on 11/3/2016.
 */

public class Movie extends RealmObject implements Parcelable {
    String Poster;
    String Title;
    String Date;
    String Review;
    String Summary;
    String Uri;
    String Videos;
    String Comments;

    String Size = "w342/";

    @PrimaryKey
    String Id;

    Movie(String id, String poster) {
        Id = id;
        Uri = "https://api.themoviedb.org/3/movie/" + id + "?api_key=" + BuildConfig.API_KEY;
        Poster = "http://image.tmdb.org/t/p/" + Size + poster;
    }

    public Movie() {
    }

    protected Movie(Parcel in) {
        Poster = in.readString();
        Title = in.readString();
        Date = in.readString();
        Review = in.readString();
        Summary = in.readString();
        Uri = in.readString();
        Videos = in.readString();
        Comments = in.readString();
        Size = in.readString();
        Id = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public void setUri(String id) {
        Uri = "https://api.themoviedb.org/3/movie/" + id + "?api_key=" + BuildConfig.API_KEY;
        Log.d("Movie", "setUri: Change happened");
    }

    public String getUri() {
        return Uri;
    }

    public void setPoster(String poster) {
        Poster = "http://image.tmdb.org/t/p/" + Size + poster;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setDate(String date) {
        Date = date;
    }

    public void setReview(String review) {
        Review = review;
    }

    public void setSummary(String summary) {
        Summary = summary;
    }

    public String getTitle() {
        return Title;
    }

    public String getReview() {
        return Review;
    }

    public String getDate() {
        return Date;
    }

    public String getPoster() {
        return Poster;
    }

    public String getSummary() {
        return Summary;
    }

    public void setSize(String size) {
        String[] supportedSizes = {"w92", "w154", "w185", "w342", "w500", "w780", "original"};
        if (Arrays.asList(supportedSizes).contains(size)) {
            Size = size;
        } else {
            Size = "w342";
        }
    }

    public String getId() {
        return Id;
    }

    public String getComments() {
        return Comments;
    }

    public String getVideos() {
        return Videos;
    }

    public void setComments(String[] comments) {
        for (int i = 0; i < comments.length; i++) {
            Comments = Comments + "/./"+comments[i];
        }
    }

    public void setVideos(String[] videos) {
        for (int i = 0; i < videos.length; i++) {
            Videos = Videos+"/./"+videos[i];
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Poster);
        dest.writeString(Title);
        dest.writeString(Date);
        dest.writeString(Review);
        dest.writeString(Summary);
        dest.writeString(Uri);
        dest.writeString(Videos);
        dest.writeString(Comments);
        dest.writeString(Size);
        dest.writeString(Id);
    }
}