package com.juxin.common;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.juxin.common.utils.SearchFileUtils;
import com.juxin.common.widget.MyTitleBar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 2016/5/9.
 */
public class ListActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String Type = "type";
    public static final String APK = "apk";
    public static final String IMAGE = "image";
    public static final String VIDEO = "video";
    public static final String AUDIO = "audio";
    public static final String DOC = "doc";
    public static final String RAR = "rar";
    public static final String HTML = "html";
    public static final String OTHER = "other";
    private MyTitleBar myTitleBar;
    private ListView mListView;
    private FileListAdapter adapter;
    private List<File> files;
    private Handler mHandler;
    private String type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        files = new ArrayList<>();
        mHandler = new Handler(getMainLooper());
        myTitleBar = (MyTitleBar) findViewById(R.id.myTitleBar);
        myTitleBar.setTitleText("文件");
        myTitleBar.setLeftText("", this);
        myTitleBar.setRighText("取消", this);
        mListView = (ListView) findViewById(R.id.mListView);
        mListView.setEmptyView(findViewById(R.id.mEmptyView));
        adapter = new FileListAdapter(this, files);
        mListView.setAdapter(adapter);

        initListener();
        initData();
    }

    private void initListener() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FileListAdapter.ViewHolder holder = (FileListAdapter.ViewHolder) view.getTag();
                if (holder != null) {
                    holder.cBox.setChecked(true);
                }
            }
        });
        adapter.setOnClickBoxListener(new FileListAdapter.OnClickBoxListener() {
            @Override
            public void onClickBox(View v, String absolutePath) {
                Intent intent = new Intent();
                intent.putExtra("path", absolutePath);
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onFileDetil(View v, String absolutePath) {
                Intent intent = new Intent();
                intent.setClass(ListActivity.this, FileDetailActivity.class);
                intent.putExtra("path", absolutePath);
                startActivity(intent);
            }
        });
    }

    private void initData() {
        type = getIntent().getStringExtra(Type);
        if (APK.equals(type)) {
            myTitleBar.setTitleText("安装包");
        } else if (IMAGE.equals(type)) {
            myTitleBar.setTitleText("图片");
        } else if (VIDEO.equals(type)) {
            myTitleBar.setTitleText("视频");
        } else if (AUDIO.equals(type)) {
            myTitleBar.setTitleText("音乐");
        } else if (DOC.equals(type)) {
            myTitleBar.setTitleText("文档");
        } else if (RAR.equals(type)) {
            myTitleBar.setTitleText("压缩包");
        } else if (HTML.equals(type)) {
            myTitleBar.setTitleText("离线网页");
        } else if (OTHER.equals(type)) {
            myTitleBar.setTitleText("其他");
        }
        new MThread().start();
    }

    class MThread extends Thread {

        @Override
        public void run() {
            final SearchFileUtils fileUtils = new SearchFileUtils(ListActivity.this);
            List<String> paths = null;
            if (APK.equals(type)) {
                paths = fileUtils.getApks();
            } else if (IMAGE.equals(type)) {
                paths = fileUtils.getImages();
            } else if (VIDEO.equals(type)) {
                paths = fileUtils.getVideos();
            } else if (AUDIO.equals(type)) {
                paths = fileUtils.getAudios();
            } else if (DOC.equals(type)) {
                paths = fileUtils.getDocs();
            } else if (RAR.equals(type)) {
                paths = fileUtils.getRars();
            } else if (HTML.equals(type)) {
                paths = fileUtils.getHtmls();
            } else if (OTHER.equals(type)) {
                paths = fileUtils.getOthers();
            }
            if (paths != null) {
                int size = paths.size();
                for (int i = 0; i < size; i++) {
                    files.add(new File(paths.get(i)));
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.llLeft) {
            finish();
        } else if (v.getId() == R.id.tvRight) {
            setResult(RESULT_OK);
            finish();
        }
    }
}
