package com.lig.intermediate.tmdbclient.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.res.Configuration;
import android.os.Bundle;

import com.lig.intermediate.tmdbclient.R;
import com.lig.intermediate.tmdbclient.adapter.MovieAdapter;
import com.lig.intermediate.tmdbclient.model.Movie;
import com.lig.intermediate.tmdbclient.model.MovieDBResponse;
import com.lig.intermediate.tmdbclient.service.MovieDataService;
import com.lig.intermediate.tmdbclient.service.RetrofitInstance;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
* https://www.themoviedb.org/u/liguopeng  -> DB web service
* https://web.postman.co/ -> testing post Get
* http://www.jsonschema2pojo.org/ -> dev tool to generate JAVA code from JSON using Gson
* */

public class MainActivity extends AppCompatActivity {

    private ArrayList<Movie> movies =  new ArrayList<>();
    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Observable<MovieDBResponse> movieDBResponseObservable;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("TMDB popular Movies Today");
        getPopularMoviesRx();

        swipeRefreshLayout = findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPopularMoviesRx();
            }
        });
    }

    public void getPopularMovies(){
        MovieDataService movieDataService = RetrofitInstance.getService();
        Call<MovieDBResponse> call = movieDataService.getPopularMovies(this.getString(R.string.api_key));

        call.enqueue(new Callback<MovieDBResponse>() {
            @Override
            public void onResponse(Call<MovieDBResponse> call, Response<MovieDBResponse> response) {
                MovieDBResponse movieDBResponse = response.body();

                if(movieDBResponse!=null && movieDBResponse.getMovies()!=null){
                    movies = (ArrayList<Movie>) movieDBResponse.getMovies();
                    showInRecycleView();

                }
            }

            @Override
            public void onFailure(Call<MovieDBResponse> call, Throwable t) {

            }
        });

    }

    public void getPopularMoviesRx(){
        MovieDataService movieDataService = RetrofitInstance.getService();
        movieDBResponseObservable = movieDataService.getPopularMoviesWithRx(this.getString(R.string.api_key));
        compositeDisposable.add(
        movieDBResponseObservable.subscribeOn(Schedulers.io())
                                 .observeOn(AndroidSchedulers.mainThread())
                                 .flatMap(new Function<MovieDBResponse, ObservableSource<Movie>>() {
                                     @Override
                                     public ObservableSource<Movie> apply(MovieDBResponse movieDBResponse) throws Exception {
                                         return Observable.fromArray(movieDBResponse.getMovies().toArray(new Movie[0]));
                                     }
                                 })
                                 .filter(new Predicate<Movie>() {
                                     @Override
                                     public boolean test(Movie movie) throws Exception {
                                         return movie.getVoteAverage() > 7.0;
                                     }
                                 })
                                 .subscribeWith(new DisposableObserver<Movie>() {
                                     @Override
                                     public void onNext(Movie movie) {
                                         movies.add(movie);
                                     }

                                     @Override
                                     public void onError(Throwable e) {

                                     }

                                     @Override
                                     public void onComplete() {
                                         showInRecycleView();

                                     }
                                 }));


    }

    private void showInRecycleView() {
        recyclerView = (RecyclerView)findViewById(R.id.rvMovies);
        movieAdapter = new MovieAdapter(this, movies);

        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        }else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(movieAdapter);
        movieAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();

        /*if(call != null){
            if(call.isExecuted()){
                call.cancel();
            }

        }*/
    }
}