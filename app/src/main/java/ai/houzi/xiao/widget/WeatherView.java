package ai.houzi.xiao.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import ai.houzi.xiao.R;
import ai.houzi.xiao.utils.Logg;
import ai.houzi.xiao.utils.UIUtils;

/**
 * 天气控件
 */
public class WeatherView extends View {

    private GestureDetector detector;
    private int width, height;
    private float lineWidth;
    private Paint mPaint;
    ArrayList<Point> weathers = new ArrayList<>();
    //    float[] weathers = {15, 25, 15, 25, 16, 21, 14, 20, 14, 19};
    float[] ws;

    public WeatherView(Context context) {
        this(context, null);
    }

    public WeatherView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WeatherView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WeatherView);
        lineWidth = a.getDimension(R.styleable.WeatherView_lineWidth, UIUtils.dip2px(context, 2));
        a.recycle();
        initView();

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return detector.onTouchEvent(event);
            }
        });
        detector = new GestureDetector(context, new GestureDetector.OnGestureListener() {

            @Override
            public boolean onDown(MotionEvent e) {
                Logg.d("onDown");
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {
                Logg.d("onShowPress");

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                Logg.d("onSingleTapUp");
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                Logg.d("onScroll");
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                Logg.d("onLongPress");

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                Logg.d("onFling");
                return false;
            }
        });
        detector.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                Logg.d("onSingleTapConfirmed");
                return false;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                Logg.d("onDoubleTap");
                return false;
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                Logg.d("onDoubleTapEvent");
                return false;
            }
        });
    }

    private void initView() {
        if (isInEditMode()) {
            return;
        }
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeJoin(Paint.Join.BEVEL);
        mPaint.setStrokeWidth(lineWidth);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        Point p;
        p = new Point();
        p.x = 15;
        p.y = 25;
        weathers.add(p);
        p = new Point();
        p.x = 15;
        p.y = 25;
        weathers.add(p);
        p = new Point();
        p.x = 16;
        p.y = 21;
        weathers.add(p);
        p = new Point();
        p.x = 14;
        p.y = 20;
        weathers.add(p);
        p = new Point();
        p.x = 14;
        p.y = 19;
        weathers.add(p);

        ws = new float[weathers.size() * 2];

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;

        float[] fs = new float[weathers.size()];
        for (int i = 0; i < weathers.size(); i++) {
            Point p = weathers.get(i);
            fs[i] = (p.x + p.y) / 2;
//            Logg.e(p.x + "=========" + p.y);
            ws[i * 2] = width / weathers.size() * i;
            ws[i * 2 + 1] = (p.x + p.y) / 2;
            Logg.d(ws[i * 2] + "=========" + ws[i * 2 + 1]);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLines(ws, mPaint);
    }
}
