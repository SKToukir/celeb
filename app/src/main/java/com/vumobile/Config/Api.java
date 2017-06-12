package com.vumobile.Config;

/**
 * Created by toukirul on 9/4/2017.
 */

public class Api {


    public static final String URL_SET_REG_ID = "http://wap.shabox.mobi/testwebapi/Fan/RegIdCheck?key=m5lxe8qg96K7U9k3eYItJ7k6kCSDre";
    public static final String URL_DELETE_POST = "http://wap.shabox.mobi/testwebapi/Post/DeletePost?key=m5lxe8qg96K7U9k3eYItJ7k6kCSDre&id=";
    public static final String URL_EDIT_POST = "http://wap.shabox.mobi/testwebapi/Post/EditPost?key=m5lxe8qg96K7U9k3eYItJ7k6kCSDre";
    // this is a post api. takes flags and MSISDN. flags = 0 means he/she is fan and when 1 then he/she is celeb
    public static final String URL_GET_SCHEDULES = "http://wap.shabox.mobi/testwebapi/Notification/Schedule?key=m5lxe8qg96K7U9k3eYItJ7k6kCSDre";

    // get celeb posts...
    public static final String URL_CELEB_POSTS = "http://wap.shabox.mobi/testwebapi/Notification/MyPost?key=m5lxe8qg96K7U9k3eYItJ7k6kCSDre";

    // chat and video call request url
    public static final String URL_CHAT_REQUEST = "http://wap.shabox.mobi/testwebapi/Request/SendRequest?key=m5lxe8qg96K7U9k3eYItJ7k6kCSDre";
    public static final String URL_VIDEO_CALL_REQUEST = "";
    public static final String URL_FAN_REQUESTS = "http://wap.shabox.mobi/testwebapi/Request/RequestList?key=m5lxe8qg96K7U9k3eYItJ7k6kCSDre&MSISDN=";
    public static final String URL_REQUESTS_ACCEPT = "http://wap.shabox.mobi/testwebapi/Request/RequestAcceptDeny?key=m5lxe8qg96K7U9k3eYItJ7k6kCSDre";

    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";
    public static final String IMAGE_DIRECTORY_NAME = "Android File Upload";

    public static final String URL_ACTIVATE_USERS = "http://wap.shabox.mobi/testwebapi/celebrity/RegisteredCelebrity?key=m5lxe8qg96K7U9k3eYItJ7k6kCSDre";
    /*Celeb online notification*/
    //public static final String URL_ONLINE_USERS = "http://wap.shabox.mobi/testwebapi/celebrity/LiveCelebrity?MSISDN=";
    public static final String URL_ONLINE_USERS = "http://wap.shabox.mobi/testwebapi/notification/pushnotification?MSISDN=";
    public static final String URL_ONLINE_KEY = "&key=m5lxe8qg96K7U9k3eYItJ7k6kCSDre";
    public static final String CELEB_IS_ONLINE = "http://wap.shabox.mobi/testwebapi/Celebrity/IsLive?MSISDN="; // 0 return offline 1 return online

    public static final String AUTH_KEY = "bTVseGU4cWc5Nks3VTlrM2VZSXRKN2s2a0NTRHJlOkNlbGViQXBw";

    public static final String CELEB_ID_NOTIFICATION= "Celeb_id";
    public static final String CELEB_USERNAME_NOTIFICATION= "UserName";
    public static final String CELEB_NAME_NOTIFICATION= "name";
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
    public static final String NOTIFICATION_FLAGS = "Flags_Notificaton";
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
    public static final String URL_SAVE_FAN_DATA= "http://wap.shabox.mobi/testwebapi/Fan/FanRegistration?key=m5lxe8qg96K7U9k3eYItJ7k6kCSDre";


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

    public static final String URL_SAVE_NOTIFICATION_DATA = "http://wap.shabox.mobi/testwebapi/notification/setNotification?key=m5lxe8qg96K7U9k3eYItJ7k6kCSDre";


    // fan subscribe
    public static final String URL_POST_FOLLOW = "http://wap.shabox.mobi/testwebapi/follower/Follow?key=m5lxe8qg96K7U9k3eYItJ7k6kCSDre";
    public static final String URL_POST_UNFOLLOW = "http://wap.shabox.mobi/testwebapi/Follower/Unfollow?key=m5lxe8qg96K7U9k3eYItJ7k6kCSDre";
    public static final String URL_GET_FOLLOW_CELEB_LIST = "http://wap.shabox.mobi/testwebapi/follower/FollowerList?key=m5lxe8qg96K7U9k3eYItJ7k6kCSDre";
    public static final String URL_GET_MOST_LIVE_CELEB_LIST = "http://wap.shabox.mobi/testwebapi/Celebrity/MostLiveCelebrity?key=m5lxe8qg96K7U9k3eYItJ7k6kCSDre";

    // fan profile
    public static final String URL_GET_CELEB_PROFILE = "http://wap.shabox.mobi/testwebapi/fan/FanInformation?key=m5lxe8qg96K7U9k3eYItJ7k6kCSDre"; //&MSISDN=8801856565865


    // notification
    public static final String URL_GET_ALL_NOTIFICATION_LIST = "http://wap.shabox.mobi/testwebapi/Notification/Notification?key=m5lxe8qg96K7U9k3eYItJ7k6kCSDre";
    public static final String NOTIF_ID = "id";
    public static final String NOTIF_CELEB_NAME = "name";
    public static final String NOTIF_CELEB_MSISDN = "MSISDN";
    public static final String NOTIF_CELEB_ID = "Celeb_id";
    public static final String NOTIF_CELEB_GENDER = "gender";
    public static final String NOTIF_IS_IMAGE = "IsImage"; // 1 IS IMAGE, 2 IS VIDEO
    public static final String NOTIF_FLAG_NOTIF = "Flags_Notificaton";
    public static final String NOTIF_CELEB_PIC_URL = "Image_url";
    public static final String NOTIF_CELEB_POST = "post";
    public static final String NOTIF_TIME = "TimeStamp";
    public static final String NOTIF_LIKE_COUNT = "likeCount";
    public static final String NOTIF_POST_URLS = "Post_Urls";



    // notif like url
    public static final String URL_NOTIFICATION_LIKE_SET_GET = "http://wap.shabox.mobi/testwebapi/Notification/like?key=m5lxe8qg96K7U9k3eYItJ7k6kCSDre"; // post
    // notif count url to show badge
    public static final String URL_NOTIFICATION_COUNTER = "http://wap.shabox.mobi/testwebapi/Notification/NotificationCount?key=m5lxe8qg96K7U9k3eYItJ7k6kCSDre"; // post // &MSISDN=8801682657985
    public static final String NOTIF_MSISDN = "MSISDN";

    // GIFT URL
    public static final String URL_GIFT = "http://wap.shabox.mobi/sticker_app_server/default.aspx?&contentCode=NEW_CONTENT"; // GET





}
