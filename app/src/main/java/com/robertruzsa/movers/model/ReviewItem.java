package com.robertruzsa.movers.model;

import java.util.Date;

public class ReviewItem {

    private int imageResource, reviewCount;
    private float rating;
    private String clientName, comment;
    private Date reviewDate;


    public ReviewItem(int imageResource, String clientName, int reviewCount, float rating, Date reviewDate, String comment) {
        this.imageResource = imageResource;
        this.clientName = clientName;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.comment = comment;
        this.reviewDate = reviewDate;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }
}
