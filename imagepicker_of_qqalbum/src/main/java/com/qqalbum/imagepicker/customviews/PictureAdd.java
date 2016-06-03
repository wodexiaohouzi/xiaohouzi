package com.qqalbum.imagepicker.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.qqalbum.imagepicker.R;
import com.qqalbum.imagepicker.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 2016/4/13.
 */
public class PictureAdd extends GridView {
    private final int COUNT = 4;
    private Context mContent;
    private int width, height, ivWidth;
    private PictureAdapter pAdapter;
    private ArrayList<String> urls;

    public PictureAdd(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContent = context;
        initView();
    }

    public PictureAdd(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContent = context;
        initView();
    }

    private void initView() {
        setSelector(android.R.color.transparent);
        setOverScrollMode(OVER_SCROLL_NEVER);
        setNumColumns(COUNT);
        urls = new ArrayList<>();
        pAdapter = new PictureAdapter(mContent, urls);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        ivWidth = w / COUNT;
    }

    public void setUrls(String[] urls) {
        this.urls.clear();
        for (int i = 0; i < urls.length; i++) {
            this.urls.add(urls[i]);
        }
        this.urls.add("");
        setAdapter(pAdapter);
    }

    public void setUrls(ArrayList<String> urls) {
        this.urls.clear();
        this.urls.addAll(urls);
        this.urls.add("");
        setAdapter(pAdapter);
    }

    public class PictureAdapter extends BaseAdapter {
        private List<String> datas;
        private LayoutInflater inflater;
        private int divisionLine;

        public PictureAdapter(Context context, List<String> datas) {
            inflater = LayoutInflater.from(context);
            this.datas = datas;
            divisionLine = ScreenUtils.dp2px(3);
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public String getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_picture, null);
                holder = new ViewHolder();
                holder.iv = (ImageView) convertView.findViewById(R.id.image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ivWidth - divisionLine * 2, ivWidth - divisionLine * 2);
            params.setMargins(divisionLine, divisionLine, divisionLine, divisionLine);
            holder.iv.setLayoutParams(params);
            if (position == datas.size() - 1) {
                holder.iv.setImageResource(R.mipmap.icon_add_image);
            } else {
                Glide.with(mContent).load(getItem(position)).placeholder(R.drawable.empty_photo).into(holder.iv);
            }
            return convertView;
        }

        class ViewHolder {
            ImageView iv;
        }
    }
}
