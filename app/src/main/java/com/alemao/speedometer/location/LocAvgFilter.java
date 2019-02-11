package com.alemao.speedometer.location;

import android.location.Location;

public class LocAvgFilter {
    private Location[] locs;
    private int size;
    private int head;

    public LocAvgFilter(int size, Location loc){
        head = 0;
        this.size = size;
        locs = new Location[size];
        for(int i=0; i<size;++i)
            locs[i] = loc;
    }

    public LocAvgFilter(Location[] locs){
        size = locs.length;
        head = size-1;
        this.locs = locs.clone();
    }

    public void addLoc(Location loc){
        ++head;
        if(head>=size) head = 0;

        locs[head] = loc;
    }

    public double getLatitude(){
        double res=0;
        for(Location loc : locs)
            res+=loc.getLatitude();
        return res/size;
    }

    public double getLongitude(){
        double res=0;
        for(Location loc : locs)
            res+=loc.getLongitude();
        return res/size;
    }

    public double getSpeed(){
        double res=0;
        for(Location loc : locs)
            res+=loc.getSpeed();
        return res/size;
    }

    public double getAltitude(){
        double res=0;
        for(Location loc : locs)
            res+=loc.getAltitude();
        return res/size;
    }

    public double getBearing(){
        double res=0;
        for(Location loc : locs)
            res+=loc.getBearing();
        return res/size;
    }

    public double getAccuracy(){
        double res=0;
        for(Location loc : locs)
            res+=loc.getAccuracy();
        return res/size;
    }

    public double getTime(){
        double res=0;
        for(Location loc : locs)
            res+=loc.getTime();
        return res/size;
    }

    public double getBearingAccuracyDegrees(){
        double res=0;
        for(Location loc : locs)
            res+=loc.getBearingAccuracyDegrees();
        return res/size;
    }

    public double getSpeedAccuracyMetersPerSecond(){
        double res=0;
        for(Location loc : locs)
            res+=loc.getSpeedAccuracyMetersPerSecond();
        return res/size;
    }

    public double getVerticalAccuracyMeters(){
        double res=0;
        for(Location loc : locs)
            res+=loc.getVerticalAccuracyMeters();
        return res/size;
    }
}
