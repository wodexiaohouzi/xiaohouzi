package com.qqalbum.imagepicker.ui;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qqalbum.imagepicker.R;
import com.qqalbum.imagepicker.adapter.PhotoWallAdapter;
import com.qqalbum.imagepicker.customviews.MyTitleBar;
import com.qqalbum.imagepicker.utils.ScreenUtils;
import com.qqalbum.imagepicker.utils.Utility;

import java.io.File;
import java.util.ArrayList;

/**
 * 选择照片页面
 * Created by hanj on 14-10-15.
 */
public class PhotoWallActivity extends Activity {
    public static final String CHOICE_COUNT = "CHOICE_COUNT";
    /**
     * 1代表可选单张
     */
    public static final int SINGLE = 1;
    /**
     * 3代表可选多张
     */
    public static final int MULTI = 3;
    private int mCount;
    private MyTitleBar myTitleBar;
    private Button btnConfirm;
    private TextView tvPreview;
    private LinearLayout llBottomHandle;

    private ArrayList<String> list;
    private ArrayList<String> paths;
    private GridView mPhotoWall;
    private PhotoWallAdapter adapter;

    /**
     * 当前文件夹路径
     */
    private String currentFolder = null;
    /**
     * 当前展示的是否为最近照片
     */
    private boolean isLatest = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_wall);
        //获取屏幕像素
        ScreenUtils.initScreen(this);

        myTitleBar = (MyTitleBar) findViewById(R.id.titleBar);
        myTitleBar.setTitleText(getString(R.string.latest_image));
        myTitleBar.setLeftText(getString(R.string.photo_album), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击返回，回到选择相册页面
                backAction();
            }
        });
        myTitleBar.setRighText(getString(R.string.main_cancel), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0, R.anim.out_from_bottom);
            }
        });

        mCount = getIntent().getIntExtra(CHOICE_COUNT, SINGLE);
        if (mCount == MULTI) {
            paths = getIntent().getStringArrayListExtra(PATHS);
            if (paths == null) {
                paths = new ArrayList<>();
            }
        }

        mPhotoWall = (GridView) findViewById(R.id.photo_wall_grid);
        list = getLatestImagePaths(100);
        adapter = new PhotoWallAdapter(this, list, mCount, paths);
        mPhotoWall.setAdapter(adapter);

        tvPreview = (TextView) findViewById(R.id.tvPreview);
        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        llBottomHandle = (LinearLayout) findViewById(R.id.llBottomHandle);
        if (mCount == SINGLE) {
            llBottomHandle.setVisibility(View.GONE);
        }
        initListener();

    }

    public static final String CODE = "code";
    public static final String PATH = "path";
    public static final String PATHS = "paths";
    public static final int SUCCESS = 100;
    public static final int FIAL = 101;

    private void initListener() {
        mPhotoWall.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mCount == SINGLE) {
                    String path = list.get(position);
                    Intent intent = new Intent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra(CODE, !TextUtils.isEmpty(path) ? SUCCESS : FIAL);
                    intent.putExtra(PATH, path);
                    setResult(RESULT_OK, intent);
                    if (PhotoAlbumActivity.activity != null && !PhotoAlbumActivity.activity.isFinishing()) {
                        PhotoAlbumActivity.activity.finish();
                    }
                    finish();
                    overridePendingTransition(0, R.anim.out_from_bottom);
                } else {
                    //放大预览
                    imageBrower(view, position, list);
                }
            }
        });
        adapter.setSeletionListener(new PhotoWallAdapter.SeletionListener() {
            @Override
            public void onSeletion(int count) {
                if (count > 0) {
                    tvPreview.setTextColor(getResources().getColor(R.color.blue));
                    btnConfirm.setEnabled(true);
                    btnConfirm.setText(getString(R.string.main_confirm) + "(" + count + ")");
                } else {
                    tvPreview.setTextColor(getResources().getColor(R.color.gray_dark_bg));
                    btnConfirm.setEnabled(false);
                    btnConfirm.setText(getString(R.string.main_confirm));
                }
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //选择图片完成,回到起始页面
                ArrayList<String> paths = getSelectImagePaths();
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(CODE, paths != null ? SUCCESS : FIAL);
                intent.putStringArrayListExtra(PATHS, paths);
                setResult(RESULT_OK, intent);
                finish();
                overridePendingTransition(0, R.anim.out_from_bottom);
            }
        });
        tvPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> paths = getSelectImagePaths();
                if (paths.size() > 0) {
                    imageBrower(null, paths.size() - 1, paths);
                }
            }
        });
    }

    /**
     * 第一次跳转至相册页面时，传递最新照片信息
     */
    private boolean firstIn = true;

    /**
     * 点击返回时，跳转至相册页面
     */
    private void backAction() {
        Intent intent = new Intent(this, PhotoAlbumActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        //传递“最近照片”分类信息
        if (firstIn) {
            if (list != null && list.size() > 0) {
                intent.putExtra("latest_count", list.size());
                intent.putExtra("latest_first_img", list.get(0));
            }
            firstIn = false;
        }

        startActivityForResult(intent, 50);
//        startActivity(intent);
        //动画
//        overridePendingTransition(R.anim.in_from_left, R.anim.out_from_right);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 50 && resultCode == RESULT_CANCELED) {
            finish();
//           overridePendingTransition(R.anim.in_from_top, R.anim.out_from_bottom);
            overridePendingTransition(0, R.anim.out_from_bottom);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //重写返回键
    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            backAction();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    /**
     * 根据图片所属文件夹路径，刷新页面
     */
    private void updateView(int code, String folderPath) {
        list.clear();
        adapter.clearSelectionMap();
        adapter.notifyDataSetChanged();

        if (code == 100) {   //某个相册
            int lastSeparator = folderPath.lastIndexOf(File.separator);
            String folderName = folderPath.substring(lastSeparator + 1);
            myTitleBar.setTitleText(folderName);
            list.addAll(getAllImagePathsByFolder(folderPath));
        } else if (code == 200) {  //最近照片
            myTitleBar.setTitleText(getString(R.string.latest_image));
            list.addAll(getLatestImagePaths(100));
        }

        adapter.notifyDataSetChanged();
        if (list.size() > 0) {
            //滚动至顶部
            mPhotoWall.smoothScrollToPosition(0);
        }
    }


    /**
     * 获取指定路径下的所有图片文件。
     */
    private ArrayList<String> getAllImagePathsByFolder(String folderPath) {
        File folder = new File(folderPath);
        String[] allFileNames = folder.list();
        if (allFileNames == null || allFileNames.length == 0) {
            return null;
        }

        ArrayList<String> imageFilePaths = new ArrayList<String>();
        for (int i = allFileNames.length - 1; i >= 0; i--) {
            if (Utility.isImage(allFileNames[i])) {
                imageFilePaths.add(folderPath + File.separator + allFileNames[i]);
            }
        }

        return imageFilePaths;
    }

    /**
     * 使用ContentProvider读取SD卡最近图片。
     */
    private ArrayList<String> getLatestImagePaths(int maxCount) {
        Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String key_MIME_TYPE = MediaStore.Images.Media.MIME_TYPE;
        String key_DATA = MediaStore.Images.Media.DATA;

        ContentResolver mContentResolver = getContentResolver();

        // 只查询jpg和png的图片,按最新修改排序
        Cursor cursor = mContentResolver.query(mImageUri, new String[]{key_DATA},
                key_MIME_TYPE + "=? or " + key_MIME_TYPE + "=? or " + key_MIME_TYPE + "=?or " + key_MIME_TYPE + "=?",
                new String[]{"image/jpg", "image/jpeg", "image/png", "image/gif"},
                MediaStore.Images.Media.DATE_MODIFIED);

        ArrayList<String> latestImagePaths = null;
        if (cursor != null) {
            //从最新的图片开始读取.
            //当cursor中没有数据时，cursor.moveToLast()将返回false
            if (cursor.moveToLast()) {
                latestImagePaths = new ArrayList<String>();

                while (true) {
                    // 获取图片的路径
                    String path = cursor.getString(0);
                    latestImagePaths.add(path);

                    if (latestImagePaths.size() >= maxCount || !cursor.moveToPrevious()) {
                        break;
                    }
                }
            }
            cursor.close();
        }

        return latestImagePaths;
    }

    //获取已选择的图片路径
    private ArrayList<String> getSelectImagePaths() {
        SparseBooleanArray map = adapter.getSelectionMap();
        if (map.size() == 0) {
            return null;
        }

        ArrayList<String> selectedImageList = new ArrayList<String>();

        for (int i = 0; i < list.size(); i++) {
            if (map.get(i)) {
                selectedImageList.add(list.get(i));
            }
        }

        return selectedImageList;
    }

    //从相册页面跳转至此页
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        int code = intent.getIntExtra("code", -1);
        if (code == 100) {
            //某个相册
            String folderPath = intent.getStringExtra("folderPath");
            if (isLatest || (folderPath != null && !folderPath.equals(currentFolder))) {
                currentFolder = folderPath;
                updateView(100, currentFolder);
                isLatest = false;
            }
        } else if (code == 200) {
            //“最近照片”
            if (!isLatest) {
                updateView(200, null);
                isLatest = true;
            }
        }
        setIntent(intent);
    }

    private void imageBrower(View view, int position, ArrayList<String> urls) {
        Intent intent = new Intent(this, ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putStringArrayListExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN && view != null) {
            ActivityOptionsCompat compat = ActivityOptionsCompat.makeScaleUpAnimation(view, (int) view.getX(), (int) view.getY(), 0, 0);
            startActivity(intent, compat.toBundle());
        } else {
            startActivity(intent);
        }
    }
}
