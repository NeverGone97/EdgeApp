package com.nextsol.taipv.edgegalaxy.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePre {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    // Context
    private Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "pref";
    private static final String LISTCONTACT = "listContact";
    private static final String LISTAPPS = "listApps";

    public SharePre(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
    public void putString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public String getString(String key) {
        return pref.getString(key, "null");
    }

    public void putBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean getBoolean(String key) {
        return pref.getBoolean(key, false);
    }
    public void saveListApps(String scoreString) {
        editor.putString(LISTAPPS, scoreString);
        editor.commit();
    }
    public void saveListContact(String scoreString) {
        editor.putString(LISTCONTACT, scoreString);
        editor.commit();
    }
    public void removeListContact(){
        editor.remove(LISTCONTACT);
        editor.apply();
        editor.commit();
    }
    public String getListContact() {
        return pref.getString(LISTCONTACT, "NullContact");
    }
    public String getListApps() {
        return pref.getString(LISTAPPS, "NullApps");
    }

    public void putInt(String key, int value) {
        editor.putInt(key,value);
        editor.commit();
    }
    public int getInt(String key){
        return pref.getInt(key,-1);
    }
}