package ai.houzi.xiao.activity.user;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import ai.houzi.xiao.R;

public class MyInfoRecyclerAdapter extends RecyclerView.Adapter<MyInfoRecyclerAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private String[] mTitles = null;
    private String[] mIcons = null;
    private Context mContext;
    private String icon = "http://pic42.nipic.com/20140608/12504116_194242259000_2.jpg";

    public MyInfoRecyclerAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mTitles = new String[5];
        this.mIcons = new String[5];
        for (int i = 0; i < mTitles.length; i++) {
            int index = i + 1;
            mTitles[i] = "item" + index;
            mIcons[i] = icon;
        }
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
        View view = mInflater.inflate(R.layout.item_myinfo_recyclerview, parent, false);
        return new ViewHolder(view);
    }

    /**
     * 数据的绑定显示
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(mContext).load(mIcons[position]).into(holder.item_iv);
        holder.item_tv1.setText(mTitles[position]);
        holder.item_tv2.setText(mTitles[position]);
    }

    @Override
    public int getItemCount() {
        return mTitles.length;
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView item_iv;
        public TextView item_tv1;
        public TextView item_tv2;

        public ViewHolder(View view) {
            super(view);
            item_iv = (ImageView) view.findViewById(R.id.item_iv);
            item_tv1 = (TextView) view.findViewById(R.id.item_tv1);
            item_tv2 = (TextView) view.findViewById(R.id.item_tv2);
        }
    }
}
