package com.flloyd.mymoviememoir.viewmodel;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.flloyd.mymoviememoir.Entity.Movie;
import com.flloyd.mymoviememoir.repository.MovieRepository;

import java.util.List;

public class MovieViewModel extends ViewModel {

    private MovieRepository movieRepository;

    private MutableLiveData<List<Movie>> allMovies;

    public MovieViewModel () {
        allMovies = new MutableLiveData<>();
    }

    public void setMovies(List<Movie> movies) {
        allMovies.setValue(movies);
    }
    public LiveData<List<Movie>> getAllMovies() {
        return movieRepository.getAllMovies();
    }
    public void initalizeVars(Application application){
        movieRepository = new MovieRepository(application);
    }

    public void insert(Movie movie) {
        movieRepository.insert(movie);
    }
    public void insertAll(Movie... movies) {
        movieRepository.insertAll(movies);
    }
    public void deleteAll() {
        movieRepository.deleteAll();
    }

    public void update(Movie... movies) {
        movieRepository.updateMovie(movies);
    }
    public void updateByID(int movieId,String movieName,String releaseDate,String saveDateTime) {
        movieRepository.updateMovieByID(movieId,movieName,releaseDate,saveDateTime);
    }

    public Movie findByID(int movieID){
        return movieRepository.findByID(movieID);
    }



}
