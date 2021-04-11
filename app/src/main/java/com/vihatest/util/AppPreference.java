package com.vihatest.util;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreference {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private static final String PREF_NAME = "MyPreference";
    private static final String USERID = "userId";
    private static final String APPLICANTUSERID = "applicantuserId";
    private static final String NAME = "firstName";
    private static final String LASTNAME = "lastName";
    private static final String EMAIL = "email";
    private static final String MOBILE = "mobile";
    private static final String PASSWORD = "password";
    private static final String PROFILE_TYPE = "profile_type";
    public AppPreference(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void clearPreference() {
        editor.clear().commit();
    }

    public void setUserId(Integer id) {
        editor.putInt(USERID, id).commit();
    }
    public void setApplicantUserId(Integer id) {
        editor.putInt(APPLICANTUSERID, id).commit();
    }
    public void setFirstName(String firstname) {
        editor.putString(NAME, firstname).commit();
    }

    public void setLastName(String lastname) {
        editor.putString(LASTNAME, lastname).commit();
    }

    public void setEmail(String email) {
        editor.putString(EMAIL, email).commit();
    }

    public void setPhone(String phone) {
        editor.putString(MOBILE, phone).commit();
    }

    public void setProfileType(int profiletype) {
        editor.putInt(PROFILE_TYPE, profiletype).commit();
    }


    public void setPassword(String password) {
        editor.putString(PASSWORD, password).commit();
    }
    public int getUserid() {
        return pref.getInt(USERID, 0);
    }
    public int getApplicantId() {
        return pref.getInt(APPLICANTUSERID, 0);
    }
}
