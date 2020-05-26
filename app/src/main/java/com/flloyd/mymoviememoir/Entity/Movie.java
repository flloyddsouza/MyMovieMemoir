package com.flloyd.mymoviememoir.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Movie {

    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "movie_name")
    public String movieName;


    @ColumnInfo(name = "release_date")
    public String releaseDate;


    @ColumnInfo(name = "save_date_time")
    public String saveDateTime;

    public Movie(String movieName, String releaseDate,String saveDateTime){
        this.movieName = movieName;
        this.releaseDate = releaseDate;
        this.saveDateTime = saveDateTime;
    }

    @Ignore
    public Movie(int uid,String movieName, String releaseDate,String saveDateTime){
        this.uid = uid;
        this.movieName = movieName;
        this.releaseDate = releaseDate;
        this.saveDateTime = saveDateTime;
    }


    public int getId() {
        return uid;
    }

    public String getMovieName() {
        return movieName;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getSaveDateTime() {
        return saveDateTime;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setSaveDateTime(String saveDateTime) {
        this.saveDateTime = saveDateTime;
    }
}
