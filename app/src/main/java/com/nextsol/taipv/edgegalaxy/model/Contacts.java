package com.nextsol.taipv.edgegalaxy.model;

import android.graphics.Bitmap;

public class Contacts {
    private Bitmap avatar;
    private String name;
    private String phone;

    public Bitmap getAvatar() {
        return avatar;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public void setAvatar(Bitmap avatar) {
        this.avatar = avatar;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
