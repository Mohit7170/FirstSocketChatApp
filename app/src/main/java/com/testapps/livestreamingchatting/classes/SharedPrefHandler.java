package com.testapps.livestreamingchatting.classes;

import android.content.Context;
import android.content.SharedPreferences;

import com.testapps.livestreamingchatting.utils.Params;

public class SharedPrefHandler implements Params {

    private final SharedPreferences sharedPreferences;

    public SharedPrefHandler(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    public void setIntValue(String key, int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public void setStringValue(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void setBooleanValue(String key, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public String getStringFromSharedPref(String key) {
        return sharedPreferences.getString(key, "");
    }

    public boolean getBooleanFromSharedPref(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public int getIntFromSharedPref(String key) {
        return sharedPreferences.getInt(key, -1);
    }

    public boolean hasKey(String key) {
        return sharedPreferences.contains(key);
    }

}

