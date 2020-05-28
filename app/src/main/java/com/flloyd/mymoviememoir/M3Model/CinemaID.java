package com.flloyd.mymoviememoir.M3Model;

public class CinemaID {
    private String cinemaid;
    private String cinemaname;
    private String cinemapostcode;

    public CinemaID(String cinemaid,String cinemaname, String cinemapostcode) {
        this.cinemaid = cinemaid;
        this.cinemaname = cinemaname;
        this.cinemapostcode = cinemapostcode;
    }

    public String getCinemaid() {
        return cinemaid;
    }

    public void setCinemaid(String cinemaid) {
        this.cinemaid = cinemaid;
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
