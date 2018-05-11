package com.nextsol.taipv.edgegalaxy.model;

public class Ring {
    private int icon;
    private int ring;
    private String title;
    private boolean playing;

    public Ring(boolean isPlaying) {
        this.playing = isPlaying;
    }

    public Ring(int icon, int ring, String title) {
        this.icon = icon;
        this.ring = ring;
        this.title = title;
    }

    public Ring(int icon, int ring, String title, boolean isPlaying) {
        this.icon = icon;
        this.ring = ring;
        this.title = title;
        this.playing = isPlaying;
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
