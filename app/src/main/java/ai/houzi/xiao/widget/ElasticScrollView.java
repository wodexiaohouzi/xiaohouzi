package ai.houzi.xiao.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

/**
 * ElasticScrollView有弹性的ScrollView
 */
public class ElasticScrollView extends ScrollView {
    private Context mContext;
    /**
     * 摩擦系数
     */
    private static final int QUOTIENT = 3;
    private View inner;
    private float y;
    private Rect normal = new Rect();
    private int sTouchSlop;

    public ElasticScrollView(Context context) {
        super(context);
        this.mContext = context;
    }

    public ElasticScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 0) {
            inner = getChildAt(0);
        }
        sTouchSlop = ViewConfiguration.get(mContext).getScaledTouchSlop();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (inner == null) {
            return super.onTouchEvent(ev);
        } else {
            commOnTouchEvent(ev);
        }
        return super.onTouchEvent(ev);
    }

    float downY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            downY = getY();
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            commOnTouchEvent(ev);
            if (Math.abs(getY() - downY) > sTouchSlop) {
                return true;
            }
        } else if (ev.getAction() == MotionEvent.ACTION_UP) {
            commOnTouchEvent(ev);
        }
        return false;
    }

    public void commOnTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                y = ev.getY();
                break;
            case MotionEvent.ACTION_UP:
                y = 0;
                if (isNeedAnimation()) {
                    animation();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (y == 0) {
                    y = ev.getY();
                }
                final float preY = y;
                float nowY = ev.getY();
                int deltaY = (int) (preY - nowY);
                // 滚动
                scrollBy(0, deltaY);

                y = nowY;
                if (mode != Mode.NONE) {
                    // 当滚动到最上或者最下时就不会再滚动，这时移动布局
                    if (isNeedMove()) {
                        if (mode == Mode.TOP) {
                            if (deltaY > 0) {
                                break;
                            }
                        } else if (mode == Mode.BOTTOM) {
                            if (deltaY < 0) {
                                break;
                            }
                        }
                        if (normal.isEmpty()) {
                            // 保存正常的布局位置
                            normal.set(inner.getLeft(), inner.getTop(), inner
                                    .getRight(), inner.getBottom());

                        }
                        // 移动布局
                        inner.layout(inner.getLeft(), inner.getTop() - deltaY / QUOTIENT, inner
                                .getRight(), inner.getBottom() - deltaY / QUOTIENT);
                    }
                }
                break;
            default:
                break;
        }
    }

    // 开启动画移动
    public void animation() {
        // 开启移动动画
        TranslateAnimation ta = new TranslateAnimation(0, 0, inner.getTop(),
                normal.top);
        ta.setDuration(200);
        inner.startAnimation(ta);
        // 设置回到正常的布局位置
        inner.layout(normal.left, normal.top, normal.right, normal.bottom);
        normal.setEmpty();
    }

    // 是否需要开启动画
    public boolean isNeedAnimation() {
        return !normal.isEmpty();
    }

    // 是否需要移动布局
    public boolean isNeedMove() {
        int offset = inner.getMeasuredHeight() - getHeight();
        int scrollY = getScrollY();
        return (scrollY > ViewConfiguration.get(mContext).getScaledTouchSlop() || scrollY == offset);
    }

    public enum Mode {
        ALL(0),//上下都有弹性（默认）
        TOP(1),//只有上边需要弹性
        BOTTOM(2),//只有下边需要弹性
        NONE(3);//都不需要弹性

        Mode(int nativeInt) {
            this.nativeInt = nativeInt;
        }

        final int nativeInt;
    }

    private Mode mode;

    /**
     * 设置弹性模式
     *
     * @param mode
     */
    public void setMoveMode(Mode mode) {
        this.mode = mode;
    }
}
