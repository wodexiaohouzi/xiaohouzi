package ai.houzi.xiao.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.EmbossMaskFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import ai.houzi.xiao.R;
import ai.houzi.xiao.utils.UIUtils;

/**
 * 圆形印章
 */
public class StampView extends View {
    private int width, height;
    private Paint mPaint, mPaint1;
    private Path path;
    private int COLOR;
    private float WIDTH;
    private String TEXT, EXPLAIN;
    private int TEXT_SIZE, RADIUS, DISTANCE, DEGREES;

    public StampView(Context context) {
        this(context, null);
    }

    public StampView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StampView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StampView);
        COLOR = a.getColor(R.styleable.StampView_svColor, 0xFFFF0000);
        WIDTH = a.getDimension(R.styleable.StampView_svWidth, 3);
        TEXT = a.getString(R.styleable.StampView_svText);
        EXPLAIN = a.getString(R.styleable.StampView_svExplain);
        a.recycle();
        TEXT_SIZE = UIUtils.dip2px(context, 5);
        DISTANCE = UIUtils.dip2px(context, 9);
        DEGREES = UIUtils.dip2px(context, 9);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(COLOR);
        mPaint.setStrokeWidth(WIDTH);
        mPaint.setStyle(Paint.Style.STROKE);

        mPaint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint1.setColor(COLOR);
        mPaint1.setStrokeWidth(0.5f);
        mPaint1.setStyle(Paint.Style.FILL);
        mPaint1.setTextSize(TEXT_SIZE);

        path = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = height = Math.min(w, h);
        RADIUS = width / 7;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//
//        //设置光源的方向
//        float[] direction = new float[]{1, 1, 1};
//        //设置要应用的环境光亮度
//        float light = 0.4f;
//        //选择要应用的反射等级
//        float specular = 6;
//        //向蒙版应用一定级别的模糊
//        float blur = 3.5f;
//        EmbossMaskFilter emboss = new EmbossMaskFilter(direction, light, specular, blur);
//        //应用蒙版
//        if (canvas.isHardwareAccelerated()) {
//            mPaint.setMaskFilter(emboss);
//            mPaint1.setMaskFilter(emboss);
//        }

        int radius = width / 2;
        int length = TEXT.length();
        char[] array = TEXT.toCharArray();
        canvas.drawCircle(radius, radius, radius - (int) WIDTH * 2, mPaint);
        canvas.save();
        canvas.rotate(-DEGREES / 2 * length - DEGREES, radius, radius);
        for (int i = 0; i < length; i++) {
            canvas.rotate(DEGREES, radius, radius);
            canvas.drawText(array[i] + "", radius, DISTANCE, mPaint1);
        }
        canvas.restore();
        canvas.drawText(EXPLAIN, radius - mPaint1.measureText(EXPLAIN) / 2, width - DISTANCE, mPaint1);

        path.reset();
        path.moveTo(radius, radius - RADIUS);
        float cos18 = (float) (RADIUS * Math.cos(18 * 2 * Math.PI / 360));
        float sin18 = (float) (RADIUS * Math.sin(18 * 2 * Math.PI / 360));
        float cos36 = (float) (RADIUS * Math.cos(36 * 2 * Math.PI / 360));
        float sin36 = (float) (RADIUS * Math.sin(36 * 2 * Math.PI / 360));
        path.lineTo(radius - sin36, radius + cos36);
        path.lineTo(radius + cos18, radius - sin18);
        path.lineTo(radius - cos18, radius - sin18);
        path.lineTo(radius + sin36, radius + cos36);
        path.close();
        canvas.drawPath(path, mPaint1);
    }
}
