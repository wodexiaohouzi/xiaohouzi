package ai.houzi.xiao.activity.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ai.houzi.xiao.R;
import ai.houzi.xiao.activity.main.BaseActivity;
import ai.houzi.xiao.activity.test.IocActivity;
import ai.houzi.xiao.activity.tools.ComPassActivity;
import ai.houzi.xiao.adapter.CommonAdapter;
import ai.houzi.xiao.adapter.ViewHolder;
import ai.houzi.xiao.widget.PullToRefreshListView;
import ai.houzi.xiao.widget.RefreshListView;
import ai.houzi.xiao.widget.TitleBar;
import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.header.StoreHouseHeader;

public class UltraPtrActivity extends BaseActivity {
    private TitleBar titleBar;
    private ListView listView;
    private MyAdapter adapter;
    private ArrayList<String> persons;
    private Handler mHandler;

    private Intent intent;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_ultra_ptr);
        intent = new Intent();
        persons = new ArrayList<>();
        mHandler = new Handler(getMainLooper());
        titleBar = (TitleBar) findViewById(R.id.titleBar);
        titleBar.setTitleText("酷炫下拉刷新");
        titleBar.setLeftText("返回", this);

        listView = (ListView) findViewById(R.id.listView);
        adapter = new MyAdapter(this, persons);
        listView.setAdapter(adapter);
        final PtrClassicFrameLayout ptrFrameLayout = (PtrClassicFrameLayout) findViewById(R.id.ptrFrameLayout);
        final StoreHouseHeader header = new StoreHouseHeader(this);
        header.setPadding(0, 50, 0, 50);
        header.initWithString("AI-HOU-ZI");
        header.setTextColor(0xFFF77CEF);
        ptrFrameLayout.setHeaderView(header);
        ptrFrameLayout.addPtrUIHandler(header);
        ptrFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ptrFrameLayout.refreshComplete();
                    }
                }, 3000);
            }
        });
    }

    @Override
    protected void initListener() {
    }

    private int index = 0;

    @Override
    protected void initDate() {
        persons.clear();
        for (int i = 0; i < 10; i++) {
            index++;
            persons.add("我是第" + index + "个超级战士！");
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void handleListener(View v) {
        switch (v.getId()) {
            case R.id.llLeft:
                finish();
                break;
        }
    }

    private class MyAdapter extends CommonAdapter<String> {
        public MyAdapter(Context context, List<String> datas) {
            super(context, android.R.layout.select_dialog_item, datas);
        }

        @Override
        public void convert(ViewHolder holder, String s) {
            holder.setText(android.R.id.text1, s);
        }
    }
}
