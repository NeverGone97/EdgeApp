package com.nextsol.taipv.edgegalaxy.model;

import android.graphics.Bitmap;

public class PeopleContact {
private int color;
private String phone;
private String name;
private Bitmap bitmap;
    public PeopleContact(int color) {
        this.color = color;
    }

    public PeopleContact(int color, String phone, String name) {
        this.color = color;
        this.phone = phone;
        this.name = name;
    }

    public PeopleContact(String phone, String name, Bitmap bitmap) {
        this.phone = phone;
        this.name = name;
        this.bitmap = bitmap;
    }

    public PeopleContact(String phone, String name) {
        this.phone = phone;
        this.name = name;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
