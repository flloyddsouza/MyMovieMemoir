package com.flloyd.mymoviememoir.M3Model;

public class Cinema {

    private String cinemaname;
    private String cinemapostcode;

    public Cinema(String cinemaname, String cinemapostcode) {

        this.cinemaname = cinemaname;
        this.cinemapostcode = cinemapostcode;
    }

    public String getCinemaname() {
        return cinemaname;
    }

    public void setCinemaname(String cinemaname) {
        this.cinemaname = cinemaname;
    }

    public String getCinemapostcode() {
        return cinemapostcode;
    }

    public void setCinemapostcode(String cinemapostcode) {
        this.cinemapostcode = cinemapostcode;
    }

}
