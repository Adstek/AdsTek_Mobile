package com.adstek.util;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import com.adstek.data.remote.models.auth.RegisterUserModel;
import com.google.gson.Gson;

import java.io.File;


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

    public RegisterUserModel getUserModel() {
        String email = mSharedPreferences.getString(Constants.KEY_EMAIL, "");
        String gender = mSharedPreferences.getString(Constants.KEY_GENDER, "");
        String firstName = mSharedPreferences.getString(Constants.KEY_FIRST_NAME, "");
        String lastName = mSharedPreferences.getString(Constants.KEY_LAST_NAME, "");
        String phoneNumber = mSharedPreferences.getString(Constants.KEY_PHONE_NUMBER, "");
        String nationality = mSharedPreferences.getString(Constants.KEY_NATIONALITY, "");
        String numberPlate = mSharedPreferences.getString(Constants.KEY_NUMBER_PLATE, "");
        String password = mSharedPreferences.getString(Constants.KEY_PASSWORD, "");
        String profileImage = mSharedPreferences.getString(Constants.KEY_PROFILE_IMAGE, "");
        String nationaldType = mSharedPreferences.getString(Constants.KEY_ID_TYPE, "");
        String idImage = mSharedPreferences.getString(Constants.KEY_ID_IMAGE, "");
        String nationaldNumber = mSharedPreferences.getString(Constants.KEY_ID_NUMBER, "");
        RegisterUserModel user = new RegisterUserModel();

        if (!email.isEmpty()) user.setEmail(email);
        if (!gender.isEmpty()) user.setGender(gender);
        if (!phoneNumber.isEmpty()) user.setPhoneNumber(phoneNumber);
        if (!nationaldType.isEmpty()) user.setNationaldType(nationaldType);
        if (!nationality.isEmpty()) user.setNationality(nationality);
        if (!numberPlate.isEmpty()) user.setNumberPlate(numberPlate);
        if (!password.isEmpty()) user.setPassword(password);
        if (!firstName.isEmpty()) user.setFirstName(firstName);
        if (!nationaldNumber.isEmpty()) user.setFirstName(nationaldNumber);
//        if (!idImage.isEmpty()) user.setNationalDImage(idImage);
        if (!lastName.isEmpty()) user.setLastName(lastName);
//        if (!profileImage.isEmpty()) user.setProfileImage(profileImage);

        return user;
    }
}
