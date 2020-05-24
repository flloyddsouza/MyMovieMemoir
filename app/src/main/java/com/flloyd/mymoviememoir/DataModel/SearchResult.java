package com.flloyd.mymoviememoir.DataModel;

public class SearchResult {
    private String name;
    private String year;
    private String image;
    private String backdropImage;

    public SearchResult(String name,String year,String image,String backdropImage){
        this.name = name;
        this.year = year;
        this.image = image;
        this.backdropImage = backdropImage;
    }

    public String getName() {
        return name;
    }

    public String getYear() {
        return year;
    }

    public String getImage() {
        return image;
    }

    public String getBackdropImage(){ return backdropImage; }

    public void setName(String name) {
        this.name = name;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setBackdropImage(String backdropImage) { this.backdropImage = backdropImage; }
}
