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
    public static final String URL_ONLINE_USERS = "http://wap.shabox.mobi/testwebapi/celebrity/LiveCelebrity?MSISDN=8801682657976&key=m5lxe8qg96K7U9k3eYItJ7k6kCSDre";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;
    public static final String URL_CHECK_CELEB_REG = "http://wap.shabox.mobi/testwebapi/celebrity/status?msisdn=";


    public static final String SHARED_PREF = "ah_firebase";
    public static final String URL_SAVE_CELEB_DATA= "http://wap.shabox.mobi/testwebapi/celebrity/CelebrityRegistration?key=m5lxe8qg96K7U9k3eYItJ7k6kCSDre";


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
