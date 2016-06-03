package com.qqalbum.imagepicker.adapter;

import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.qqalbum.imagepicker.R;
import com.qqalbum.imagepicker.ui.PhotoWallActivity;
import com.qqalbum.imagepicker.utils.Logg;
import com.qqalbum.imagepicker.utils.SDCardImageLoader;
import com.qqalbum.imagepicker.utils.ScreenUtils;

import java.util.ArrayList;

/**
 * PhotoWall中GridView的适配器
 *
 * @author hanj
 */

public class PhotoWallAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> imagePathList = null;
    private ArrayList<String> paths = null;

    private SDCardImageLoader loader;

    //记录是否被选择
    private SparseBooleanArray selectionMap;
    private int mCount;

    public PhotoWallAdapter(Context context, ArrayList<String> imagePathList, int choiceCount, ArrayList<String> paths) {
        this.context = context;
        this.imagePathList = imagePathList;
        this.mCount = choiceCount;
        this.paths = paths;
        loader = new SDCardImageLoader(ScreenUtils.getScreenW(), ScreenUtils.getScreenH());
        selectionMap = new SparseBooleanArray();
    }

    @Override
    public int getCount() {
        return imagePathList == null ? 0 : imagePathList.size();
    }

    @Override
    public String getItem(int position) {
        return imagePathList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String filePath = getItem(position);

        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.photo_wall_item, null);
            holder = new ViewHolder();

            holder.imageView = (ImageView) convertView.findViewById(R.id.photo_wall_item_photo);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.photo_wall_item_cb);
            if (mCount == PhotoWallActivity.SINGLE) {
                holder.checkBox.setVisibility(View.INVISIBLE);
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (mCount == PhotoWallActivity.MULTI) {
            //tag的key必须使用id的方式定义以保证唯一，否则会出现IllegalArgumentException.
            holder.checkBox.setTag(R.id.tag_first, position);
            holder.checkBox.setTag(R.id.tag_second, holder.imageView);
            holder.checkBox.setChecked(paths.contains(filePath));
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Integer position = (Integer) buttonView.getTag(R.id.tag_first);
                    ImageView image = (ImageView) buttonView.getTag(R.id.tag_second);

                    if (isChecked) {
                        selectionMap.put(position, isChecked);
                        image.setColorFilter(context.getResources().getColor(R.color.image_checked_bg));
                    } else {
                        selectionMap.delete(position);
                        image.setColorFilter(null);
                    }
                    if (listener != null) {
                        listener.onSeletion(selectionMap.size());
                    }
                }
            });
        }
        holder.imageView.setTag(filePath);
        loader.loadImage(4, filePath, holder.imageView);
        return convertView;
    }

    private class ViewHolder {
        ImageView imageView;
        CheckBox checkBox;
    }

    public SparseBooleanArray getSelectionMap() {
        return selectionMap;
    }

    public void clearSelectionMap() {
        selectionMap.clear();
    }

    private SeletionListener listener;

    public interface SeletionListener {
        void onSeletion(int count);
    }

    public void setSeletionListener(SeletionListener listener) {
        this.listener = listener;
    }
}
