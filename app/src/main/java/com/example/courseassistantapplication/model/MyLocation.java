package com.example.courseassistantapplication.model;

import android.location.Location;

public class MyLocation {
    private double latitude;
    private double longitude;
    private double altitude;

    // Boş kurucu, Firebase'in veri okuması için gereklidir
    public MyLocation() {
    }

    public MyLocation(double latitude, double longitude, double altitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    // İki MyLocation nesnesi arasındaki mesafeyi hesapla
    public float distanceTo(Location other) {
        float[] results = new float[1];
        Location.distanceBetween(this.latitude, this.longitude, other.getLatitude(), other.getLongitude(), results);
        return results[0];
    }
}
