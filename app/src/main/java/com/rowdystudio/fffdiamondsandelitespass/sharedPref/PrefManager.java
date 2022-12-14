package com.rowdystudio.fffdiamondsandelitespass.sharedPref;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by hsn on 28/11/2017.
 */


public class PrefManager {

    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;

    // Shared preferences file name
    private static final String PREF_NAME = "scracth_and_win";


    public PrefManager(Context context) {
        // shared pref mode
        int PRIVATE_MODE = 0;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setBoolean(String PREF_NAME, Boolean val) {
        editor.putBoolean(PREF_NAME, val);
        editor.commit();
    }



    public void setInt(String PREF_NAME, int VAL) {
        editor.putInt(PREF_NAME, VAL);
        editor.commit();
    }

    public boolean getBoolean(String PREF_NAME) {
        return pref.getBoolean(PREF_NAME, true);
    }

    public void remove(String PREF_NAME) {
        if (pref.contains(PREF_NAME)) {
            editor.remove(PREF_NAME);
            editor.commit();
        }
    }
    public void setString(String PREF_NAME, String VAL) {
        editor.putString(PREF_NAME, VAL);
        editor.commit();
    }
    public String getString(String PREF_NAME) {
        if (pref.contains(PREF_NAME)) {
            return pref.getString(PREF_NAME, null);
        }
        return "";
    }


    public void setCount(String PREF_NAME, int VALUE) {
        editor.putInt(PREF_NAME, VALUE);
        editor.apply();
    }

    public int getCount(String PREF_NAME) {
        if (pref.contains(PREF_NAME)) {
            return pref.getInt(PREF_NAME, 0);
        }
        return 0;
    }

    public int getInt(String key) {
        if (pref.contains(key)) {
            return pref.getInt(key, 0);
        }
        return 0;
    }
}
