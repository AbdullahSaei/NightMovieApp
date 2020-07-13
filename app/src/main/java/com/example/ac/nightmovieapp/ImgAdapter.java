package com.example.ac.nightmovieapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by AC on 10/21/2016.
 */
class ImgAdapter extends BaseAdapter {
    private Context mContext;
    private Movie[] Data;

    ImgAdapter(Context c, Movie[] data) {
        mContext = c;
        Data = data;
    }

    @Override
    public int getCount() {
        return Data.length;
    }

    @Override
    public Object getItem(int position) {
        return Data[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.poster_grid, parent, false);
            ImageView iv_poster = (ImageView) convertView.findViewById(R.id.iv_poster);
            Picasso.with(mContext).load(Data[position].getPoster()).into(iv_poster);
            return convertView;
    }
}
