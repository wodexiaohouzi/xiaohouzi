package ai.houzi.xiao.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.StatFs;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import ai.houzi.xiao.activity.main.MyApplication;

/**
 * =getDir
 * /data/data/com.auto/app_hehe
 * =getCacheDir
 * /data/data/com.auto/cache
 * =getFilesDir
 * /data/data/com.auto/files
 * =getObbDir
 * /storage/emulated/0/Android/obb/com.auto
 * =getCacheDir
 * /data/data/com.auto/files
 * =getExternalCacheDir
 * /storage/emulated/0/Android/data/com.auto/cache
 * =getExternalFilesDir
 * /storage/emulated/0/Android/data/com.auto/files/nihao
 * =Environment.getDataDirectory
 * /data
 * =Environment.getDownloadCacheDirectory
 * /cache
 * =Environment.getExternalStorageDirectory
 * /storage/emulated/0
 * =Environment.getRootDirectory
 * /system
 */
public class FilePath {
    /**
     * /storage/emulated/0
     */
    public static final String SDirPath = Environment.getExternalStorageDirectory().toString();

    /**
     * /storage/emulated/0/ai.houzi.zi/image:图片路径
     */
    public static final String imagePath = Environment.getExternalStorageDirectory().toString() + File.separator + MyApplication.packageName + File.separator + "images";
    /**
     * /system
     */
    public static final String rootDirPath = Environment.getRootDirectory().toString();
    /**
     * /cache
     */
    public static final String downloadCacheDirPath = Environment.getDownloadCacheDirectory().toString();
//----------------------跟项目相关的------------------------

    /**
     * 获取文件夹路径
     *
     * @param context
     * @param fileName
     * @return
     */
    public static String getDirPath(Context context, String fileName) {
        return context.getDir(fileName, Context.MODE_PRIVATE).toString();
    }

    /**
     * 获取文件夹
     *
     * @param context
     * @param fileName
     * @return
     */
    public static File getDir(Context context, String fileName) {
        return context.getDir(fileName, Context.MODE_PRIVATE);
    }

    //文件存储根目录
    public static String getFileRoot(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File external = context.getExternalFilesDir(null);
            if (external != null) {
                return external.getAbsolutePath();
            }
        }

        return context.getFilesDir().getAbsolutePath();
    }

    /**
     * @return SD卡是否存在
     */
    public static boolean existSDCard() {
        return (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED));
    }

    public interface Unit {
        int BYTE = 0, KBYTE = 1, MBYTE = 2;
    }

    /**
     * @param unit 单位类型：0：Byte，1：KB，other:MB
     * @return SD卡剩余空间
     */
    public static long getSDFreeSize(int unit) {
        //取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        //获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        //空闲的数据块的数量
        long freeBlocks = sf.getAvailableBlocks();
        //返回SD卡空闲大小
        if (unit == 0) {
            return freeBlocks * blockSize;  //单位Byte
        } else if (unit == 1) {
            return (freeBlocks * blockSize) / 1024;   //单位KB
        } else {
            return (freeBlocks * blockSize) / 1024 / 1024; //单位MB
        }
    }

    /**
     * @return SD卡总容量
     */
    public static long getSDAllSize() {
        //取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        //获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        //获取所有数据块数
        long allBlocks = sf.getBlockCount();
        //返回SD卡大小
        //return allBlocks * blockSize; //单位Byte
        //return (allBlocks * blockSize)/1024; //单位KB
        return (allBlocks * blockSize) / 1024 / 1024; //单位MB
    }

    private static Handler handler;

    /**
     * 将布局文件保存成图片文件
     */
    public static void saveLayout2File(View view, final SaveFileListener saveFileListener) {
        handler = new Handler(Looper.getMainLooper());
        final Bitmap bmp = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        view.draw(new Canvas(bmp));

        File dir = new File(imagePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        final String photoUrl = imagePath + System.currentTimeMillis() + ".png";//换成自己的图片保存路径
        final File file = new File(photoUrl);

        new Thread() {
            @Override
            public void run() {
                try {
                    final boolean bitMapOk = bmp.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(file));
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (saveFileListener != null) {
                                saveFileListener.onSaveFile(bitMapOk, photoUrl);
                            }
                        }
                    });
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public interface SaveFileListener {
        void onSaveFile(boolean saveOk, String photoUrl);
    }
}
