package com.lig.intermediate.tmdbclient.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.lig.intermediate.tmdbclient.R;
import com.lig.intermediate.tmdbclient.model.MovieDBResponse;
import com.lig.intermediate.tmdbclient.service.MovieDataService;
import com.lig.intermediate.tmdbclient.service.RetrofitInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
* https://www.themoviedb.org/u/liguopeng  -> DB web service
* https://web.postman.co/ -> testing post Get
* http://www.jsonschema2pojo.org/ -> dev tool to generate JAVA code from JSON using Gson
* */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("TMDB popular Movies Today");
        getPopularMovies();
    }

    public void getPopularMovies(){
        MovieDataService movieDataService = RetrofitInstance.getService();
        Call<MovieDBResponse> call = movieDataService.getPopularMovies(this.getString(R.string.api_key));

        call.enqueue(new Callback<MovieDBResponse>() {
            @Override
            public void onResponse(Call<MovieDBResponse> call, Response<MovieDBResponse> response) {
                MovieDBResponse movieDBResponse = response.body();
            }

            @Override
            public void onFailure(Call<MovieDBResponse> call, Throwable t) {

            }
        });

    }

}