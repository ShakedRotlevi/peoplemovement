package com.example.shakedrotlevi.peoplemovementapp;



public class Group {
    public String name, time, startLoc, endLoc, description;
    public static String welcome;

    public Group(){

    }
    public Group(String name, String time, String startLoc, String endLoc, String description ) {
        this.name = name;
        this.time = time;
        this.startLoc = startLoc;
        this.endLoc = endLoc;
        this.description = description;

    }
    public String getName() {
        return name;
    }

    /*public void setLatitude(double latitude) {
        lat = latitude;
    }*/

    public String getTime() {
        return time;
    }

    public String getStartLoc(){return startLoc;}
    public String getEndLoc(){return endLoc;}
    public String getDescription(){return description;}


   /* public void setLongitude(double longitude) {
        lon = longitude;
    }*/

}
