package com.juxin.common;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.juxin.common.utils.Final;
import com.juxin.common.utils.SearchFileUtils;
import com.juxin.common.widget.MyTitleBar;

import java.util.ArrayList;
import java.util.List;

/**
 * 文件管理器
 */
public class FileActivity extends AppCompatActivity implements View.OnClickListener {

    private MyTitleBar myTitleBar;
    private List<Entity> entities = null;
    private GridView mGridView;
    private LinearLayout llSdCard;
    private FileAdapter mFileAdapter;
    private int[] icons = {R.drawable.filesystem_icon_apk,
            R.drawable.filesystem_icon_photo,
            R.drawable.filesystem_icon_movie,
            R.drawable.filesystem_icon_music,
            R.drawable.filesystem_icon_word,
            R.drawable.filesystem_icon_rar,
            R.drawable.filesystem_icon_link,
            R.drawable.filesystem_icon_default};
    private String[] names = {"安装包", "图片", "视频", "音乐", "文档", "压缩包", "离线网页", "其他"};
    private int type;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);
        entities = new ArrayList<>();
        type = getIntent().getIntExtra("type", FileListActivity.sFILE);
        mHandler = new Handler(getMainLooper());
        initView();
        initListener();
        initData();
    }


    private void initView() {
        myTitleBar = (MyTitleBar) findViewById(R.id.myTitleBar);
        myTitleBar.setTitleText("文件");
        myTitleBar.setLeftText("返回", this);
        llSdCard = (LinearLayout) findViewById(R.id.llSdCard);
        mGridView = (GridView) findViewById(R.id.mGridView);
        mFileAdapter = new FileAdapter(this, entities);
        mGridView.setAdapter(mFileAdapter);
    }

    private void initListener() {
        llSdCard.setOnClickListener(this);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(FileActivity.this, ListActivity.class);
                switch (position) {
                    case 0:
                        intent.putExtra(ListActivity.Type, ListActivity.APK);
                        break;
                    case 1:
                        intent.putExtra(ListActivity.Type, ListActivity.IMAGE);
                        break;
                    case 2:
                        intent.putExtra(ListActivity.Type, ListActivity.VIDEO);
                        break;
                    case 3:
                        intent.putExtra(ListActivity.Type, ListActivity.AUDIO);
                        break;
                    case 4:
                        intent.putExtra(ListActivity.Type, ListActivity.DOC);
                        break;
                    case 5:
                        intent.putExtra(ListActivity.Type, ListActivity.RAR);
                        break;
                    case 6:
                        intent.putExtra(ListActivity.Type, ListActivity.HTML);
                        break;
                    case 7:
                        intent.putExtra(ListActivity.Type, ListActivity.OTHER);
                        break;
                }
                startActivityForResult(intent, 100);
            }
        });
    }

    private void initData() {
        int length = icons.length;
        Entity entity;
        for (int i = 0; i < length; i++) {
            entity = new Entity();
            entity.icon = icons[i];
            entity.name = names[i];
            entity.count = 0;
            entities.add(entity);
        }
        mFileAdapter.notifyDataSetChanged();
        new MThread().start();
    }

    class MThread extends Thread {

        @Override
        public void run() {
            final SearchFileUtils fileUtils = new SearchFileUtils(FileActivity.this);
            entities.get(0).count = fileUtils.getApks().size();
            entities.get(1).count = fileUtils.getImages().size();
            entities.get(2).count = fileUtils.getVideos().size();
            entities.get(3).count = fileUtils.getAudios().size();
            entities.get(4).count = fileUtils.getDocs().size();
            entities.get(5).count = fileUtils.getRars().size();
            entities.get(6).count = fileUtils.getHtmls().size();
            entities.get(7).count = fileUtils.getOthers().size();
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mFileAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        if (v == llSdCard) {
            Intent intent = new Intent();
            intent.setClass(this, FileListActivity.class);
            intent.putExtra("type", type);
            intent.putExtra("path", "/sdcard/");
            startActivityForResult(intent, Final.FILE_LIST);
        } else if (v.getId() == R.id.llLeft) {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == Final.FILE_LIST) {
                setResult(RESULT_OK, data);
                finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    class Entity {
        public int icon;
        public String name;
        public int count;

        @Override
        public String toString() {
            return "Entity{" +
                    "icon=" + icon +
                    ", name='" + name + '\'' +
                    ", count=" + count +
                    '}';
        }
    }


}
