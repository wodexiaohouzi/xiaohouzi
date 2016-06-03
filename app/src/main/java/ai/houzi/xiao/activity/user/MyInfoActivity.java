package ai.houzi.xiao.activity.user;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import ai.houzi.xiao.R;
import ai.houzi.xiao.activity.main.BaseActivity;
import ai.houzi.xiao.widget.FlutterView;
import ai.houzi.xiao.widget.MyScrollView;
import ai.houzi.xiao.widget.TitleBar;

/**
 * 个人信息
 */
public class MyInfoActivity extends BaseActivity {

    private TitleBar titleBar;
    private FlutterView mFlutter;
    private RecyclerView mRecyclerView;
    private MyInfoRecyclerAdapter mAdapter;
    private MyScrollView mScrollView;
    private LinearLayout llKongJian;
    private Button btnEditInfo;
    private Intent intent;

    String url = "http://b.hiphotos.baidu.com/zhidao/pic/item/a08b87d6277f9e2ffea334581d30e924b999f3c8.jpg";

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mFlutter.setFlutterUrl(url, R.mipmap.myinfo_background);
                    titleBar.showProgressBar(false);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_myinfo);
        intent = new Intent();
        mFlutter = (FlutterView) findViewById(R.id.mFlutter);
        titleBar = (TitleBar) findViewById(R.id.titleBar);
        titleBar.setTitleText("我的资料");
        titleBar.setLeftText("返回", this);
        titleBar.setTitleAlpha(0);
        titleBar.setTitleVisible(false);
        mScrollView = (MyScrollView) findViewById(R.id.mScrollView);
        mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);
        //设置固定大小
        mRecyclerView.setHasFixedSize(true);
        //创建线性布局
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        //垂直方向
        mLayoutManager.setOrientation(OrientationHelper.HORIZONTAL);
        //给RecyclerView设置布局管理器
        mRecyclerView.setLayoutManager(mLayoutManager);
        //创建适配器，并且设置
        mAdapter = new MyInfoRecyclerAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        llKongJian = (LinearLayout) findViewById(R.id.llKongJian);
        btnEditInfo = (Button) findViewById(R.id.btnEditInfo);
    }

    @Override
    protected void initListener() {
        llKongJian.setOnClickListener(this);
        btnEditInfo.setOnClickListener(this);
        mScrollView.setScrollViewListener(new MyScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(ScrollView scrollView, int x, int y, int oldx, int oldy) {
                if (y > 80 && y < 335) {
                    titleBar.setTitleAlpha(y - 80);
                    titleBar.setTitleVisible(true);
                    titleBar.setTitleTextAlpha(y - 80);
                } else if (y <= 80) {
                    titleBar.setTitleAlpha(0);
                    titleBar.setTitleVisible(false);
                } else {
                    titleBar.setTitleAlpha(255);
                    titleBar.setTitleVisible(true);
                }
            }
        });
    }

    @Override
    protected void initDate() {
        titleBar.showProgressBar(true);
        mHandler.sendEmptyMessageDelayed(0, 3000);
    }

    @Override
    protected void handleListener(View v) {
        switch (v.getId()) {
            case R.id.llLeft:
                finish();
                break;
            case R.id.llKongJian:
                intent.setClass(MyInfoActivity.this, TopicActivity.class);
                startActivity(intent);
                break;
            case R.id.btnEditInfo:
                intent.setClass(MyInfoActivity.this, EditInfoActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
