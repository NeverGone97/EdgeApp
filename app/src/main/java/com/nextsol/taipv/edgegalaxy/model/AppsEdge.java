package com.nextsol.taipv.edgegalaxy.model;

import android.graphics.Bitmap;

public class AppsEdge {
private String nameApp;
private Bitmap icon;
private int delete;
    public AppsEdge(String nameApp, Bitmap icon) {
        this.nameApp = nameApp;
        this.icon = icon;
    }

    public AppsEdge(String nameApp, Bitmap icon, int delete) {
        this.nameApp = nameApp;
        this.icon = icon;
        this.delete = delete;
    }

    public AppsEdge(Bitmap icon) {
        this.icon = icon;
    }

    public AppsEdge() {
    }

    public String getNameApp() {
        return nameApp;
    }

    public void setNameApp(String nameApp) {
        this.nameApp = nameApp;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    public int getDelete() {
        return delete;
    }

    public void setDelete(int delete) {
        this.delete = delete;
    }
}
