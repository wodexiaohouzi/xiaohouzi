package com.juxin.common.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.juxin.common.Apk;

import java.util.ArrayList;

/**
 * Created by hp on 2016/5/6.
 */
public class Util {
    /**
     * 使用ContentProvider读取SD卡最近图片。
     */
    public static ArrayList<String> getImages(Context context) {
        Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String key_MIME_TYPE = MediaStore.Images.Media.MIME_TYPE;
        String key_DATA = MediaStore.Images.Media.DATA;

        ContentResolver mContentResolver = context.getContentResolver();

        // 只查询jpg和png的图片,按最新修改排序
        Cursor cursor = mContentResolver.query(mImageUri, new String[]{key_DATA},
                key_MIME_TYPE + "=? or " + key_MIME_TYPE + "=? or " + key_MIME_TYPE + "=?or " + key_MIME_TYPE + " =?or " + key_MIME_TYPE + " =?",
                new String[]{"image/jpg", "image/jpeg", "image/png", "image/gif", "image/bmp"},
                MediaStore.Images.Media.DATE_MODIFIED);

        ArrayList<String> latestImagePaths = new ArrayList<String>();
        if (cursor != null) {
            //从最新的图片开始读取.
            //当cursor中没有数据时，cursor.moveToLast()将返回false
            if (cursor.moveToLast()) {
                while (true) {
                    // 获取图片的路径
                    String path = cursor.getString(0);
                    latestImagePaths.add(path);

                    if (!cursor.moveToPrevious()) {
                        break;
                    }
                }
            }
            cursor.close();
        }

        return latestImagePaths;
    }

    /**
     * 使用ContentProvider读取SD卡Video。
     */
    public static ArrayList<String> getVideos(Context context) {
        Uri mVideoUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

        String key_MIME_TYPE = MediaStore.Video.Media.MIME_TYPE;
        String key_DATA = MediaStore.Video.Media.DATA;

        ContentResolver mContentResolver = context.getContentResolver();

        // 只查询jpg和png的图片,按最新修改排序
        Cursor cursor = mContentResolver.query(mVideoUri, new String[]{key_DATA},
                key_MIME_TYPE + "=? or " + key_MIME_TYPE + "=? or " + key_MIME_TYPE + "=?or " + key_MIME_TYPE + " =?or " + key_MIME_TYPE + " =?or " + key_MIME_TYPE + " =?or " + key_MIME_TYPE + " =?",
                new String[]{"video/rmvb", "video/rm", "video/avi", "video/mkv", "video/wmv", "video/mp4", "video/3gp"},
                MediaStore.Video.Media.DATE_MODIFIED);
        ArrayList<String> latestVideoPaths = new ArrayList<String>();
        if (cursor != null) {
            //从最新的图片开始读取.
            //当cursor中没有数据时，cursor.moveToLast()将返回false
            if (cursor.moveToLast()) {
                while (true) {
                    // 获取Video的路径
                    String path = cursor.getString(0);
                    latestVideoPaths.add(path);

                    if (!cursor.moveToPrevious()) {
                        break;
                    }
                }
            }
            cursor.close();
        }

        return latestVideoPaths;
    }

    /**
     * 使用ContentProvider读取SD卡Audio。
     */
    public static ArrayList<String> getAudios(Context context) {
        Uri mAudioUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        String key_MIME_TYPE = MediaStore.Audio.Media.MIME_TYPE;
        String key_DATA = MediaStore.Audio.Media.DATA;

        ContentResolver mContentResolver = context.getContentResolver();

        // 只查询jpg和png的图片,按最新修改排序
        Cursor cursor = mContentResolver.query(mAudioUri, new String[]{key_DATA},
                key_MIME_TYPE + "=? or " + key_MIME_TYPE + "=? or " + key_MIME_TYPE + "=?or " + key_MIME_TYPE + " =?or " + key_MIME_TYPE + " =?or " + key_MIME_TYPE + " =?or " + key_MIME_TYPE + " =?",
                new String[]{"audio/mp3", "audio/wav", "audio/ogg", "audio/wma", "audio/ape", "audio/mp4", "audio/aac"},
                MediaStore.Audio.Media.DATE_MODIFIED);
        ArrayList<String> latestAudioPaths = new ArrayList<String>();
        if (cursor != null) {
            //从最新的图片开始读取.
            //当cursor中没有数据时，cursor.moveToLast()将返回false
            if (cursor.moveToLast()) {

                while (true) {
                    // 获取Audio的路径
                    String path = cursor.getString(0);
                    latestAudioPaths.add(path);

                    if (!cursor.moveToPrevious()) {
                        break;
                    }
                }
            }
            cursor.close();
        }

        return latestAudioPaths;
    }

    /*
     * 采用了新的办法获取APK图标，之前的失败是因为android中存在的一个BUG,通过
     * appInfo.publicSourceDir = apkPath;来修正这个问题，详情参见:
     * http://code.google.com/p/android/issues/detail?id=9151
     */
    public static Drawable getApkIcon(Context context, String apkPath) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(apkPath,
                PackageManager.GET_ACTIVITIES);
        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
            appInfo.sourceDir = apkPath;
            appInfo.publicSourceDir = apkPath;
            try {
                return appInfo.loadIcon(pm);
            } catch (OutOfMemoryError e) {
                Log.e("111", e.toString());
            }
        }
        return null;
    }

    public static Apk getApkInfo(Context context, String apkPath) {
        Apk apk = null;
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(apkPath,
                PackageManager.GET_ACTIVITIES);
        if (info != null) {
            apk = new Apk();
            ApplicationInfo appInfo = info.applicationInfo;
            apk.versionName = info.versionName;
            apk.versionCode = info.versionCode;
            apk.packageName = info.packageName;
            apk.firstInstallTime = info.firstInstallTime;
            apk.lastUpdateTime = info.lastUpdateTime;
            apk.lastUpdateTime = info.lastUpdateTime;
            appInfo.sourceDir = apkPath;
            appInfo.publicSourceDir = apkPath;
            try {
                apk.apkIcon = appInfo.loadIcon(pm);
            } catch (OutOfMemoryError e) {
                Log.e("111", e.toString());
            }
        }
        return apk;
    }
}
