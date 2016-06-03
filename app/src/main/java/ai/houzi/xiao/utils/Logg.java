package ai.houzi.xiao.utils;

import android.util.Log;

import ai.houzi.xiao.activity.main.MyApplication;

/**
 * Log管理
 */
public class Logg {
    private static final String TAG = MyApplication.packageName;
    private static final boolean isLog = true;

    public static void e(Object text) {
        if (isLog) {
            String line = "╔";
            String line2 = "╚";
            String mText = "║   小侯子====>>====" + text.toString() + "  ║";
            for (int i = 0; i < mText.length(); i++) {
                line += "═";
                line2 += "═";
            }
            Log.e(TAG, line + "╗");
            Log.e(TAG, mText);
            Log.e(TAG, line2 + "╝");
        }
    }

    public static void i(Object text) {
        if (isLog) {
            String line = "╔";
            String line2 = "╚";
            String mText = "║小侯子====>>====" + text.toString() + "║";
            for (int i = 0; i < mText.length(); i++) {
                line += "═";
                line2 += "═";
            }
            Log.i(TAG, line + "╗");
            Log.i(TAG, mText);
            Log.i(TAG, line2 + "╝");
        }
    }

    public static void d(Object text) {
        if (isLog) {
            String line = "╔";
            String line2 = "╚";
            String mText = "║小侯子====>>====" + text.toString() + "║";
            for (int i = 0; i < mText.length(); i++) {
                line += "═";
                line2 += "═";
            }
            Log.d(TAG, line + "╗");
            Log.d(TAG, mText);
            Log.d(TAG, line2 + "╝");
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
