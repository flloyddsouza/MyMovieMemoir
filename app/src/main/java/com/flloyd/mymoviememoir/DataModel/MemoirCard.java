package com.flloyd.mymoviememoir.DataModel;

import androidx.annotation.NonNull;

public class MemoirCard {

    private String movieName;
    private String releaseDate;
    private String watchDate;
    private String cinemaPostCode;
    private String comment;
    private float rating;

    public MemoirCard(String movieName,String releaseDate,String watchDate,String cinemaPostCode,String comment,float rating){
        this.movieName = movieName;
        this.releaseDate = releaseDate;
        this.watchDate = watchDate;
        this.cinemaPostCode = cinemaPostCode;
        this.comment = comment;
        this.rating = rating;

    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }


    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }


    public String getWatchDate() {
        return watchDate;
    }

    public void setWatchDate(String watchDate) {
        this.watchDate = watchDate;
    }

    public String getCinemaPostCode() {
        return cinemaPostCode;
    }

    public void setCinemaPostCode(String cinemaPostCode) {
        this.cinemaPostCode = cinemaPostCode;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }


    public String getComment() {
        return comment;
    }


    public void setComment(String comment) {
        this.comment = comment;
    }

    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
