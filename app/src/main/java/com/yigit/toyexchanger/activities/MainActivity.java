package com.yigit.toyexchanger.activities;

import static com.yigit.toyexchanger.Config.BANNER_HOME;
import static com.yigit.toyexchanger.Config.INTERSTITIAL_RECIPES_LIST;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yigit.toyexchanger.BuildConfig;
import com.yigit.toyexchanger.Config;

import com.yigit.toyexchanger.R;
import com.yigit.toyexchanger.YeniRekalm.YeniReklamActivity;
import com.yigit.toyexchanger.database.prefs.AdsPref;
import com.yigit.toyexchanger.database.prefs.SharedPref;
import com.yigit.toyexchanger.models.dataholder;
import com.yigit.toyexchanger.utils.AdsManager;
import com.yigit.toyexchanger.utils.AppBarLayoutBehavior;
import com.yigit.toyexchanger.utils.Constant;
import com.yigit.toyexchanger.utils.RtlViewPager;
import com.yigit.toyexchanger.utils.Tools;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.gms.tasks.Task;
import com.solodroid.push.sdk.provider.OneSignalPush;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
   FloatingActionButton btn_floating;
    ImageView img_notify ;
    TextView teslimat_adresi;
    SharedPreferences sharedPreferences=null;
    SharedPreferences.Editor editor;

    private static final String TAG = "MainActivity";
    private ViewPager viewPager;
    private RtlViewPager viewPagerRTL;
    private AppUpdateManager appUpdateManager;
    private long exitTime = 0;
    private BottomSheetDialog mBottomSheetDialog;
    BottomNavigationView navigation;
    Toolbar toolbar;
    SharedPref sharedPref;
    AdsPref adsPref;
    CoordinatorLayout parentView;
    AdsManager adsManager;
    Tools tools;
    OneSignalPush.Builder onesignal;
    View lytExitDialog;
    LinearLayout lytPanelView;
    LinearLayout lytPanelDialog;
    String lastuser="";
    String teslimatValue="" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tools.getTheme(this);
        sharedPref = new SharedPref(this);
        adsPref = new AdsPref(this);
        tools = new Tools(this);
        setContentView(R.layout.activity_main);
        Tools.setNavigation(this, sharedPref);
        onesignal = new OneSignalPush.Builder(this);
        onesignal.requestNotificationPermission();
        sharedPref.resetPostToken();
        sharedPref.resetPageToken();
        img_notify=findViewById(R.id.img_notify);
        btn_floating=findViewById(R.id.btn_floating);
        teslimat_adresi=findViewById(R.id.teslimat_adresi);

        sharedPreferences = getSharedPreferences("MofidxMuhendis",0);
        lastuser= sharedPreferences.getString("username","");

        checkGuncelAdress();
        teslimatValue= sharedPreferences.getString("teslimatkey","");

        initComponent();

        adsManager = new AdsManager(this);
        adsManager.initializeAd();
        adsManager.updateConsentStatus();
        adsManager.loadBannerAd(BANNER_HOME);
        adsManager.loadInterstitialAd(INTERSTITIAL_RECIPES_LIST, adsPref.getInterstitialAdInterval());
        sharedPref.setIsFirstTimeLaunch(false);

        Tools.notificationOpenHandler(this, getIntent());

        if (!BuildConfig.DEBUG) {
            appUpdateManager = AppUpdateManagerFactory.create(getApplicationContext());
            checkUpdate();
            inAppReview();
        }

        initExitDialog();


        img_notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Şu anda bildirim bulunmamaktadır!", Toast.LENGTH_SHORT).show();
            }
        });

        teslimat_adresi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,ActivityTeslimatAdresi.class);
                startActivity(i);
            }
        });

        if (teslimatValue!=""){
//            // İki satırlı metni ayarla
            String birinciSatir = "Teslimat Adresi";
            String ikinciSatir = teslimatValue;
            String tamMetin = birinciSatir + "\n" + ikinciSatir;

// SpannableStringBuilder ile renkleri ayarla
            SpannableStringBuilder spannable = new SpannableStringBuilder(tamMetin);

// Birinci satırın rengini ayarla (açık gri)
            spannable.setSpan(new ForegroundColorSpan(Color.LTGRAY),
                    0, birinciSatir.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// İkinci satırın rengini ayarla (siyah)
            spannable.setSpan(new ForegroundColorSpan(Color.BLACK),
                    birinciSatir.length() + 1, tamMetin.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// TextView'e ayarla
            teslimat_adresi.setText(spannable);
//            teslimat_adresi.setText(teslimatValue);
        }


        btn_floating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, YeniReklamActivity.class);
                startActivity(i);
            }
        });


        //On Create
    }

    private void checkGuncelAdress() {


        FirebaseDatabase database  = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users").child(lastuser).child("Adreslerim");

// Verilerin olup olmadığını kontrol et
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {


                    // Veriler varsa
//                    Toast.makeText(getApplicationContext(), "Adresler mevcut!"+teslimatValue, Toast.LENGTH_SHORT).show();
                } else {

                    // Veriler yoksa
//                    Toast.makeText(getApplicationContext(), "Adres bulunamadı.", Toast.LENGTH_SHORT).show();
                    editor = sharedPreferences.edit();
                    editor.putString("teslimatkey","");
                    editor.commit();


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Hata durumunda

                Toast.makeText(getApplicationContext(), "Bir hata oluştu: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void showSnackBar(String msg) {
        Snackbar.make(parentView, msg, Snackbar.LENGTH_SHORT).show();
    }

    public void initComponent() {

        parentView = findViewById(R.id.tab_coordinator_layout);

        AppBarLayout appBarLayout = findViewById(R.id.tab_appbar_layout);
        ((CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams()).setBehavior(new AppBarLayoutBehavior());

        toolbar = findViewById(R.id.toolbar);
        Tools.setupToolbar(this, toolbar, getString(R.string.app_name), false);
        if (!sharedPref.getIsDarkTheme()) {
            toolbar.setPopupTheme(com.google.android.material.R.style.ThemeOverlay_AppCompat_Light);
        } else {
            Tools.darkToolbar(this, toolbar);
            toolbar.getContext().setTheme(com.google.android.material.R.style.ThemeOverlay_AppCompat_Dark);
        }

        navigation = findViewById(R.id.navigation);
        navigation.getMenu().clear();
        navigation.inflateMenu(R.menu.menu_navigation);
        if (sharedPref.getIsDarkTheme()) {
            navigation.setBackgroundColor(ContextCompat.getColor(this, R.color.color_dark_bottom_navigation));
        } else {
            navigation.setBackgroundColor(ContextCompat.getColor(this, R.color.color_light_bottom_navigation));
        }
        navigation.setLabelVisibilityMode(BottomNavigationView.LABEL_VISIBILITY_LABELED);

        viewPager = findViewById(R.id.viewpager);
        viewPagerRTL = findViewById(R.id.viewpager_rtl);
        if (Config.ENABLE_RTL_MODE) {
            tools.setupViewPagerRTL(this, viewPagerRTL, navigation, toolbar, sharedPref);
        } else {
            tools.setupViewPager(this, viewPager, navigation, toolbar, sharedPref);
        }

        Menu menu = navigation.getMenu();
        MenuItem hesabItem = menu.findItem(R.id.navigation_hesab);

        if (!Objects.equals(lastuser, "")){
            hesabItem.setTitle(lastuser); // تحديث العنوان باسم المستخدم
            // تحديث الأيقونة
            hesabItem.setIcon(R.drawable.blueprofile); // استبدل ic_new_icon بالأيقونة الجديدة
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.action_search) {
            Intent intent = new Intent(getApplicationContext(), ActivitySearch.class);
            startActivity(intent);
            destroyBannerAd();
        } else if (menuItem.getItemId() == R.id.menu_settings) {
            Intent intent = new Intent(getApplicationContext(), ActivitySettings.class);
            startActivity(intent);
        } else if (menuItem.getItemId() == R.id.menu_rate) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID)));
        } else if (menuItem.getItemId() == R.id.menu_more) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(sharedPref.getMoreAppsUrl())));
        } else if (menuItem.getItemId() == R.id.menu_share) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text) + "\n" + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
            intent.setType("text/plain");
            startActivity(intent);
        } else if (menuItem.getItemId() == R.id.menu_about) {
            aboutDialog();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void aboutDialog() {
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        View view = inflater.inflate(R.layout.dialog_about, null);

        TextView txt_app_version = view.findViewById(R.id.txt_app_version);
        txt_app_version.setText(getString(R.string.msg_about_version) + " " + BuildConfig.VERSION_NAME);

        final MaterialAlertDialogBuilder alert = new MaterialAlertDialogBuilder(MainActivity.this);
        alert.setView(view);
        alert.setPositiveButton(R.string.dialog_option_ok, (dialog, which) -> dialog.dismiss());
        alert.show();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        if (Config.ENABLE_RTL_MODE) {
            if (viewPagerRTL.getCurrentItem() != 0) {
                viewPagerRTL.setCurrentItem((0), true);
            } else {
                exitApp();
            }
        } else {
            if (viewPager.getCurrentItem() != 0) {
                viewPager.setCurrentItem((0), true);
            } else {
                exitApp();
            }
        }
    }

    public void exitApp() {
        if (Config.ENABLE_EXIT_DIALOG) {
            if (lytExitDialog.getVisibility() != View.VISIBLE) {
                showDialog(true);
            }
        } else {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                showSnackBar(getString(R.string.press_again_to_exit));
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                destroyBannerAd();
                Constant.isAppOpen = false;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        adsManager.resumeBannerAd(BANNER_HOME);
        teslimatValue= sharedPreferences.getString("teslimatkey","");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyBannerAd();
        Constant.isAppOpen = false;
    }

    @Override
    public AssetManager getAssets() {
        return getResources().getAssets();
    }

    public void showInterstitialAd() {
        adsManager.showInterstitialAd();
    }

    public void destroyBannerAd() {
        adsManager.destroyBannerAd();
    }

    private void inAppReview() {
        if (sharedPref.getInAppReviewToken() <= 3) {
            sharedPref.updateInAppReviewToken(sharedPref.getInAppReviewToken() + 1);
        } else {
            ReviewManager manager = ReviewManagerFactory.create(this);
            Task<ReviewInfo> request = manager.requestReviewFlow();
            request.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    ReviewInfo reviewInfo = task.getResult();
                    manager.launchReviewFlow(MainActivity.this, reviewInfo).addOnFailureListener(e -> {
                    }).addOnCompleteListener(complete -> {
                                Log.d(TAG, "In-App Review Success");
                            }
                    ).addOnFailureListener(failure -> {
                        Log.d(TAG, "In-App Review Rating Failed");
                    });
                }
            }).addOnFailureListener(failure -> Log.d("In-App Review", "In-App Request Failed " + failure));
        }
        Log.d(TAG, "in app review token : " + sharedPref.getInAppReviewToken());
    }

    private void checkUpdate() {
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                startUpdateFlow(appUpdateInfo);
            } else if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                startUpdateFlow(appUpdateInfo);
            }
        });
    }

    @SuppressWarnings("deprecation")
    private void startUpdateFlow(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, this, Constant.IMMEDIATE_APP_UPDATE_REQ_CODE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.IMMEDIATE_APP_UPDATE_REQ_CODE) {
            if (resultCode == RESULT_CANCELED) {
                showSnackBar("Update canceled");
            } else if (resultCode == RESULT_OK) {
                showSnackBar("Update success!");
            } else {
                showSnackBar("Update Failed!");
                checkUpdate();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == OneSignalPush.NOTIFICATION_PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "notification permission granted");
            }
        }
    }

    public void initExitDialog() {

        lytExitDialog = findViewById(R.id.lyt_dialog_exit);
        lytPanelView = findViewById(R.id.lyt_panel_view);
        lytPanelDialog = findViewById(R.id.lyt_panel_dialog);

        if (sharedPref.getIsDarkTheme()) {
            lytPanelView.setBackgroundColor(getResources().getColor(R.color.color_dialog_background_dark_overlay));
            lytPanelDialog.setBackgroundResource(R.drawable.bg_rounded_dark);
        } else {
            lytPanelView.setBackgroundColor(getResources().getColor(R.color.color_dialog_background_light));
            lytPanelDialog.setBackgroundResource(R.drawable.bg_rounded_default);
        }

        lytPanelView.setOnClickListener(view -> {
            //empty state
        });

        LinearLayout nativeAdView = findViewById(R.id.native_ad_view);
        Tools.setNativeAdStyle(this, nativeAdView, Constant.NATIVE_AD_STYLE_EXIT_DIALOG);
        adsManager.loadNativeAd(Config.NATIVE_AD_EXIT_DIALOG, Constant.NATIVE_AD_STYLE_EXIT_DIALOG);

        Button btnCancel = findViewById(R.id.btn_cancel);
        Button btnExit = findViewById(R.id.btn_exit);

        FloatingActionButton btnRate = findViewById(R.id.btn_rate);
        FloatingActionButton btnShare = findViewById(R.id.btn_share);

        btnCancel.setOnClickListener(view -> showDialog(false));

        btnExit.setOnClickListener(view -> {
            showDialog(false);
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                finish();
                // Tüm aktiviteleri kapat
                finishAffinity();

                // Uygulamanın tamamen kapanmasını sağla
                System.exit(0);

                destroyBannerAd();
                Constant.isAppOpen = false;
            }, 300);
        });

        btnRate.setOnClickListener(v -> {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID)));
            showDialog(false);
        });

        btnShare.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text) + "\n" + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
            intent.setType("text/plain");
            startActivity(intent);
            showDialog(false);
        });
    }

    private void showDialog(boolean show) {
        if (show) {
            lytExitDialog.setVisibility(View.VISIBLE);
            slideUp(findViewById(R.id.dialog_card_view));
            ObjectAnimator.ofFloat(lytExitDialog, View.ALPHA, 0.1f, 1.0f).setDuration(300).start();
            Tools.fullScreenMode(this, true);
        } else {
            slideDown(findViewById(R.id.dialog_card_view));
            ObjectAnimator.ofFloat(lytExitDialog, View.ALPHA, 1.0f, 0.1f).setDuration(300).start();
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                lytExitDialog.setVisibility(View.GONE);
                Tools.fullScreenMode(this, false);
                Tools.setNavigation(this, sharedPref);
            }, 300);
        }
    }

    public void slideUp(View view) {
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(0, 0, findViewById(R.id.main_content).getHeight(), 0);
        animate.setDuration(300);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    public void slideDown(View view) {
        TranslateAnimation animate = new TranslateAnimation(0, 0, 0, findViewById(R.id.main_content).getHeight());
        animate.setDuration(300);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

}
