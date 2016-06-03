package ai.houzi.xiao.activity.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

import ai.houzi.xiao.R;
import ai.houzi.xiao.activity.main.BaseActivity;
import ai.houzi.xiao.activity.test.IocActivity;
import ai.houzi.xiao.activity.test.KeyBoardActivity;
import ai.houzi.xiao.activity.test.TimeActivity;
import ai.houzi.xiao.activity.tools.ComPassActivity;
import ai.houzi.xiao.adapter.CommonAdapter;
import ai.houzi.xiao.adapter.ViewHolder;
import ai.houzi.xiao.widget.PullToRefreshListView;
import ai.houzi.xiao.widget.RefreshListView;
import ai.houzi.xiao.widget.TitleBar;

public class ListViewTestActivity extends BaseActivity implements RefreshListView.OnRefreshListListener {
    private TitleBar titleBar;
    private PullToRefreshListView refreshListView;
    private MyAdapter adapter;
    private ArrayList<String> persons;
    private Handler mHandler;

    private Intent intent;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_listview);
        intent = new Intent();
        persons = new ArrayList<>();
        mHandler = new Handler(getMainLooper());
        titleBar = (TitleBar) findViewById(R.id.titleBar);
        titleBar.setTitleText("自定义ListView");
        titleBar.setLeftText("返回", this);

        refreshListView = (PullToRefreshListView) findViewById(R.id.refreshListView);
        adapter = new MyAdapter(this, persons);
        refreshListView.setAdapter(adapter);
//        refreshListView.setOnRefreshListListener(this);
        refreshListView.setonRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        index = 0;
                        initDate();
                        refreshListView.onRefreshComplete();
                    }
                }, 3000);
            }

            @Override
            public void onLoadMore() {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 1; i++) {
                            index++;
                            persons.add("我是第" + index + "个超级战士！");
                        }
                        refreshListView.onRefreshComplete();
                        adapter.notifyDataSetChanged();
                    }
                }, 2000);
            }
        });
    }

    @Override
    protected void initListener() {
        refreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showBigToast(persons.get(position - 1));
                switch (position) {
                    case 1:
                        intent.setClass(ListViewTestActivity.this, IocActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent.setClass(ListViewTestActivity.this, DragEditActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        intent.setClass(ListViewTestActivity.this, ComPassActivity.class);
                        startActivity(intent);
                        break;
                    case 4:
                        intent.setClass(ListViewTestActivity.this, UltraPtrActivity.class);
                        startActivity(intent);
                        break;
                    case 5:
                        intent.setClass(ListViewTestActivity.this, TestActivity.class);
                        startActivity(intent);
                        break;
                    case 6:
                        intent.setClass(ListViewTestActivity.this, TimeActivity.class);
                        startActivity(intent);
                        break;
                    case 7:
                        intent.setClass(ListViewTestActivity.this, KeyBoardActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    private int index = 0;

    @Override
    protected void initDate() {
        persons.clear();
        persons.add("注解使用");
        persons.add("拖拽编辑");
        persons.add("指南针");
        persons.add("酷炫下拉刷新");
        persons.add("测试");
        persons.add("时间选择器");
        persons.add("自定义键盘demo");
        for (int i = 0; i < 1; i++) {
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

    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                refreshListView.onRefreshComplete(true);
            }
        }, 3000);
    }

    @Override
    public void onLoadMore() {

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
