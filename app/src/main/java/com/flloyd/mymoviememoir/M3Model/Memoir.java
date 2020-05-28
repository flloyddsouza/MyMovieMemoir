package com.flloyd.mymoviememoir.M3Model;

public class Memoir {

    private String moviename;
    private String releasedate;
    private String watchdate;
    private String watchtime;
    private String review;
    private String rating;
    private PersonID personid;
    private CinemaID cinemaid;


    public Memoir( String moviename, String releasedate, String watchdate, String watchtime,String review, String rating) {
        this.moviename = moviename;
        this.releasedate = releasedate + "T00:00:00+11:00";;
        this.watchdate = watchdate + "T00:00:00+11:00";;
        this.watchtime = "1970-01-01T" + watchtime + ":00+10:00";
        this.review = review;
        this.rating = rating;
    }



    public String getMoviename() {
        return moviename;
    }

    public void setMoviename(String moviename) {
        this.moviename = moviename;
    }

    public String getReleasedate() {
        return releasedate;
    }

    public void setReleasedate(String releasedate) {
        this.releasedate = releasedate;
    }

    public String getWatchdate() {
        return watchdate;
    }

    public void setWatchdate(String watchdate) {
        this.watchdate = watchdate;
    }

    public String getWatchtime() {
        return watchtime;
    }

    public void setWatchtime(String watchtime) {
        this.watchtime = watchtime;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public PersonID getPersonid() {
        return personid;
    }

    public void setPersonid(PersonID personid) {
        this.personid = personid;
    }

    public CinemaID getCinemaid() {
        return cinemaid;
    }

    public void setCinemaid(CinemaID cinemaid) {
        this.cinemaid = cinemaid;
    }
}
