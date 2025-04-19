package com.yigit.toyexchanger.utils;

import static com.yigit.toyexchanger.utils.Constant.PAGER_NUMBER;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.yigit.toyexchanger.fragments.FragmentCategory;
import com.yigit.toyexchanger.fragments.FragmentFavorite;
import com.yigit.toyexchanger.fragments.FragmentProfile;
import com.yigit.toyexchanger.fragments.FragmentRecipes;

@SuppressWarnings("ALL")
public class NavigationAdapter {

    public static class BottomNavigationAdapter extends FragmentPagerAdapter {

        public BottomNavigationAdapter(FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new FragmentRecipes();
                case 1:
                    return new FragmentCategory();
                case 2:
                    return new FragmentFavorite();
                case 3:
                    return new FragmentProfile();
            }
            return null;
        }

        @Override
        public int getCount() {
            return PAGER_NUMBER;
        }

    }

}
