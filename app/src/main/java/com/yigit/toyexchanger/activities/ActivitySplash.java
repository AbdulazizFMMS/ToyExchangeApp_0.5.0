package com.yigit.toyexchanger.activities;

import static com.solodroid.ads.sdk.util.Constant.ADMOB;
import static com.solodroid.ads.sdk.util.Constant.APPLOVIN;
import static com.solodroid.ads.sdk.util.Constant.APPLOVIN_MAX;
import static com.solodroid.ads.sdk.util.Constant.GOOGLE_AD_MANAGER;
import static com.solodroid.ads.sdk.util.Constant.WORTISE;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.yigit.toyexchanger.BuildConfig;
import com.yigit.toyexchanger.Config;
import com.yigit.toyexchanger.R;
import com.yigit.toyexchanger.callbacks.CallbackConfig;
import com.yigit.toyexchanger.callbacks.CallbackLabel;
import com.yigit.toyexchanger.database.prefs.AdsPref;
import com.yigit.toyexchanger.database.prefs.SharedPref;
import com.yigit.toyexchanger.database.sqlite.DbLabel;
import com.yigit.toyexchanger.models.Ads;
import com.yigit.toyexchanger.models.App;
import com.yigit.toyexchanger.models.Blog;
import com.yigit.toyexchanger.models.Category;
import com.yigit.toyexchanger.rests.RestAdapter;
import com.yigit.toyexchanger.utils.AdsManager;
import com.yigit.toyexchanger.utils.Constant;
import com.yigit.toyexchanger.utils.Tools;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivitySplash extends AppCompatActivity {

    public static final String TAG = "ActivitySplash";
    Call<CallbackConfig> callbackConfigCall = null;
    Call<CallbackLabel> callbackLabelCall = null;
    ImageView imgSplash;
    AdsManager adsManager;
    SharedPref sharedPref;
    AdsPref adsPref;
    App app;
    Blog blog;
    Ads ads;
    List<Category> labels = new ArrayList<>();
    DbLabel dbLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tools.transparentStatusBarNavigation(ActivitySplash.this);
        setContentView(R.layout.activity_splash);
        dbLabel = new DbLabel(this);
        sharedPref = new SharedPref(this);
        adsPref = new AdsPref(this);
        adsManager = new AdsManager(this);

        imgSplash = findViewById(R.id.img_splash);
        if (sharedPref.getIsDarkTheme()) {
            imgSplash.setImageResource(R.drawable.bg_splash_dark);
            Tools.darkNavigation(this);
        } else {
            imgSplash.setImageResource(R.drawable.bg_splash_default);
            Tools.lightNavigation(this);
        }

        new Handler(Looper.getMainLooper()).postDelayed(this::requestConfig, Constant.DELAY_SPLASH);

    }

    private void requestConfig() {
        if (Config.ACCESS_KEY.contains("XXXXX")) {
            new MaterialAlertDialogBuilder(this)
                    .setTitle("App not configured")
                    .setMessage("Please put your Server Key and Rest API Key from settings menu in your admin panel to AppConfig, you can see the documentation for more detailed instructions.")
                    .setPositiveButton(getString(R.string.dialog_option_ok), (dialogInterface, i) -> finish())
                    .setCancelable(false)
                    .show();
        } else {
            String data = Tools.decode(Config.ACCESS_KEY);
            String[] results = data.split("_applicationId_");
            String remoteUrl = results[0];
            String applicationId = results[1];

            if (applicationId.equals(BuildConfig.APPLICATION_ID)) {
                requestAPI(remoteUrl);
            } else {
                new MaterialAlertDialogBuilder(this)
                        .setTitle("Error")
                        .setMessage("Whoops! invalid access key or applicationId, please check your configuration")
                        .setPositiveButton(getString(R.string.dialog_option_ok), (dialog, which) -> finish())
                        .setCancelable(false)
                        .show();
            }
            Log.d(TAG, "Start request config");
        }
    }

    private void requestAPI(String remoteUrl) {
        if (remoteUrl.startsWith("http://") || remoteUrl.startsWith("https://")) {
            if (remoteUrl.contains("https://drive.google.com")) {
                String driveUrl = remoteUrl.replace("https://", "").replace("http://", "");
                List<String> data = Arrays.asList(driveUrl.split("/"));
                String googleDriveFileId = data.get(3);
                callbackConfigCall = RestAdapter.createApiGoogleDrive().getDriveJsonFileId(googleDriveFileId);
                Log.d(TAG, "Request API from Google Drive Share link");
                Log.d(TAG, "Google drive file id : " + data.get(3));
            } else {
                callbackConfigCall = RestAdapter.createApiJsonUrl().getJsonUrl(remoteUrl);
                Log.d(TAG, "Request API from Json Url");
            }
        } else {
            callbackConfigCall = RestAdapter.createApiGoogleDrive().getDriveJsonFileId(remoteUrl);
            Log.d(TAG, "Request API from Google Drive File ID");
        }
        callbackConfigCall.enqueue(new Callback<CallbackConfig>() {
            public void onResponse(@NonNull Call<CallbackConfig> call, @NonNull Response<CallbackConfig> response) {
                CallbackConfig resp = response.body();
                displayApiResults(resp);
            }

            public void onFailure(@NonNull Call<CallbackConfig> call, @NonNull Throwable th) {
                Log.e(TAG, "initialize failed");
                showOpenAdsIfAvailable(false);
            }
        });
    }

    private void displayApiResults(CallbackConfig resp) {

        if (resp != null) {
            app = resp.app;
            ads = resp.ads;
            blog = resp.blog;
            labels = resp.labels;

            sharedPref.saveBlogCredentials(blog.blogger_id, blog.api_key);
            adsManager.saveConfig(sharedPref, app);
            adsManager.saveAds(adsPref, ads);

            if (!app.status) {
                startActivity(new Intent(getApplicationContext(), ActivityRedirect.class));
                finish();
                Log.d(TAG, "App status is suspended");
            } else {
                if (app.custom_label_list) {
                    dbLabel.truncateTableCategory(DbLabel.TABLE_LABEL);
                    dbLabel.addListCategory(labels, DbLabel.TABLE_LABEL);
                    showOpenAdsIfAvailable(true);
                } else {
                    requestLabel();
                }
                Log.d(TAG, "App status is live");
            }
            Log.d(TAG, "initialize success");
        } else {
            Log.d(TAG, "initialize failed");
            showOpenAdsIfAvailable(false);
        }

    }

    private void requestLabel() {
        this.callbackLabelCall = RestAdapter.createApiCategory(sharedPref.getBloggerId()).getLabel();
        this.callbackLabelCall.enqueue(new Callback<CallbackLabel>() {
            public void onResponse(@NonNull Call<CallbackLabel> call, @NonNull Response<CallbackLabel> response) {
                CallbackLabel resp = response.body();
                if (resp == null) {
                    showOpenAdsIfAvailable(false);
                    return;
                }

                dbLabel.truncateTableCategory(DbLabel.TABLE_LABEL);
                if (sharedPref.getCustomLabelList()) {
                    dbLabel.addListCategory(labels, DbLabel.TABLE_LABEL);
                } else {
                    dbLabel.addListCategory(resp.feed.category, DbLabel.TABLE_LABEL);
                }

                showOpenAdsIfAvailable(true);
                Log.d(TAG, "Success initialize label with count " + resp.feed.category.size() + " items");
            }

            public void onFailure(@NonNull Call<CallbackLabel> call, @NonNull Throwable th) {
                Log.e("onFailure", "onFailure: " + th.getMessage());
                if (!call.isCanceled()) {
                    showOpenAdsIfAvailable(false);
                }
            }
        });
    }

    private void showOpenAdsIfAvailable(boolean show) {
        if (sharedPref.getIsFirstTimeLaunch()) {
            startMainActivity();
        } else {
            if (show) {
                if (adsPref.getAdStatus()) {
                    switch (adsPref.getMainAds()) {
                        case ADMOB:
                            if (!adsPref.getAdMobAppOpenId().equals("0")) {
                                ((MyApplication) getApplication()).showAdIfAvailable(ActivitySplash.this, this::startMainActivity);
                            } else {
                                startMainActivity();
                            }
                            break;
                        case GOOGLE_AD_MANAGER:
                            if (!adsPref.getAdManagerAppOpenId().equals("0")) {
                                ((MyApplication) getApplication()).showAdIfAvailable(ActivitySplash.this, this::startMainActivity);
                            } else {
                                startMainActivity();
                            }
                            break;
                        case APPLOVIN:
                        case APPLOVIN_MAX:
                            if (!adsPref.getApplovinMaxAppOpenId().equals("0")) {
                                ((MyApplication) getApplication()).showAdIfAvailable(ActivitySplash.this, this::startMainActivity);
                            } else {
                                startMainActivity();
                            }
                            break;
                        case WORTISE:
                            if (!adsPref.getWortiseAppOpenId().equals("0")) {
                                ((MyApplication) getApplication()).showAdIfAvailable(ActivitySplash.this, this::startMainActivity);
                            } else {
                                startMainActivity();
                            }
                            break;
                        default:
                            startMainActivity();
                            break;
                    }
                } else {
                    startMainActivity();
                }
            } else {
                startMainActivity();
            }
        }
    }

    private void startMainActivity() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
