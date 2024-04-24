package com.purple.iptv.player.utils;


import android.text.format.Time;


import com.smartrecording.recordingplugin.app.MyApplication;
import com.xunison.recordingplugin.R;

import java.util.ArrayList;

public class Config {


    public static final String CONNECTION_TYPE_PORTAL = "portal";
    public static final String CONNECTION_TYPE_PLAYLIST = "playlist";
    public static final String BROADCAST_EPG_DOWNLOAD = "com.multiiptv.m3u.xstream.epg_downloaded";


    public static final String MEDIA_TYPE_UNKNOWN = "Unknown";
    public static final String MEDIA_TYPE_LIVE = "live";
    public static final String MEDIA_TYPE_LIVE_FULL_SCREEN = "live_fullscreen";
    public static final String MEDIA_TYPE_EPG = "epg";
    public static final String MEDIA_TYPE_MOVIE = "movie";
    public static final String MEDIA_TYPE_SERIES = "series";
    public static final String MEDIA_TYPE_SEASONS = "seasons";
    public static final String MEDIA_TYPE_EPISODES = "episodes";
    public static final String MEDIA_TYPE_CATCH_UP = "catch_up";
    public static final String MEDIA_TYPE_MULTI_SCREEN = "multi_screen";
    public static final String MEDIA_TYPE_UNIVERSAL_FAVOURITE = "universal_favourite";
    public static final String MEDIA_TYPE_UNIVERSAL_SEARCH = "universal_search";
    public static final String MEDIA_TYPE_UNIVERSAL_HISTORY = "universal_history";
    public static final String ADD_FAVOURITE = "add";
    public static final String REMOVE_FAVOURITE = "remove";
    public static final String ADD_ARCHIVE = "add";
    public static final String REMOVE_ARCHIVE = "remove";
    public static final String RECORDING_PENDING = "Pending...";
    public static final String RECORDING_RECORDING = "Recording...";
    public static final String RECORDING_COMPLETED = "Completed";

    public static final String APP_MODE_XSTREAM = "Xstream";
    public static final String APP_MODE_UNIVERSAL = "Universal";
    //long click
    public static final String LONGCLICKMOVIE = "frommovie";
    public static final String LONGCLICKLIVETV = "fromlivetv";
    public static final String LONGCLICKEPG = "fromepg";
    public static final String LONGCLICKSERIES = "fromseries";


    /*
     *
     * REMOTE CONFIG
     *
     * */


    public static final String KEY_REMOTE_CONFIG_API_KEY = "api_key";
    public static final String KEY_REMOTE_CONFIG_ADS_APP_ID = "ads_app_id";
    public static final String KEY_REMOTE_CONFIG_ADS_INTERSTITIAL = "ads_intrestial";
    public static final String KEY_REMOTE_CONFIG_ADS_BANNER = "ads_banner";
    public static final String KEY_REMOTE_CONFIG_ADS_REWARDED = "ads_rewarded";
    public static final String KEY_REMOTE_CONFIG_SLACK_TOKEN = "slack_token";
    public static final String KEY_REMOTE_CONFIG_REGISTER = "register";
    public static final String KEY_REMOTE_CONFIG_LOGIN = "login";
    public static final String KEY_REMOTE_CONFIG_LIST_M3U_UPDATE = "list_m3u_update";
    public static final String KEY_REMOTE_CONFIG_LIST_XSTREAM_UPDATE = "list_xstream_update";
    public static final String KEY_REMOTE_CONFIG_LIST_GET_LIST = "list_get";
    public static final String KEY_REMOTE_CONFIG_LIST_DELETE = "deleteurl";
    public static final String KEY_REMOTE_CONFIG_UPDATE_M3U_EPG_URL = "list_update_epg";
    public static final String KEY_REMOTE_CONFIG_M3U_PARSER = "m3u_parse";
    public static final String KEY_REMOTE_CONFIG_PACKAGE_NAME = "package_name";
    public static final String KEY_REMOTE_CONFIG_HEADER_KEY = "header_key";
    public static final String KEY_REMOTE_CONFIG_HEADER_VALUE = "header_value";
    public static final String KEY_REMOTE_CONFIG_YANDEX_KEY = "yandex_key";
    public static final String KEY_REMOTE_CONFIG_STARTUP_MSG = "startup_msg";
    public static final String KEY_REMOTE_CONFIG_SHOW_ADS = "ads_status";
    public static final String KEY_REMOTE_CONFIG_VERSION_FORCE_UPDATE = "version_force_update";
    public static final String KEY_REMOTE_CONFIG_VERSION_DOWNLOAD_URL = "version_download_url";
    public static final String KEY_REMOTE_CONFIG_VERSION_DOWNLOAD_URL_APK = "version_download_url_apk";
    public static final String KEY_REMOTE_CONFIG_VERSION_UPDATE_MSG = "version_update_msg";
    public static final String KEY_REMOTE_CONFIG_VERSION_NAME = "version_name";
    public static final String KEY_REMOTE_CONFIG_VERSION_CODE = "version_code";
    public static final String KEY_REMOTE_CONFIG_WEB_PRIVACY_POLICY = "privacy_policy";
    public static final String KEY_REMOTE_CONFIG_PRIVATE_BASE_URL = "private_video_url";
    public static final String KEY_REMOTE_CONFIG_PRIVATE_ACCESS_ON = "private_access";
    public static final String KEY_REMOTE_CONFIG_DOMAIN_URL = "domain_url";
    public static final String KEY_REMOTE_CONFIG_PORTAL_URL = "portal_url";
    public static final String KEY_REMOTE_CONFIG_IMDB_API = "imdb_api";
    public static final String KEY_REMOTE_CONFIG_IMDB_IMAGE_API = "image_imdb";
    public static final String KEY_REMOTE_CONFIG_TRAKT_API_KEY = "trakt_api_key";
    public static final String KEY_REMOTE_CONFIG_PLAYER_FOR_LIVE = "playerForLiveTv";
    public static final String KEY_REMOTE_CONFIG_PLAYER_FOR_SERIES = "playerForSeries";
    public static final String KEY_REMOTE_CONFIG_PLAYER_FOR_MOVIE = "playerForMovie";
    public static final String KEY_REMOTE_CONFIG_PLAYER_FOR_EPG = "playerForEpg";
    public static final String KEY_REMOTE_CONFIG_PLAYER_FOR_PRIME_VIDEO = "playerForPrimeVideo";
    public static final String KEY_REMOTE_CONFIG_VPN_URL = "vpn_url";
    public static final String KEY_REMOTE_CONFIG_VPN_GATE_URL = "vpngate";
    public static final String KEY_REMOTE_CONFIG_VPN_GATE_ID = "vpn_id";
    public static final String KEY_REMOTE_CONFIG_VPN_USER_NAME = "vpn_username";
    public static final String KEY_REMOTE_CONFIG_VPN_PASSWORD = "vpn_password";


    public static final String KEY_REMOTE_4K_URL = "content_4k";
    public static final String KEY_REMOTE_4K_GDRIVE_API_KEY = "g_api_key";
    public static final String KEY_REMOTE_4k_ALLOW = "allow_4k";
    public static final String KEY_REMOTE_CONFIG_VPN_ON = "vpn";
    public static final String KEY_REMOTE_APP_LOGO = "app_logo";
    public static final String KEY_REMOTE_APP_TV_BANNER = "app_tv_banner";
    public static final String KEY_REMOTE_APP_MOBILE_ICON = "app_mobile_icon";
    public static final String KEY_REMOTE_SPLASH_IMAGE = "splash_image";
    public static final String KEY_REMOTE_ALLOW_CAST = "allow_cast";


    /*
     *
     * SETTINGS CONSTANT
     *
     * */
//    public static String SETTINGS_EXTERNAL_PLAYERS = MyApplication.getContext().getString(R.string.settings_external_player);
//    public static String SETTINGS_PLAYER_SELECTIONS = MyApplication.getContext().getString(R.string.settings_player_selection);
//    public static String SETTINGS_PARENTAL_CONTROL = MyApplication.getContext().getString(R.string.settings_parental_control);
//    public static String SETTINGS_ACCOUNT_INFO = MyApplication.getContext().getString(R.string.settings_acc_info);
//    public static String SETTINGS_STREAM_FORMATS = MyApplication.getContext().getString(R.string.settings_stream_format);
//    public static String SETTINGS_PRIVACY_POLICY = MyApplication.getContext().getString(R.string.settings_privacy_policy);
    public static String SETTINGS_DEFAULT_PLAYER = "Built-in Player";
    public static String SETTINGS_IJK_PLAYER = "IJK Player";
    public static String SETTINGS_EXO_PLAYER = "Exo Player";
//    public static String SETTINGS_12_hour_Format = MyApplication.getContext().getString(R.string.setting_12_hour);
    public static String SETTINGS_24_hour_format = MyApplication.getContext().getString(R.string.setting_24_hour);
    //public static String SETTINGS_TIMEZONE ="Asia/Kolkata";
    public static String SETTINGS_TIMEZONE = getsystimezone();//TimeZone.getDefault().getID();

    private static String getsystimezone() {
        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        return today.timezone;
    }

//    public static String SETTINGS_VPN = MyApplication.getContext().getString(R.string.settings_vpn);
//    public static String SETTINGS_VPN_OPEN = MyApplication.getContext().getString(R.string.settings_free_vpn_2);
//    public static String SETTINGS_VPN_HYDRA = MyApplication.getContext().getString(R.string.settings_free_vpn_1);
//    public static String SETTINGS_REMOVE_ADS = MyApplication.getContext().getString(R.string.settings_upgrade_to_premium);
//    public static String SETTINGS_CLEAR_CATCH = MyApplication.getContext().getString(R.string.settings_clear_catch);
//    public static String SETTINGS_CHANGE_LANGUAGE = MyApplication.getContext().getString(R.string.settings_change_language);
//    public static String SETTINGS_DEFAULT_STREAM_TYPE = MyApplication.getContext().getString(R.string.settings_default);
//    public static String SETTINGS_ABOUT_US = MyApplication.getContext().getString(R.string.settings_about_us);
//    public static String SETTINGS_SUPPORT_US = MyApplication.getContext().getString(R.string.settings_support_us);
//    public static String SETTINGS_CHECK_UPDATE = MyApplication.getContext().getString(R.string.settings_check_update);
//    public static String SETTINGS_SHARE_SCREEN = MyApplication.getContext().getString(R.string.settings_share_screen);
//    public static String SETTINGS_WEBSITE = MyApplication.getContext().getString(R.string.settings_web);
//    public static String SETTINGS_TIME_FORMAT = MyApplication.getContext().getString(R.string.settings_time_format);
//    public static String SETTINGS_SPEED_TEST = MyApplication.getContext().getString(R.string.settings_speed_test);
//    public static String SETTINGS_GENERAL_SETTINGS = MyApplication.getContext().getString(R.string.settings_general_setting);
//    public static String SETTINGS_DEVICE_TYPE = MyApplication.getContext().getString(R.string.settings_device_type);
//    public static String SETTINGS_REQUEST_US = MyApplication.getContext().getString(R.string.settings_request_us);
//    public static String SETTINGS_APP_LIST = MyApplication.getContext().getString(R.string.settings_app_list);
//    public static String SETTINGS_APP_PRIVATE_MENU = MyApplication.getContext().getString(R.string.settings_private_menu);
//    public static String SETTINGS_REFRESH_DATA = MyApplication.getContext().getString(R.string.settings_refresh_data);

    public static boolean SHOW_CONSTANT = false;
    public static boolean ShowLog = false;
    public static int RECENT_ITEM_NUMBER = 5;
    public static final long REWARDED_AD_DURATION = 5000;
    public static final String NOTIFICATION_CHANNEL_ID = "com.purple.ip_tv";
    public static final String PUSH_NOTIFICATION = "pushNotification";
    public static int SHORT_EPG_COUNT = 4;
    public static String DOMAIN_URL = "https://hiboss.b-cdn.net";
    public static String PARSE_URL = "http://vithani.org/api/m3uparse/simple.php?url=";
    public static final String EXTRA_PARAM = "/api/";
    public static String M3U_PARSE_URL = "http://vithani.org/api/m3uparse/m3u.php";
    public static String BASE_URL = DOMAIN_URL + EXTRA_PARAM;
    public static String API_KEY = "cda11uT8cBLzm6a1YvsiUWOEgrFowk95K2DM3tHAPRCX4ypGjN";
    public static ArrayList<Object> genreArray = new ArrayList<>();
    public static ArrayList<Object> movieYearArray = new ArrayList<>();
    public static ArrayList<Object> seriesYearArray = new ArrayList<>();
    public static final String CATEGORY_IMAGE_BASE = "https://mindgeek.in/upload/category/";
    public static final String CHANNEL_IMAGE_BASE = "https://mindgeek.in/upload/";
    public static final String XSTREAM_CONST_PART = "/player_api.php";
    public static final String XSTREAM_EPG_CONST_PART = "/xmltv.php?";
    public static final String XSTREAM_LIVE_STREAM_CONST = "get_live_streams";
    public static final String XSTREAM_LIVE_CATEGORY_CONST = "get_live_categories";
    public static final String XSTREAM_MOVIE_STREAM_CONST = "get_vod_streams";
    public static final String XSTREAM_MOVIE_CATEGORY_CONST = "get_vod_categories";
    public static final String XSTREAM_LIVE_SERIES_CONST = "get_series";
    public static final String XSTREAM_SERIES_CATEGORY_CONST = "get_series_categories";
    public static final String XSTREAM_GET_EPG = "get_short_epg";
    public static final String XSTREAM_INFO_VOD = "get_vod_info";
    public static final String XSTREAM_INFO_SERIES = "get_series_info";
    public static final String XSTREAM_SIMPLE_DATA_TABLE = "get_simple_data_table";
    public static final String M3U_UNCATEGORIESD = "get_ucategorised";
    public static final String CATCH_UP = "catch_up";
    public static final String MULTI_SCREEN = "multi_screen";
    public static final String RECORDING = "recording";
    public static final String SCHEDULE_RECORDING = "schedule_recording";
    public static final String PRIVATE_MEDIA = "private_media";
    public static final String PRIVATE_NEW_RELEASE = "get_posts";
    public static final String PRIVATE_TV_SERIES = "get_category_index";
    public static final String PRIVATE_MOVIES = "get_movie_index";
    public static final String PRIVATE_SEARCH = "get_search_results";
    public static final String PRIVATE_MOVIE_FILTER = "get_movie_indexv2";
    public static final String PRIVATE_SERIES_FILTER = "get_category_indexv2";
    public static final String PRIVATE_GET_RECENTLY_PLAYED = "get_recently_played";
    public static final String PRIVATE_GET_FAVOURITES = "get_favourite";
    public static final String PRIVATE_FILTER = "get_filter";
    public static final String PRIVATE_4K = "get_4k";
    public static final String PRIVATE_POST_PER_SEASON_SERIES = "get_category_postsperseasonv2";
    public static final String PRIVATE_GET_CATEGORY_POST_MOVIE = "get_category_posts";
    public static final String PRIVATE_GET_GENRE_LIST = "get_genre_listv2";
    public static final String PRIVATE_GET_MOVIE_YEAR_LIST = "get_movie_yearlistv2";
    public static final String PRIVATE_GET_SERIES_YEAR_LIST = "get_category_yearlistv2";
    public static final String REQUEST_US = "get_category_yearlistv2";

    public static final String SEASONS = "Seasons";
    public static final String EPISODES = "Episodes";

    public static final String LIVE_TYPE_M3U = "live_m3u";
    public static final String LIVE_TYPE_XSTREAM = "live_xstream";
    public static final String LIVE_UNCATEGORISED = "UNCATEGORISED";

    public static final String CHOICE_DEFAULT_NAME = "IPTV Service";
    public static final String APP_YPE_CHOICE = "Choice IPTV";
    public static final String APP_TYPE_PURPLE = "Purple IPTV";
    public static final String APP_TYPE_MASTERMIND = "Mastermind IPTV";
    public static final String APP_TYPE_PURPLE_SMART_TV_PRO = "Purple Smart TV Pro";
    public static final String APP_TYPE_CHALLENGE = "Challenge IPTV";
    public static final String APP_TYPE_TEQIQ = "Teqiq IPTV";
    public static final String APP_TYPE_BOX = "Box TV";

    public static final String MASTERMIND_DEFAULT_NAME = "Mastermind";

    public static final int SORT_BY_DEFAULT = 1;
    public static final int SORT_BY_LATEST = 2;
    public static final int SORT_BY_A_Z = 3;
    public static final int SORT_BY_Z_A = 4;


    public static final int UPDATE_BY_PLAYSTORE = 1;
    public static final int UPDATE_BY_APK = 2;

    public static final String APP_LOGO = "app_logo";
    public static final String APP_MOBILE_ICON = "app_mobile_icon";
    public static final String APP_TV_BANNER = "app_tv_banner";
    public static final String APP_SPLASH = "app_splash";
    public static final String APP_BACK_IMAGE = "app_back_image";
    // for mqtt key
    public static final String BUTTERFLY_E = "mqtt_endpoint";
    public static final String BUTTERFLY_S = "mqtt_server";

    // for ticker dashbord_ticker
    public static final String KEY_DASHBOARDTICKER = "dashbord_ticker";
    // for login ticker
    public static final String KEY_LOGINTICKER = "login_ticker";
    // for recent n default play when app start
    public static final String KEY_DEFAULTPLAY = "set_default_play";
    public static final String KEY_RECENTPLAY = "set_recent_play";

    //for cloud fav/recent save
    public static final String KEY_CLOUDRECENTFAV = "cloud_recent_fav";
    public static final String KEY_CLOUDAPIURL = "cloud_recent_fav_url";
    // for reminder
    public static final String KEY_REMINDME = "remind_me";
    // for cloud recording
    public static final String KEY_CLOUDRECORDING = "cloud_recording";
    //reminder channel status
    public static final String Reminderpending = "pending";
    public static final String Reminderwatched = "watched";
    public static final String Remindermissed = "missed";
    public static final String Reminderdismissed = "dismissed";
    public static final String Reminderrecord = "recordnow";


    public static final String YOUTUBE_KEY = "AIzaSyDAPxS5SxOvPqH-sBtifqIg8TgRKSf4c74";
    public static final int ADS_MARGIN = 4;

    public static String regExp_dateTime_24hour = "(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/(20[1-9][0-9]) ([01]?[0-9]|2[0-3]):([0-5][0-9])";

    public static String getRegExp_dateTime_12hour = "(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/(20[1-9][0-9]) ([01]?[0-9]|2[0-3]):([0-5][0-9]) ([ap]m)";
    public static boolean is24_7selected = false;
    // for broadcast receiver speed
    public static String _downloadstatus = "downloadstatus";
    public static String RECORDING_FILENAME = "r_filename";
    public static String _REMAININGTIIMESTR = "remainingtimestr";
    public static String _REMAININGTIIME = "remainingtime";
    public static String _downloadspeed = "downloadspeed";
    public static String MY_TRIGGER = "trigger";


}

