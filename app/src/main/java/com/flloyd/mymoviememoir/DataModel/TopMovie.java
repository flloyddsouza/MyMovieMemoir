package com.flloyd.mymoviememoir.DataModel;

import java.util.Date;

public class TopMovie {
    private String movieName;
    private Double rating;
    private Date releaseDate;

    public TopMovie(String movieName, Double rating, Date releaseDate){
        this.movieName = movieName;
        this.rating = rating;
        this.releaseDate = releaseDate;
    }

    public String getMovieName() {
        return movieName;
    }

    public Double getRating() {
        return rating;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }


}
