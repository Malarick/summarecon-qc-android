package com.summarecon.qcapp.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.File;

public class Configuration {

    public static String APP_EXTERNAL_IMAGES_DIRECTORY;
    public static String APP_EXTERNAL_DATABASE_DIRECTORY;
    public static String APP_EXTERNAL_CACHE_DIRECTORY;
    public static String APP_EXTERNAL_TEMP_DIRECTORY;
    public static String APP_EXTERNAL_DATABASE_SCRIPT_DIRECTORY;
    public static String DATABASE_NAME = "summareconqc.db";
    public static String DATABASE_SCRIPT_NAME = "summareconqc.sql";
    private static SharedPreferences sharedPreferences;

    public Configuration(Context context) {
        APP_EXTERNAL_IMAGES_DIRECTORY = context.getExternalFilesDir("images").getAbsolutePath();
        APP_EXTERNAL_CACHE_DIRECTORY = context.getExternalCacheDir().getAbsolutePath();
        APP_EXTERNAL_TEMP_DIRECTORY = context.getExternalFilesDir("tmp").getAbsolutePath();
        APP_EXTERNAL_DATABASE_DIRECTORY = context.getExternalFilesDir("databases").getAbsolutePath() + File.separator + DATABASE_NAME;
        APP_EXTERNAL_DATABASE_DIRECTORY = context.getExternalFilesDir(null).getAbsolutePath().replace("files", "") + "databases" + File.separator + DATABASE_NAME;
        APP_EXTERNAL_DATABASE_SCRIPT_DIRECTORY = APP_EXTERNAL_TEMP_DIRECTORY + File.separator + DATABASE_SCRIPT_NAME;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }
}