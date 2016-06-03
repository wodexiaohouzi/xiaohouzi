package com.juxin.common;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.juxin.common.utils.FileSizeUtil;
import com.juxin.common.utils.Util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

public class FileListAdapter extends BaseAdapter {

    private SimpleDateFormat format;
    private LayoutInflater mInflater;
    private List<File> fileList;
    private Context context;

    public FileListAdapter(Context context, List<File> fileList) {
        this.fileList = fileList;
        this.context = context;
        mInflater = LayoutInflater.from(context);
        format = new SimpleDateFormat("yyyy-MM-dd");
    }

    @Override
    public int getCount() {
        return fileList.size();
    }

    @Override
    public Object getItem(int position) {
        return fileList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.file_row, null);
            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.filetext);
            holder.fileInfo = (TextView) convertView.findViewById(R.id.fileInfo);
            holder.icon = (ImageView) convertView.findViewById(R.id.fileicon);
            holder.cBox = (CheckBox) convertView.findViewById(R.id.file_check);
            holder.ivDetails = (ImageView) convertView.findViewById(R.id.ivDetails);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final File f = fileList.get(position);
        holder.text.setText(f.getName());
        if (f.isDirectory()) {
            holder.cBox.setVisibility(View.GONE);
            holder.fileInfo.setVisibility(View.GONE);
            holder.ivDetails.setImageResource(R.drawable.theme_item_arrow_normal);
            holder.icon.setImageResource(R.drawable.filesystem_icon_folder);
        } else {
            holder.cBox.setVisibility(View.VISIBLE);
            holder.fileInfo.setVisibility(View.VISIBLE);
            holder.ivDetails.setImageResource(R.drawable.details_fg_normal);
            holder.fileInfo.setText(FileSizeUtil.FormetFileSize(FileSizeUtil.getFileSize(f)) + "\t" + format.format(f.lastModified()));
            holder.cBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        if (onClickBoxListener != null) {
                            onClickBoxListener.onClickBox(buttonView, f.getAbsolutePath());
                        }
                    }
                }
            });
            holder.ivDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickBoxListener != null) {
                        onClickBoxListener.onFileDetil(v, f.getAbsolutePath());
                    }
                }
            });
            String name = f.getName().toLowerCase();
            if (name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".gif") || name.endsWith(".bmp")) {
                Glide.with(context).load(f).placeholder(R.drawable.filesystem_icon_photo).into(holder.icon);
            } else if (name.endsWith(".mp3") || name.endsWith(".wav") || name.endsWith(".ogg") || name.endsWith(".wma") || name.endsWith(".ape") || name.endsWith(".aac")) {
//                Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
//                cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.CONTENT_TYPE))
//                MediaPlayer player = new MediaPlayer();
//                try {
//                    player.setDataSource(f.getPath());
//                    player.get
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                Glide.with(context).load(f).placeholder(R.drawable.filesystem_icon_music).into(holder.icon);
                holder.icon.setImageResource(R.drawable.filesystem_icon_music);
            } else if (name.endsWith(".rmvb") || name.endsWith(".rm") || name.endsWith(".avi") || name.endsWith(".mkv") || name.endsWith(".wmv") || name.endsWith(".3gp") || name.endsWith(".mp4")) {
                Glide.with(context).load(f).placeholder(R.drawable.filesystem_icon_movie).into(holder.icon);
            } else if (name.endsWith(".txt")) {
                holder.icon.setImageResource(R.drawable.filesystem_icon_text);
            } else if (name.endsWith(".doc") || name.endsWith(".docx")) {
                holder.icon.setImageResource(R.drawable.filesystem_icon_word);
            } else if (name.endsWith(".apk")) {
                Drawable apkIcon = Util.getApkIcon(context, f.getPath());
                if (apkIcon == null) {
                    holder.icon.setImageResource(R.drawable.filesystem_icon_apk);
                } else {
                    holder.icon.setImageDrawable(apkIcon);
                }
            } else if (name.endsWith(".zip") || name.endsWith(".rar")) {
                holder.icon.setImageResource(R.drawable.filesystem_icon_rar);
            } else if (name.endsWith(".ppt")) {
                holder.icon.setImageResource(R.drawable.filesystem_icon_ppt);
            } else if (name.endsWith(".pdf")) {
                holder.icon.setImageResource(R.drawable.filesystem_icon_pdf);
            } else if (name.endsWith(".chm")) {
                holder.icon.setImageResource(R.drawable.filesystem_icon_chm);
            } else {
                holder.icon.setImageResource(R.drawable.filesystem_icon_default);
            }
        }
        return convertView;
    }

    private OnClickBoxListener onClickBoxListener;

    interface OnClickBoxListener {
        public void onClickBox(View v, String absolutePath);

        public void onFileDetil(View v, String absolutePath);
    }

    public void setOnClickBoxListener(OnClickBoxListener listener) {
        this.onClickBoxListener = listener;
    }

    public final class ViewHolder {
        public TextView text;
        public TextView fileInfo;
        public ImageView icon;
        public CheckBox cBox;
        private ImageView ivDetails;
    }
}
