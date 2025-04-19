package com.yigit.toyexchanger.utils;

import static com.solodroid.ads.sdk.util.Constant.IRONSOURCE;

import android.app.Activity;

import com.yigit.toyexchanger.BuildConfig;
import com.yigit.toyexchanger.Config;
import com.yigit.toyexchanger.R;
import com.yigit.toyexchanger.database.prefs.AdsPref;
import com.yigit.toyexchanger.database.prefs.SharedPref;
import com.yigit.toyexchanger.models.Ads;
import com.yigit.toyexchanger.models.App;
import com.solodroid.ads.sdk.format.AdNetwork;
import com.solodroid.ads.sdk.format.BannerAd;
import com.solodroid.ads.sdk.format.InterstitialAd;
import com.solodroid.ads.sdk.format.NativeAd;
import com.solodroid.ads.sdk.gdpr.GDPR;

public class AdsManager {

    Activity activity;
    AdNetwork.Initialize adNetwork;
    BannerAd.Builder bannerAd;
    InterstitialAd.Builder interstitialAd;
    NativeAd.Builder nativeAd;
    SharedPref sharedPref;
    AdsPref adsPref;
    GDPR gdpr;

    public AdsManager(Activity activity) {
        this.activity = activity;
        this.sharedPref = new SharedPref(activity);
        this.adsPref = new AdsPref(activity);
        this.gdpr = new GDPR(activity);
        adNetwork = new AdNetwork.Initialize(activity);
        bannerAd = new BannerAd.Builder(activity);
        interstitialAd = new InterstitialAd.Builder(activity);
        nativeAd = new NativeAd.Builder(activity);
    }

    public void initializeAd() {
        if (adsPref.getAdStatus()) {
            adNetwork.setAdStatus("1")
                    .setAdNetwork(adsPref.getMainAds())
                    .setBackupAdNetwork(adsPref.getBackupAds())
                    .setStartappAppId(adsPref.getStartappAppId())
                    .setUnityGameId(adsPref.getUnityGameId())
                    .setIronSourceAppKey(adsPref.getIronSourceAppKey())
                    .setWortiseAppId(adsPref.getWortiseAppId())
                    .setDebug(BuildConfig.DEBUG)
                    .build();
        }
    }

    public void loadBannerAd(boolean placement) {
        if (adsPref.getAdStatus()) {
            if (placement) {
                bannerAd.setAdStatus("1")
                        .setAdNetwork(adsPref.getMainAds())
                        .setBackupAdNetwork(adsPref.getBackupAds())
                        .setAdMobBannerId(adsPref.getAdMobBannerId())
                        .setGoogleAdManagerBannerId(adsPref.getAdManagerBannerId())
                        .setFanBannerId(adsPref.getFanBannerId())
                        .setUnityBannerId(adsPref.getUnityBannerId())
                        .setAppLovinBannerId(adsPref.getApplovinMaxBannerId())
                        .setAppLovinBannerZoneId(adsPref.getApplovinDiscoveryBannerZoneId())
                        .setIronSourceBannerId(adsPref.getIronSourceBannerId())
                        .setWortiseBannerId(adsPref.getWortiseBannerId())
                        .setDarkTheme(sharedPref.getIsDarkTheme())
                        .setPlacementStatus(1)
                        .setLegacyGDPR(false)
                        .build();
            }
        }
    }

    public void loadInterstitialAd(boolean placement, int interval) {
        if (adsPref.getAdStatus()) {
            if (placement) {
                interstitialAd.setAdStatus("1")
                        .setAdNetwork(adsPref.getMainAds())
                        .setBackupAdNetwork(adsPref.getBackupAds())
                        .setAdMobInterstitialId(adsPref.getAdMobInterstitialId())
                        .setGoogleAdManagerInterstitialId(adsPref.getAdManagerInterstitialId())
                        .setFanInterstitialId(adsPref.getFanInterstitialId())
                        .setUnityInterstitialId(adsPref.getUnityInterstitialId())
                        .setAppLovinInterstitialId(adsPref.getApplovinMaxInterstitialId())
                        .setAppLovinInterstitialZoneId(adsPref.getApplovinDiscoveryInterstitialZoneId())
                        .setIronSourceInterstitialId(adsPref.getIronSourceInterstitialId())
                        .setWortiseInterstitialId(adsPref.getWortiseInterstitialId())
                        .setInterval(interval)
                        .setPlacementStatus(1)
                        .setLegacyGDPR(false)
                        .build();
            }
        }
    }

    public void loadNativeAd(boolean placement, String style) {
        if (adsPref.getAdStatus()) {
            if (placement) {
                nativeAd.setAdStatus("1")
                        .setAdNetwork(adsPref.getMainAds())
                        .setBackupAdNetwork(adsPref.getBackupAds())
                        .setAdMobNativeId(adsPref.getAdMobNativeId())
                        .setAdManagerNativeId(adsPref.getAdManagerNativeId())
                        .setFanNativeId(adsPref.getFanNativeId())
                        .setAppLovinNativeId(adsPref.getApplovinMaxNativeManualId())
                        .setAppLovinDiscoveryMrecZoneId(adsPref.getApplovinDiscoveryBannerMrecZoneId())
                        .setPlacementStatus(1)
                        .setDarkTheme(sharedPref.getIsDarkTheme())
                        .setLegacyGDPR(false)
                        .setNativeAdBackgroundColor(android.R.color.transparent, android.R.color.transparent)
                        .setNativeAdStyle(style)
                        .build();
                if (sharedPref.getIsDarkTheme()) {
                    nativeAd.setNativeAdBackgroundResource(R.drawable.bg_native_dark);
                } else {
                    nativeAd.setNativeAdBackgroundResource(R.drawable.bg_native_light);
                }
            }
        }
    }

    public void showInterstitialAd() {
        interstitialAd.show();
    }

    public void destroyBannerAd() {
        bannerAd.destroyAndDetachBanner();
    }

    public void resumeBannerAd(boolean placement) {
        if (adsPref.getAdStatus() && !adsPref.getIronSourceBannerId().equals("0")) {
            if (adsPref.getMainAds().equals(IRONSOURCE) || adsPref.getBackupAds().equals(IRONSOURCE)) {
                loadBannerAd(placement);
            }
        }
    }

    public void updateConsentStatus() {
        if (Config.ENABLE_GDPR_EU_CONSENT) {
            gdpr.updateGDPRConsentStatus(adsPref.getMainAds(), false, false);
        }
    }

    public void saveAds(AdsPref adsPref, Ads ads) {
        adsPref.saveAds(
                ads.ad_status,
                ads.main_ads,
                ads.backup_ads,
                ads.admob_banner_id,
                ads.admob_interstitial_id,
                ads.admob_native_id,
                ads.admob_app_open_id,
                ads.ad_manager_banner_id,
                ads.ad_manager_interstitial_id,
                ads.ad_manager_native_id,
                ads.ad_manager_app_open_id,
                ads.fan_banner_id,
                ads.fan_interstitial_id,
                ads.fan_native_id,
                ads.startapp_app_id,
                ads.unity_game_id,
                ads.unity_banner_id,
                ads.unity_interstitial_id,
                ads.applovin_max_banner_id,
                ads.applovin_max_interstitial_id,
                ads.applovin_max_native_manual_id,
                ads.applovin_max_app_open_id,
                ads.applovin_discovery_banner_zone_id,
                ads.applovin_discovery_banner_mrec_zone_id,
                ads.applovin_discovery_interstitial_zone_id,
                ads.ironsource_app_key,
                ads.ironsource_banner_id,
                ads.ironsource_interstitial_id,
                ads.wortise_app_id,
                ads.wortise_banner_id,
                ads.wortise_interstitial_id,
                ads.wortise_native_id,
                ads.wortise_app_open_id,
                ads.interstitial_ad_interval
        );
    }

    public void saveConfig(SharedPref sharedPref, App app) {
        sharedPref.saveConfig(
                app.more_apps_url,
                app.redirect_url,
                app.privacy_policy_url,
                app.custom_label_list
        );
    }

}
