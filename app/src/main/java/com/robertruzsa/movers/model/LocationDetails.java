package com.robertruzsa.movers.model;

public class LocationDetails {
    private String pickupLocationFloorNumber, dropoffLocationFloorNumber, pickupLocationParkingInfo, dropoffLocationParkingInfo;
    private boolean pickupLocationElevator, dropoffLocationElevator;
    private float distanceInKilometers;

    public LocationDetails(String pickupLocationFloorNumber, String dropoffLocationFloorNumber, String pickupLocationParkingInfo, String dropoffLocationParkingInfo, boolean pickupLocationElevator, boolean dropoffLocationElevator, int distanceInKilometers) {
        this.pickupLocationFloorNumber = pickupLocationFloorNumber;
        this.dropoffLocationFloorNumber = dropoffLocationFloorNumber;
        this.pickupLocationParkingInfo = pickupLocationParkingInfo;
        this.dropoffLocationParkingInfo = dropoffLocationParkingInfo;
        this.pickupLocationElevator = pickupLocationElevator;
        this.dropoffLocationElevator = dropoffLocationElevator;
        this.distanceInKilometers = distanceInKilometers;
    }

    public String getPickupLocationFloorNumber() {
        return pickupLocationFloorNumber;
    }

    public void setPickupLocationFloorNumber(String pickupLocationFloorNumber) {
        this.pickupLocationFloorNumber = pickupLocationFloorNumber;
    }

    public String getDropoffLocationFloorNumber() {
        return dropoffLocationFloorNumber;
    }

    public void setDropoffLocationFloorNumber(String dropoffLocationFloorNumber) {
        this.dropoffLocationFloorNumber = dropoffLocationFloorNumber;
    }

    public String getPickupLocationParkingInfo() {
        return pickupLocationParkingInfo;
    }

    public void setPickupLocationParkingInfo(String pickupLocationParkingInfo) {
        this.pickupLocationParkingInfo = pickupLocationParkingInfo;
    }

    public String getDropoffLocationParkingInfo() {
        return dropoffLocationParkingInfo;
    }

    public void setDropoffLocationParkingInfo(String dropoffLocationParkingInfo) {
        this.dropoffLocationParkingInfo = dropoffLocationParkingInfo;
    }

    public boolean isPickupLocationElevator() {
        return pickupLocationElevator;
    }

    public void setPickupLocationElevator(boolean pickupLocationElevator) {
        this.pickupLocationElevator = pickupLocationElevator;
    }

    public boolean isDropoffLocationElevator() {
        return dropoffLocationElevator;
    }

    public void setDropoffLocationElevator(boolean dropoffLocationElevator) {
        this.dropoffLocationElevator = dropoffLocationElevator;
    }

    public float getDistanceInKilometers() {
        return distanceInKilometers;
    }

    public void setDistanceInKilometers(float distanceInKilometers) {
        this.distanceInKilometers = distanceInKilometers;
    }
}
