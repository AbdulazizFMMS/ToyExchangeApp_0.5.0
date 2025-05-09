package com.yigit.toyexchanger.fragments;

import static com.yigit.toyexchanger.Config.CATEGORY_COLUMNS_COUNT;
import static com.yigit.toyexchanger.utils.Tools.EXTRA_OBJC;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.yigit.toyexchanger.Config;
import com.yigit.toyexchanger.R;
import com.yigit.toyexchanger.activities.ActivityCategoryDetail;
import com.yigit.toyexchanger.activities.MainActivity;
import com.yigit.toyexchanger.adapters.AdapterCategory;
import com.yigit.toyexchanger.callbacks.CallbackLabel;
import com.yigit.toyexchanger.database.prefs.SharedPref;
import com.yigit.toyexchanger.database.sqlite.DbLabel;
import com.yigit.toyexchanger.models.Category;
import com.yigit.toyexchanger.models.Feed;
import com.yigit.toyexchanger.rests.RestAdapter;
import com.yigit.toyexchanger.utils.Constant;
import com.yigit.toyexchanger.utils.Tools;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentCategory extends Fragment {

    private static final String TAG = "FragmentCategory";
    private View rootView;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private AdapterCategory adapterLabel;
    private ShimmerFrameLayout lytShimmer;
    private Call<CallbackLabel> callbackCall = null;
    SharedPref sharedPref;
    DbLabel dbLabel;
    Activity activity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_category, container, false);

        sharedPref = new SharedPref(activity);

        dbLabel = new DbLabel(activity);

        recyclerView = rootView.findViewById(R.id.recycler_view);
        lytShimmer = rootView.findViewById(R.id.shimmer_view_container);

        swipeRefreshLayout = rootView.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.color_light_primary);

        recyclerView.setLayoutManager(new GridLayoutManager(activity, CATEGORY_COLUMNS_COUNT));
        adapterLabel = new AdapterCategory(activity, new ArrayList<>());
        recyclerView.setAdapter(adapterLabel);

        loadLabelFromDatabase();

        // on swipe list
        swipeRefreshLayout.setOnRefreshListener(() -> {
            adapterLabel.resetListData();
            swipeProgress(true);
            new Handler().postDelayed(this::loadLabelFromDatabase, 1000);
        });

        initShimmerLayout();

        return rootView;
    }

    public void loadLabelFromDatabase() {

        swipeProgress(false);
        List<Category> categories = dbLabel.getAllCategory(DbLabel.TABLE_LABEL);
        adapterLabel.setListData(categories);

        if (categories.size() == 0) {
            showNoItemView(true);
        }

        //Log.d(TAG, "Total labels " + categories.size());
        //Log.d(TAG, "first item " + categories.get(0).term);

        // on item list clicked
        adapterLabel.setOnItemClickListener((v, obj, position) -> {
            Intent intent = new Intent(activity, ActivityCategoryDetail.class);
            intent.putExtra(EXTRA_OBJC, obj.term);
            startActivity(intent);

            ((MainActivity) activity).showInterstitialAd();
            ((MainActivity) activity).destroyBannerAd();
        });

    }

    private void requestAction() {
        showFailedView(false, "");
        swipeProgress(true);
        showNoItemView(false);
        new Handler(Looper.getMainLooper()).postDelayed(this::requestAPI, Constant.DELAY_REFRESH);
    }

    private void requestAPI() {
        this.callbackCall = RestAdapter.createApiCategory(sharedPref.getBloggerId()).getLabel();
        this.callbackCall.enqueue(new Callback<CallbackLabel>() {
            public void onResponse(@NonNull Call<CallbackLabel> call, @NonNull Response<CallbackLabel> response) {
                CallbackLabel resp = response.body();
                if (resp == null) {
                    onFailRequest();
                    return;
                }
                displayAllData(resp);
                swipeProgress(false);
                recyclerView.setVisibility(View.VISIBLE);
            }

            public void onFailure(@NonNull Call<CallbackLabel> call, @NonNull Throwable th) {
                Log.e("onFailure", "onFailure: " + th.getMessage());
                if (!call.isCanceled()) {
                    onFailRequest();
                }
            }
        });
    }

    private void displayAllData(CallbackLabel resp) {
        displayData(resp.feed);
    }

    public void displayData(final Feed feed) {

        recyclerView.setLayoutManager(new GridLayoutManager(activity, CATEGORY_COLUMNS_COUNT));
        adapterLabel = new AdapterCategory(activity, feed.category);

        recyclerView.setAdapter(adapterLabel);
        adapterLabel.setOnItemClickListener((view, obj, position) -> {
            Intent intent = new Intent(activity, ActivityCategoryDetail.class);
            intent.putExtra(EXTRA_OBJC, obj.term);
            startActivity(intent);

            ((MainActivity) activity).showInterstitialAd();
            ((MainActivity) activity).destroyBannerAd();
        });
    }

    private void onFailRequest() {
        swipeProgress(false);
        if (Tools.isConnect(activity)) {
            showFailedView(true, getString(R.string.failed_text));
        } else {
            showFailedView(true, getString(R.string.failed_text));
        }
    }

    private void showFailedView(boolean flag, String message) {
        View lytFailed = rootView.findViewById(R.id.lyt_failed);
        ((TextView) rootView.findViewById(R.id.failed_message)).setText(message);
        if (flag) {
            recyclerView.setVisibility(View.GONE);
            lytFailed.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            lytFailed.setVisibility(View.GONE);
        }
        rootView.findViewById(R.id.failed_retry).setOnClickListener(view -> requestAction());
    }

    private void showNoItemView(boolean show) {
        View lytNoItem = rootView.findViewById(R.id.lyt_no_item);
        ((TextView) rootView.findViewById(R.id.no_item_message)).setText(R.string.no_category_found);
        if (show) {
            recyclerView.setVisibility(View.GONE);
            lytNoItem.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            lytNoItem.setVisibility(View.GONE);
        }
    }

    private void swipeProgress(final boolean show) {
        if (!show) {
            swipeRefreshLayout.setRefreshing(show);
            recyclerView.setVisibility(View.VISIBLE);
            lytShimmer.setVisibility(View.GONE);
            lytShimmer.stopShimmer();
            return;
        }
        swipeRefreshLayout.post(() -> {
            swipeRefreshLayout.setRefreshing(show);
            recyclerView.setVisibility(View.GONE);
            lytShimmer.setVisibility(View.VISIBLE);
            lytShimmer.startShimmer();
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (callbackCall != null && callbackCall.isExecuted()) {
            callbackCall.cancel();
        }
        lytShimmer.stopShimmer();
    }

    private void initShimmerLayout() {
        ViewStub stub = rootView.findViewById(R.id.lytShimmerView);
        if (Config.CATEGORY_COLUMNS_COUNT == 2) {
            stub.setLayoutResource(R.layout.shimmer_category_grid);
        } else {
            stub.setLayoutResource(R.layout.shimmer_category_list);
        }
        stub.inflate();
    }

}
