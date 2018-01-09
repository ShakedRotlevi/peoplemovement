package com.example.shakedrotlevi.peoplemovementapp;

/**
 * Created by shakedrotlevi on 11/13/17.
 */

public class Cluster {
    public double lat, lon;


    public Cluster(){

    }
    public Cluster(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;

    }
    public double getLat() {
        return lat;
    }

    /*public void setLatitude(double latitude) {
        lat = latitude;
    }*/

    public double getLon() {
        return lon;
    }

}
