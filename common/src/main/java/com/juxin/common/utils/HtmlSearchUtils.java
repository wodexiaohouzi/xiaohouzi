package com.juxin.common.utils;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 2016/5/9.
 */
public class HtmlSearchUtils {
    private List<String> htmlList = new ArrayList<>();

    public List<String> getHtmls() {
        return htmlList;
    }

    public HtmlSearchUtils(Context context) {
        super();
        FindAllHTMLFile(new File("/sdcard/"));
    }

    /**
     * 运用递归的思想，递归去找每个目录下面的apk文件
     */
    private void FindAllHTMLFile(File file) {
        // SD卡上的文件目录
        if (file.isFile()) {
            String name_s = file.getName();
            if (name_s.toLowerCase().endsWith(".html") || name_s.toLowerCase().endsWith(".htm")) {
                htmlList.add(file.getAbsolutePath());
            }
        } else {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File file_str : files) {
                    FindAllHTMLFile(file_str);
                }
            }
        }
    }
}
