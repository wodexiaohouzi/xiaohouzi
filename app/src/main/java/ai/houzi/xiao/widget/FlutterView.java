package ai.houzi.xiao.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import ai.houzi.xiao.R;
import ai.houzi.xiao.activity.main.MyApplication;
import ai.houzi.xiao.utils.UIUtils;

/**
 * 图片漂浮效果的View
 */
public class FlutterView extends FrameLayout {
    private Context mContext;
    private ImageView imageView;
    private Drawable mDrawable;

    private TranslateAnimation translateAnimation1;
    private TranslateAnimation translateAnimation2;
    private TranslateAnimation translateAnimation3;
    private TranslateAnimation translateAnimation4;

    public FlutterView(Context context) {
        this(context, null);
    }

    public FlutterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlutterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        this.mContext = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FlutterView);
        mDrawable = a.getDrawable(R.styleable.FlutterView_fvSrc);
        a.recycle();
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if (mDrawable != null) {
            imageView.setImageDrawable(mDrawable);
        } else {
            imageView.setImageResource(R.mipmap.myinfo_background);
        }
        translateAnimation1 = new TranslateAnimation(-1, -UIUtils.dip2px(mContext, 99), 0, 0);
        translateAnimation1.setDuration(20000);
        translateAnimation2 = new TranslateAnimation(-UIUtils.dip2px(mContext, 99), -UIUtils.dip2px(mContext, 99), 0, -(1080 * UIUtils.dip2px(mContext, 99) / 1920));
        translateAnimation2.setDuration(20000);
        translateAnimation3 = new TranslateAnimation(-UIUtils.dip2px(mContext, 99), -1, -(1080 * UIUtils.dip2px(mContext, 99) / 1920), -(1080 * UIUtils.dip2px(mContext, 99) / 1920));
        translateAnimation3.setDuration(20000);
        translateAnimation4 = new TranslateAnimation(-1, -1, -(1080 * UIUtils.dip2px(mContext, 99) / 1920), 0);
        translateAnimation4.setDuration(20000);
        translateAnimation1.setAnimationListener(new MyAnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                imageView.startAnimation(translateAnimation2);
            }
        });
        translateAnimation2.setAnimationListener(new MyAnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                imageView.startAnimation(translateAnimation3);
            }
        });
        translateAnimation3.setAnimationListener(new MyAnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                imageView.startAnimation(translateAnimation4);
            }
        });
        translateAnimation4.setAnimationListener(new MyAnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                imageView.startAnimation(translateAnimation1);
            }
        });
        /**可获得焦点*/
        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        if (mode == MeasureSpec.EXACTLY && widthSize != 0 && heightSize != 0) {
            setMeasuredDimension(widthSize, heightSize);
        } else {
            setMeasuredDimension(widthSize, 268 * widthSize / 409);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (getChildCount() == 0) {
            imageView.setLayoutParams(new ViewGroup.LayoutParams(w + UIUtils.dip2px(mContext, 100), h + 1080 * UIUtils.dip2px(mContext, 100) / 1920));
            addView(imageView);
        }

        imageView.startAnimation(translateAnimation1);
    }

    class MyAnimationListener implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    /**
     * 设置图片资源
     *
     * @param resId 图片资源id
     */
    public void setFlutterResoure(int resId) {
        imageView.setImageResource(resId);
    }

    /**
     * 设置Bitmap
     *
     * @param bitmap 图片Bitmap
     */
    public void setFlutterBitmap(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }

    /**
     * 设置图片地址
     *
     * @param urlString 图片地址
     * @param resId     默认错误图片
     */
    public void setFlutterUrl(String urlString, int resId) {
        Glide.with(MyApplication.context).load(urlString).error(resId).into(imageView);
    }
}
