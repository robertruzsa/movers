package com.robertruzsa.movers.model;

import android.widget.RatingBar;

import java.util.ArrayList;
import java.util.List;

public class MoverItem {
    private int imageResource;
    private String id, name, price;
    private float rating;
    private int ratingCount;
    private boolean isSelected = false;

    public MoverItem(int imageResource, String id, String name, String price, float rating, int ratingCount) {
        this.imageResource = imageResource;
        this.id = id;
        this.name = name;
        this.price = price;
        this.rating = rating;
        this.ratingCount = ratingCount;
    }

    public String getId() {
        return id;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public float getRating() {
        return rating;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
