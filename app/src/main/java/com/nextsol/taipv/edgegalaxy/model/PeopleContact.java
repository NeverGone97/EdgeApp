package com.nextsol.taipv.edgegalaxy.model;

public class PeopleContact {
private int color;
private long phone;
private String name;

    public PeopleContact(int color) {
        this.color = color;
    }

    public PeopleContact(int color, long phone, String name) {
        this.color = color;
        this.phone = phone;
        this.name = name;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
