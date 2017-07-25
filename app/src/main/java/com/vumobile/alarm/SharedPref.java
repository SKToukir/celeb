package com.vumobile.alarm;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by toukirul on 23/7/2017.
 */

public class SharedPref {


    static public void SaveList(Context context, ArrayList<String> list) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        Set<String> set = new HashSet<String>();
        set.addAll(list);
        editor.putStringSet("key", set);
        editor.commit();
    }

    static public ArrayList<String> getList(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Set<String> set = new HashSet<String>();
        set = preferences.getStringSet("key", null);

        ArrayList<String> sample = new ArrayList<String>(set);

        return sample;
    }

    static public void clearListShared(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();

        editor.clear();
        editor.commit();
    }

}
