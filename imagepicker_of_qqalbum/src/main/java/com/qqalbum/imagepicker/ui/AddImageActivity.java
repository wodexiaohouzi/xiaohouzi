package com.qqalbum.imagepicker.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.qqalbum.imagepicker.R;
import com.qqalbum.imagepicker.customviews.MyDialog;
import com.qqalbum.imagepicker.customviews.MyTitleBar;
import com.qqalbum.imagepicker.customviews.PictureAdd;
import com.qqalbum.imagepicker.utils.FilePath;
import com.qqalbum.imagepicker.utils.Final;
import com.qqalbum.imagepicker.utils.Logg;
import com.qqalbum.imagepicker.utils.ScreenUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AddImageActivity extends Activity implements View.OnClickListener {
    private MyTitleBar titleBar;
    private PictureAdd pAdd;
    private ArrayList<String> urlList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addimage);
        ScreenUtils.initScreen(this);
        urlList = new ArrayList<>();
        titleBar = (MyTitleBar) findViewById(R.id.titleBar);
        titleBar.setTitleText("选择图片");
        titleBar.setLeftText("返回", this);

        pAdd = (PictureAdd) findViewById(R.id.pAdd);
        pAdd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == parent.getCount() - 1) {
                    showDialog();
                }
            }
        });
        pAdd.setUrls(urlList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 101:
                    if (data != null) {
                        int code = data.getIntExtra(PhotoWallActivity.CODE, 0);
                        if (code == PhotoWallActivity.SUCCESS) {
                            ArrayList<String> list = data.getStringArrayListExtra(PhotoWallActivity.PATHS);
                            urlList.clear();
                            urlList.addAll(list);
                            pAdd.setUrls(urlList);
                        }
                    }
                    break;
                case Final.CAMERA: {
                    String sdStatus = Environment.getExternalStorageState();
                    if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                        Logg.i("TestFile",
                                "SD card is not avaiable/writeable right now.");
                        return;
                    }
                    String name = new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式

                    FileOutputStream b = null;
                    //???????????????????????????????为什么不能直接保存在系统相册位置呢？？？？？？？？？？？？
                    File file = new File(FilePath.imagePath);
                    file.mkdirs();// 创建文件夹
                    String fileName = FilePath.imagePath + name;

                    try {
                        b = new FileOutputStream(fileName);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            b.flush();
                            b.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    pAdd.setUrls(new String[]{fileName});
                }
                break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.llLeft) {
            finish();
        } else if (v.getId() == R.id.tvAlbum) {
            myDialog.dismiss();
            Intent intent = new Intent();
            intent.setClass(AddImageActivity.this, PhotoWallActivity.class);
            intent.putExtra(PhotoWallActivity.CHOICE_COUNT, PhotoWallActivity.MULTI);
            intent.putStringArrayListExtra(PhotoWallActivity.PATHS, urlList);
            startActivityForResult(intent, 101);
        } else if (v.getId() == R.id.tvCamera) {
            myDialog.dismiss();
            Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intentCamera, Final.CAMERA);
        }
    }


    private MyDialog myDialog;
    private View dialogView;

    private void showDialog() {
        if (dialogView == null) {
            dialogView = View.inflate(AddImageActivity.this, R.layout.dialog_album_camera, null);
            TextView tvAlbum = (TextView) dialogView.findViewById(R.id.tvAlbum);
            TextView tvCamera = (TextView) dialogView.findViewById(R.id.tvCamera);
            TextView tvCancle = (TextView) dialogView.findViewById(R.id.tvCancle);
            tvAlbum.setOnClickListener(this);
            tvCamera.setOnClickListener(this);
            tvCancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myDialog.dismiss();
                }
            });
        }
        if (myDialog == null) {
            myDialog = new MyDialog(AddImageActivity.this)
                    .addView(dialogView)
                    .setWindow(0, ScreenUtils.dp2px(8), ScreenUtils.getScreenW() - ScreenUtils.dp2px(16), 0, 0.9f);
        }
        if (myDialog != null) {
            myDialog.show();
        }
    }
}
