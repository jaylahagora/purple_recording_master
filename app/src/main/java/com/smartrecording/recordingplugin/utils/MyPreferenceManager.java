package com.smartrecording.recordingplugin.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;


import com.google.gson.Gson;
import com.purple.iptv.player.utils.Config;
import com.xunison.recordingplugin.BuildConfig;
import com.xunison.recordingplugin.R;
import com.smartrecording.recordingplugin.model.ColorModel;
import com.smartrecording.recordingplugin.model.RemoteConfigModel;

import java.util.HashSet;
import java.util.Set;

import static com.smartrecording.recordingplugin.utils.Constant.getAndroid11Dir;
import static com.smartrecording.recordingplugin.utils.Constant.isAndroid10_or_Above;


/**
 * Created by Admin on 17-08-2017.
 */

public class MyPreferenceManager {


    private String TAG = MyPreferenceManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;
    SharedPreferences pref_online;
    SharedPreferences pref_custom;
    SharedPreferences pref_xstream_user_info;
    SharedPreferences pref_settings;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Editor for Shared preferences
    SharedPreferences.Editor online_editor;

    // Editor for Shared preferences
    SharedPreferences.Editor custom_editor;
    SharedPreferences.Editor settings_editor;
    SharedPreferences.Editor xstream_user_info_editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = BuildConfig.APPLICATION_ID + "_default";
    // Sharedpref file name
    private static final String PREF_NAME_ONLINE = BuildConfig.APPLICATION_ID + "_online";
    // Sharedpref file name
    private static final String PREF_NAME_SETTINGS = BuildConfig.APPLICATION_ID + "_settings";
    private static final String PREF_NAME_CUSTOM = BuildConfig.APPLICATION_ID + "_custom";
    private static final String PREF_NAME_XSTREAM_USER_INFO = BuildConfig.APPLICATION_ID + "_xstream_info";
    private static final String KEY_CONFIG_SHOW_ADS = "showAds";
    private static final String KEY_CONFIG_IS_SUBSCRIBED = "is_subscribed";
    private static final String KEY_CONFIG_PACKAGE_NAME = "package_name";
    private static final String KEY_CONFIG_GoogleAppAdID = "googleAppAdId";
    private static final String KEY_CONFIG_GoogleInterstitialAdId = "googleInterstitialAdID";
    private static final String KEY_CONFIG_GoogleBannerAdId = "googleBannerAdId";
    private static final String KEY_CONFIG_GoogleRewardAdId = "googleRewardedAdId";
    private static final String KEY_CONFIG_OnlineLogin = "onlineLogin";
    private static final String KEY_CONFIG_OnlineRegister = "onlineRegister";
    private static final String KEY_CONFIG_OnlineAddM3uList = "onlineAddM3uList";
    private static final String KEY_CONFIG_OnlineAddXstreamList = "onlineAddXstreamList";
    private static final String KEY_CONFIG_OnlineGetLIst = "onlineGetList";
    private static final String KEY_CONFIG_OnlineDeleteListItem = "onlineDeleteListItem";
    private static final String KEY_CONFIG_OnlineUpdateM3uEpgUrl = "onlineUpdateM3uEpgUrl";
    private static final String KEY_CONFIG_OnlineHeaderValue = "onlineHeaderValue";
    private static final String KEY_CONFIG_OnlineHeaderKey = "onlineHeaderKey";
    private static final String KEY_CONFIG_YandexKey = "yandexKey";
    private static final String KEY_CONFIG_StartupMsg = "startupMsg";
    private static final String KEY_CONFIG_VersionForceUpdate = "version_force_update";
    private static final String KEY_CONFIG_VersionMessage = "version_message";
    private static final String KEY_CONFIG_VersionUrl = "version_url";
    private static final String KEY_CONFIG_VersionUrlApk = "version_url_apk";
    private static final String KEY_CONFIG_VersionName = "version_name";
    private static final String KEY_CONFIG_VersionCode = "version_code";
    private static final String KEY_CONFIG_WebPrivacyPolicy = "web_privacy_policy";
    private static final String KEY_CONFIG_ImdbApi = "imdb_api";
    private static final String KEY_CONFIG_ImdbImageApi = "imdb_image_api";
    private static final String KEY_CONFIG_TraktApiKey = "trakt_api_key";
    private static final String KEY_CONFIG_DomainUrl = "domain_url";
    private static final String KEY_CONFIG_PortalUrl = "portal_url";
    private static final String KEY_CONFIG_IsPrivateAccessOn = "is_private_access_on";
    private static final String KEY_CONFIG_AppType = "app_type";
    private static final String KEY_CONFIG_ExpiryDate = "expire_Date";
    private static final String KEY_CONFIG_Private4KUrl = "private_4k_url";
    private static final String KEY_CONFIG_Private4kGdrivaApiKey = "private_4k_gdrive_api_key";
    private static final String KEY_CONFIG_Is4kOn = "is_4k_on";
    private static final String KEY_CONFIG_IsVpnOn = "is_vpn_on";
    private static final String KEY_CONFIG_IsCastOn = "is_cast_on";
    private static final String KEY_CONFIG_IS_REMOTE_SUPPORT = "is_remote_support";
    private static final String KEY_CONFIG_VpnUrl = "vpn_url";
    private static final String KEY_CONFIG_VpnGateUrl = "vpn_gate_url";
    private static final String KEY_CONFIG_VpnGateId = "vpn_gate_id";
    private static final String KEY_CONFIG_VpnUSERNAME = "vpn_user_name";
    private static final String KEY_CONFIG_VpnPASSWORD = "vpn_password";
    private static final String KEY_CONFIG_AppImage = "app_image";
    private static final String KEY_CONFIG_AppLogo = "app_logo";
    private static final String KEY_CONFIG_AppTvBanner = "app_tv_banner";
    private static final String KEY_CONFIG_AppBackImage = "back_image";
    private static final String KEY_CONFIG_SplashImage = "splash_image";
    private static final String KEY_CONFIG_AppMobileIcon = "app_mobile_icon";
    private static final String KEY_CONFIG_SlackToken = "slack_token";
    private static final String KEY_CONFIG_SubProductId = "sub_product_id";
    private static final String KEY_CONFIG_SubLicenceKey = "sub_licence_key";
    private static final String KEY_CONFIG_MainConfigUrl = "main_config_url";
    private static final String KEY_CONFIG_AboutName = "about_name";
    private static final String KEY_CONFIG_AboutDescription = "about_description";
    private static final String KEY_CONFIG_AboutDeveloped = "about_developed";
    private static final String KEY_CONFIG_AboutSkype = "about_skype";
    private static final String KEY_CONFIG_AboutWhatsapp = "about_whatsapp";
    private static final String KEY_CONFIG_AboutTelegram = "about_telegram";
    private static final String KEY_CONFIG_SupportEmail = "support_email";
    private static final String KEY_CONFIG_SupportSkype = "support_skype";
    private static final String KEY_CONFIG_SupportWeb = "support_web";
    private static final String KEY_CONFIG_SupportWhatsapp = "support_whatsapp";
    private static final String KEY_CONFIG_SupportTelegram = "support_telegram";
    private static final String KEY_CONFIG_BASE_M3U_TO_JSON = "base_m3u_tojson_converter";
    private static final String KEY_CONFIG_PRIVATE_DOMAIN_URL = "private_domain_url";
    private static final String KEY_CONFIG_SubInAppStatus = "sub_in_app_status";
    private static final String KEY_CONFIG_DEVICE_TYPE = "device_type";
    private static final String KEY_CONFIG_BACKGROUND_COLOR = "background_color";
    private static final String KEY_CONFIG_BACKGROUND_IMAGE_ARRAY = "background_image_array";
    private static final String KEY_CONFIG_BACKGROUND_AUTO_CHANGE = "background_auto_change";
    private static final String KEY_CONFIG_BACKGROUND_MANUAL_CHANGE = "background_manual_change";
    private static final String KEY_CONFIG_SHOW_WIFI = "background_show_wifi";
    private static final String KEY_CONFIG_SHOW_SETTINGS = "background_show_settings";
    private static final String KEY_CONFIG_DNS_LIST = "dns_list";
    private static final String KEY_CONFIG_APP_MODE = "app_mode";
    private static final String KEY_CONFIG_APP_LIST = "background_show_app";
    private static final String KEY_CONFIG_THEME_DEFAULT_LAYOUT = "theme_defult_layout";
    private static final String KEY_CONFIG_THEME_MENU_IMAGE_STATUS = "theme_menu_image_status";
    private static final String KEY_CONFIG_THEME_LIVE_TV = "theme_live_tv";
    private static final String KEY_CONFIG_THEME_EPG_GUIDE = "theme_epg_guide";
    private static final String KEY_CONFIG_THEME_MOVIES = "theme_movies";
    private static final String KEY_CONFIG_THEME_SERIES = "theme_series";
    private static final String KEY_CONFIG_THEME_VPN = "theme_vpn";
    private static final String KEY_CONFIG_THEME_FAVOURITE = "theme_favorite";
    private static final String KEY_CONFIG_THEME_RECORDING = "theme_recording";
    private static final String KEY_CONFIG_THEME_MULTI_SCREEN = "theme_multi_screen";
    private static final String KEY_CONFIG_THEME_CATCHUP = "theme_catchup";
    private static final String KEY_CONFIG_THEME_APP_SETTINGS = "theme_app_settings";
    private static final String KEY_CONFIG_THEME_SYSTEM_SETTINGS = "theme_system_settings";
    private static final String KEY_CONFIG_THEME_SEARCH = "theme_search";
    private static final String KEY_CONFIG_THEME_RECENT = "theme_recent";
    private static final String KEY_CONFIG_THEME_ACCOUNT_INFO = "theme_account_info";
    private static final String KEY_CONFIG_THEME_STREAM_FORMAT = "theme_stream_format";
    private static final String KEY_CONFIG_THEME_PARENTAL_CONTROL = "theme_parent_control";
    private static final String KEY_CONFIG_THEME_PLAYER_SELECTION = "theme_player_selection";
    private static final String KEY_CONFIG_THEME_EXTERNAL_PLAYER = "theme_external_player";
    private static final String KEY_CONFIG_THEME_SHARE_SCREEN = "theme_share_screen";
    private static final String KEY_CONFIG_THEME_TIME_FORMAT = "theme_time_format";
    private static final String KEY_CONFIG_THEME_CLEAR_CATCH = "theme_clear_cache";
    private static final String KEY_CONFIG_THEME_CHANGE_LANGUAGE = "theme_change_language";
    private static final String KEY_CONFIG_THEME_PRIVACY_POLICY = "theme_privacy_policy";
    private static final String KEY_CONFIG_THEME_SUPPORT = "theme_support";
    private static final String KEY_CONFIG_THEME_CHECK_UPDATE = "theme_check_update";
    private static final String KEY_CONFIG_THEME_SPEED_TEST = "theme_speed_test";
    private static final String KEY_CONFIG_THEME_GENERAL_SETTINGS = "theme_genEral_setting";
    private static final String KEY_CONFIG_THEME_DEVICE_TYPE = "theme_device_type";
    private static final String KEY_CONFIG_PRIVATE_MENU = "private_menu";
    private static final String KEY_CONFIG_CLOUD_TIME_SHIFT = "cloud_time_shift";
    private static final String KEY_CONFIG_CLOUD_CATCHUP = "cloud_catchup";
    private static final String KEY_DASHBOARDTICKER = "dashboardticker";
    private static final String KEY_LOGINTICKER = "loginticker";
    private static final String KEY_ONLINE_USERID = "key_online_userid";
    private static final String KEY_APP_TYPE = "key_app_type";
    private static final String KEY_PRIVATE_ACCESS_ON = "key_private_access";
    private static final String KEY_IS_LOGIN_SHOWN = "is_login_shown";
    private static final String KEY_SHOW_INSTRUCTION_DIALOG = "show_instruction_dialog";
    private static final String KEY_STORE_RESPONSE = "store_response";
    private static final String KEY_PREMIUM = "online_user_premium";
    private static final String KEY_CURRENT_RECORDING_STRING_SET = "currently_recording_string_set";

    private static final String KEY_APP_TYPE_CUSTOM = "key_app_type";
    private static final String KEY_SMART_TV_CODE = "key_smart_tv_code";
    private static final String KEY_LAST_REWARDED_AD_TIME = "key_last_rewarded_ad_time";
    private static final String KEY_LANGUAGE_CODE = "key_language_code";
    private static final String KEY_RECORDING_STORAGE_PATH = "key_recording_storage_path";
    private static final String KEY_SHOW_LANGUAGE_SELECTION = "key_show_language_selection";
    private static final String KEY_EXTERNAL_STORAGE_URL = "key_external_storage_uri";
    private static final String KEY_SHOW_DEVICE_LAYOUT_SELECTION = "Device_layout_selection";

    private static final String KEY_SETTINGS_PLAYER_FOR_LIVE_TV = "player_for_livetv";
    private static final String KEY_SETTINGS_PLAYER_FOR_MOVIE = "player_for_movie";
    private static final String KEY_SETTINGS_PLAYER_FOR_SERIES = "player_for_series";
    private static final String KEY_SETTINGS_PLAYER_FOR_EPG = "player_for_epg";
    private static final String KEY_SETTINGS_PLAYER_FOR_CLOUDSHIFT = "player_for_cloudshift";
    private static final String KEY_SETTINGS_PLAYER_FOR_CATCHUP = "player_for_catchup";
    private static final String KEY_SETTINGS_PLAYER_FOR_PRIVATE_MEDIA = "player_for_private_media";
    private static final String KEY_SETTINGS_MULTISCREEN_LAYOUT_DIALOG_SHOW = "multiscreen_layout_dialog";
    private static final String KEY_SETTINGS_DEFAULT_MULTISCREEN_LAYOUT = "multiscreen_default_layout";
    private static final String KEY_SETTINGS_PARENTAL_CONTROL_PASSWORD = "parental_control_password";
    private static final String KEY_SETTINGS_TIME_FORMAT = "time_format";
    private static final String KEY_SETTINGS_TIME_ZONE = "time_zone";
    private static final String KEY_SETTINGS_STREAM_FORMAT = "stream_format";
    private static final String KEY_SETTINGS_START_ON_BOOT_UP = "start_on_boot_up";
    private static final String KEY_SETTINGS_LAYOUT = "wanted_layout";
    private static final String KEY_SETTINGS_AUTO_BACKGROUND = "auto_background";
    private static final String KEY_UNIVERSAL_APP_CODE = "universal_app_code";
    private static final String KEY_BGIMGES = "bgimgs";

    // fore recent play or default play
    private static final String KEY_DEFAULTPLAY = "defaultplay";
    private static final String KEY_RECENTPLAY = "recentplay";
    // for store recent fav on server
    private static final String KEY_CLOUDRECENT_FAV = "cloud_recent_fav";
    private static final String KEY_CLOUDRECENT_URL = "cloud_recent_fav_url";
    //show hide live tv,movies,series
    private static final String KEY_SHOWHIDE_LIVE_TVCATEGORY = "showhide_tv_category";
    private static final String KEY_SHOWHIDE_MOVIECATEGORY = "showhide_movie_category";
    private static final String KEY_SHOWHIDE_SERIESCATEGORY = "showhide_series_category";
    //show hide archive

    private static final String KEY_SHOWHIDE_ARCHIVE_LIVETV = "showhide_archive_livetv";
    private static final String KEY_SHOWHIDE_ARCHIVE_MOVIE = "showhide_archive_movie";
    private static final String KEY_SHOWHIDE_ARCHIVE_SERIES = "showhide_archive_series";
    private static final String KEY_SHOWHIDE_ARCHIVE_EPG = "showhide_archive_epg";
    private static final String KEY_SHOWHIDE_ARCHIVE_CATCHUP = "showhide_archive_catchup";
    //show/hide channel archive
    private static final String KEY_SHOWHIDE_ARCHIVE_LIVETV_CHANNEL = "showhide_archive_livetv_channel";
    //Notification
    private static final String KEY_STOREFIRSTNOTIFICATIONID = "storenotificationid";
    private static final String KEY_STORENOTIFICATIONMODEL = "storenotificationmodel";
    //Store u pass and remember
    private static final String KEY_xtreamUSERNAMEINPREF = "u_nameinprefxtream";
    private static final String KEY_xtreamPASSINPREF = "pass_inprefxtream";
    private static final String KEY_customUSERNAMEINPREF = "u_nameinprefcustom";
    private static final String KEY_customPASSINPREF = "pass_inprefcustom";

    // flag fro login logout

    private static final String KEY_FLAGFORLOGIN = "isautologin";
    // for reminder option
    private static final String KEY_CLOUDREMINDER = "isreminderenable";
    // for cloud rec
    private static final String KEY_CLOUDRECORDING = "cloud_recording";

    //for running task
    private static final String KEY_ISRECORDINGRUNNED = "runrecording";
    private static final String KEY_ISRECORDINGRUNNED2 = "runrecording2";
    private static final String KEY_RUNNEDRECORDTIME = "runrecordingtime";
    private static final String KEY_RUNNEDRECORDTIME2 = "runrecordingtime2";
    // for color model
    private static final String KEY_COLORMODEL = "colormodel";

    public MyPreferenceManager(Context context) {
        this._context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        pref_online = context.getSharedPreferences(PREF_NAME_ONLINE, PRIVATE_MODE);
        pref_custom = context.getSharedPreferences(PREF_NAME_CUSTOM, PRIVATE_MODE);
        pref_settings = context.getSharedPreferences(PREF_NAME_SETTINGS, PRIVATE_MODE);
        pref_xstream_user_info = context.getSharedPreferences(PREF_NAME_XSTREAM_USER_INFO, PRIVATE_MODE);
        editor = pref.edit();
        settings_editor = pref_settings.edit();
        online_editor = pref_online.edit();
        custom_editor = pref_custom.edit();
        xstream_user_info_editor = pref_xstream_user_info.edit();

    }

    public void storeRemoteConfig(RemoteConfigModel model) {
        custom_editor.putBoolean(KEY_CONFIG_SHOW_ADS, model.isShowAds());
        custom_editor.putBoolean(KEY_CONFIG_IS_SUBSCRIBED, model.isIs_subscribed());
        custom_editor.putString(KEY_CONFIG_PACKAGE_NAME, model.getPackage_name());
        custom_editor.putString(KEY_CONFIG_GoogleAppAdID, model.getGoogleAppAdId());
        custom_editor.putString(KEY_CONFIG_GoogleInterstitialAdId, model.getGoogleInterstitialAdID());
        custom_editor.putString(KEY_CONFIG_GoogleBannerAdId, model.getGoogleBannerAdId());
        custom_editor.putString(KEY_CONFIG_GoogleRewardAdId, model.getGoogleRewardedAdId());
        custom_editor.putString(KEY_CONFIG_OnlineLogin, model.getOnlineLogin());
        custom_editor.putString(KEY_CONFIG_OnlineRegister, model.getOnlineRegister());
        custom_editor.putString(KEY_CONFIG_OnlineAddM3uList, model.getOnlineAddM3uList());
        custom_editor.putString(KEY_CONFIG_OnlineAddXstreamList, model.getOnlineAddXstreamList());
        custom_editor.putString(KEY_CONFIG_OnlineGetLIst, model.getOnlineGetList());
        custom_editor.putString(KEY_CONFIG_OnlineDeleteListItem, model.getOnlineDeleteListItem());
        custom_editor.putString(KEY_CONFIG_OnlineUpdateM3uEpgUrl, model.getOnlineUpdateM3uEpgUrl());
        custom_editor.putString(KEY_CONFIG_OnlineHeaderValue, model.getOnlineHeaderValue());
        custom_editor.putString(KEY_CONFIG_OnlineHeaderKey, model.getOnlineHeaderKey());
        custom_editor.putString(KEY_CONFIG_YandexKey, model.getYandexKey());
        custom_editor.putString(KEY_CONFIG_StartupMsg, model.getStartupMsg());
        custom_editor.putBoolean(KEY_CONFIG_VersionForceUpdate, model.isVersion_force_update());
        custom_editor.putString(KEY_CONFIG_VersionMessage, model.getVersion_message());
        custom_editor.putString(KEY_CONFIG_VersionUrl, model.getVersion_url());
        custom_editor.putString(KEY_CONFIG_VersionUrlApk, model.getVersion_url_apk());
        custom_editor.putString(KEY_CONFIG_VersionName, model.getVersion_name());
        custom_editor.putLong(KEY_CONFIG_VersionCode, model.getVersion_code());
        custom_editor.putString(KEY_CONFIG_WebPrivacyPolicy, model.getWeb_privacy_policy());
        custom_editor.putString(KEY_CONFIG_ImdbApi, model.getImdb_api());
        custom_editor.putString(KEY_CONFIG_ImdbImageApi, model.getImdb_image_api());
        custom_editor.putString(KEY_CONFIG_TraktApiKey, model.getTrakt_api_key());
        custom_editor.putString(KEY_CONFIG_DomainUrl, model.getDomain_url());
        custom_editor.putString(KEY_CONFIG_PortalUrl, model.getPortal_url());
        custom_editor.putBoolean(KEY_CONFIG_IsPrivateAccessOn, model.isIs_private_access_on());
        custom_editor.putString(KEY_CONFIG_AppType, model.getApp_type());
        custom_editor.putString(KEY_CONFIG_ExpiryDate, model.getExpire_Date());
        custom_editor.putString(KEY_CONFIG_PRIVATE_DOMAIN_URL, model.getPrivate_domain_url());
        custom_editor.putString(KEY_CONFIG_Private4KUrl, model.getPrivate_4k_url());
        custom_editor.putString(KEY_CONFIG_Private4kGdrivaApiKey, model.getPrivate_4k_gdrive_api_key());
        custom_editor.putBoolean(KEY_CONFIG_Is4kOn, model.isIs_4k_on());
        custom_editor.putBoolean(KEY_CONFIG_IsVpnOn, model.isIs_vpn_on());
        custom_editor.putBoolean(KEY_CONFIG_IsCastOn, model.isIs_cast_on());
        custom_editor.putBoolean(KEY_CONFIG_IS_REMOTE_SUPPORT, model.isIs_remote_support());
        custom_editor.putBoolean(KEY_CONFIG_DEVICE_TYPE, model.isShow_device_type());
        custom_editor.putString(KEY_CONFIG_VpnUrl, model.getVpn_url());
        custom_editor.putString(KEY_CONFIG_VpnGateUrl, model.getVpn_gate_url());
        custom_editor.putString(KEY_CONFIG_VpnGateId, model.getVpn_gate_id());
        custom_editor.putString(KEY_CONFIG_VpnUSERNAME, model.getVpn_user_name());
        custom_editor.putString(KEY_CONFIG_VpnPASSWORD, model.getVpn_password());
        custom_editor.putBoolean(KEY_CONFIG_AppImage, model.isApp_img());
        custom_editor.putString(KEY_CONFIG_AppBackImage, model.getBack_image());
        custom_editor.putString(KEY_CONFIG_AppLogo, model.getApp_logo());
        custom_editor.putString(KEY_CONFIG_AppTvBanner, model.getApp_tv_banner());
        custom_editor.putString(KEY_CONFIG_SplashImage, model.getSplash_image());
        custom_editor.putString(KEY_CONFIG_AppMobileIcon, model.getApp_mobile_icon());
        custom_editor.putString(KEY_CONFIG_SlackToken, model.getSlack_token());
        custom_editor.putString(KEY_CONFIG_SubProductId, model.getSub_product_id());
        custom_editor.putString(KEY_CONFIG_SubLicenceKey, model.getSub_licence_key());
        custom_editor.putBoolean(KEY_CONFIG_SubInAppStatus, model.getSub_in_app_status());
        custom_editor.putString(KEY_CONFIG_MainConfigUrl, model.getMain_config_url());
        custom_editor.putString(KEY_CONFIG_AboutName, model.getAbout_name());
        custom_editor.putString(KEY_CONFIG_AboutDescription, model.getAbout_description());
        custom_editor.putString(KEY_CONFIG_AboutDeveloped, model.getAbout_developed());
        custom_editor.putString(KEY_CONFIG_AboutSkype, model.getAbout_skype());
        custom_editor.putString(KEY_CONFIG_AboutWhatsapp, model.getAbout_whatsapp());
        custom_editor.putString(KEY_CONFIG_AboutTelegram, model.getAbout_telegram());
        custom_editor.putString(KEY_CONFIG_SupportEmail, model.getSupport_email());
        custom_editor.putString(KEY_CONFIG_SupportSkype, model.getSupport_skype());
        custom_editor.putString(KEY_CONFIG_SupportWeb, model.getSupport_web());
        custom_editor.putString(KEY_CONFIG_SupportEmail, model.getSupport_email());
        custom_editor.putString(KEY_CONFIG_SupportWhatsapp, model.getSupport_whatsapp());
        custom_editor.putString(KEY_CONFIG_SupportTelegram, model.getSupport_telegram());
        custom_editor.putString(KEY_CONFIG_BASE_M3U_TO_JSON, model.getBase_m3u_to_json_converter());
        custom_editor.putString(KEY_CONFIG_BACKGROUND_COLOR, model.getBackground_orverlay_color_code());
        custom_editor.putStringSet(KEY_CONFIG_BACKGROUND_IMAGE_ARRAY, model.getImageBackArray());
        custom_editor.putBoolean(KEY_CONFIG_BACKGROUND_AUTO_CHANGE, model.isBackground_auto_change());
        custom_editor.putBoolean(KEY_CONFIG_BACKGROUND_MANUAL_CHANGE, model.isBackground_mannual_change());
        custom_editor.putBoolean(KEY_CONFIG_SHOW_WIFI, model.isShowWIFI());
        custom_editor.putBoolean(KEY_CONFIG_SHOW_SETTINGS, model.isShowSettings());
        custom_editor.putBoolean(KEY_CONFIG_PRIVATE_MENU, model.isPrivate_menu());
        // custom_editor.putStringSet(KEY_CONFIG_DNS_LIST, model.getDnsArray());
        custom_editor.putString(KEY_CONFIG_APP_MODE, model.getApp_mode());
        custom_editor.putBoolean(KEY_CONFIG_APP_LIST, model.isShowAppList());
        custom_editor.putString(KEY_CONFIG_THEME_DEFAULT_LAYOUT, model.getTheme_default_layout());
        custom_editor.putString(KEY_CONFIG_THEME_MENU_IMAGE_STATUS, model.getTheme_menu_image_status());
        custom_editor.putString(KEY_CONFIG_THEME_LIVE_TV, model.getTheme_live_tv());
        custom_editor.putString(KEY_CONFIG_THEME_EPG_GUIDE, model.getTheme_epg_guide());
        custom_editor.putString(KEY_CONFIG_THEME_MOVIES, model.getTheme_movies());
        custom_editor.putString(KEY_CONFIG_THEME_SERIES, model.getTheme_series());
        custom_editor.putString(KEY_CONFIG_THEME_VPN, model.getTheme_vpn());
        custom_editor.putString(KEY_CONFIG_THEME_FAVOURITE, model.getTheme_favorite());
        custom_editor.putString(KEY_CONFIG_THEME_RECORDING, model.getTheme_recording());
        custom_editor.putString(KEY_CONFIG_THEME_MULTI_SCREEN, model.getTheme_multi_screen());
        custom_editor.putString(KEY_CONFIG_THEME_CATCHUP, model.getTheme_catchup());
        custom_editor.putString(KEY_CONFIG_THEME_APP_SETTINGS, model.getTheme_app_settings());
        custom_editor.putString(KEY_CONFIG_THEME_SYSTEM_SETTINGS, model.getTheme_system_settings());
        custom_editor.putString(KEY_CONFIG_THEME_SEARCH, model.getTheme_search());
        custom_editor.putString(KEY_CONFIG_THEME_RECENT, model.getTheme_recent());
        custom_editor.putString(KEY_CONFIG_THEME_ACCOUNT_INFO, model.getTheme_account_info());
        custom_editor.putString(KEY_CONFIG_THEME_STREAM_FORMAT, model.getTheme_stream_format());
        custom_editor.putString(KEY_CONFIG_THEME_PARENTAL_CONTROL, model.getTheme_parent_control());
        custom_editor.putString(KEY_CONFIG_THEME_PLAYER_SELECTION, model.getTheme_player_selection());
        custom_editor.putString(KEY_CONFIG_THEME_EXTERNAL_PLAYER, model.getTheme_external_player());
        custom_editor.putString(KEY_CONFIG_THEME_SHARE_SCREEN, model.getTheme_share_screen());
        custom_editor.putString(KEY_CONFIG_THEME_TIME_FORMAT, model.getTheme_time_format());
        custom_editor.putString(KEY_CONFIG_THEME_CLEAR_CATCH, model.getTheme_clear_cache());
        custom_editor.putString(KEY_CONFIG_THEME_CHANGE_LANGUAGE, model.getTheme_change_language());
        custom_editor.putString(KEY_CONFIG_THEME_PRIVACY_POLICY, model.getTheme_privacy_policy());
        custom_editor.putString(KEY_CONFIG_THEME_SUPPORT, model.getTheme_support());
        custom_editor.putString(KEY_CONFIG_THEME_CHECK_UPDATE, model.getTheme_check_update());
        custom_editor.putString(KEY_CONFIG_THEME_SPEED_TEST, model.getTheme_speed_test());
        custom_editor.putString(KEY_CONFIG_THEME_GENERAL_SETTINGS, model.getTheme_general_setting());
        custom_editor.putString(KEY_CONFIG_THEME_DEVICE_TYPE, model.getTheme_device_type());
        custom_editor.putBoolean(KEY_CONFIG_CLOUD_TIME_SHIFT, model.isCloudTimeShift());
        custom_editor.putBoolean(KEY_CONFIG_CLOUD_CATCHUP, model.isCloudcatchup());
        custom_editor.putBoolean(KEY_DASHBOARDTICKER, model.getDashbord_ticker());
        custom_editor.putBoolean(KEY_LOGINTICKER, model.isLogin_ticker());
        custom_editor.putBoolean(KEY_DEFAULTPLAY, model.isDefault_play());
        custom_editor.putBoolean(KEY_RECENTPLAY, model.isRecent_play());
        // for recent fav add to server
        custom_editor.putString(KEY_CLOUDRECENT_FAV, model.getCloud_recent_fav());
        custom_editor.putString(KEY_CLOUDRECENT_URL, model.getCloud_recent_fav_url());
        //for reminder
        custom_editor.putBoolean(KEY_CLOUDREMINDER, model.getRemind_me());
        //for cloud recording
        custom_editor.putBoolean(KEY_CLOUDRECORDING, model.isCloud_recording());
        custom_editor.commit();
    }

    public RemoteConfigModel getRemoteConfig() {
        RemoteConfigModel model = new RemoteConfigModel();
        model.setShowAds(pref_custom.getBoolean(KEY_CONFIG_SHOW_ADS, true));
        model.setIs_subscribed(pref_custom.getBoolean(KEY_CONFIG_IS_SUBSCRIBED, false));
        model.setPackage_name(pref_custom.getString(KEY_CONFIG_PACKAGE_NAME, ""));
        model.setGoogleAppAdId(pref_custom.getString(KEY_CONFIG_GoogleAppAdID, ""));
        model.setGoogleInterstitialAdID(pref_custom.getString(KEY_CONFIG_GoogleInterstitialAdId, ""));
        model.setGoogleBannerAdId(pref_custom.getString(KEY_CONFIG_GoogleBannerAdId, ""));
        model.setGoogleRewardedAdId(pref_custom.getString(KEY_CONFIG_GoogleRewardAdId, ""));
        model.setOnlineLogin(pref_custom.getString(KEY_CONFIG_OnlineLogin, ""));
        model.setOnlineRegister(pref_custom.getString(KEY_CONFIG_OnlineRegister, ""));
        model.setOnlineAddM3uList(pref_custom.getString(KEY_CONFIG_OnlineAddM3uList, ""));
        model.setOnlineAddXstreamList(pref_custom.getString(KEY_CONFIG_OnlineAddXstreamList, ""));
        model.setOnlineGetList(pref_custom.getString(KEY_CONFIG_OnlineGetLIst, ""));
        model.setOnlineDeleteListItem(pref_custom.getString(KEY_CONFIG_OnlineDeleteListItem, ""));
        model.setOnlineUpdateM3uEpgUrl(pref_custom.getString(KEY_CONFIG_OnlineUpdateM3uEpgUrl, ""));
        model.setOnlineHeaderValue(pref_custom.getString(KEY_CONFIG_OnlineHeaderValue, ""));
        model.setOnlineHeaderKey(pref_custom.getString(KEY_CONFIG_OnlineHeaderKey, ""));
        model.setYandexKey(pref_custom.getString(KEY_CONFIG_YandexKey, ""));
        model.setStartupMsg(pref_custom.getString(KEY_CONFIG_StartupMsg, ""));
        model.setVersion_force_update(pref_custom.getBoolean(KEY_CONFIG_VersionForceUpdate, false));
        model.setVersion_message(pref_custom.getString(KEY_CONFIG_VersionMessage, ""));
        model.setVersion_url(pref_custom.getString(KEY_CONFIG_VersionUrl, ""));
        model.setVersion_url_apk(pref_custom.getString(KEY_CONFIG_VersionUrlApk, ""));
        model.setVersion_name(pref_custom.getString(KEY_CONFIG_VersionName, ""));
        model.setVersion_code(pref_custom.getLong(KEY_CONFIG_VersionCode, 1));
        model.setWeb_privacy_policy(pref_custom.getString(KEY_CONFIG_WebPrivacyPolicy, ""));
        model.setImdb_api(pref_custom.getString(KEY_CONFIG_ImdbApi, ""));
        model.setImdb_image_api(pref_custom.getString(KEY_CONFIG_ImdbImageApi, ""));
        model.setTrakt_api_key(pref_custom.getString(KEY_CONFIG_TraktApiKey, ""));
        model.setDomain_url(pref_custom.getString(KEY_CONFIG_DomainUrl, ""));
        model.setPortal_url(pref_custom.getString(KEY_CONFIG_PortalUrl, ""));
        model.setIs_private_access_on(pref_custom.getBoolean(KEY_CONFIG_IsPrivateAccessOn, false));
        model.setApp_type(pref_custom.getString(KEY_CONFIG_AppType, ""));
        model.setExpire_Date(pref_custom.getString(KEY_CONFIG_ExpiryDate, null));
        model.setPrivate_domain_url(pref_custom.getString(KEY_CONFIG_PRIVATE_DOMAIN_URL, ""));
        model.setPrivate_4k_url(pref_custom.getString(KEY_CONFIG_Private4KUrl, ""));
        model.setPrivate_4k_gdrive_api_key(pref_custom.getString(KEY_CONFIG_Private4kGdrivaApiKey, ""));
        model.setIs_4k_on(pref_custom.getBoolean(KEY_CONFIG_Is4kOn, false));
        model.setIs_vpn_on(pref_custom.getBoolean(KEY_CONFIG_IsVpnOn, true));
        model.setIs_cast_on(pref_custom.getBoolean(KEY_CONFIG_IsCastOn, true));
        model.setShow_device_type(pref_custom.getBoolean(KEY_CONFIG_DEVICE_TYPE, true));
        model.setIs_remote_support(pref_custom.getBoolean(KEY_CONFIG_IS_REMOTE_SUPPORT, true));
        model.setPrivate_menu(pref_custom.getBoolean(KEY_CONFIG_PRIVATE_MENU, false));
        model.setVpn_url(pref_custom.getString(KEY_CONFIG_VpnUrl, ""));
        model.setVpn_gate_url(pref_custom.getString(KEY_CONFIG_VpnGateUrl, ""));
        model.setVpn_gate_id(pref_custom.getString(KEY_CONFIG_VpnGateId, ""));
        model.setVpn_user_name(pref_custom.getString(KEY_CONFIG_VpnUSERNAME, ""));
        model.setVpn_password(pref_custom.getString(KEY_CONFIG_VpnPASSWORD, ""));
        model.setApp_img(pref_custom.getBoolean(KEY_CONFIG_AppImage, false));
        model.setApp_logo(pref_custom.getString(KEY_CONFIG_AppLogo, ""));
        model.setBack_image(pref_custom.getString(KEY_CONFIG_AppBackImage, ""));
        model.setApp_tv_banner(pref_custom.getString(KEY_CONFIG_AppTvBanner, ""));
        model.setSplash_image(pref_custom.getString(KEY_CONFIG_SplashImage, ""));
        model.setApp_mobile_icon(pref_custom.getString(KEY_CONFIG_AppMobileIcon, ""));
        model.setSlack_token(pref_custom.getString(KEY_CONFIG_SlackToken, ""));
        model.setSub_product_id(pref_custom.getString(KEY_CONFIG_SubProductId, ""));
        model.setSub_licence_key(pref_custom.getString(KEY_CONFIG_SubLicenceKey, ""));
        model.setSub_in_app_status(pref_custom.getBoolean(KEY_CONFIG_SubInAppStatus, true));
        model.setMain_config_url(pref_custom.getString(KEY_CONFIG_MainConfigUrl, ""));
        model.setAbout_name(pref_custom.getString(KEY_CONFIG_AboutName, ""));
        model.setAbout_description(pref_custom.getString(KEY_CONFIG_AboutDescription, ""));
        model.setAbout_developed(pref_custom.getString(KEY_CONFIG_AboutDeveloped, ""));
        model.setAbout_skype(pref_custom.getString(KEY_CONFIG_AboutSkype, ""));
        model.setAbout_whatsapp(pref_custom.getString(KEY_CONFIG_AboutWhatsapp, ""));
        model.setAbout_telegram(pref_custom.getString(KEY_CONFIG_AboutTelegram, ""));
        model.setSupport_email(pref_custom.getString(KEY_CONFIG_SupportEmail, ""));
        model.setSupport_skype(pref_custom.getString(KEY_CONFIG_SupportSkype, ""));
        model.setSupport_web(pref_custom.getString(KEY_CONFIG_SupportWeb, ""));
        model.setSupport_whatsapp(pref_custom.getString(KEY_CONFIG_SupportWhatsapp, ""));
        model.setSupport_telegram(pref_custom.getString(KEY_CONFIG_SupportTelegram, ""));
        model.setBase_m3u_to_json_converter(pref_custom.getString(KEY_CONFIG_BASE_M3U_TO_JSON, ""));
        model.setBackground_orverlay_color_code(pref_custom.getString(KEY_CONFIG_BACKGROUND_COLOR, "#000000"));
        model.setImageBackArray(pref_custom.getStringSet(KEY_CONFIG_BACKGROUND_IMAGE_ARRAY, null));
        model.setBackground_auto_change(pref_custom.getBoolean(KEY_CONFIG_BACKGROUND_AUTO_CHANGE, true));
        model.setBackground_mannual_change(pref_custom.getBoolean(KEY_CONFIG_BACKGROUND_MANUAL_CHANGE, true));
        model.setShowWIFI(pref_custom.getBoolean(KEY_CONFIG_SHOW_WIFI, true));
        model.setShowSettings(pref_custom.getBoolean(KEY_CONFIG_SHOW_SETTINGS, true));
        //  model.setDnsArray(pref_custom.getStringSet(KEY_CONFIG_DNS_LIST, null));
        model.setApp_mode(pref_custom.getString(KEY_CONFIG_APP_MODE, ""));
        model.setShowAppList(pref_custom.getBoolean(KEY_CONFIG_APP_LIST, false));
        model.setTheme_default_layout(pref_custom.getString(KEY_CONFIG_THEME_DEFAULT_LAYOUT, ""));
        model.setTheme_menu_image_status(pref_custom.getString(KEY_CONFIG_THEME_MENU_IMAGE_STATUS, ""));
        model.setTheme_live_tv(pref_custom.getString(KEY_CONFIG_THEME_LIVE_TV, ""));
        model.setTheme_epg_guide(pref_custom.getString(KEY_CONFIG_THEME_EPG_GUIDE, ""));
        model.setTheme_movies(pref_custom.getString(KEY_CONFIG_THEME_MOVIES, ""));
        model.setTheme_series(pref_custom.getString(KEY_CONFIG_THEME_SERIES, ""));
        model.setTheme_vpn(pref_custom.getString(KEY_CONFIG_THEME_VPN, ""));
        model.setTheme_favorite(pref_custom.getString(KEY_CONFIG_THEME_FAVOURITE, ""));
        model.setTheme_recording(pref_custom.getString(KEY_CONFIG_THEME_RECORDING, ""));
        model.setTheme_multi_screen(pref_custom.getString(KEY_CONFIG_THEME_MULTI_SCREEN, ""));
        model.setTheme_catchup(pref_custom.getString(KEY_CONFIG_THEME_CATCHUP, ""));
        model.setTheme_app_settings(pref_custom.getString(KEY_CONFIG_THEME_APP_SETTINGS, ""));
        model.setTheme_system_settings(pref_custom.getString(KEY_CONFIG_THEME_SYSTEM_SETTINGS, ""));
        model.setTheme_search(pref_custom.getString(KEY_CONFIG_THEME_SEARCH, ""));
        model.setTheme_recent(pref_custom.getString(KEY_CONFIG_THEME_RECENT, ""));
        model.setTheme_account_info(pref_custom.getString(KEY_CONFIG_THEME_ACCOUNT_INFO, ""));
        model.setTheme_stream_format(pref_custom.getString(KEY_CONFIG_THEME_STREAM_FORMAT, ""));
        model.setTheme_parent_control(pref_custom.getString(KEY_CONFIG_THEME_PARENTAL_CONTROL, ""));
        model.setTheme_player_selection(pref_custom.getString(KEY_CONFIG_THEME_PLAYER_SELECTION, ""));
        model.setTheme_external_player(pref_custom.getString(KEY_CONFIG_THEME_EXTERNAL_PLAYER, ""));
        model.setTheme_share_screen(pref_custom.getString(KEY_CONFIG_THEME_SHARE_SCREEN, ""));
        model.setTheme_time_format(pref_custom.getString(KEY_CONFIG_THEME_TIME_FORMAT, ""));
        model.setTheme_clear_cache(pref_custom.getString(KEY_CONFIG_THEME_CLEAR_CATCH, ""));
        model.setTheme_change_language(pref_custom.getString(KEY_CONFIG_THEME_CHANGE_LANGUAGE, ""));
        model.setTheme_privacy_policy(pref_custom.getString(KEY_CONFIG_THEME_PRIVACY_POLICY, ""));
        model.setTheme_support(pref_custom.getString(KEY_CONFIG_THEME_SUPPORT, ""));
        model.setTheme_check_update(pref_custom.getString(KEY_CONFIG_THEME_CHECK_UPDATE, ""));
        model.setTheme_speed_test(pref_custom.getString(KEY_CONFIG_THEME_SPEED_TEST, ""));
        model.setTheme_general_setting(pref_custom.getString(KEY_CONFIG_THEME_GENERAL_SETTINGS, ""));
        model.setTheme_device_type(pref_custom.getString(KEY_CONFIG_THEME_DEVICE_TYPE, ""));
        model.setCloudTimeShift(pref_custom.getBoolean(KEY_CONFIG_CLOUD_TIME_SHIFT, true));
        model.setCloudcatchup(pref_custom.getBoolean(KEY_CONFIG_CLOUD_CATCHUP, true));
        model.setDashbord_ticker(pref_custom.getBoolean(KEY_DASHBOARDTICKER, false));
        model.setLogin_ticker(pref_custom.getBoolean(KEY_LOGINTICKER, false));
        model.setDefault_play(pref_custom.getBoolean(KEY_DEFAULTPLAY, false));
        model.setRecent_play(pref_custom.getBoolean(KEY_RECENTPLAY, false));
        // for recent fav add to server
        model.setCloud_recent_fav_url(pref_custom.getString(KEY_CLOUDRECENT_URL, ""));
        model.setCloud_recent_fav(pref_custom.getString(KEY_CLOUDRECENT_FAV, ""));
        // for reminder
        model.setRemind_me(pref_custom.getBoolean(KEY_CLOUDREMINDER, false));
        //for cloud recording
        model.setCloud_recording(pref_custom.getBoolean(KEY_CLOUDRECORDING, false));
        return model;
    }

    public void setPlayerForLiveTV(String package_name) {
        settings_editor.putString(KEY_SETTINGS_PLAYER_FOR_LIVE_TV, package_name);
        settings_editor.commit();
    }

//    public String getPlayerForLiveTV() {
//        return pref_settings.getString(KEY_SETTINGS_PLAYER_FOR_LIVE_TV,
//                Config.SETTINGS_DEFAULT_PLAYER);
//    }

    public void setPlayerForMovie(String package_name) {
        settings_editor.putString(KEY_SETTINGS_PLAYER_FOR_MOVIE, package_name);
        settings_editor.commit();
    }

//    public String getPlayerForMovie() {
//        return pref_settings.getString(KEY_SETTINGS_PLAYER_FOR_MOVIE, Config.SETTINGS_DEFAULT_PLAYER);
//    }

    public void setPlayerForSeries(String package_name) {
        settings_editor.putString(KEY_SETTINGS_PLAYER_FOR_SERIES, package_name);
        settings_editor.commit();
    }

//    public String getPlayerForSeries() {
//        return pref_settings.getString(KEY_SETTINGS_PLAYER_FOR_SERIES,
//                Config.SETTINGS_DEFAULT_PLAYER);
//    }

    public void setPlayerForEPG(String package_name) {
        settings_editor.putString(KEY_SETTINGS_PLAYER_FOR_EPG, package_name);
        settings_editor.commit();
    }

//    public String getPlayerForEPG() {
//        return pref_settings.getString(KEY_SETTINGS_PLAYER_FOR_EPG,
//                Config.SETTINGS_DEFAULT_PLAYER);
//    }

    public void setPlayerForPrivateMedia(String package_name) {
        settings_editor.putString(KEY_SETTINGS_PLAYER_FOR_EPG, package_name);
        settings_editor.commit();
    }

    // for cloud time shift
    public void setPlayerForCloudtimeshift(String package_name) {
        settings_editor.putString(KEY_SETTINGS_PLAYER_FOR_CLOUDSHIFT, package_name);
        settings_editor.commit();
    }
//
//    public String getPlayerForCloudtimeshift() {
//        return pref_settings.getString(KEY_SETTINGS_PLAYER_FOR_CLOUDSHIFT,
//                Config.SETTINGS_DEFAULT_PLAYER);
//
//    }

    // for catch
    public void setPlayerForCatchUp(String package_name) {
        settings_editor.putString(KEY_SETTINGS_PLAYER_FOR_CATCHUP, package_name);
        settings_editor.commit();
    }

//    public String getPlayerForCatchUp() {
//        return pref_settings.getString(KEY_SETTINGS_PLAYER_FOR_CATCHUP,
//                Config.SETTINGS_DEFAULT_PLAYER);
//
//    }

//    public String getPlayerForPrivateMedia() {
//        return pref_settings.getString(KEY_SETTINGS_PLAYER_FOR_PRIVATE_MEDIA,
//                Config.SETTINGS_DEFAULT_PLAYER);
//    }

    public void setUniversalCode(String universal_code) {
        settings_editor.putString(KEY_UNIVERSAL_APP_CODE, universal_code);
        settings_editor.commit();
    }

    public String getUniversalCode() {
        return pref_settings.getString(KEY_UNIVERSAL_APP_CODE, null);
    }


    public void showMultiScreenLayoutDialog(boolean show) {
        settings_editor.putBoolean(KEY_SETTINGS_MULTISCREEN_LAYOUT_DIALOG_SHOW, show);
        settings_editor.commit();
    }

    public void showStartOnBootUp(boolean start) {
        settings_editor.putBoolean(KEY_SETTINGS_START_ON_BOOT_UP, start);
        settings_editor.commit();
    }

    public boolean getStartOnBootUp() {
        return pref_settings.getBoolean(KEY_SETTINGS_START_ON_BOOT_UP,
                false);
    }

    public void setAutoBackground(boolean start) {
        settings_editor.putBoolean(KEY_SETTINGS_AUTO_BACKGROUND, start);
        settings_editor.commit();
    }

    public boolean getAutoBackground() {
        return pref_settings.getBoolean(KEY_SETTINGS_AUTO_BACKGROUND,
                true);
    }

    public void wantTVLayout(boolean tv_layout) {
        settings_editor.putBoolean(KEY_SETTINGS_LAYOUT, tv_layout);
        settings_editor.commit();
    }

    public boolean getWantTVLayout() {
        return pref_settings.getBoolean(KEY_SETTINGS_LAYOUT,
                false);
    }

    public void setTimeZone(String time_format) {
        settings_editor.putString(KEY_SETTINGS_TIME_ZONE, time_format);
        settings_editor.commit();
    }

    public String getTimeZone() {
        return pref_settings.getString(KEY_SETTINGS_TIME_ZONE,
                Config.SETTINGS_TIMEZONE);
    }

    public void setTimeFormat(String time_format) {
        settings_editor.putString(KEY_SETTINGS_TIME_FORMAT, time_format);
        settings_editor.commit();
    }


    public String getTimeFormat() {
        return pref_settings.getString(KEY_SETTINGS_TIME_FORMAT,
                Config.SETTINGS_24_hour_format);
    }

    public void setStreamFormat(String time_format) {
        settings_editor.putString(KEY_SETTINGS_STREAM_FORMAT, time_format);
        settings_editor.commit();
    }

//    public String getStreamFormat() {
//        return pref_settings.getString(KEY_SETTINGS_STREAM_FORMAT,
//                Config.SETTINGS_DEFAULT_STREAM_TYPE);
//    }

    public boolean toShowMultiScreenLayoutDialog() {
        return pref_settings.getBoolean(KEY_SETTINGS_MULTISCREEN_LAYOUT_DIALOG_SHOW,
                true);
    }

    public void setDefaultMultiScreenLayout(int type) {
        settings_editor.putInt(KEY_SETTINGS_DEFAULT_MULTISCREEN_LAYOUT, type);
        settings_editor.commit();
    }

//    public int getDefaultMultiScreenLayout() {
//        return pref_settings.getInt(KEY_SETTINGS_DEFAULT_MULTISCREEN_LAYOUT,
//                FlowLayout.SCREEN_OF_4);
//    }

    public void setParentalControlPassword(String password) {
        settings_editor.putString(KEY_SETTINGS_PARENTAL_CONTROL_PASSWORD, password);
        settings_editor.commit();
    }

    public String getParentalControlPassword() {
        return pref_settings.getString(KEY_SETTINGS_PARENTAL_CONTROL_PASSWORD,
                null);
    }

    public void setCurrentlyRecordingList(Set<String> list) {
        custom_editor.putStringSet(KEY_CURRENT_RECORDING_STRING_SET, list);
        custom_editor.commit();
    }

    public Set<String> getCurrentlyRecordingList() {
        return pref_custom.getStringSet(KEY_CURRENT_RECORDING_STRING_SET, new HashSet<String>());
    }

    public void setIsLoginShown(boolean isLoginShown) {
        custom_editor.putBoolean(KEY_IS_LOGIN_SHOWN, isLoginShown);
        custom_editor.commit();
    }

    public boolean getIsLoginShown() {
        return pref_custom.getBoolean(KEY_IS_LOGIN_SHOWN, false);
    }

    public void setShowInstructionDialog(boolean doShow) {
        custom_editor.putBoolean(KEY_SHOW_INSTRUCTION_DIALOG, doShow);
        custom_editor.commit();
    }

    public boolean getShowInstructionDialog() {
        return pref_custom.getBoolean(KEY_SHOW_INSTRUCTION_DIALOG, true);
    }

    public void setAppType(String app_type) {
        custom_editor.putString(KEY_APP_TYPE_CUSTOM, app_type);
        custom_editor.commit();
    }

    public String getAppType() {
        return pref_custom.getString(KEY_APP_TYPE_CUSTOM, null);
    }

    public void setSmartTvCode(String code) {
        custom_editor.putString(KEY_SMART_TV_CODE, code);
        custom_editor.commit();
    }

    public String getSmartTvCode() {
        return pref_custom.getString(KEY_SMART_TV_CODE, null);
    }

    public void setLanguageCode(String code) {
        custom_editor.putString(KEY_LANGUAGE_CODE, code);
        custom_editor.commit();
    }

    public String getLanguageCode() {
        return pref_custom.getString(KEY_LANGUAGE_CODE, "en");
    }

    public void setRecordingStoragePath(String path) {
        custom_editor.putString(KEY_RECORDING_STORAGE_PATH, path);
        custom_editor.commit();
    }

    public String getRecordingStoragePath() {
        // FIXME: 6/22/2021
       // if (isAndroid10_or_Above()) {
            return pref_custom.getString(KEY_RECORDING_STORAGE_PATH,
                    getAndroid11Dir(_context) + "/" +
                            _context.getString(R.string.defaultrecordpath));
//        } else {
//            return pref_custom.getString(KEY_RECORDING_STORAGE_PATH,
//                    Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
//                            _context.getString(R.string.defaultrecordpath));
//        }

    }

    public void setExternalStorageUri(String uri) {
        custom_editor.putString(KEY_EXTERNAL_STORAGE_URL, uri);
        custom_editor.commit();
    }

    public String getExternalStorageUri() {
        return pref_custom.getString(KEY_EXTERNAL_STORAGE_URL, null);
    }

    public void setRewardedAdTime(long time) {
        custom_editor.putLong(KEY_LAST_REWARDED_AD_TIME, time);
        custom_editor.commit();
    }

    public long getRewardedAdTime() {
        return pref_custom.getLong(KEY_LAST_REWARDED_AD_TIME, -1);
    }

    public void clearSmartTvCode() {
        custom_editor.putString(KEY_SMART_TV_CODE, null);
        custom_editor.commit();
    }

    public void setLanguageSelectionShown(boolean doShow) {
        custom_editor.putBoolean(KEY_SHOW_LANGUAGE_SELECTION, doShow);
        custom_editor.commit();
    }

    public boolean getLanguageSelectionShown() {
        return pref_custom.getBoolean(KEY_SHOW_LANGUAGE_SELECTION, false);
    }

    public void setDeviceLayoutSelectionShown(boolean doShow) {
        custom_editor.putBoolean(KEY_SHOW_DEVICE_LAYOUT_SELECTION, doShow);
        custom_editor.commit();
    }

    public boolean getDeviceLayoutSelectionShown() {
        return pref_custom.getBoolean(KEY_SHOW_DEVICE_LAYOUT_SELECTION, false);
    }

    public void clearOnlineUser() {
        online_editor.clear();
        online_editor.commit();
    }

    public void clear() {
        editor.clear();
        editor.commit();
    }

    //live tv
    public void setShowhideLiveTvcategory(boolean doShow) {
        custom_editor.putBoolean(KEY_SHOWHIDE_LIVE_TVCATEGORY, doShow);
        custom_editor.commit();
    }

    public boolean getShowhideLiveTvcategory() {
        return pref_custom.getBoolean(KEY_SHOWHIDE_LIVE_TVCATEGORY, false);
    }

    //movies
    public void setShowhideMoviecategory(boolean doShow) {
        custom_editor.putBoolean(KEY_SHOWHIDE_MOVIECATEGORY, doShow);
        custom_editor.commit();
    }

    public boolean getShowhideMoviecategory() {
        return pref_custom.getBoolean(KEY_SHOWHIDE_MOVIECATEGORY, false);
    }

    //series
    public void setShowhideSeriescategory(boolean doShow) {
        custom_editor.putBoolean(KEY_SHOWHIDE_SERIESCATEGORY, doShow);
        custom_editor.commit();
    }

    public boolean getShowhideSeriescategory() {
        return pref_custom.getBoolean(KEY_SHOWHIDE_SERIESCATEGORY, false);
    }

    //Archive live tv
    public void setSHOWHIDE_ARCHIVE_LIVETV(boolean doShow) {
        custom_editor.putBoolean(KEY_SHOWHIDE_ARCHIVE_LIVETV, doShow);
        custom_editor.commit();
    }

    public boolean getSHOWHIDE_ARCHIVE_LIVETV() {
        return pref_custom.getBoolean(KEY_SHOWHIDE_ARCHIVE_LIVETV, false);
    }

    //Archive live tv inside
    public void setSHOWHIDE_ARCHIVE_LIVETV_CHANNEL(boolean doShow) {
        custom_editor.putBoolean(KEY_SHOWHIDE_ARCHIVE_LIVETV_CHANNEL, doShow);
        custom_editor.commit();
    }

    public boolean getSHOWHIDE_ARCHIVE_LIVETV_CHANNEL() {
        return pref_custom.getBoolean(KEY_SHOWHIDE_ARCHIVE_LIVETV_CHANNEL, false);
    }
    // archive movie

    public void setSHOWHIDE_ARCHIVE_MOVIE(boolean doShow) {
        custom_editor.putBoolean(KEY_SHOWHIDE_ARCHIVE_MOVIE, doShow);
        custom_editor.commit();
    }

    public boolean getSHOWHIDE_ARCHIVE_MOVIE() {
        return pref_custom.getBoolean(KEY_SHOWHIDE_ARCHIVE_MOVIE, false);
    }

    //archive series
    public void setSHOWHIDE_ARCHIVE_SERIES(boolean doShow) {
        custom_editor.putBoolean(KEY_SHOWHIDE_ARCHIVE_SERIES, doShow);
        custom_editor.commit();
    }

    public boolean getSHOWHIDE_ARCHIVE_SERIES() {
        return pref_custom.getBoolean(KEY_SHOWHIDE_ARCHIVE_SERIES, false);
    }

    // archive epg


    public void setSHOWHIDE_ARCHIVE_EPG(boolean doShow) {
        custom_editor.putBoolean(KEY_SHOWHIDE_ARCHIVE_EPG, doShow);
        custom_editor.commit();
    }

    public boolean getSHOWHIDE_ARCHIVE_EPG() {
        return pref_custom.getBoolean(KEY_SHOWHIDE_ARCHIVE_EPG, false);
    }

    // Archive catchup


    public void setSHOWHIDE_ARCHIVE_CATCHUP(boolean doShow) {
        custom_editor.putBoolean(KEY_SHOWHIDE_ARCHIVE_CATCHUP, doShow);
        custom_editor.commit();
    }

    public boolean getSHOWHIDE_ARCHIVE_CATCHUP() {
        return pref_custom.getBoolean(KEY_SHOWHIDE_ARCHIVE_CATCHUP, false);
    }


    // store notification id
//    KEY_STOREFIRSTNOTIFICATIONID
    public void setSTOREFIRSTNOTIFICATIONID(String doShow) {
        custom_editor.putString(KEY_STOREFIRSTNOTIFICATIONID, doShow);
        custom_editor.commit();
    }

    public String getSTOREFIRSTNOTIFICATIONID() {
        return pref_custom.getString(KEY_STOREFIRSTNOTIFICATIONID, "");
    }


    public void setNOTIFICATIONMODEL(String doShow) {
        custom_editor.putString(KEY_STORENOTIFICATIONMODEL, doShow);
        custom_editor.commit();
    }

    public String getNOTIFICATIONMODEL() {
        return pref_custom.getString(KEY_STORENOTIFICATIONMODEL, "");
    }
    //for username pass

    //xtream
    public void setusernameinpref(String doShow) {
        custom_editor.putString(KEY_xtreamUSERNAMEINPREF, doShow);
        custom_editor.commit();
    }

    public String getusernameinpref() {
        return pref_custom.getString(KEY_xtreamUSERNAMEINPREF, "");
    }

    public void setpassinpref(String doShow) {
        custom_editor.putString(KEY_xtreamPASSINPREF, doShow);
        custom_editor.commit();
    }

    public String getpassinpref() {
        return pref_custom.getString(KEY_xtreamPASSINPREF, "");
    }

    //custom login
    public void setcustomusernameinpref(String doShow) {
        custom_editor.putString(KEY_customUSERNAMEINPREF, doShow);
        custom_editor.commit();
    }

    public String getcustomusernameinpref() {
        return pref_custom.getString(KEY_customUSERNAMEINPREF, "");
    }

    public void setcustompassinpref(String doShow) {
        custom_editor.putString(KEY_customPASSINPREF, doShow);
        custom_editor.commit();
    }

    public String getcustompassinpref() {
        return pref_custom.getString(KEY_customPASSINPREF, "");
    }

    public void setColormodel(String doShow) {
        custom_editor.putString(KEY_COLORMODEL, doShow);
        custom_editor.commit();
    }

    public String getColormodel() {
        ColorModel colorModel = new ColorModel();
        colorModel.setUnselected_btn_color(_context.getResources().getColor(R.color.unselected_btn_color));
        colorModel.setUnselected_categoryList(_context.getResources().getColor(R.color.unselected_categoryList));
        colorModel.setSelected_color(_context.getResources().getColor(R.color.selected_color));
        colorModel.setFocused_selected_color(_context.getResources().getColor(R.color.focused_selected_color));
        colorModel.setSelected_categoryList(_context.getResources().getColor(R.color.selected_categoryList));
        colorModel.setSecondary_text_color(_context.getResources().getColor(R.color.secondary_text_color));
        colorModel.setColor_dialog_bg(_context.getResources().getColor(R.color.color_dialog_bg));
        colorModel.setTab_selected(_context.getResources().getColor(R.color.tab_selected));
        colorModel.setFocused_color(_context.getResources().getColor(R.color.focused_color));
        Gson gson = new Gson();
        String colorModelforrecording = gson.toJson(colorModel);
        return pref_custom.getString(KEY_COLORMODEL, colorModelforrecording);
    }

    // autologin disable
    public void setautologin(boolean doShow) {
        custom_editor.putBoolean(KEY_FLAGFORLOGIN, doShow);
        custom_editor.commit();
    }


    public boolean getgetautologin() {
        return pref_custom.getBoolean(KEY_FLAGFORLOGIN, false);
    }


    public void setIsrunningRecording(boolean doShow) {
        custom_editor.putBoolean(KEY_ISRECORDINGRUNNED, doShow);
        custom_editor.commit();
    }

    public boolean getIsRunningRecording() {
        return pref_custom.getBoolean(KEY_ISRECORDINGRUNNED, false);
    }

    public void setRunningtasktime(long doShow) {
        custom_editor.putLong(KEY_RUNNEDRECORDTIME, doShow);
        custom_editor.commit();
    }

    public long getRunningtasktime() {
        return pref_custom.getLong(KEY_RUNNEDRECORDTIME, -1);
    }

    public void setIsrunningRecording2(boolean doShow) {
        custom_editor.putBoolean(KEY_ISRECORDINGRUNNED2, doShow);
        custom_editor.commit();
    }

    public boolean getIsRunningRecording2() {
        return pref_custom.getBoolean(KEY_ISRECORDINGRUNNED2, false);
    }

    public void setRunningtasktime2(long doShow) {
        custom_editor.putLong(KEY_RUNNEDRECORDTIME2, doShow);
        custom_editor.commit();
    }

    public long getRunningtasktime2() {
        return pref_custom.getLong(KEY_RUNNEDRECORDTIME2, -1);
    }

    /*bgimage */
    public String getBgimage() {
        return pref_custom.getString(KEY_BGIMGES, "");
    }

    public void setbgimage(String doShow) {
        custom_editor.putString(KEY_BGIMGES, doShow);
        custom_editor.commit();
    }

}
