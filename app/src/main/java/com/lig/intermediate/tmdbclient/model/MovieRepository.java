package com.lig.intermediate.tmdbclient.model;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.lig.intermediate.tmdbclient.R;
import com.lig.intermediate.tmdbclient.service.MovieDataService;
import com.lig.intermediate.tmdbclient.service.RetrofitInstance;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {
    private Application application;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private MutableLiveData<List<Movie>> moviesLiveData = new MutableLiveData<>();
    private ArrayList<Movie> movies =  new ArrayList<>();
    private Observable<MovieDBResponse> movieDBResponseObservable;

    public MovieRepository(Application application){
        this.application = application;
        MovieDataService movieDataService = RetrofitInstance.getService();
        movieDBResponseObservable = movieDataService.getPopularMoviesWithRx(application.getApplicationContext().getString(R.string.api_key));
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
                                moviesLiveData.postValue(movies);

                            }
                        }));
    }


    public MutableLiveData<List<Movie>> getMoviesLiveData() {
        return moviesLiveData;
    }

    public void clear(){
        compositeDisposable.clear();
    }





}
