package ai.houzi.xiao.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import ai.houzi.xiao.utils.UIUtils;

/**
 * Created by hp on 2016/5/16.
 */
public class RoundProgressBar extends View {
    private int circleColor = 0xFF333333;
    private int progressColor = 0xFFFFFFFF;

    private int WIDTH, HEIGHT, RADIUS;
    private Paint mPaint;

    public RoundProgressBar(Context context) {
        this(context, null);
    }

    public RoundProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(UIUtils.dip2px(context, 2));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        WIDTH = w;
        HEIGHT = h;
        RADIUS = Math.min(w, h);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画外圆环
        mPaint.setColor(circleColor);
        canvas.drawCircle(WIDTH / 2, HEIGHT / 2, RADIUS, mPaint);
        //画进度
        mPaint.setColor(progressColor);
        RectF oval = new RectF(WIDTH / 2 - RADIUS, HEIGHT / 2 - RADIUS, WIDTH / 2 + RADIUS, HEIGHT / 2 + RADIUS);  //用于定义的圆弧的形状和大小的界限
        canvas.drawArc(oval, 0, 45, false, mPaint);
    }
}
