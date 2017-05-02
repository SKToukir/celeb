package com.vumobile.Config;

/**
 * Created by toukirul on 9/4/2017.
 */

public class Api {

    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";
    public static final String IMAGE_DIRECTORY_NAME = "Android File Upload";

    public static final String URL_ACTIVATE_USERS = "http://wap.shabox.mobi/testwebapi/celebrity/RegisteredCelebrity?key=m5lxe8qg96K7U9k3eYItJ7k6kCSDre";

    /*Celeb online notification*/
    public static final String URL_ONLINE_USERS = "http://wap.shabox.mobi/testwebapi/celebrity/LiveCelebrity?MSISDN=";
    public static final String URL_ONLINE_KEY = "&key=m5lxe8qg96K7U9k3eYItJ7k6kCSDre";

    public static final String AUTH_KEY = "bTVseGU4cWc5Nks3VTlrM2VZSXRKN2s2a0NTRHJlOkNlbGViQXBw";

    public static final String CELEB_ID_NOTIFICATION= "ID";
    public static final String CELEB_USERNAME_NOTIFICATION= "UserName";
    public static final String CELEB_NAME_NOTIFICATION= "Name";
    public static final String CELEB_MSISDN_NOTIFICATION= "MSISDN";
    public static final String CELEB_COUNTRY_NOTIFICATION= "Country";
    public static final String CELEB_CELEBID_NOTIFICATION= "Celeb_id";
    public static final String CELEB_EMAIL_NOTIFICATION= "email";
    public static final String CELEB_DOB_NOTIFICATION= "dob";
    public static final String CELEB_GENDER_NOTIFICATION= "gender";
    public static final String CELEB_IMAGE_URL_NOTIFICATION= "Image_url";
    public static final String CELEB_REG_STATUS_NOTIFICATION= "Reg_status";
    public static final String CELEB_FB_LOGIN_STATUS_NOTIFICATION= "Fb_login_status";
    public static final String CELEB_REGID_STATUS_NOTIFICATION= "RegId";
    public static final String CELEB_LIVE_STATUS_NOTIFICATION= "Live_status";
    /*------------------------------------------------------------------*/

    /*Get single celeb name and msisdn*/
    public static final String URL_GET_SINGLE_CELEB = "http://wap.shabox.mobi/testwebapi/celebrity/CelebrityInformation?key=m5lxe8qg96K7U9k3eYItJ7k6kCSDre&MSISDN=";
    public static final String URL_GET_SINGLE_CELEB_NAME = "Name";
    public static final String URL_GET_SINGLE_CELEB_PHONE = "MSISDN";




    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;
    public static final String URL_CHECK_CELEB_REG = "http://wap.shabox.mobi/testwebapi/celebrity/status?msisdn=";


    public static final String SHARED_PREF = "ah_firebase";
    public static final String URL_SAVE_CELEB_DATA= "http://wap.shabox.mobi/testwebapi/celebrity/CelebrityRegistration?key=m5lxe8qg96K7U9k3eYItJ7k6kCSDre";
    public static final String URL_SAVE_FAN_DATA= "http://wap.shabox.mobi/testwebapi/celebrity/FanRegistration?key=m5lxe8qg96K7U9k3eYItJ7k6kCSDre";


    public static final String URL_PARENT_SAVE_CELEB = "http://vumobile.biz/Toukir/celeb_comment/savebroadcastdata.php?vid=";
    public static final String URL_CELEB_NAME_SAVE_CELEB = "&celeb_name=";
    public static final String URL_GET_VID = "http://vumobile.biz/Toukir/celeb_comment/getVid.php?room_name=";
    public static final String URL_CELEBRITY = "http://wap.shabox.mobi/GCMPanel/ClubzAPI.aspx?cat=picture";
    public static final String URL_OTP_REQUEST= "http://wap.shabox.mobi/testwebapi/celebrity/RequestOtp?key=m5lxe8qg96K7U9k3eYItJ7k6kCSDre";
    public static final String URL_POST_LIVE = "http://wap.shabox.mobi/testwebapi/celebrity/LiveStatus?key=m5lxe8qg96K7U9k3eYItJ7k6kCSDre";

    // Celeb Api
    public static final String CELEB_NAME = "Name";
    public static final String CELEB_IMAGE = "Image_url";
    public static final String CELEB_CODE_MSISDN = "MSISDN";


}
