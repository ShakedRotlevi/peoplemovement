package com.example.shakedrotlevi.peoplemovementapp;


import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class Group {
    public String name, time, description, startName, endName;
    //public LatLng startLoc, endLoc;
    public LocationObject startLoc;// = new LatLng(0.0,0.0);
    public LocationObject endLoc;// = new LatLng(0.0,0.0);
    public ArrayList<String> members = new ArrayList<String>();
    public String creator;
    public static String welcome;
    public LocationObject groupLoc;
    public long hour;
    public long minutes;
    public String status;
    public Group(){

    }

    public Group(String name, LocationObject startLoc, LocationObject endLoc, String startName, String endName, String description, String creator, LocationObject creatorLoc, ArrayList<String> members, String status, String time, long hour, long minutes) {
        this.name = name;
        this.time = time;
       // this.startLoc = new LatLng(startLoc.latitude,startLoc.longitude);

        //this.endLoc = new LatLng(endLoc.latitude,endLoc.longitude);
        this.startLoc = startLoc;
        this.endLoc = endLoc;
        this.startName=startName;
        this.endName = endName;
        this.description = description;
        this.creator=creator;
        this.members=members;
        this.groupLoc = creatorLoc;
        this.hour =hour;
        this.minutes = minutes;
        this.status = status;

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
    public long getHour() {
        return hour;
    }
    public long getMinutes() {
        return minutes;
    }
    public String getStatus() {
        return status;
    }


    public LocationObject getStartLoc(){return startLoc;}
    public LocationObject getEndLoc(){return endLoc;}


    public String getStartName(){return startName;}
    public String getEndName(){return endName;}
    public String getDescription(){return description;}

    public String getCreator(){return creator;}
    public LocationObject getGroupLoc(){return groupLoc;}
    public ArrayList<String> getMembers(){return members;}

    public void addMembers(String user){
        this.members.add(user);
    }


   /* public void setLongitude(double longitude) {
        lon = longitude;
    }*/

}
