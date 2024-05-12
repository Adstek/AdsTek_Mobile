package com.adstek.util;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import com.google.gson.Gson;


public class SharedPref {
    private static Context _mcontext;
    private static SharedPreferences mSharedPreferences;

    public SharedPref(Context _mcontext) {
        this._mcontext = _mcontext;
        mSharedPreferences = this._mcontext.getSharedPreferences("MyPref", 0);
    }

    public void setPref(String title, boolean value) {
        SharedPreferences.Editor ed = mSharedPreferences.edit();
        ed.putBoolean(title, value);
        ed.apply();

    }


    public void setPref(String title, String value) {
        SharedPreferences.Editor ed = mSharedPreferences.edit();
        ed.putString(title, value);
        ed.apply();

    }

    public void setPref(String title, int value) {
        SharedPreferences.Editor ed = mSharedPreferences.edit();
        ed.putInt(title, value);
        ed.apply();

    }

    public <T> void setPref(String title, T value) {
        String jsonValue = new Gson().toJson(value);
        SharedPreferences.Editor ed = mSharedPreferences.edit();
        ed.putString(title, jsonValue);
        ed.apply();
    }

    public void saveUri(String title, Uri value) {
        SharedPreferences.Editor ed = mSharedPreferences.edit();
        ed.putString(title, value.toString());
        ed.apply();

    }

    public boolean getPref(String title, boolean def) {
        return mSharedPreferences.getBoolean(title, def);
    }


    public String getPref(String title, String def) {
        return mSharedPreferences.getString(title, def);
    }

    public int getPref(String title, int def) {
        return mSharedPreferences.getInt(title, def);
    }

    public Uri getUri(String title, String def) {
        return Uri.parse(mSharedPreferences.getString(title, def));
    }

    public <T> Object getObject(String title, Class<T> tClass) {
        String value = mSharedPreferences.getString(title, null);

        return new Gson().fromJson(value, tClass);
    }
}
