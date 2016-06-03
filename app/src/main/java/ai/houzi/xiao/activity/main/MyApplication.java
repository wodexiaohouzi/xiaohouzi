package ai.houzi.xiao.activity.main;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class MyApplication extends Application {

    //字体
    public static Typeface zhongqi;
    public static Typeface gongzhang;
    // 全局的context
    public static Context context;
    // 屏幕的宽(像素)
    public static int sWidth;
    // 屏幕的高(像素)
    public static int sHeight;
    public static String userId;
    public static String userPhone;
    //获取到主线程的handler
    private static Handler mMainThreadHanler;
    //获取到主线程
    private static Thread mMainThread;
    //获取到主线程的id
    private static int mMainThreadId;
    public static String packageName;
    public static String locationCity;
    public static String locationCityKey;

    public static Handler getMainThreadHandler() {
        return mMainThreadHanler;
    }

    public static Thread getMainThread() {
        return mMainThread;
    }

    public static int getMainThreadId() {
        return mMainThreadId;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        sWidth = dm.widthPixels;// 屏幕宽度
        sHeight = dm.heightPixels;// 屏幕高度
        // =========================加载字体=================================
        zhongqi = Typeface.createFromAsset(getAssets(), "fonts/zhongqi.ttf");
        gongzhang = Typeface.createFromAsset(getAssets(), "fonts/gongzhang.ttf");

        packageName = this.getPackageName();
//        try {
//            PackageInfo packageInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
//            packageInfo.versionName;
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }

        mMainThreadHanler = new Handler();
        mMainThread = Thread.currentThread();
        mMainThreadId = android.os.Process.myTid();
    }
}
