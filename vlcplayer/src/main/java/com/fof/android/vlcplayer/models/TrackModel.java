package com.fof.android.vlcplayer.models;



public class TrackModel {

    int id;
    String name;
    boolean isCurrentTrack;

    public boolean isCurrentTrack() {
        return isCurrentTrack;
    }

    public void setCurrentTrack(boolean currentTrack) {
        isCurrentTrack = currentTrack;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
