package com.juxin.common.utils;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取手机上apk文件信息类，主要是判断是否安装再手机上了，安装的版本比较现有apk版本信息
 * <a href="http://my.oschina.net/arthor" target="_blank" rel="nofollow">@author</a>  Dylan
 */
public class SearchFileUtils {
    private List<String> apkList = new ArrayList<>();
    private List<String> imageList = new ArrayList<>();
    private List<String> videoList = new ArrayList<>();
    private List<String> audioList = new ArrayList<>();
    private List<String> docList = new ArrayList<>();
    private List<String> rarList = new ArrayList<>();
    private List<String> htmlList = new ArrayList<>();
    private List<String> otherList = new ArrayList<>();

    public List<String> getApks() {
        return apkList;
    }

    public List<String> getImages() {
        return imageList;
    }

    public List<String> getVideos() {
        return videoList;
    }

    public List<String> getAudios() {
        return audioList;
    }

    public List<String> getDocs() {
        return docList;
    }

    public List<String> getRars() {
        return rarList;
    }

    public List<String> getHtmls() {
        return htmlList;
    }

    public List<String> getOthers() {
        return otherList;
    }

    public SearchFileUtils(Context context) {
        super();
        FindAllFile(new File("/sdcard/"));
    }

    /**
     * 运用递归的思想，递归去找每个目录下面的apk文件
     */
    private void FindAllFile(File file) {
        String name = file.getName().toLowerCase();
        if (name.startsWith(".") || name.contains("cache") || name.contains("data")) {
            return;
        }
        // SD卡上的文件目录
        if (file.isFile()) {
            if (file.canRead() && !file.isHidden() && file.length() > 0 && file.canWrite() && name.contains(".")) {
                if (name.matches(Final.apkReg)) {
                    apkList.add(file.getAbsolutePath());
                } else if (name.matches(Final.imageReg)) {
                    imageList.add(file.getAbsolutePath());
                } else if (name.matches(Final.videoReg)) {
                    videoList.add(file.getAbsolutePath());
                } else if (name.matches(Final.audioReg)) {
                    audioList.add(file.getAbsolutePath());
                } else if (name.matches(Final.docReg)) {
                    docList.add(file.getAbsolutePath());
                } else if (name.matches(Final.rarReg)) {
                    rarList.add(file.getAbsolutePath());
                } else if (name.matches(Final.htmlReg)) {
                    htmlList.add(file.getAbsolutePath());
                } else {
                    otherList.add(file.getAbsolutePath());
                }
            }
        } else {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File file_str : files) {
                    if (file.getAbsolutePath().split("/").length < 5) {
                        FindAllFile(file_str);
                    }
                }
            }
        }
    }

}