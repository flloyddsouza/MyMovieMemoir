package com.flloyd.mymoviememoir.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.flloyd.mymoviememoir.Entity.Movie;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface MovieDAO {

    @Query("SELECT * FROM movie")
    LiveData<List<Movie>> getAll();


    @Query("SELECT * FROM movie WHERE uid = :movieId LIMIT 1")
    Movie findByID(int movieId);

    @Insert
    void insertAll(Movie... movies);

    @Insert
    long insert(Movie movie);

    @Delete
    void delete(Movie movie);

    @Update(onConflict = REPLACE)
    void updateMovies(Movie... movies);

    @Query("DELETE FROM movie")
    void deleteAll();


    @Query("UPDATE movie SET movie_name=:movieName,release_date=:releaseDate, save_date_time=:saveDateTime WHERE uid = :id")
    void updateByID(int id, String movieName, String releaseDate, String saveDateTime);

}
