package ai.houzi.xiao.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ai.houzi.xiao.R;

/**
 * 可以刷新的ListView
 */
public class RefreshListView extends ListView implements AbsListView.OnScrollListener {
    private final static int NORMAL = 0;//正常
    private final static int READY = 1;//往下拉的时候
    private final static int RELEASE = 2;//释放
    private final static int REFRESHING = 3;//刷新
    private final static int RATIO = 1;//比例系数
    private int state;//当前的状态
    private View header;
    private int headerHeight, dropHeight = dip2px(60);//header的高度，和下拉开始刷新的高度
    private int firstVisibleItem;//当前显示的第一个item
    private int scrollState;
    private boolean isRefreshing;//是否正在刷新中

    public RefreshListView(Context context) {
        this(context, null);
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    TextView tvHeadStateText, tvHeadTime;//头部状态文字，头部更新时间
    ImageView ivHeadRefrsh, pbHead;//头部下拉icon，头部加载中icon
    private AnimationDrawable animationDrawable;//加载中动画

    private Animation animation;
    private Animation reverseAnimation;

    private Resources resource;
    private SimpleDateFormat format;
    private Handler mHandler;

    private void initView(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= 9) {
            setOverScrollMode(ListView.OVER_SCROLL_NEVER);
        }
        resource = context.getResources();
        format = new SimpleDateFormat("MM-dd", Locale.CHINA);
        mHandler = new Handler(Looper.getMainLooper());
        header = LayoutInflater.from(context).inflate(R.layout.refresh_listivew_header, null);
        tvHeadStateText = (TextView) header.findViewById(R.id.tvHeadStateText);
        tvHeadTime = (TextView) header.findViewById(R.id.tvHeadTime);
        ivHeadRefrsh = (ImageView) header.findViewById(R.id.ivHeadRefrsh);
        pbHead = (ImageView) header.findViewById(R.id.pbHead);
        animationDrawable = (AnimationDrawable) pbHead.getDrawable();

        animation = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(200);
        animation.setFillAfter(true);

        reverseAnimation = new RotateAnimation(-180, 0,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        reverseAnimation.setInterpolator(new LinearInterpolator());
        reverseAnimation.setDuration(200);
        reverseAnimation.setFillAfter(true);
        this.addHeaderView(header);
        measureView(header);
        headerHeight = header.getMeasuredHeight();
        topPadding(-headerHeight);

        setOnScrollListener(this);
    }

    private void topPadding(int top) {
        header.setPadding(header.getPaddingLeft(), top, header.getPaddingRight(), header.getPaddingBottom());
        header.invalidate();
    }

    private int startY;//按下时Y值

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                handleMove(event);
                break;
            case MotionEvent.ACTION_UP:
                if (state == RELEASE) {
                    state = REFRESHING;
                    //刷新数据
                    if (listListener != null && (event.getY() - startY) / RATIO > dropHeight && !isRefreshing) {
                        isRefreshing = true;
                        listListener.onRefresh();
                    }
                    refreshViewByState();
                } else if (state == READY) {
                    state = NORMAL;
                    refreshViewByState();
                }
                isBack = false;
                break;

        }
        return super.onTouchEvent(event);
    }

    private void handleMove(MotionEvent event) {
        int tempY = (int) event.getY();
        int space = (tempY - startY) / RATIO;
        int topPadding = space - headerHeight;
        switch (state) {
            case NORMAL:
                if (space > 0) {
                    if (firstVisibleItem > 0) {
                        startY = tempY;
                    } else {
                        state = READY;
                        refreshViewByState();
                    }
                }
                break;
            case READY:
                topPadding(topPadding);
                if (space >= dropHeight + 20 && scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    state = RELEASE;
                    refreshViewByState();
                }
                break;
            case RELEASE:
                topPadding(topPadding);
                if (space < dropHeight + 20) {
                    isBack = true;
                    state = READY;
                    refreshViewByState();
                } else if (space <= 0) {
                    state = NORMAL;
                    refreshViewByState();
                }
                break;
        }
    }

    boolean isBack;

    /**
     * 通过状态来改变head样式
     */
    private void refreshViewByState() {
        switch (state) {
            case NORMAL:
                setSelection(0);
                isRefreshing = false;
                topPadding(-headerHeight);
                ivHeadRefrsh.setVisibility(VISIBLE);
                pbHead.setVisibility(INVISIBLE);
                animationDrawable.stop();
                tvHeadStateText.setText(resource.getString(R.string.refresh_listview_header_hint_normal));
                ivHeadRefrsh.clearAnimation();
                break;
            case READY:
                tvHeadStateText.setText(resource.getString(R.string.refresh_listview_header_hint_normal));
                tvHeadTime.setText(format.format(new Date()));
                ivHeadRefrsh.clearAnimation();
                ivHeadRefrsh.startAnimation(reverseAnimation);
//                if (isBack) {
//                    Logg.e("================READY=======" + isBack);
//                    ivHeadRefrsh.startAnimation(reverseAnimation);
//                    isBack = false;
//                }
                break;
            case RELEASE:
                tvHeadStateText.setText(resource.getString(R.string.refresh_listview_header_hint_release));
                ivHeadRefrsh.clearAnimation();
                ivHeadRefrsh.startAnimation(animation);
                break;
            case REFRESHING:
                setSelection(0);
                topPadding(dropHeight - headerHeight);
                ivHeadRefrsh.setVisibility(INVISIBLE);
                pbHead.setVisibility(VISIBLE);
                animationDrawable.start();
                tvHeadStateText.setText(resource.getString(R.string.refresh_listview_header_hint_loading));
                ivHeadRefrsh.clearAnimation();
                break;
        }
    }

    private OnRefreshListListener listListener;

    public interface OnRefreshListListener {
        void onRefresh();

        void onLoadMore();
    }

    public void setOnRefreshListListener(OnRefreshListListener listener) {
        this.listListener = listener;
    }

    public void onRefreshComplete(boolean isSuccess) {
        state = NORMAL;
        if (isSuccess) {
            tvHeadStateText.setText(resource.getString(R.string.refresh_listview_header_hint_success));
        } else {
            tvHeadStateText.setText(resource.getString(R.string.refresh_listview_header_hint_fail));
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshViewByState();
            }
        }, 500);
    }

    /**
     * 通知父容器，占用的宽，高；
     *
     * @param child
     */
    private void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
                    MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0,
                    MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    private int dip2px(int dip) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        this.scrollState = scrollState;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.firstVisibleItem = firstVisibleItem;
    }
}
