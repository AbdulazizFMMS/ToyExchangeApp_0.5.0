package com.yigit.toyexchanger.callbacks;

import com.yigit.toyexchanger.models.Ads;
import com.yigit.toyexchanger.models.App;
import com.yigit.toyexchanger.models.Blog;
import com.yigit.toyexchanger.models.Category;

import java.util.ArrayList;
import java.util.List;

public class CallbackConfig {

    public Blog blog = null;
    public App app = null;
    public Ads ads = null;
    public List<Category> labels = new ArrayList<>();

}
