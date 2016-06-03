package com.qqalbum.imagepicker.customviews;

import android.app.Activity;
import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qqalbum.imagepicker.R;
import com.qqalbum.imagepicker.utils.ScreenUtils;

public class MyTitleBar extends RelativeLayout {


    private RelativeLayout rlTitle;//根布局
    private LinearLayout llLeft;//左边返回布局
    private ImageView ibLeft;//左边返回图标
    //分别对应左边返回文字，标题，副标题，右边按钮文字
    private TextView tvLeft, tvTitle, tvTitle2, tvRight;
    private ImageView pbTitle;//标题位置的进度条
    private CheckBox cbRight;
    private AnimationDrawable animationDrawable;

    public MyTitleBar(Context context) {
        super(context);
        init(context);
    }

    public MyTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyTitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        if (isInEditMode()) {
            return;
        }
        View view = View.inflate(context, R.layout.my_title_bar, this);
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        rlTitle = (RelativeLayout) findViewById(R.id.rlTitle);
        llLeft = (LinearLayout) findViewById(R.id.llLeft);
        ibLeft = (ImageView) findViewById(R.id.ibLeft);
        tvLeft = (TextView) view.findViewById(R.id.tvLeft);
        tvTitle2 = (TextView) view.findViewById(R.id.tvTitle2);
        tvRight = (TextView) view.findViewById(R.id.tvRight);
        pbTitle = (ImageView) view.findViewById(R.id.pbTitle);
        cbRight = (CheckBox) view.findViewById(R.id.cbRight);
        animationDrawable = (AnimationDrawable) pbTitle.getDrawable();
        ibLeft.setVisibility(INVISIBLE);
        tvLeft.setVisibility(INVISIBLE);
        tvTitle.setVisibility(GONE);
        tvTitle2.setVisibility(GONE);
        pbTitle.setVisibility(GONE);
        tvRight.setVisibility(INVISIBLE);
        cbRight.setVisibility(INVISIBLE);
    }


    /**
     * 设置左边按钮的文字和点击事件
     *
     * @param text
     */
    public void setLeftText(String text, OnClickListener listener) {
        tvLeft.setText(TextUtils.isEmpty(text) ? "\t\t" : text);
        ibLeft.setVisibility(VISIBLE);
        tvLeft.setVisibility(VISIBLE);
        llLeft.setOnClickListener(listener);
    }

    /**
     * 设置左边按钮图标
     *
     * @param resId
     */
    public void setLeftIcon(int resId) {
        ibLeft.setImageResource(resId);
    }


    /**
     * 设置标题的文字
     *
     * @param text
     */
    public void setTitleText(CharSequence text) {
        tvTitle.setText(text);
        tvTitle.setVisibility(VISIBLE);
    }

    /**
     * 设置副标题的文字()
     *
     * @param text
     */
    public void setTitle2Text(String text) {
        tvTitle2.setText(text);
        tvTitle2.setVisibility(VISIBLE);
    }

    /**
     * 设置右边按钮的文字
     *
     * @param text
     */
    public void setRighText(String text, OnClickListener listener) {
        tvRight.setText(text);
        tvRight.setCompoundDrawables(null, null, null, null);
        tvRight.setVisibility(VISIBLE);
        tvRight.setOnClickListener(listener);
    }

    public void setRightIcon(int resId, OnClickListener listener) {
        tvRight.setText("");
        Drawable drawable = getResources().getDrawable(resId);
        // 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tvRight.setCompoundDrawables(drawable, null, null, null);
        tvRight.setVisibility(VISIBLE);
        tvRight.setOnClickListener(listener);
    }

    public void setRightCheckBox(int resId, CompoundButton.OnCheckedChangeListener listener) {
        if (resId > 0) {
            cbRight.setButtonDrawable(resId);
        }
        cbRight.setVisibility(VISIBLE);
        cbRight.setOnCheckedChangeListener(listener);
    }

    public void setRightChecked(boolean isChecked) {
        cbRight.setChecked(isChecked);
    }

    /**
     * 设置右边按钮隐藏和显示
     *
     * @param flag
     */
    public void setRightVisible(boolean flag) {
        if (flag) {
            tvRight.setVisibility(VISIBLE);
        } else {
            tvRight.setVisibility(INVISIBLE);
        }
    }

    /**
     * 设置左边图标Icon隐藏和显示
     *
     * @param flag
     */
    public void setLeftIconVisible(boolean flag) {
        if (flag) {
            ibLeft.setVisibility(VISIBLE);
        } else {
            ibLeft.setVisibility(GONE);
        }
    }

    /**
     * 设置左边按钮隐藏和显示
     *
     * @param flag
     */
    public void setLeftVisible(boolean flag) {
        if (flag) {
            llLeft.setVisibility(VISIBLE);
        } else {
            llLeft.setVisibility(INVISIBLE);
        }
    }

    /**
     * 设置标题隐藏和显示
     *
     * @param flag
     */
    public void setTitleVisible(boolean flag) {
        if (flag) {
            tvTitle.setVisibility(VISIBLE);
        } else {
            tvTitle.setVisibility(GONE);
        }
    }

    /**
     * 设置副标题隐藏和显示
     *
     * @param flag
     */
    public void setTitle2Visible(boolean flag) {
        if (flag) {
            tvTitle2.setVisibility(VISIBLE);
        } else {
            tvTitle2.setVisibility(GONE);
        }
    }

    /**
     * 设置标题栏的颜色
     *
     * @param color
     */
    public void setBackgroundByColor(int color) {
        rlTitle.setBackgroundColor(color);
    }

    /**
     * 设置标题栏的背景
     *
     * @param rid
     */
    public void setBackgroundById(int rid) {
        rlTitle.setBackgroundResource(rid);
    }

    /**
     * 设置进度条的显示隐藏
     *
     * @param flag
     */
    public void showProgressBar(boolean flag) {
        if (animationDrawable != null) {
            if (flag) {
                animationDrawable.start();
                pbTitle.setVisibility(VISIBLE);
            } else {
                animationDrawable.stop();
                pbTitle.setVisibility(GONE);
            }
        }
    }

    /**
     * 设置标题栏的透明度
     *
     * @param alpha 0~255——0代表全透明255代表不透明
     */
    public void setTitleAlpha(int alpha) {
        Drawable background = getBackground();
        if (background != null) {
            background.setAlpha(alpha);
        }
    }

    /**
     * 设置标题文字的透明度
     *
     * @param alpha
     */
    public void setTitleTextAlpha(int alpha) {
        tvTitle.setAlpha(alpha / 255f);
    }

}
