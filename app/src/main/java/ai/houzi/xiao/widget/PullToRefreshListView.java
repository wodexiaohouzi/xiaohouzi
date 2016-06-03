package ai.houzi.xiao.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ai.houzi.xiao.R;

import static android.widget.Toast.*;

public class PullToRefreshListView extends ListView implements AbsListView.OnScrollListener {

    private static final String TAG = "PullToRefreshListView";

    private final static int RELEASE_TO_REFRESH = 0;
    private final static int PULL_TO_REFRESH = 1;
    private final static int REFRESHING = 2;
    private final static int DONE = 3;
    private final static int LOADING = 4;

    // 实际的padding的距离与界面上偏移距离的比例
    private final static int RATIO = 3;
    private LayoutInflater inflater;

    //listview的头部 用于显示刷新的箭头等
    private LinearLayout headView;
    private TextView tvHeadStateText;
    private TextView tvHeadTime;
    private ImageView ivHeadRefrsh;
    private ImageView pbHead;
    private AnimationDrawable animationDrawable;//加载中动画

    //箭头旋转的动画
    private RotateAnimation animation;
    private RotateAnimation reverseAnimation;

    // 用于保证startY的值在一个完整的touch事件中只被记录一次
    private boolean isRecored;
    private int headContentWidth;
    private int headContentHeight;
    private int dropHeight = dip2px(60);
    private int startY;
    private int firstItemIndex;
    private int state;
    private boolean isBack;

    private OnRefreshListener refreshListener;

    private boolean isRefreshable;
    private SimpleDateFormat format;

    public PullToRefreshListView(Context context) {
        super(context);
        init(context);
    }

    public PullToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= 9) {
            setOverScrollMode(ListView.OVER_SCROLL_NEVER);
        }
        setCacheColorHint(context.getResources().getColor(R.color.transparent));
        format = new SimpleDateFormat("MM-dd", Locale.CHINA);
        inflater = LayoutInflater.from(context);
        headView = (LinearLayout) inflater.inflate(R.layout.refresh_listivew_header, null);

        ivHeadRefrsh = (ImageView) headView
                .findViewById(R.id.ivHeadRefrsh);
        ivHeadRefrsh.setMinimumWidth(70);
        ivHeadRefrsh.setMinimumHeight(50);
        pbHead = (ImageView) headView.findViewById(R.id.pbHead);
        animationDrawable = (AnimationDrawable) pbHead.getDrawable();
        tvHeadStateText = (TextView) headView.findViewById(R.id.tvHeadStateText);
        tvHeadTime = (TextView) headView.findViewById(R.id.tvHeadTime);

        measureView(headView);
        headContentHeight = headView.getMeasuredHeight();
        headContentWidth = headView.getMeasuredWidth();

        headView.setPadding(0, -1 * headContentHeight, 0, 0);
        headView.invalidate();

        Log.v("size", "width:" + headContentWidth + " height:"
                + headContentHeight);

        addHeaderView(headView, null, false);
        setOnScrollListener(this);

        animation = new RotateAnimation(0, -180,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(250);
        animation.setFillAfter(true);

        reverseAnimation = new RotateAnimation(-180, 0,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        reverseAnimation.setInterpolator(new LinearInterpolator());
        reverseAnimation.setDuration(200);
        reverseAnimation.setFillAfter(true);

        state = DONE;
        isRefreshable = false;
    }

    private int indexY;
    private boolean isLoadingMore;

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                         int totalItemCount) {
        firstItemIndex = firstVisibleItem;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                indexY = view.getFirstVisiblePosition();
                break;
            case OnScrollListener.SCROLL_STATE_IDLE:
                int position = view.getFirstVisiblePosition();
                // 判断滚动到底部
                if (view.getLastVisiblePosition() == (view.getCount() - 1) && position <= indexY) {
                    if (refreshListener != null) {
                        isLoadingMore = true;
                        refreshListener.onLoadMore();
                    }
                }
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isRefreshable) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (firstItemIndex == 0 && !isRecored) {
                        isRecored = true;
                        startY = (int) event.getY();
                        Log.v(TAG, "在down时候记录当前位置");
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (state != REFRESHING && state != LOADING) {
                        if (state == DONE) {
                            // 什么都不做
                        }
                        if (state == PULL_TO_REFRESH) {
                            state = DONE;
                            changeHeaderViewByState();

                            Log.v(TAG, "由下拉刷新状态，到done状态");
                        }
                        if (state == RELEASE_TO_REFRESH) {
                            state = REFRESHING;
                            changeHeaderViewByState();
                            onRefresh();

                            Log.v(TAG, "由松开刷新状态，到done状态");
                        }
                    }
                    isRecored = false;
                    isBack = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                    int tempY = (int) event.getY();
                    if (!isRecored && firstItemIndex == 0) {
                        Log.v(TAG, "在move时候记录下位置");
                        isRecored = true;
                        startY = tempY;
                    }
                    if (state != REFRESHING && isRecored && state != LOADING) {
                        // 保证在设置padding的过程中，当前的位置一直是在head，否则如果当列表超出屏幕的话，当在上推的时候，列表会同时进行滚动
                        // 可以松手去刷新了
                        if (state == RELEASE_TO_REFRESH) {
                            setSelection(0);
                            // 往上推了，推到了屏幕足够掩盖head的程度，但是还没有推到全部掩盖的地步
                            if (((tempY - startY) / RATIO < dropHeight)
                                    && (tempY - startY) > 0) {
                                state = PULL_TO_REFRESH;
                                changeHeaderViewByState();
                                Log.v(TAG, "由松开刷新状态转变到下拉刷新状态");
                            }
                            // 一下子推到顶了
                            else if (tempY - startY <= 0) {
                                state = DONE;
                                changeHeaderViewByState();
                                Log.v(TAG, "由松开刷新状态转变到done状态");
                            }
                            // 往下拉了，或者还没有上推到屏幕顶部掩盖head的地步
                            else {
                                // 不用进行特别的操作，只用更新paddingTop的值就行了
                            }
                        }
                        // 还没有到达显示松开刷新的时候,DONE或者是PULL_To_REFRESH状态
                        if (state == PULL_TO_REFRESH) {
                            setSelection(0);
                            // 下拉到可以进入RELEASE_TO_REFRESH的状态
                            if ((tempY - startY) / RATIO >= dropHeight) {
                                state = RELEASE_TO_REFRESH;
                                isBack = true;
                                changeHeaderViewByState();
                                Log.v(TAG, "由done或者下拉刷新状态转变到松开刷新");
                            }
                            // 上推到顶了
                            else if (tempY - startY <= 0) {
                                state = DONE;
                                changeHeaderViewByState();
                                Log.v(TAG, "由DOne或者下拉刷新状态转变到done状态");
                            }
                        }
                        // done状态下
                        if (state == DONE) {
                            if (tempY - startY > 0) {
                                state = PULL_TO_REFRESH;
                                changeHeaderViewByState();
                            }
                        }
                        // 更新headView的size
                        if (state == PULL_TO_REFRESH) {
                            headView.setPadding(0, -1 * headContentHeight + (tempY - startY) / RATIO, 0, 0);
                        }
                        // 更新headView的paddingTop
                        if (state == RELEASE_TO_REFRESH) {
                            headView.setPadding(0, (tempY - startY) / RATIO
                                    - headContentHeight, 0, 0);
                        }
                    }
                    break;
            }
        }

        return super.onTouchEvent(event);
    }

    // 当状态改变时候，调用该方法，以更新界面
    private void changeHeaderViewByState() {
        switch (state) {
            case RELEASE_TO_REFRESH:
                ivHeadRefrsh.setVisibility(View.VISIBLE);
                pbHead.setVisibility(View.GONE);
                animationDrawable.stop();
                tvHeadStateText.setVisibility(View.VISIBLE);
                tvHeadTime.setVisibility(View.VISIBLE);

                ivHeadRefrsh.clearAnimation();
                ivHeadRefrsh.startAnimation(animation);
                tvHeadStateText.setText("放开以刷新");
                Log.v(TAG, "当前状态，松开刷新");
                break;
            case PULL_TO_REFRESH:
                pbHead.setVisibility(View.GONE);
                animationDrawable.stop();
                tvHeadStateText.setVisibility(View.VISIBLE);
                tvHeadTime.setVisibility(View.VISIBLE);
                ivHeadRefrsh.clearAnimation();
                ivHeadRefrsh.setVisibility(View.VISIBLE);
                // 是由RELEASE_To_REFRESH状态转变来的
                if (isBack) {
                    isBack = false;
                    ivHeadRefrsh.clearAnimation();
                    ivHeadRefrsh.startAnimation(reverseAnimation);
                    tvHeadStateText.setText("下拉刷新");
                } else {
                    tvHeadStateText.setText("下拉刷新");
                }
                Log.v(TAG, "当前状态，下拉刷新");
                break;

            case REFRESHING:
                int paddingTop = headView.getPaddingTop();
                ValueAnimator animator = ValueAnimator.ofInt(paddingTop, dropHeight - headContentHeight).setDuration(200);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int value = (int) animation.getAnimatedValue();
                        headView.setPadding(0, value, 0, 0);
                    }
                });
                animator.start();
                pbHead.setVisibility(View.VISIBLE);
                animationDrawable.start();
                ivHeadRefrsh.clearAnimation();
                ivHeadRefrsh.setVisibility(View.GONE);
                tvHeadStateText.setText("正在刷新...");
                tvHeadTime.setVisibility(View.VISIBLE);
                Log.v(TAG, "当前状态,正在刷新...");
                break;
            case DONE:
                ValueAnimator animator1 = ValueAnimator.ofInt(dropHeight - headContentHeight, -headContentHeight).setDuration(200);
                animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int value = (int) animation.getAnimatedValue();
                        headView.setPadding(0, value, 0, 0);
                    }
                });
                animator1.start();
                pbHead.setVisibility(View.INVISIBLE);
                animationDrawable.stop();
                ivHeadRefrsh.clearAnimation();
                ivHeadRefrsh.setImageResource(R.drawable.arrow);
                tvHeadStateText.setText("下拉刷新");
                tvHeadTime.setVisibility(View.VISIBLE);
                Log.v(TAG, "当前状态，done");
                break;
        }
    }

    public void setonRefreshListener(OnRefreshListener refreshListener) {
        this.refreshListener = refreshListener;
        isRefreshable = true;
    }

    public interface OnRefreshListener {
        void onRefresh();

        void onLoadMore();
    }

    public void onRefreshComplete() {
        isLoadingMore = false;
        state = DONE;
        tvHeadTime.setText(format.format(new Date()));
        changeHeaderViewByState();
    }

    private void onRefresh() {
        if (refreshListener != null) {
            refreshListener.onRefresh();
        }
    }

    // 此方法直接照搬自网络上的一个下拉刷新的demo，此处是“估计”headView的width以及height
    private void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
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

    public void setAdapter(BaseAdapter adapter) {
        tvHeadTime.setText(format.format(new Date()));
        super.setAdapter(adapter);
    }

    private int dip2px(int dip) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }
}
