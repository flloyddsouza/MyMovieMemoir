package com.flloyd.mymoviememoir.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.flloyd.mymoviememoir.DAO.MovieDAO;
import com.flloyd.mymoviememoir.Database.MovieDatabase;
import com.flloyd.mymoviememoir.Entity.Movie;

import java.util.List;

public class MovieRepository {

    private MovieDAO dao;
    private LiveData<List<Movie>> allMovies;
    private List<Movie> allMovieList;
    private Movie movie;

    public MovieRepository(Application application){
        MovieDatabase db = MovieDatabase.getInstance(application);
        dao=db.movieDAO();
    }
    public LiveData<List<Movie>> getAllMovies() {
        allMovies = dao.getAll();
        return allMovies;
    }



    public void insert(final Movie movie){
        MovieDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.insert(movie);
            }
        });
    }


    public void deleteAll(){
        MovieDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.deleteAll();
            }
        });
    }


    public void delete(final Movie movie){
        MovieDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.delete(movie);
            }
        });
    }


    public void insertAll(final Movie... movies){
        MovieDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.insertAll(movies);
            }
        });
    }


    public void updateMovie(final Movie... movies){
        MovieDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.updateMovies(movies);
            }
        });
    }


    public void updateMovieByID(final int movieId, final String movieName, final String releaseDate, final String saveDateTime) {
        MovieDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.updateByID(movieId, movieName, releaseDate, saveDateTime);
            }
        });

    }

    public Movie findByID(final int movieId){
            MovieDatabase.databaseWriteExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    Movie runMovie = dao.findByID(movieId);
                    setMovie(runMovie);
                }
            });
            return movie;
        }


        public void setMovie(Movie movie){
            this.movie = movie;
        }
}
