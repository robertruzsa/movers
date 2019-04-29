package com.robertruzsa.movers.model;

public class VehicleItem {
    private String id, vehicleType, kilometreCharge, initialCharge, requiredHours, hourlyRate;

    public VehicleItem(String id, String vehicleType, String kilometreCharge, String initialCharge, String requiredHours, String hourlyRate) {
        this.id = id;
        this.vehicleType = vehicleType;
        this.kilometreCharge = kilometreCharge;
        this.initialCharge = initialCharge;
        this.requiredHours = requiredHours;
        this.hourlyRate = hourlyRate;
    }

    public VehicleItem() {
        this.vehicleType = "";
        this.kilometreCharge = "";
        this.initialCharge = "";
        this.requiredHours = "";
        this.hourlyRate = "";
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public String getKilometreCharge() {
        return kilometreCharge;
    }

    public String getInitialCharge() {
        return initialCharge;
    }

    public String getRequiredHours() {
        return requiredHours;
    }

    public String getHourlyRate() {
        return hourlyRate;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public void setKilometreCharge(String kilometreCharge) {
        this.kilometreCharge = kilometreCharge;
    }

    public void setInitialCharge(String initialCharge) {
        this.initialCharge = initialCharge;
    }

    public void setRequiredHours(String requiredHours) {
        this.requiredHours = requiredHours;
    }

    public void setHourlyRate(String hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public String getId() {
        return id;
    }
}
