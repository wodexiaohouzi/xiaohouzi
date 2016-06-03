package ai.houzi.xiao.utils;

import android.content.Context;
import android.content.SharedPreferences;

import ai.houzi.xiao.activity.main.MyApplication;

/**
 * SharedPreferences
 */
public class SharedPerUtil {
    public static final String USER_ACCOUNT = "user_account";
    public static final String USER_INFO = "user_info";
    public static final String LOGIN_HISTORY = "login_history";

    public static final String USER = "user_";
    SharedPreferences preferences;
    static SharedPerUtil perUtil;

    private SharedPerUtil() {

    }

    private static SharedPerUtil getInstence() {
        if (perUtil == null) {
            perUtil = new SharedPerUtil();
        }
        return perUtil;
    }

    private SharedPreferences getSharedPerferences(String name) {
        return MyApplication.context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }


    public static String getUser(String userId) {
        return getInstence().getSharedPerferences(USER_INFO).getString(USER + userId, null);
    }

    public static String getLoginHistory() {
        return getInstence().getSharedPerferences(LOGIN_HISTORY).getString(LOGIN_HISTORY, "");
    }

    public static void setLoginHistory(String history) {
        getInstence().getSharedPerferences(LOGIN_HISTORY).edit().putString(LOGIN_HISTORY, history).commit();
    }
}
