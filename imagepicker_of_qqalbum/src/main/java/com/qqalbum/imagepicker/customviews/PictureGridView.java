package com.qqalbum.imagepicker.customviews;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
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
import com.qqalbum.imagepicker.ui.ImagePagerActivity;
import com.qqalbum.imagepicker.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 2016/4/13.
 */
public class PictureGridView extends LinearLayout {
    private GridView gridView;
    private ImageView imageView;
    private final int COUNT = 3;
    private Context mContent;
    private int width, height, ivWidth;
    private PictureAdapter pAdapter;
    private ArrayList<String> urls;

    public PictureGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContent = context;
        initView();
    }

    public PictureGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContent = context;
        initView();
    }

    private void initView() {
        urls = new ArrayList<>();
        imageView = new ImageView(mContent);
        imageView.setAdjustViewBounds(true);
        imageView.setBackgroundResource(R.drawable.empty_photo);
        gridView = new GridView(mContent);
        gridView.setSelector(android.R.color.transparent);
        gridView.setOverScrollMode(OVER_SCROLL_NEVER);
        gridView.setNumColumns(COUNT);
        imageView.setVisibility(GONE);
        gridView.setVisibility(GONE);
        addView(imageView);
        addView(gridView);
        if (isInEditMode()) {
            return;
        }
        pAdapter = new PictureAdapter(mContent, urls);
        gridView.setAdapter(pAdapter);

        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                imageBrower(0, urls);
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                imageBrower(position, urls);
            }
        });
    }

    private void imageBrower(int position, ArrayList<String> urls) {
        Intent intent = new Intent(mContent, ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putStringArrayListExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        mContent.startActivity(intent);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        ivWidth = (w) / COUNT;
        imageView.setMaxWidth(width * 3 / 4);
        imageView.setMaxHeight(width * 3 / 4);
    }

    public void setUrls(String[] urls) {
        this.urls.clear();
        for (int i = 0; i < urls.length; i++) {
            this.urls.add(urls[i]);
        }
        if (this.urls.size() == 1) {
            imageView.setVisibility(VISIBLE);
            gridView.setVisibility(GONE);
            Glide.with(mContent).load(urls[0]).into(imageView);
            imageView.setTag(urls[0]);
        } else {
            imageView.setVisibility(GONE);
            gridView.setVisibility(VISIBLE);
            pAdapter.notifyDataSetChanged();
        }
    }

    public void setUrls(ArrayList<String> urls) {
        this.urls = urls;
        if (this.urls.size() == 1) {
            imageView.setVisibility(VISIBLE);
            gridView.setVisibility(GONE);
            Glide.with(mContent).load(urls.get(0)).into(imageView);
        } else {
            imageView.setVisibility(GONE);
            gridView.setVisibility(VISIBLE);
            pAdapter.notifyDataSetChanged();
        }
    }


    public class PictureAdapter extends BaseAdapter {
        private List<String> datas;
        private LayoutInflater inflater;
        private int divisionLine;

        public PictureAdapter(Context context, List<String> datas) {
            inflater = LayoutInflater.from(context);
            this.datas = datas;
            ScreenUtils.initScreen((Activity) context);
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
                convertView = inflater.inflate(com.qqalbum.imagepicker.R.layout.item_picture, null);
                holder = new ViewHolder();
                holder.iv = (ImageView) convertView.findViewById(com.qqalbum.imagepicker.R.id.image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ivWidth, ivWidth);
            params.setMargins(divisionLine, divisionLine, divisionLine, 0);
            holder.iv.setLayoutParams(params);
            Glide.with(mContent).load(getItem(position)).placeholder(R.drawable.empty_photo).into(holder.iv);
            return convertView;
        }

        class ViewHolder {
            ImageView iv;
        }
    }
}
