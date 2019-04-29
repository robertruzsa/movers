package com.robertruzsa.movers.model;

import com.parse.ParseUser;

import java.util.Date;

public class RequestItem {

    private int imageResource, price;
    private String id, clientName, pickupLocation, dropoffLocation,  movingDetails;
    LocationDetails locationDetails;
    Date requestSubmitDate, movingDate;

    private boolean isSelected = false;

    public RequestItem(int imageResource, int price, String clientName, String pickupLocation, String dropoffLocation, String movingDetails, LocationDetails locationDetails, Date requestSubmitDate, Date movingDate) {
        this.imageResource = imageResource;
        this.price = price;
        this.clientName = clientName;
        this.pickupLocation = pickupLocation;
        this.dropoffLocation = dropoffLocation;
        this.movingDetails = movingDetails;
        this.locationDetails = locationDetails;
        this.requestSubmitDate = requestSubmitDate;
        this.movingDate = movingDate;
    }

    public RequestItem(String clientName, Date movingDate, String pickupLocation, String dropoffLocation) {
        this.clientName = clientName;
        this. movingDate = movingDate;
        this.pickupLocation = pickupLocation;
        this.dropoffLocation = dropoffLocation;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public String getDropoffLocation() {
        return dropoffLocation;
    }

    public void setDropoffLocation(String dropoffLocation) {
        this.dropoffLocation = dropoffLocation;
    }

    public String getMovingDetails() {
        return movingDetails;
    }

    public void setMovingDetails(String movingDetails) {
        this.movingDetails = movingDetails;
    }

    public LocationDetails getLocationDetails() {
        return locationDetails;
    }

    public void setLocationDetails(LocationDetails locationDetails) {
        this.locationDetails = locationDetails;
    }

    public Date getRequestSubmitDate() {
        return requestSubmitDate;
    }

    public void setRequestSubmitDate(Date requestSubmitDate) {
        this.requestSubmitDate = requestSubmitDate;
    }

    public Date getMovingDate() {
        return movingDate;
    }

    public void setMovingDate(Date movingDate) {
        this.movingDate = movingDate;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
