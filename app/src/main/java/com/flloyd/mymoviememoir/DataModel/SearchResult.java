package com.flloyd.mymoviememoir.DataModel;

public class SearchResult {
    private String name;
    private String year;
    private String image;

    public SearchResult(String name,String year,String image){
        this.name = name;
        this.year = year;
        this.image = image;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
