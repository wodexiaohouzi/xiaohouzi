package com.qqalbum.imagepicker.utils;

import android.util.Log;

/**
 * Created by hp on 2016/4/6.
 */
public class Logg {
    private static final String TAG = "小侯子：";
    private static boolean isLog = true;

    public static void e(Object text) {
        if (isLog) {
            Log.e(TAG, "小侯子====>>====" + text);
        }
    }

    public static void i(Object text) {
        if (isLog) {
            Log.i(TAG, "小侯子====>>====" + text);
        }
    }

    public static void d(Object text) {
        if (isLog) {
            Log.d(TAG, "小侯子====>>====" + text);
        }
    }


    public static void e(String tag, Object text) {
        if (isLog) {
            Log.e(tag, "小侯子====>>====" + text);
        }
    }

    public static void i(String tag, Object text) {
        if (isLog) {
            Log.i(tag, "小侯子====>>====" + text);
        }
    }

    public static void d(String tag, Object text) {
        if (isLog) {
            Log.d(tag, "小侯子====>>====" + text);
        }
    }
}
