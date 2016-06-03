package ai.houzi.xiao.activity.user;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import ai.houzi.xiao.R;
import ai.houzi.xiao.activity.main.BaseActivity;
import ai.houzi.xiao.adapter.CommonAdapter;
import ai.houzi.xiao.adapter.ViewHolder;
import ai.houzi.xiao.widget.TitleBar;
import ai.houzi.xiao.widget.dslv.DragSortListView;

/**
 * 拖拽列表
 */
public class DragEditActivity extends BaseActivity {
    private TitleBar titleBar;
    private DragSortListView mListView;
    private EditAdapter adapter;
    private ArrayList<String> persons;

    private final DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {
        @Override
        public void drop(int from, int to) {
            String item = persons.get(from);
            persons.remove(item);
            persons.add(to, item);
            adapter.notifyDataSetChanged();
        }
    };
    private final DragSortListView.RemoveListener onRemove = new DragSortListView.RemoveListener() {
        @Override
        public void remove(int which) {
            // adapter.remove(adapter.getItem(which));
        }
    };

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_edit);
        persons = new ArrayList<>();
        titleBar = (TitleBar) findViewById(R.id.titleBar);
        titleBar.setTitleText("编辑排序");
        titleBar.setLeftText("返回", this);
        mListView = (DragSortListView) findViewById(R.id.mListView);
        mListView.setDropListener(onDrop);
        mListView.setRemoveListener(onRemove);

        adapter = new EditAdapter(this, persons);
        adapter.setOnDeleteListener(new EditAdapter.onDeleteListener() {
            @Override
            public void onDelete(View view) {
                persons.remove(view.getTag());
                adapter.notifyDataSetChanged();
            }
        });
        mListView.setAdapter(adapter);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initDate() {
        for (int i = 0; i < 10; i++) {
            persons.add("我是第" + (i + 1) + "个人");
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

    private static class EditAdapter extends CommonAdapter<String> implements View.OnClickListener {


        public EditAdapter(Context context, List<String> datas) {
            super(context, R.layout.item_edit_draglist, datas);
        }

        @Override
        public void convert(ViewHolder holder, String s) {
            holder.setText(R.id.tvName, s);
            holder.getView(R.id.ivDelete).setTag(s);
            holder.getView(R.id.ivDelete).setOnClickListener(this);
        }

        onDeleteListener delListener;

        public interface onDeleteListener {
            void onDelete(View view);
        }

        public void setOnDeleteListener(onDeleteListener listener) {
            this.delListener = listener;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ivDelete:
                    delListener.onDelete(v);
                    break;
            }
        }
    }
}
