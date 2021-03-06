package com.vumobile.fan.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by toukirul on 12/4/2017.
 */

public class Session {

    public static final String MSISDN = "msisdn";
    public static final String GENDER = "gender";
    public static final String CELEB_ID = "celeb_id";
    public static final String VIDEO_CALL_REQUEST = "video_call_request";
    public static String MY_PREFS_NAME = "login_session";
    public static String USER_NAME = "name";
    public static String USER_PHONE = "phone_number";
    public static String CHECK_LOGIN = "false";
    public static String IS_CELEB = "is_celeb";
    public static String FB_PROFILE_PIC_URL= "fb_pf_url";
    public static String FB_PROFILE_NAME= "fb_name";
    public static String REGISTERED_CELEB = "registered_celeb";
    public static String FB_LOGIN_STATUS = "fb_status";

    public static final String MY_COUNTER_NOTIF_PREFERENCE = "counter_pref_name";
    public static final String MY_COUNTER_NOTIF_KEY = "counter_pref_key";

    public void saveData(String uName, String phoneNumber,boolean isCeleb, boolean checkLogin, Context cntx){

        SharedPreferences.Editor editor = cntx.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString(USER_NAME, uName);
        editor.putString(USER_PHONE, phoneNumber);
        editor.putBoolean(CHECK_LOGIN, checkLogin);
        editor.putBoolean(IS_CELEB, isCeleb);
        editor.commit();

    }

    public void saveCelebId (String celebId, Context cntx){

        SharedPreferences.Editor editor = cntx.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString(CELEB_ID, celebId);
        editor.commit();

    }

    public void saveGender (String celebId, Context cntx){

        SharedPreferences.Editor editor = cntx.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString(CELEB_ID, celebId);
        editor.commit();

    }

    public void saveMsisdn (String msisdn, Context cntx){

        SharedPreferences.Editor editor = cntx.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString(MSISDN, msisdn);
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

    public void saveVideoCallRequestStatus(Context context, boolean requestStatus){
        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putBoolean(VIDEO_CALL_REQUEST, requestStatus);
        editor.commit();
    }

    public static boolean isVideoCallrequestAccepted(Context cntx,String requestKey){
        boolean prefs = cntx.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).getBoolean(requestKey,false);
        return prefs;
    }

    public void saveChatRequestStatus(Context context, boolean requestStatus){
        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putBoolean(FB_PROFILE_NAME, requestStatus);
        editor.commit();
    }

    public static boolean isChatrequestAccepted(Context cntx,String requestKey){
        boolean prefs = cntx.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).getBoolean(requestKey,false);
        return prefs;
    }

    public void saveFbLoginStatus(Context cntx, boolean isReg){
        SharedPreferences.Editor editor = cntx.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putBoolean(FB_LOGIN_STATUS, isReg);
        editor.commit();
    }

    public static boolean isFbLogIn(Context cntx,String regKey){
        boolean prefs = cntx.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).getBoolean(regKey,false);
        return prefs;
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
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
        prefs = cntx.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        //SharedPreferences preferences = (SharedPreferences) cntx.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).Edit();
        SharedPreferences.Editor editor = prefs.edit();

        editor.clear();
        editor.commit();
    }

    public static void setNotifShowCounter(Context cntx, int count){
        SharedPreferences sharedpreferences = cntx.getSharedPreferences(MY_COUNTER_NOTIF_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(MY_COUNTER_NOTIF_KEY, count);
        editor.apply();
    }


    public static String fetchCelebId(Context cntx){
        String celebId = cntx.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).getString(CELEB_ID,"no data saved");
        return celebId;
    }

    public static String fetchGender(Context cntx){
        String celebId = cntx.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).getString(GENDER,"no data saved");
        return celebId;
    }

    public static int fetchNotifShowCounter(Context cntx){
        int countNotif = cntx.getSharedPreferences(MY_COUNTER_NOTIF_PREFERENCE, Context.MODE_PRIVATE).getInt(MY_COUNTER_NOTIF_KEY, 0);
        return countNotif;
    }

    public static void clearNotifShowCounter(Context cntx){
        SharedPreferences sharedpreferences = cntx.getSharedPreferences(MY_COUNTER_NOTIF_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.apply();
    }




}
