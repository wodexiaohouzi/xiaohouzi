package ai.houzi.xiao.utils;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

/**
 * 打开系统工具
 */
public class SystemTools {

    public static void calendar(Context context) {
        try {
            Intent i = new Intent();
            ComponentName cn = null;
            if (Integer.parseInt(Build.VERSION.SDK) >= 8) {
                cn = new ComponentName("com.android.calendar",
                        "com.android.calendar.LaunchActivity");

            } else {
                cn = new ComponentName("com.google.android.calendar",
                        "com.android.calendar.LaunchActivity");
            }
            i.setComponent(cn);
            context.startActivity(i);
        } catch (ActivityNotFoundException e) {
            // TODO: handle exception
            Logg.e("ActivityNotFoundException", e.toString());
        }
    }
}
