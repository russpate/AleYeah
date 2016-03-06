package com.theironyard;

/**
 * Created by drewmunnerlyn on 3/4/16.
 */
public class Beer {
    int id;
    String author;
    String beerName;
    String beerType;
    String alcoholContent;
    boolean isGood;
    String comment;
//    String image;



    public Beer(int id, String author, String beerName, String beerType, String alcoholContent, boolean isGood, String comment) { //, String image
        this.id = id;
        this.author = author;
        this.beerName = beerName;
        this.beerType = beerType;
        this.alcoholContent = alcoholContent;
        this.isGood = isGood;
        this.comment = comment;
//        this.image = image;

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

    public String getAlcoholContent() {
        return alcoholContent;
    }

    public void setAlcoholContent(String alcoholContent) {
        this.alcoholContent = alcoholContent;
    }

    public boolean isGood() {
        return isGood;
    }

    public void setIsGood(boolean isGood) {
        this.isGood = isGood;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


//    public String getImage() {
//        return image;
//    }
//
//    public void setImage(String image) {
//        this.image = image;
//    }
}
