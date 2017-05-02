package com.vumobile.fan.login;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by toukirul on 12/4/2017.
 */

public class Session {

    public static String MY_PREFS_NAME = "login_session";
    public static String USER_NAME = "name";
    public static String USER_PHONE = "phone_number";
    public static String CHECK_LOGIN = "false";
    public static String IS_CELEB = "is_celeb";
    public static String FB_PROFILE_PIC_URL= "fb_pf_url";
    public static String FB_PROFILE_NAME= "fb_name";
    public static String REGISTERED_CELEB = "registered_celeb";

    public void saveData(String uName, String phoneNumber,boolean isCeleb, boolean checkLogin, Context cntx){

        SharedPreferences.Editor editor = cntx.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString(USER_NAME, uName);
        editor.putString(USER_PHONE, phoneNumber);
        editor.putBoolean(CHECK_LOGIN, checkLogin);
        editor.putBoolean(IS_CELEB, isCeleb);
        editor.commit();

    }

    public void saveData(Context cntx, String uName, String phoneNumber,boolean isCeleb, boolean checkLogin,String imgUrl){

        SharedPreferences.Editor editor = cntx.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString(FB_PROFILE_NAME, uName);
        editor.putString(USER_PHONE, phoneNumber);
        editor.putString(FB_PROFILE_PIC_URL, imgUrl);
        editor.putBoolean(CHECK_LOGIN, checkLogin);
        editor.putBoolean(IS_CELEB, isCeleb);
        editor.commit();

    }

    public void saveProfilePicUrl(Context cntx, String pfUrl, String fbName){
        SharedPreferences.Editor editor = cntx.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString(FB_PROFILE_PIC_URL, pfUrl);
        editor.putString(FB_PROFILE_NAME, fbName);
        editor.commit();
    }

    public void saveCelebState(Context cntx, boolean isReg){
        SharedPreferences.Editor editor = cntx.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putBoolean(REGISTERED_CELEB, isReg);
        editor.commit();
    }

    public static boolean isReg(Context cntx,String regKey){
        boolean prefs = cntx.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).getBoolean(regKey,false);
        return prefs;
    }

    public static String retreiveName(Context cntx,String nameKey){
        String prefs = cntx.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).getString(nameKey,"no data saved");
        return prefs;
    }

    public static String retreivePFUrl(Context cntx,String pfKey){
        String prefs = cntx.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).getString(pfKey,"null");
        return prefs;
    }

    public static String retreiveFbName(Context cntx,String fbNameKey){
        String prefs = cntx.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).getString(fbNameKey,"null");
        return prefs;
    }


    public static String retreivePhone(Context cntx,String phoneKey){
        String prefs = cntx.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).getString(phoneKey,"no data saved");
        return prefs;
    }


    public static boolean isLogin(Context cntx,String loginKey){
        boolean prefs = cntx.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).getBoolean(loginKey,false);
        return prefs;
    }
    public static boolean isCeleb(Context cntx,String isCelebKey){
        boolean prefs = cntx.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).getBoolean(isCelebKey,false);
        return prefs;
    }

    public static void clearAllSharedData(Context cntx){
        SharedPreferences preferences = (SharedPreferences) cntx.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        SharedPreferences.Editor editor = preferences.edit();

        editor.clear();
        editor.commit();
    }
}
