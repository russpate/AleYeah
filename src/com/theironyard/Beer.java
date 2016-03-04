package com.theironyard;

/**
 * Created by drewmunnerlyn on 3/4/16.
 */
public class Beer {
    int id;
    String author;
    String beerName;
    String beerType;
    int alcoholContent;
    boolean is_Good;
    String comment;

    boolean isAuthor;


    public Beer(int id,  String author, String beerName, String beerType, int alcoholContent, boolean is_Good, String comment) {
        this.id = id;
        this.author = author;
        this.beerName = beerName;
        this.beerType = beerType;
        this.alcoholContent = alcoholContent;
        this.is_Good = is_Good;
        this.comment = comment;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBeerName() {
        return beerName;
    }

    public void setBeerName(String beerName) {
        this.beerName = beerName;
    }

    public String getBeerType() {
        return beerType;
    }

    public void setBeerType(String beerType) {
        this.beerType = beerType;
    }

    public int getAlcoholContent() {
        return alcoholContent;
    }

    public void setAlcoholContent(int alcoholContent) {
        this.alcoholContent = alcoholContent;
    }

    public boolean is_Good() {
        return is_Good;
    }

    public void setIs_Good(boolean is_Good) {
        this.is_Good = is_Good;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isAuthor() {
        return isAuthor;
    }

    public void setAuthor(boolean author) {
        isAuthor = author;
    }
}
