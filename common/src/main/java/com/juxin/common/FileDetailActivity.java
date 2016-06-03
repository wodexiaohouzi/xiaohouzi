package com.juxin.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.juxin.common.utils.FileSizeUtil;
import com.juxin.common.utils.Final;
import com.juxin.common.utils.Util;
import com.juxin.common.widget.MyTitleBar;

import java.io.File;
import java.text.SimpleDateFormat;

/**
 * Created by hp on 2016/5/9.
 */
public class FileDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private MyTitleBar myTitleBar;
    private TextView tvName, tvType, tvVersion, tvSize, tvTime, tvLocation, tvReName, tvOpenLocation;
    private View lineOpenLocation;
    private SimpleDateFormat format;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filedetail);

        format = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        initView();
        initListener();
        initData();
    }

    private void initView() {
        myTitleBar = (MyTitleBar) findViewById(R.id.myTitleBar);
        myTitleBar.setTitleText("文件详情");
        myTitleBar.setLeftText("", this);
        tvName = (TextView) findViewById(R.id.tvName);
        tvType = (TextView) findViewById(R.id.tvType);
        tvVersion = (TextView) findViewById(R.id.tvVersion);
        tvSize = (TextView) findViewById(R.id.tvSize);
        tvTime = (TextView) findViewById(R.id.tvTime);
        tvLocation = (TextView) findViewById(R.id.tvLocation);
        tvReName = (TextView) findViewById(R.id.tvReName);
        tvOpenLocation = (TextView) findViewById(R.id.tvOpenLocation);
        lineOpenLocation = findViewById(R.id.lineOpenLocation);
        String from = getIntent().getStringExtra("from");
        if ("sdcard".equals(from)) {
            tvOpenLocation.setVisibility(View.INVISIBLE);
            lineOpenLocation.setVisibility(View.INVISIBLE);
        } else {
            tvOpenLocation.setVisibility(View.VISIBLE);
            lineOpenLocation.setVisibility(View.VISIBLE);
        }
    }

    private void initListener() {
        tvReName.setOnClickListener(this);
        tvOpenLocation.setOnClickListener(this);
    }

    private void initData() {
        String path = getIntent().getStringExtra("path");
        file = new File(path);
        if (file.exists()) {
            tvName.setText(file.getName());
            String name = file.getName().toLowerCase();
            if (name.matches(Final.apkReg)) {
                tvType.setText("类型：\t安装包");
                Apk apk = Util.getApkInfo(this, path);
                tvVersion.setText("版本：\t" + apk.versionName);
                tvVersion.setVisibility(View.VISIBLE);
            } else if (name.matches(Final.imageReg)) {
                tvType.setText("类型：\t图片");
            } else if (name.matches(Final.videoReg)) {
                tvType.setText("类型：\t视频");
            } else if (name.matches(Final.audioReg)) {
                tvType.setText("类型：\t音乐");
            } else if (name.matches(Final.docReg)) {
                tvType.setText("类型：\t文档");
            } else if (name.matches(Final.rarReg)) {
                tvType.setText("类型：\t压缩包");
            } else if (name.matches(Final.htmlReg)) {
                tvType.setText("类型：\t离线网页");
            } else {
                tvType.setText("类型：\t其他");
            }
            tvSize.setText("大小：\t" + FileSizeUtil.FormetFileSize(FileSizeUtil.getFileSize(file)));
            tvTime.setText("时间：\t" + format.format(file.lastModified()));
            tvLocation.setText("目录：\t" + file.getParent().replace("/sdcard", "手机SD卡"));
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.llLeft) {
            finish();
        } else if (v.getId() == R.id.tvReName) {
            if (file.exists()) {
                Intent intent = new Intent();
                intent.setClass(FileDetailActivity.this, ReNameActivity.class);
                intent.putExtra("name", file.getName());
                startActivityForResult(intent, Final.RENAME);
            }
        } else if (v.getId() == R.id.tvOpenLocation) {
            if (file.exists()) {
                Intent intent = new Intent();
                intent.setClass(this, FileListActivity.class);
                intent.putExtra("type", FileListActivity.sFILE);
                intent.putExtra("from", "sdcard");
                intent.putExtra("path", file.getParent() + File.separator);
                startActivityForResult(intent, 100);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Final.RENAME) {
                if (data != null) {
                    String name = data.getStringExtra("name");
                    boolean isReName = file.renameTo(new File(file.getParent() + File.separator + name));
                    if (isReName) {
                        tvName.setText(name);
                    } else {
                        Toast.makeText(this, "重命名失败！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
}
