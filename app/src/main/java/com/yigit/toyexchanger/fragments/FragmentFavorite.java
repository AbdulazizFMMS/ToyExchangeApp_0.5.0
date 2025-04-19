package com.yigit.toyexchanger.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.yigit.toyexchanger.R;
import com.yigit.toyexchanger.activities.ActivityRecipesDetail;
import com.yigit.toyexchanger.activities.MainActivity;
import com.yigit.toyexchanger.adapters.AdapterFavorite;
import com.yigit.toyexchanger.database.prefs.SharedPref;
import com.yigit.toyexchanger.database.sqlite.DbFavorite;
import com.yigit.toyexchanger.models.Post;
import com.yigit.toyexchanger.utils.Constant;

import java.util.ArrayList;
import java.util.List;

public class FragmentFavorite extends Fragment {

    List<Post> posts = new ArrayList<>();
    private View rootView;
    LinearLayout lytNoFavorite;
    private RecyclerView recyclerView;
    AdapterFavorite adapterFavorite;
    DbFavorite dbFavorite;
    SharedPref sharedPref;
    Activity activity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_favorite, container, false);

        sharedPref = new SharedPref(activity);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        lytNoFavorite = rootView.findViewById(R.id.lyt_no_favorite);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(sharedPref.getRecipesColumns(), StaggeredGridLayoutManager.VERTICAL));

        loadDataFromDatabase();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDataFromDatabase();
    }

    public void loadDataFromDatabase() {
        dbFavorite = new DbFavorite(activity);
        posts = dbFavorite.getAllData();

        adapterFavorite = new AdapterFavorite(activity, recyclerView, posts);
        recyclerView.setAdapter(adapterFavorite);

        showNoItemView(posts.size() == 0);

        adapterFavorite.setOnItemClickListener((v, obj, position) -> {
            Intent intent = new Intent(activity, ActivityRecipesDetail.class);
            intent.putExtra(Constant.EXTRA_OBJC, obj);
            startActivity(intent);
            sharedPref.savePostId(obj.id);
            
            ((MainActivity) activity).showInterstitialAd();
            ((MainActivity) activity).destroyBannerAd();
        });

    }

    private void showNoItemView(boolean show) {
        ((TextView) rootView.findViewById(R.id.no_item_message)).setText(R.string.no_favorite_found);
        if (show) {
            lytNoFavorite.setVisibility(View.VISIBLE);
        } else {
            lytNoFavorite.setVisibility(View.GONE);
        }
    }

}
