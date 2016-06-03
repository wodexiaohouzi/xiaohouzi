package ai.houzi.xiao.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import ai.houzi.xiao.R;

/**
 * 大吐司
 */
public class BigToast extends Toast {

    private final LayoutInflater inflater;
    private View view;
    private ImageView ivIcon;
    private TextView tvContent;

    public BigToast(Context context) {
        super(context);
        inflater = LayoutInflater.from(context);
        if (view == null) {
            view = inflater.inflate(R.layout.big_toast, null);
            ivIcon = (ImageView) view.findViewById(R.id.ivIcon);
            tvContent = (TextView) view.findViewById(R.id.tvContent);
        }
    }

    public void showToast() {
        if (view != null) {
            setView(view);
            setGravity(Gravity.CENTER, 0, 0);
        }
        tvContent.setText(mContent);
        switch (mIcon) {
            case RIGHT:
                ivIcon.setImageResource(R.mipmap.icon_right);
                break;
            case WRONG:
                ivIcon.setImageResource(R.mipmap.icon_warn);
                break;
            default:
                ivIcon.setImageResource(mIcon);
                break;
        }
        show();
    }

    /**
     * 正确的图标
     */
    public static final int RIGHT = 0;
    /**
     * 错误的图标
     */
    public static final int WRONG = 1;
    private int mIcon;

    public void setIcon(int resId) {
        this.mIcon = resId;
    }

    private String mContent;

    public void setContent(String text) {
        this.mContent = text;
    }


}
