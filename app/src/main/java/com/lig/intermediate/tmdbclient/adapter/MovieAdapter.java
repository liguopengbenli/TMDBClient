package com.lig.intermediate.tmdbclient.adapter;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lig.intermediate.tmdbclient.model.Movie;

import java.net.ContentHandler;
import java.util.ArrayList;

public class MovieAdapter {

    private Context context;
    private ArrayList<Movie> movieArrayList;

    public MovieAdapter(Context context, ArrayList<Movie> movieArrayList){
        this.context = context;
        this.movieArrayList = movieArrayList;
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder{


        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
