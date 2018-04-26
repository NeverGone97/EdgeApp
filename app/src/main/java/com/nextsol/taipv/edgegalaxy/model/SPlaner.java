package com.nextsol.taipv.edgegalaxy.model;

import android.widget.TextView;

public class SPlaner {
    private String Title, Time;
    private int icon;

    public SPlaner(String title, String time, int icon) {
        Title = title;
        Time = time;
        this.icon = icon;
    }

    public String getTitle() {
        return Title;
    }

    public String getTime() {
        return Time;
    }

    public SPlaner(String title, String time) {
        Title = title;
        Time = time;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setTime(String time) {
        Time = time;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
