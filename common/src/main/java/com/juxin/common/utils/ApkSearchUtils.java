package com.juxin.common.utils;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取手机上apk文件信息类，主要是判断是否安装再手机上了，安装的版本比较现有apk版本信息
 * <a href="http://my.oschina.net/arthor" target="_blank" rel="nofollow">@author</a>  Dylan
 */
public class ApkSearchUtils {
    private List<String> apkList = new ArrayList<>();

    public List<String> getApks() {
        return apkList;
    }

    public ApkSearchUtils(Context context) {
        super();
        FindAllAPKFile(new File("/sdcard/"));
    }

    /**
     * 运用递归的思想，递归去找每个目录下面的apk文件
     */
    private void FindAllAPKFile(File file) {
        // SD卡上的文件目录
        if (file.isFile()) {
            String name_s = file.getName();
            if (name_s.toLowerCase().endsWith(".apk")) {
                apkList.add(file.getAbsolutePath());
            }
        } else {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File file_str : files) {
                    FindAllAPKFile(file_str);
                }
            }
        }
    }

}