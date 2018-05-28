package com.nextsol.taipv.edgegalaxy.model;

public class Ring {
    private int icon;
    private int ring;
    private String title;
    private boolean playing;
    private String nameSong;
    public Ring(int icon, int ring, String title, boolean playing, String nameSong) {
        this.icon = icon;
        this.ring = ring;
        this.title = title;
        this.playing = playing;
        this.nameSong = nameSong;
    }

    public boolean isPlaying() {
        return playing;
    }

    public String getNameSong() {
        return nameSong;
    }

    public void setNameSong(String nameSong) {
        this.nameSong = nameSong;
    }

    public boolean getPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getRing() {
        return ring;
    }

    public void setRing(int ring) {
        this.ring = ring;
    }
}
