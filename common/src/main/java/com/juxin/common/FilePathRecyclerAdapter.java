package com.juxin.common;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FilePathRecyclerAdapter extends RecyclerView.Adapter<FilePathRecyclerAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private List<String> filePath = null;
    private Context mContext;

    public FilePathRecyclerAdapter(Context context, List<String> filePath) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.filePath = filePath;
    }

    /**
     * item显示类型
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_cell, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, onItemClickListener);
        return viewHolder;
    }

    /**
     * 数据的绑定显示
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvPath.setText(filePath.get(position).replace("sdcard", "手机SD卡"));
    }

    @Override
    public int getItemCount() {
        return filePath.size();
    }

    private OnItemClickListener onItemClickListener;

    interface OnItemClickListener {
        void onClick(View v, int position);
    }

    public void setOnClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void addData(int position, String path) {
        filePath.add(position, path);
        notifyItemInserted(position);
    }

    public void removeData(int position) {
        ArrayList<String> ps = new ArrayList<>();
        for (int i = position; i < getItemCount(); i++) {
            ps.add(filePath.get(i));
        }
        filePath.removeAll(ps);
        notifyDataSetChanged();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvPath;
        private OnItemClickListener onItemClickListener;

        public ViewHolder(View view, OnItemClickListener listener) {
            super(view);
            this.onItemClickListener = listener;
            tvPath = (TextView) view.findViewById(R.id.tvPath);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onClick(v, getPosition());
                    }
                }
            });
        }
    }
}
