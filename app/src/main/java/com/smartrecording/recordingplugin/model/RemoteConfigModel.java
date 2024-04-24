package com.smartrecording.recordingplugin.model;

import com.purple.iptv.player.utils.Config;

import java.util.Set;

public class RemoteConfigModel {

    public boolean showAds = false;
    public boolean is_subscribed = false;
    public String package_name = "";
    public String googleAppAdId = "";
    public String googleInterstitialAdID = "";
    public String googleBannerAdId = "";
    public String googleRewardedAdId = "";
    public String onlineLogin = "";
    public String onlineRegister = "";
    public String onlineAddM3uList = "";
    public String onlineAddXstreamList = "";
    public String onlineGetList = "";
    public String onlineDeleteListItem = "";
    public String onlineUpdateM3uEpgUrl;
    public String onlineHeaderValue = "";
    public String onlineHeaderKey = "";
    public String yandexKey = "";
    public String startupMsg = "";
    public boolean version_force_update = false;
    public String version_message = "";
    public String version_url = "";
    public String version_url_apk = "";
    public String version_name = "";
    public long version_code = 1;
    public String web_privacy_policy = "";
    public String imdb_api = "";
    public String imdb_image_api = "";
    public String trakt_api_key = "";
    public String domain_url = "";
    public String portal_url = "";
    public boolean is_private_access_on = false;
    public String app_type = Config.APP_TYPE_PURPLE;
    public String expire_Date = null;
    public String private_domain_url = "";
    public String private_4k_url = "";
    public String private_4k_gdrive_api_key = "";
    public boolean is_4k_on = false;
    public boolean is_vpn_on = true;
    public boolean is_cast_on = true;
    public boolean is_remote_support = true;
    public String vpn_url = "http://www.vpngate.net/api/iphone/";
    public String vpn_gate_url = "http://www.vpngate.net/api/iphone/";
    public String vpn_gate_id = "";
    public String vpn_user_name = "";
    public String vpn_password = "";
    public String app_logo = "";
    public String app_tv_banner = "";
    public String splash_image = "";
    public String app_mobile_icon = "";
    public String slack_token = "";
    public String sub_product_id = "com.purple.iptv.player.subscription";
    public String sub_licence_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwRXad6Pw+X2JlyvTLI/otKEOC9M5C0xGAiHxUSYDnyRGPSOB3THa/fJTSu5OBn0OnM2d5z0sChKezHBcHekql1EoiDkBvfkH0CcgHKjsdUmDow/ZRB+eom674gtpPDEbrLUbxItfwr0zZQkOPN9F8m4PyDzR4GpNrM97qpvrCEVSGxikoxQ/+6QyPRQOhNVZDtK72nebzv6pW389ZQfv2/erEUWajNGj/QyxV/NxtGR2hHud6pZk8AZkdhk1hmcAaY/spvlUHmW1fzhAph073ihQu+Ng7qXhZVVFZobrl4RCKiOp2alK6Tj6SoyRRddUnNmvATvQUjutHBBzBPoXjQIDAQAB";
    public boolean sub_in_app_status;
    public String main_config_url = "";
    public String about_name = "";
    public String about_description = "";
    public String about_developed = "";
    public String about_skype = "";
    public String about_whatsapp = "";
    public String about_telegram = "";
    public String support_email = "";
    public String support_skype = "";
    public String support_web = "";
    public String support_whatsapp = "";
    public String support_telegram = "";
    public String base_m3u_to_json_converter = "";
    public boolean show_language_selection;
    public boolean show_device_type;
    public boolean app_img;
    public String back_image = "";
    public String background_orverlay_color_code = "";
    private Set<String> imageBackArray;
    private boolean background_auto_change;
    private boolean background_mannual_change;
    private boolean showWIFI;
    private boolean showSettings;
    private boolean startup_auto_boot;
    private boolean private_menu;
    //private Set<String> dnsArray;
    public String app_mode = "";
    private boolean showAppList;
    public String theme_default_layout;
    public String theme_menu_image_status;
    public String theme_live_tv;
    public String theme_epg_guide;
    public String theme_movies;
    public String theme_series;
    public String theme_vpn;
    public String theme_favorite;
    public String theme_recording;
    public String theme_multi_screen;
    public String theme_catchup;
    public String theme_app_settings;
    public String theme_system_settings;
    public String theme_search;
    public String theme_recent;
    public String theme_account_info;
    public String theme_stream_format;
    public String theme_parent_control;
    public String theme_player_selection;
    public String theme_external_player;
    public String theme_share_screen;
    public String theme_time_format;
    public String theme_clear_cache;
    public String theme_change_language;
    public String theme_privacy_policy;
    public String theme_support;
    public String theme_check_update;
    public String theme_speed_test;
    public String theme_general_setting;
    public String theme_device_type;
    //for mqtt
    public String mqtt_endpoint;
    public String mqtt_server;
    //for ticker
    private boolean dashbord_ticker;
    private boolean login_ticker;
    // for recent play
    private boolean default_play;
    private boolean recent_play;
    // for cloud fav/recent add remove
    private String cloud_recent_fav;
    private String cloud_recent_fav_url;
    // for reminder
    private boolean remind_me;
    //for cloud recording
    private boolean cloud_recording;


    public String getCloud_recent_fav() {
        return cloud_recent_fav;
    }

    public void setCloud_recent_fav(String cloud_recent_fav) {
        this.cloud_recent_fav = cloud_recent_fav;
    }

    public String getCloud_recent_fav_url() {
        return cloud_recent_fav_url;
    }

    public void setCloud_recent_fav_url(String cloud_recent_fav_url) {
        this.cloud_recent_fav_url = cloud_recent_fav_url;
    }

    public boolean isDefault_play() {
        return default_play;
    }

    public void setDefault_play(boolean default_play) {
        this.default_play = default_play;
    }

    public boolean isRecent_play() {
        return recent_play;
    }

    public void setRecent_play(boolean recent_play) {
        this.recent_play = recent_play;
    }


    public boolean isLogin_ticker() {
        return login_ticker;
    }

    public void setLogin_ticker(boolean login_ticker) {
        this.login_ticker = login_ticker;
    }

    public boolean getDashbord_ticker() {
        return dashbord_ticker;
    }

    public void setDashbord_ticker(boolean dashbord_ticker) {
        this.dashbord_ticker = dashbord_ticker;
    }


    public String getMqtt_endpoint() {
        return mqtt_endpoint;
    }

    public void setMqtt_endpoint(String mqtt_endpoint) {
        this.mqtt_endpoint = mqtt_endpoint;
    }

    public String getMqtt_server() {
        return mqtt_server;
    }

    public void setMqtt_server(String mqtt_server) {
        this.mqtt_server = mqtt_server;
    }


    private boolean cloudTimeShift;

    public boolean isCloudcatchup() {
        return cloudcatchup;
    }

    public void setCloudcatchup(boolean cloudcatchup) {
        this.cloudcatchup = cloudcatchup;
    }

    private boolean cloudcatchup;

    public boolean isCloudTimeShift() {
        return cloudTimeShift;
    }

    public void setCloudTimeShift(boolean cloudTimeShift) {
        this.cloudTimeShift = cloudTimeShift;
    }

    public boolean isPrivate_menu() {
        return private_menu;
    }

    public void setPrivate_menu(boolean private_menu) {
        this.private_menu = private_menu;
    }

    public String getTheme_default_layout() {
        return theme_default_layout;
    }

    public void setTheme_default_layout(String theme_default_layout) {
        this.theme_default_layout = theme_default_layout;
    }

    public String getTheme_menu_image_status() {
        return theme_menu_image_status;
    }

    public void setTheme_menu_image_status(String theme_menu_image_status) {
        this.theme_menu_image_status = theme_menu_image_status;
    }

    public String getTheme_live_tv() {
        return theme_live_tv;
    }

    public void setTheme_live_tv(String theme_live_tv) {
        this.theme_live_tv = theme_live_tv;
    }

    public String getTheme_epg_guide() {
        return theme_epg_guide;
    }

    public void setTheme_epg_guide(String theme_epg_guide) {
        this.theme_epg_guide = theme_epg_guide;
    }

    public String getTheme_movies() {
        return theme_movies;
    }

    public void setTheme_movies(String theme_movies) {
        this.theme_movies = theme_movies;
    }

    public String getTheme_series() {
        return theme_series;
    }

    public void setTheme_series(String theme_series) {
        this.theme_series = theme_series;
    }

    public String getTheme_vpn() {
        return theme_vpn;
    }

    public void setTheme_vpn(String theme_vpn) {
        this.theme_vpn = theme_vpn;
    }

    public String getTheme_favorite() {
        return theme_favorite;
    }

    public void setTheme_favorite(String theme_favorite) {
        this.theme_favorite = theme_favorite;
    }

    public String getTheme_recording() {
        return theme_recording;
    }

    public void setTheme_recording(String theme_recording) {
        this.theme_recording = theme_recording;
    }

    public String getTheme_multi_screen() {
        return theme_multi_screen;
    }

    public void setTheme_multi_screen(String theme_multi_screen) {
        this.theme_multi_screen = theme_multi_screen;
    }

    public String getTheme_catchup() {
        return theme_catchup;
    }

    public void setTheme_catchup(String theme_catchup) {
        this.theme_catchup = theme_catchup;
    }

    public String getTheme_app_settings() {
        return theme_app_settings;
    }

    public void setTheme_app_settings(String theme_app_settings) {
        this.theme_app_settings = theme_app_settings;
    }

    public String getTheme_system_settings() {
        return theme_system_settings;
    }

    public void setTheme_system_settings(String theme_system_settings) {
        this.theme_system_settings = theme_system_settings;
    }

    public String getTheme_search() {
        return theme_search;
    }

    public void setTheme_search(String theme_search) {
        this.theme_search = theme_search;
    }

    public String getTheme_recent() {
        return theme_recent;
    }

    public void setTheme_recent(String theme_recent) {
        this.theme_recent = theme_recent;
    }

    public String getTheme_account_info() {
        return theme_account_info;
    }

    public void setTheme_account_info(String theme_account_info) {
        this.theme_account_info = theme_account_info;
    }

    public String getTheme_stream_format() {
        return theme_stream_format;
    }

    public void setTheme_stream_format(String theme_stream_format) {
        this.theme_stream_format = theme_stream_format;
    }

    public String getTheme_parent_control() {
        return theme_parent_control;
    }

    public void setTheme_parent_control(String theme_parent_control) {
        this.theme_parent_control = theme_parent_control;
    }

    public String getTheme_player_selection() {
        return theme_player_selection;
    }

    public void setTheme_player_selection(String theme_player_selection) {
        this.theme_player_selection = theme_player_selection;
    }

    public String getTheme_external_player() {
        return theme_external_player;
    }

    public void setTheme_external_player(String theme_external_player) {
        this.theme_external_player = theme_external_player;
    }

    public String getTheme_share_screen() {
        return theme_share_screen;
    }

    public void setTheme_share_screen(String theme_share_screen) {
        this.theme_share_screen = theme_share_screen;
    }

    public String getTheme_time_format() {
        return theme_time_format;
    }

    public void setTheme_time_format(String theme_time_format) {
        this.theme_time_format = theme_time_format;
    }

    public String getTheme_clear_cache() {
        return theme_clear_cache;
    }

    public void setTheme_clear_cache(String theme_clear_cache) {
        this.theme_clear_cache = theme_clear_cache;
    }

    public String getTheme_change_language() {
        return theme_change_language;
    }

    public void setTheme_change_language(String theme_change_language) {
        this.theme_change_language = theme_change_language;
    }

    public String getTheme_privacy_policy() {
        return theme_privacy_policy;
    }

    public void setTheme_privacy_policy(String theme_privacy_policy) {
        this.theme_privacy_policy = theme_privacy_policy;
    }

    public String getTheme_support() {
        return theme_support;
    }

    public void setTheme_support(String theme_support) {
        this.theme_support = theme_support;
    }

    public String getTheme_check_update() {
        return theme_check_update;
    }

    public void setTheme_check_update(String theme_check_update) {
        this.theme_check_update = theme_check_update;
    }

    public String getTheme_speed_test() {
        return theme_speed_test;
    }

    public void setTheme_speed_test(String theme_speed_test) {
        this.theme_speed_test = theme_speed_test;
    }

    public String getTheme_general_setting() {
        return theme_general_setting;
    }

    public void setTheme_general_setting(String theme_general_setting) {
        this.theme_general_setting = theme_general_setting;
    }

    public String getTheme_device_type() {
        return theme_device_type;
    }

    public void setTheme_device_type(String theme_device_type) {
        this.theme_device_type = theme_device_type;
    }

    public boolean isShowAppList() {
        return showAppList;
    }

    public void setShowAppList(boolean showAppList) {
        this.showAppList = showAppList;
    }

    public String getApp_mode() {
        return app_mode;
    }

    public void setApp_mode(String app_mode) {
        this.app_mode = app_mode;
    }

   /* public Set<String> getDnsArray() {
        return dnsArray;
    }

    public void setDnsArray(Set<String> dnsArray) {
        this.dnsArray = dnsArray;
    }*/

    public boolean isShowSettings() {
        return showSettings;
    }

    public void setShowSettings(boolean showSettings) {
        this.showSettings = showSettings;
    }

    public boolean isStartup_auto_boot() {
        return startup_auto_boot;
    }

    public void setStartup_auto_boot(boolean startup_auto_boot) {
        this.startup_auto_boot = startup_auto_boot;
    }

    public boolean isShowWIFI() {
        return showWIFI;
    }

    public void setShowWIFI(boolean showWIFI) {
        this.showWIFI = showWIFI;
    }

    public boolean isBackground_auto_change() {
        return background_auto_change;
    }

    public void setBackground_auto_change(boolean background_auto_change) {
        this.background_auto_change = background_auto_change;
    }

    public boolean isBackground_mannual_change() {
        return background_mannual_change;
    }

    public void setBackground_mannual_change(boolean background_mannual_change) {
        this.background_mannual_change = background_mannual_change;
    }

    public String getBackground_orverlay_color_code() {
        return background_orverlay_color_code;
    }

    public void setBackground_orverlay_color_code(String background_orverlay_color_code) {
        this.background_orverlay_color_code = background_orverlay_color_code;
    }

    public Set<String> getImageBackArray() {
        return imageBackArray;
    }

    public void setImageBackArray(Set<String> imageBackArray) {
        this.imageBackArray = imageBackArray;
    }

    public String getBack_image() {
        return back_image;
    }

    public void setBack_image(String back_image) {
        this.back_image = back_image;
    }

    public boolean isApp_img() {
        return app_img;
    }

    public void setApp_img(boolean app_img) {
        this.app_img = app_img;
    }

    public boolean isShow_device_type() {
        return show_device_type;
    }

    public void setShow_device_type(boolean show_device_type) {
        this.show_device_type = show_device_type;
    }

    public boolean isShow_language_selection() {
        return show_language_selection;
    }

    public void setShow_language_selection(boolean show_language_selection) {
        this.show_language_selection = show_language_selection;
    }

    public boolean getSub_in_app_status() {
        return sub_in_app_status;
    }

    public void setSub_in_app_status(boolean sub_in_app_status) {
        this.sub_in_app_status = sub_in_app_status;
    }

    public String getPrivate_domain_url() {
        return private_domain_url;
    }

    public void setPrivate_domain_url(String private_domain_url) {
        this.private_domain_url = private_domain_url;
    }

    public String getVpn_user_name() {
        return vpn_user_name;
    }

    public void setVpn_user_name(String vpn_user_name) {
        this.vpn_user_name = vpn_user_name;
    }

    public String getVpn_password() {
        return vpn_password;
    }

    public void setVpn_password(String vpn_password) {
        this.vpn_password = vpn_password;
    }

    public String getBase_m3u_to_json_converter() {
        return base_m3u_to_json_converter;
    }

    public void setBase_m3u_to_json_converter(String base_m3u_to_json_converter) {
        this.base_m3u_to_json_converter = base_m3u_to_json_converter;
    }

    public boolean isIs_remote_support() {
        return is_remote_support;
    }

    public void setIs_remote_support(boolean is_remote_support) {
        this.is_remote_support = is_remote_support;
    }

    public String getVpn_gate_url() {
        return vpn_gate_url;
    }

    public void setVpn_gate_url(String vpn_gate_url) {
        this.vpn_gate_url = vpn_gate_url;
    }

    public String getVpn_gate_id() {
        return vpn_gate_id;
    }

    public void setVpn_gate_id(String vpn_gate_id) {
        this.vpn_gate_id = vpn_gate_id;
    }

    public boolean isShowAds() {
        return showAds;
    }

    public void setShowAds(boolean showAds) {
        this.showAds = showAds;
    }

    public boolean isIs_subscribed() {
        return is_subscribed;
    }

    public void setIs_subscribed(boolean is_subscribed) {
        this.is_subscribed = is_subscribed;
    }

    public String getPackage_name() {
        return package_name;
    }

    public void setPackage_name(String package_name) {
        this.package_name = package_name;
    }

    public String getGoogleAppAdId() {
        return googleAppAdId;
    }

    public void setGoogleAppAdId(String googleAppAdId) {
        this.googleAppAdId = googleAppAdId;
    }

    public String getGoogleInterstitialAdID() {
        return googleInterstitialAdID;
    }

    public void setGoogleInterstitialAdID(String googleInterstitialAdID) {
        this.googleInterstitialAdID = googleInterstitialAdID;
    }

    public String getGoogleBannerAdId() {
        return googleBannerAdId;
    }

    public void setGoogleBannerAdId(String googleBannerAdId) {
        this.googleBannerAdId = googleBannerAdId;
    }

    public String getGoogleRewardedAdId() {
        return googleRewardedAdId;
    }

    public void setGoogleRewardedAdId(String googleRewardedAdId) {
        this.googleRewardedAdId = googleRewardedAdId;
    }

    public String getOnlineLogin() {
        return onlineLogin;
    }

    public void setOnlineLogin(String onlineLogin) {
        this.onlineLogin = onlineLogin;
    }

    public String getOnlineRegister() {
        return onlineRegister;
    }

    public void setOnlineRegister(String onlineRegister) {
        this.onlineRegister = onlineRegister;
    }

    public String getOnlineAddM3uList() {
        return onlineAddM3uList;
    }

    public void setOnlineAddM3uList(String onlineAddM3uList) {
        this.onlineAddM3uList = onlineAddM3uList;
    }

    public String getOnlineAddXstreamList() {
        return onlineAddXstreamList;
    }

    public void setOnlineAddXstreamList(String onlineAddXstreamList) {
        this.onlineAddXstreamList = onlineAddXstreamList;
    }

    public String getOnlineGetList() {
        return onlineGetList;
    }

    public void setOnlineGetList(String onlineGetList) {
        this.onlineGetList = onlineGetList;
    }

    public String getOnlineDeleteListItem() {
        return onlineDeleteListItem;
    }

    public void setOnlineDeleteListItem(String onlineDeleteListItem) {
        this.onlineDeleteListItem = onlineDeleteListItem;
    }

    public String getOnlineUpdateM3uEpgUrl() {
        return onlineUpdateM3uEpgUrl;
    }

    public void setOnlineUpdateM3uEpgUrl(String onlineUpdateM3uEpgUrl) {
        this.onlineUpdateM3uEpgUrl = onlineUpdateM3uEpgUrl;
    }

    public String getOnlineHeaderValue() {
        return onlineHeaderValue;
    }

    public void setOnlineHeaderValue(String onlineHeaderValue) {
        this.onlineHeaderValue = onlineHeaderValue;
    }

    public String getOnlineHeaderKey() {
        return onlineHeaderKey;
    }

    public void setOnlineHeaderKey(String onlineHeaderKey) {
        this.onlineHeaderKey = onlineHeaderKey;
    }

    public String getYandexKey() {
        return yandexKey;
    }

    public void setYandexKey(String yandexKey) {
        this.yandexKey = yandexKey;
    }

    public String getStartupMsg() {
        return startupMsg;
    }

    public void setStartupMsg(String startupMsg) {
        this.startupMsg = startupMsg;
    }

    public boolean isVersion_force_update() {
        return version_force_update;
    }

    public void setVersion_force_update(boolean version_force_update) {
        this.version_force_update = version_force_update;
    }

    public String getVersion_message() {
        return version_message;
    }

    public void setVersion_message(String version_message) {
        this.version_message = version_message;
    }

    public String getVersion_url() {
        return version_url;
    }

    public void setVersion_url(String version_url) {
        this.version_url = version_url;
    }

    public String getVersion_url_apk() {
        return version_url_apk;
    }

    public void setVersion_url_apk(String version_url_apk) {
        this.version_url_apk = version_url_apk;
    }

    public String getVersion_name() {
        return version_name;
    }

    public void setVersion_name(String version_name) {
        this.version_name = version_name;
    }

    public long getVersion_code() {
        return version_code;
    }

    public void setVersion_code(long version_code) {
        this.version_code = version_code;
    }

    public String getWeb_privacy_policy() {
        return web_privacy_policy;
    }

    public void setWeb_privacy_policy(String web_privacy_policy) {
        this.web_privacy_policy = web_privacy_policy;
    }

    public String getImdb_api() {
        return imdb_api;
    }

    public void setImdb_api(String imdb_api) {
        this.imdb_api = imdb_api;
    }

    public String getImdb_image_api() {
        return imdb_image_api;
    }

    public void setImdb_image_api(String imdb_image_api) {
        this.imdb_image_api = imdb_image_api;
    }

    public String getTrakt_api_key() {
        return trakt_api_key;
    }

    public void setTrakt_api_key(String trakt_api_key) {
        this.trakt_api_key = trakt_api_key;
    }

    public String getDomain_url() {
        return domain_url;
    }

    public void setDomain_url(String domain_url) {
        this.domain_url = domain_url;
    }

    public String getPortal_url() {
        return portal_url;
    }

    public void setPortal_url(String portal_url) {
        this.portal_url = portal_url;
    }

    public boolean isIs_private_access_on() {
        return is_private_access_on;
    }

    public void setIs_private_access_on(boolean is_private_access_on) {
        this.is_private_access_on = is_private_access_on;
    }

    public String getApp_type() {
        return app_type;
    }

    public void setApp_type(String app_type) {
        this.app_type = app_type;
    }

    public String getExpire_Date() {
        return expire_Date;
    }

    public void setExpire_Date(String expire_Date) {
        this.expire_Date = expire_Date;
    }

    public String getPrivate_4k_url() {
        return private_4k_url;
    }

    public void setPrivate_4k_url(String private_4k_url) {
        this.private_4k_url = private_4k_url;
    }

    public String getPrivate_4k_gdrive_api_key() {
        return private_4k_gdrive_api_key;
    }

    public void setPrivate_4k_gdrive_api_key(String private_4k_gdrive_api_key) {
        this.private_4k_gdrive_api_key = private_4k_gdrive_api_key;
    }

    public boolean isIs_4k_on() {
        return is_4k_on;
    }

    public void setIs_4k_on(boolean is_4k_on) {
        this.is_4k_on = is_4k_on;
    }

    public boolean isIs_vpn_on() {
        return is_vpn_on;
    }

    public void setIs_vpn_on(boolean is_vpn_on) {
        this.is_vpn_on = is_vpn_on;
    }

    public boolean isIs_cast_on() {
        return is_cast_on;
    }

    public void setIs_cast_on(boolean is_cast_on) {
        this.is_cast_on = is_cast_on;
    }

    public String getVpn_url() {
        return vpn_url;
    }

    public void setVpn_url(String vpn_url) {
        this.vpn_url = vpn_url;
    }

    public String getApp_logo() {
        return app_logo;
    }

    public void setApp_logo(String app_logo) {
        this.app_logo = app_logo;
    }

    public String getApp_tv_banner() {
        return app_tv_banner;
    }

    public void setApp_tv_banner(String app_tv_banner) {
        this.app_tv_banner = app_tv_banner;
    }

    public String getSplash_image() {
        return splash_image;
    }

    public void setSplash_image(String splash_image) {
        this.splash_image = splash_image;
    }

    public String getApp_mobile_icon() {
        return app_mobile_icon;
    }

    public void setApp_mobile_icon(String app_mobile_icon) {
        this.app_mobile_icon = app_mobile_icon;
    }

    public String getSlack_token() {
        return slack_token;
    }

    public void setSlack_token(String slack_token) {
        this.slack_token = slack_token;
    }

    public String getSub_product_id() {
        return sub_product_id;
    }

    public void setSub_product_id(String sub_product_id) {
        this.sub_product_id = sub_product_id;
    }

    public String getSub_licence_key() {
        return sub_licence_key;
    }

    public void setSub_licence_key(String sub_licence_key) {
        this.sub_licence_key = sub_licence_key;
    }

    public String getMain_config_url() {
        return main_config_url;
    }

    public void setMain_config_url(String main_config_url) {
        this.main_config_url = main_config_url;
    }

    public String getAbout_name() {
        return about_name;
    }

    public void setAbout_name(String about_name) {
        this.about_name = about_name;
    }

    public String getAbout_description() {
        return about_description;
    }

    public void setAbout_description(String about_description) {
        this.about_description = about_description;
    }

    public String getAbout_developed() {
        return about_developed;
    }

    public void setAbout_developed(String about_developed) {
        this.about_developed = about_developed;
    }

    public String getAbout_skype() {
        return about_skype;
    }

    public void setAbout_skype(String about_skype) {
        this.about_skype = about_skype;
    }

    public String getAbout_whatsapp() {
        return about_whatsapp;
    }

    public void setAbout_whatsapp(String about_whatsapp) {
        this.about_whatsapp = about_whatsapp;
    }

    public String getAbout_telegram() {
        return about_telegram;
    }

    public void setAbout_telegram(String about_telegram) {
        this.about_telegram = about_telegram;
    }

    public String getSupport_email() {
        return support_email;
    }

    public void setSupport_email(String support_email) {
        this.support_email = support_email;
    }

    public String getSupport_skype() {
        return support_skype;
    }

    public void setSupport_skype(String support_skype) {
        this.support_skype = support_skype;
    }

    public String getSupport_web() {
        return support_web;
    }

    public void setSupport_web(String support_web) {
        this.support_web = support_web;
    }

    public String getSupport_whatsapp() {
        return support_whatsapp;
    }

    public void setSupport_whatsapp(String support_whatsapp) {
        this.support_whatsapp = support_whatsapp;
    }

    public String getSupport_telegram() {
        return support_telegram;
    }

    public void setSupport_telegram(String support_telegram) {
        this.support_telegram = support_telegram;
    }



    public boolean getRemind_me() {
        return remind_me;
    }

    public void setRemind_me(boolean remind_me) {
        this.remind_me = remind_me;
    }

    public boolean isCloud_recording() {
        return cloud_recording;
    }

    public void setCloud_recording(boolean cloud_recording) {
        this.cloud_recording = cloud_recording;
    }
}
