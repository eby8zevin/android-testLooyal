package com.ahmadabuhasan.looyal.utils;

import static com.ahmadabuhasan.looyal.utils.Constant.DATA_NAME;
import static com.ahmadabuhasan.looyal.utils.Constant.KEY_DATE_REGISTER;
import static com.ahmadabuhasan.looyal.utils.Constant.KEY_LOGIN;
import static com.ahmadabuhasan.looyal.utils.Constant.KEY_PASSWORD;
import static com.ahmadabuhasan.looyal.utils.Constant.KEY_TEXT_AREA;
import static com.ahmadabuhasan.looyal.utils.Constant.KEY_USERNAME;
import static com.ahmadabuhasan.looyal.utils.Constant.PREFS_NAME;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setBooleanPref(String booleanPref, Boolean value) {
        editor.putBoolean(booleanPref, value);
        editor.apply();
    }

    public void setStringPref(String stringPref, String value) {
        editor.putString(stringPref, value);
        editor.apply();
    }

    public void setTextArea(String textArea, String value) {
        editor.putString(textArea, value);
        editor.apply();
    }

    public Boolean getIsLogin() {
        return sharedPreferences.getBoolean(KEY_LOGIN, false);
    }

    public String getUsername() {
        return sharedPreferences.getString(KEY_USERNAME, "");
    }

    public String getPassword() {
        return sharedPreferences.getString(KEY_PASSWORD, "");
    }

    public String getTextArea() {
        return sharedPreferences.getString(KEY_TEXT_AREA, null);
    }

    public String getDateRegister() {
        return sharedPreferences.getString(KEY_DATE_REGISTER, "");
    }

    public String getCityName() {
        return sharedPreferences.getString(DATA_NAME, "");
    }

    public void getClearData() {
        editor.clear().apply();
    }
}
