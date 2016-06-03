package com.juxin.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class FileAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<FileActivity.Entity> entities;
    private Context context;

    public FileAdapter(Context context, List<FileActivity.Entity> entities) {
        this.entities = entities;
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return entities.size();
    }

    @Override
    public Object getItem(int position) {
        return entities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_gridview, null);
            holder = new ViewHolder();
            holder.ivIcon = (ImageView) convertView.findViewById(R.id.ivIcon);
            holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            holder.tvNum = (TextView) convertView.findViewById(R.id.tvNum);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        FileActivity.Entity entity = entities.get(position);
        holder.ivIcon.setImageResource(entity.icon);
        holder.tvName.setText(entity.name);
        holder.tvNum.setText(entity.count + "");
        return convertView;
    }

    final class ViewHolder {
        public ImageView ivIcon;
        public TextView tvName;
        public TextView tvNum;
    }
}
