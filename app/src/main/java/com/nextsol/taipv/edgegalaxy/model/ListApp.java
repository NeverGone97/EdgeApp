package com.nextsol.taipv.edgegalaxy.model;

public class ListApp {
    public static final String TABLE_NAME = "ListApps";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "nameapp";
    public static final String COLUMN_IMAGE = "imageapp";
    private int id;
    private String nameApp;
    private byte[] imageApp;
    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY"
                    + COLUMN_NAME + " TEXT,"
                    + COLUMN_IMAGE + " BLOB"
                    + ")";

    public ListApp() {
    }

    public ListApp(int id, String nameApp, byte[] imageApp) {
        this.id = id;
        this.nameApp = nameApp;
        this.imageApp = imageApp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameApp() {
        return nameApp;
    }

    public void setNameApp(String nameApp) {
        this.nameApp = nameApp;
    }

    public byte[] getImageApp() {
        return imageApp;
    }

    public void setImageApp(byte[] imageApp) {
        this.imageApp = imageApp;
    }
}
