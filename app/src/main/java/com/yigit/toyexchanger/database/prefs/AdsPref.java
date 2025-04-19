package com.yigit.toyexchanger.database.prefs;

import android.content.Context;
import android.content.SharedPreferences;

public class AdsPref {

    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public AdsPref(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("ads_setting", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveAds(boolean adStatus, String mainAds, String backupAds, String adMobBannerId, String adMobInterstitialId, String adMobNativeId, String adMobAppOpenId, String adManagerBannerId, String adManagerInterstitialId, String adManagerNativeId, String adManagerAppOpenId, String fanBannerId, String fanInterstitialId, String fanNativeId, String startAppId, String unityGameId, String unityBannerId, String unityInterstitialId, String applovinMaxBannerId, String applovinMaxInterstitialId, String applovinMaxNativeAdManualId, String applovinMaxAppOpenId, String applovinDiscoveryBannerZoneId, String applovinDiscoveryBannerMrecZoneId, String applovinDiscoveryInterstitialZoneId, String ironsourceAppKey, String ironsourceBannerId, String ironsourceInterstitialId, String wortiseAppId, String wortiseBannerId, String wortiseInterstitialId, String wortiseNativeId, String wortiseAppOpenId, int interstitialAdInterval) {
        editor.putBoolean("ad_status", adStatus);
        editor.putString("main_ads", mainAds);
        editor.putString("backup_ads", backupAds);
        editor.putString("admob_banner_id", adMobBannerId);
        editor.putString("admob_interstitial_id", adMobInterstitialId);
        editor.putString("admob_native_id", adMobNativeId);
        editor.putString("admob_app_open_id", adMobAppOpenId);
        editor.putString("ad_manager_banner_id", adManagerBannerId);
        editor.putString("ad_manager_interstitial_id", adManagerInterstitialId);
        editor.putString("ad_manager_native_id", adManagerNativeId);
        editor.putString("ad_manager_app_open_id", adManagerAppOpenId);
        editor.putString("fan_banner_id", fanBannerId);
        editor.putString("fan_interstitial_id", fanInterstitialId);
        editor.putString("fan_native_id", fanNativeId);
        editor.putString("startapp_app_id", startAppId);
        editor.putString("unity_game_id", unityGameId);
        editor.putString("unity_banner_id", unityBannerId);
        editor.putString("unity_interstitial_id", unityInterstitialId);
        editor.putString("applovin_max_banner_id", applovinMaxBannerId);
        editor.putString("applovin_max_interstitial_id", applovinMaxInterstitialId);
        editor.putString("applovin_max_native_manual_id", applovinMaxNativeAdManualId);
        editor.putString("applovin_max_app_open_id", applovinMaxAppOpenId);
        editor.putString("applovin_discovery_banner_zone_id", applovinDiscoveryBannerZoneId);
        editor.putString("applovin_discovery_banner_mrec_zone_id", applovinDiscoveryBannerMrecZoneId);
        editor.putString("applovin_discovery_interstitial_zone_id", applovinDiscoveryInterstitialZoneId);
        editor.putString("ironsource_app_key", ironsourceAppKey);
        editor.putString("ironsource_banner_id", ironsourceBannerId);
        editor.putString("ironsource_interstitial_id", ironsourceInterstitialId);
        editor.putString("wortise_app_id", wortiseAppId);
        editor.putString("wortise_banner_id", wortiseBannerId);
        editor.putString("wortise_interstitial_id", wortiseInterstitialId);
        editor.putString("wortise_native_id", wortiseNativeId);
        editor.putString("wortise_app_open_id", wortiseAppOpenId);
        editor.putInt("interstitial_ad_interval", interstitialAdInterval);
        editor.apply();
    }

    public boolean getAdStatus() {
        return sharedPreferences.getBoolean("ad_status", true);
    }

    public String getMainAds() {
        return sharedPreferences.getString("main_ads", "0");
    }

    public String getBackupAds() {
        return sharedPreferences.getString("backup_ads", "none");
    }

    public String getAdMobBannerId() {
        return sharedPreferences.getString("admob_banner_id", "0");
    }

    public String getAdMobInterstitialId() {
        return sharedPreferences.getString("admob_interstitial_id", "0");
    }

    public String getAdMobNativeId() {
        return sharedPreferences.getString("admob_native_id", "0");
    }

    public String getAdMobAppOpenId() {
        return sharedPreferences.getString("admob_app_open_id", "0");
    }

    public String getAdManagerBannerId() {
        return sharedPreferences.getString("ad_manager_banner_id", "0");
    }

    public String getAdManagerInterstitialId() {
        return sharedPreferences.getString("ad_manager_interstitial_id", "0");
    }

    public String getAdManagerNativeId() {
        return sharedPreferences.getString("ad_manager_native_id", "0");
    }

    public String getAdManagerAppOpenId() {
        return sharedPreferences.getString("ad_manager_app_open_id", "0");
    }

    public String getFanBannerId() {
        return sharedPreferences.getString("fan_banner_id", "0");
    }

    public String getFanInterstitialId() {
        return sharedPreferences.getString("fan_interstitial_id", "0");
    }

    public String getFanNativeId() {
        return sharedPreferences.getString("fan_native_id", "0");
    }


    public String getStartappAppId() {
        return sharedPreferences.getString("startapp_app_id", "0");
    }

    public String getUnityGameId() {
        return sharedPreferences.getString("unity_game_id", "0");
    }

    public String getUnityBannerId() {
        return sharedPreferences.getString("unity_banner_id", "banner");
    }

    public String getUnityInterstitialId() {
        return sharedPreferences.getString("unity_interstitial_id", "video");
    }

    public String getApplovinMaxBannerId() {
        return sharedPreferences.getString("applovin_max_banner_id", "0");
    }

    public String getApplovinMaxInterstitialId() {
        return sharedPreferences.getString("applovin_max_interstitial_id", "0");
    }

    public String getApplovinMaxNativeManualId() {
        return sharedPreferences.getString("applovin_max_native_manual_id", "0");
    }

    public String getApplovinMaxAppOpenId() {
        return sharedPreferences.getString("applovin_max_app_open_id", "0");
    }

    public String getApplovinDiscoveryBannerZoneId() {
        return sharedPreferences.getString("applovin_discovery_banner_zone_id", "0");
    }

    public String getApplovinDiscoveryBannerMrecZoneId() {
        return sharedPreferences.getString("applovin_discovery_banner_mrec_zone_id", "0");
    }

    public String getApplovinDiscoveryInterstitialZoneId() {
        return sharedPreferences.getString("applovin_discovery_interstitial_zone_id", "0");
    }

    public String getIronSourceAppKey() {
        return sharedPreferences.getString("ironsource_app_key", "0");
    }

    public String getIronSourceBannerId() {
        return sharedPreferences.getString("ironsource_banner_id", "0");
    }

    public String getIronSourceInterstitialId() {
        return sharedPreferences.getString("ironsource_interstitial_id", "0");
    }

    public String getWortiseAppId() {
        return sharedPreferences.getString("wortise_app_id", "0");
    }

    public String getWortiseBannerId() {
        return sharedPreferences.getString("wortise_banner_id", "0");
    }

    public String getWortiseInterstitialId() {
        return sharedPreferences.getString("wortise_interstitial_id", "0");
    }

    public String getWortiseNativeId() {
        return sharedPreferences.getString("wortise_native_id", "0");
    }

    public String getWortiseAppOpenId() {
        return sharedPreferences.getString("wortise_app_open_id", "0");
    }

    public int getInterstitialAdInterval() {
        return sharedPreferences.getInt("interstitial_ad_interval", 3);
    }

    public void saveCounter(int counter) {
        editor.putInt("counter", counter);
        editor.apply();
    }

    public int getCounter() {
        return sharedPreferences.getInt("counter", 0);
    }

}
