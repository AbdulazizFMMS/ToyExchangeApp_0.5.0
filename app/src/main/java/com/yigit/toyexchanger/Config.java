package com.yigit.toyexchanger;

import com.yigit.toyexchanger.utils.Constant;

public class Config {

    //please check the documentation for the guide to generate your access key
    public static final String ACCESS_KEY = "WVVoU01HTklUVFpNZVRsclkyMXNNbHBUTlc1aU1qbHVZa2RWZFZreU9YUk1NbHB3WWtkVmRscERPSGhhYmtWM1RUQlNTVXhWZEhOU2FtUnVUMWQ0Y0ZkWVFuaFRSVGxhWlZkd1Rsa3diRXBTV0dzeFZXNUZkbVJ0Ykd4a2Vqa3hZek5CT1dNeWFHaGpiV3gxV2pFNWFHTklRbk5oVjA1b1pFZHNkbUpyYkd0WU1rNTJZbE0xTldGWFpIQmtRelV3WWpOc2JHVkhUbTlaVnpWdVdsaEtabGxZUW5kaVIyeHFXVmhTY0dJeU5VcGFSamxvWlcxc05tVnRSbmxaWnowOQ==";

    //default RECIPES columns count for the first time launch, supported value : Constant.RECIPES_TWO_COLUMNS or Constant.RECIPES_THREE_COLUMNS
    public static final int DEFAULT_RECIPES_COLUMNS = Constant.RECIPES_TWO_COLUMNS;

    //label sorting, supported value : Constant.LABEL_NAME_ASCENDING, Constant.LABEL_NAME_DESCENDING or Constant.LABEL_DEFAULT
    public static final String LABELS_SORTING = Constant.LABEL_NAME_ASCENDING;

    //category columns count, supported value : Constant.CATEGORY_ONE_COLUMNS or Constant.CATEGORY_TWO_COLUMNS
    public static final int CATEGORY_COLUMNS_COUNT = Constant.CATEGORY_ONE_COLUMNS;

    //RTL direction, e.g : for Arabic Language
    public static final boolean ENABLE_RTL_MODE = false;

    //enable copy text in the content description
    public static final boolean ENABLE_TEXT_SELECTION = false;

    //set true if you want to first image in the post details as the primary image
    public static final boolean FIRST_POST_IMAGE_AS_MAIN_IMAGE = true;

    //set true if you want to display related recipes in the recipes description
    public static final boolean DISPLAY_RELATED_RECIPES = true;

    //show dialog message when user exit app
    public static final boolean ENABLE_EXIT_DIALOG = true;

    //GDPR EU Consent
    public static final boolean ENABLE_GDPR_EU_CONSENT = true;

    //Ad Placement in the particular screen, 1 to enable and 0 to disable
    public static final boolean BANNER_HOME = false;
    public static final boolean BANNER_RECIPES_DETAIL = false;
    public static final boolean BANNER_CATEGORY_DETAIL = false;
    public static final boolean BANNER_SEARCH = false;
    public static final boolean INTERSTITIAL_RECIPES_LIST = false;
    public static final boolean INTERSTITIAL_RECIPES_DETAIL = false;
    public static final boolean NATIVE_AD_RECIPES_LIST = false;
    public static final boolean NATIVE_AD_RECIPES_DETAIL = false;
    public static final boolean NATIVE_AD_EXIT_DIALOG = false;

}