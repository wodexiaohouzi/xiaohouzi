package com.juxin.common;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.juxin.common.widget.MyTitleBar;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by hp on 2016/5/5.
 */
public class FileListActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int sFILE = 0;//选择文件
    public static final int sFORDER = 1;//选择文件夹路径
    public static final int sFILE_OR_FORDER = 2;//选择文件或文件夹
    private MyTitleBar myTitleBar;
    private List<File> fileList = null;
    private List<String> pathList = null;
    private String rootPath = "/sdcard/";
    private String selectPath = "";
    private String PATH = "";
    private ListView mListView;
    private FileListAdapter mFileAdapter;
    private RecyclerView mRecyclerView;
    private View sdRoot;
    private FilePathRecyclerAdapter recyclerAdapter;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filelist);
        type = getIntent().getIntExtra("type", sFILE);
        PATH = getIntent().getStringExtra("path");
        pathList = new ArrayList<>();
        initView();
        initListener();
        initData();
    }


    private void initView() {
        myTitleBar = (MyTitleBar) findViewById(R.id.myTitleBar);
        myTitleBar.setTitleText("手机SD卡");
        myTitleBar.setLeftText("返回", this);
        if (type == sFILE) {
            myTitleBar.setRighText("取消", this);
        } else {
            myTitleBar.setRighText("确定", this);
        }
        mListView = (ListView) findViewById(R.id.mListView);
        mListView.setEmptyView(findViewById(R.id.mEmptyView));
        mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerAdapter = new FilePathRecyclerAdapter(this, pathList);
        mRecyclerView.setAdapter(recyclerAdapter);
        sdRoot = findViewById(R.id.sdRoot);
    }

    private void initListener() {
        sdRoot.setOnClickListener(this);
        recyclerAdapter.setOnClickListener(new FilePathRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                String s = pathList.get(position);
//                s = (s == "手机SD卡" ? "sdcard" : s);
                selectPath = selectPath.substring(0, selectPath.indexOf(s) + s.length());
                getFileDir(selectPath);
            }
        });
    }

    private void initData() {
        String[] split = PATH.split("/");
        int length = split.length - 1;
        for (int i = 1; i < length; i++) {
            pathList.add(split[i]);
        }
        recyclerAdapter.notifyDataSetChanged();
        mRecyclerView.smoothScrollToPosition(pathList.size());
        getFileDir(PATH);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.llLeft) {
            finish();
        } else if (v.getId() == R.id.tvRight) {
            if (type == sFILE) {
                setResult(RESULT_OK);
                finish();
            } else {
                Intent intent = new Intent();
                intent.putExtra("path", selectPath + "/");
                setResult(RESULT_OK, intent);
                finish();
            }
        } else if (v.getId() == R.id.sdRoot) {
            getFileDir(rootPath);
        }
    }

    private void getFileDir(String filePath) {
        File f = new File(filePath);
        if (f.exists() && f.canWrite()) {
            selectPath = filePath;
            String name = f.getName();
//            name = ("sdcard".equals(name) ? "手机SD卡" : name);
            if (pathList.contains(name)) {
                recyclerAdapter.removeData(pathList.indexOf(name) + 1);
            } else {
                recyclerAdapter.addData(pathList.size(), name);
                mRecyclerView.smoothScrollToPosition(pathList.size());
            }
            fileList = new ArrayList<>();
            File[] files = f.listFiles();
            int length = files.length;
            for (int i = 0; i < length; i++) {
                File file = files[i];
                if (file.exists() && file.canWrite() && file.canRead() && !file.isHidden()) {
                    fileList.add(file);
                }
            }
            Collections.sort(fileList, new ComparatorValues());
            mFileAdapter = new FileListAdapter(this, fileList);
            mFileAdapter.setOnClickBoxListener(new FileListAdapter.OnClickBoxListener() {
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
                    intent.setClass(FileListActivity.this, FileDetailActivity.class);
                    intent.putExtra("path", absolutePath);
                    intent.putExtra("from", "sdcard");
                    startActivity(intent);
                }
            });
            mListView.setAdapter(mFileAdapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    File file = new File(fileList.get(position).getPath());
                    if (file.isDirectory()) {
                        if (file.canWrite()) {
                            if (file.isDirectory()) {
                                getFileDir(fileList.get(position).getPath());
                            }
                        } else {
                            LinearLayout lay = new LinearLayout(FileListActivity.this);
                            lay.setOrientation(LinearLayout.HORIZONTAL);
                            ImageView image = new ImageView(FileListActivity.this);
                            TextView text = new TextView(FileListActivity.this);
                            text.setTextColor(Color.RED);
                            text.setTextSize(20);
                            text.setText("很抱歉您的权限不足!");
                            Toast toast = Toast.makeText(FileListActivity.this, text.getText().toString(), Toast.LENGTH_LONG);
                            image.setImageResource(android.R.drawable.stat_sys_warning);
                            lay.addView(image);
                            lay.addView(text);
                            toast.setView(lay);
                            toast.show();
                        }
                    } else {
                        FileListAdapter.ViewHolder holder = (FileListAdapter.ViewHolder) view.getTag();
                        if (holder != null) {
                            holder.cBox.setChecked(true);
                        }
                    }
                }
            });
        } else {
            LinearLayout lay = new LinearLayout(FileListActivity.this);
            lay.setOrientation(LinearLayout.HORIZONTAL);
            ImageView image = new ImageView(FileListActivity.this);
            TextView text = new TextView(FileListActivity.this);
            text.setTextColor(Color.RED);
            text.setTextSize(20);
            text.setText("无SD卡,无法完成下载!");
            Toast toast = Toast.makeText(FileListActivity.this, text.getText().toString(), Toast.LENGTH_LONG);
            image.setImageResource(android.R.drawable.stat_sys_warning);
            lay.addView(image);
            lay.addView(text);
            toast.setView(lay);
            toast.show();
            this.finish();
        }
    }

    public static final class ComparatorValues implements Comparator<File> {

        @Override
        public int compare(File file1, File file2) {
            if (file1.isDirectory() && !file2.isDirectory()) {
                return -1;
            }
            if (!file1.isDirectory() && file2.isDirectory()) {
                return 1;
            }
            i = 0;
            comp(file1.getName(), file2.getName());
            return re;
        }

        int i = 0;
        int re = 0;

        private void comp(String name1, String name2) {
            i++;
            if (name1.length() - 1 < i) {
                re = -1;
            } else if (name2.length() - 1 < i) {
                re = 1;
            } else {
                int m1 = name1.substring(0, i).toLowerCase().hashCode();
                int m2 = name2.substring(0, i).toLowerCase().hashCode();
                if (m1 > m2) {
                    re = 1;
                } else if (m1 < m2) {
                    re = -1;
                } else {
                    comp(name1, name2);
                }
            }
        }
    }
}
