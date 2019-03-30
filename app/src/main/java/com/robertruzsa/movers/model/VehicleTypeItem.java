package com.robertruzsa.movers.model;

import android.widget.ImageView;

public class VehicleTypeItem {
    private int imageResource;
    private String title, description;
    private boolean isSelected = false;

    public VehicleTypeItem(int imageResource, String title, String description) {
        this.imageResource = imageResource;
        this.title = title;
        this.description = description;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected() {
        return isSelected;
    }
}
