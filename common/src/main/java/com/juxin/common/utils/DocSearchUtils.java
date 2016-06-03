package com.juxin.common.utils;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 2016/5/9.
 */
public class DocSearchUtils {
    private List<String> docList = new ArrayList<>();

    public List<String> getDocs() {
        return docList;
    }

    public DocSearchUtils(Context context) {
        super();
        FindAllDOCFile(new File("/sdcard/"));
    }

    /**
     * 运用递归的思想，递归去找每个目录下面的apk文件
     */
    private void FindAllDOCFile(File file) {
        // SD卡上的文件目录
        if (file.isFile()) {
            String name_s = file.getName();
            if (name_s.toLowerCase().endsWith(".txt") || name_s.toLowerCase().endsWith(".doc") || name_s.toLowerCase().endsWith(".docx") || name_s.toLowerCase().endsWith(".ppt") || name_s.toLowerCase().endsWith(".pdf") || name_s.toLowerCase().endsWith(".xls") || name_s.toLowerCase().endsWith(".wpd")) {
                docList.add(file.getAbsolutePath());
            }
        } else {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File file_str : files) {
                    FindAllDOCFile(file_str);
                }
            }
        }
    }
}
