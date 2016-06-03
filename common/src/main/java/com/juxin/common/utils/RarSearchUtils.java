package com.juxin.common.utils;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 2016/5/9.
 */
public class RarSearchUtils {
    private List<String> rarList = new ArrayList<>();

    public List<String> getRars() {
        return rarList;
    }

    public RarSearchUtils(Context context) {
        super();
        FindAllRARFile(new File("/sdcard/"));
    }

    /**
     * 运用递归的思想，递归去找每个目录下面的apk文件
     */
    private void FindAllRARFile(File file) {
        // SD卡上的文件目录
        if (file.isFile()) {
            String name_s = file.getName();
            if (name_s.toLowerCase().endsWith(".rar") || name_s.toLowerCase().endsWith(".zip") || name_s.toLowerCase().endsWith(".cab") || name_s.toLowerCase().endsWith(".iso")) {
                rarList.add(file.getAbsolutePath());
            }
        } else {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File file_str : files) {
                    FindAllRARFile(file_str);
                }
            }
        }
    }
}
