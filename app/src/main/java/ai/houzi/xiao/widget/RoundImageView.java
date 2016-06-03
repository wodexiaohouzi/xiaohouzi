package ai.houzi.xiao.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import ai.houzi.xiao.R;

/**
 * @author xiaolei
 */
public class RoundImageView extends ImageView {
    /**
     * 圆环
     */
    private static final int RING = 0;
    /**
     * 带圆角矩形
     */
    private static final int ROUND = 1;
    /**
     * 边框的宽度
     */
    private int BORDER_WIDTH = 0;
    /**
     * 圆角角度(只针对ROUND型)
     */
    private int BORDER_CORNER = 0;
    /**
     * 边框颜色
     */
    private int BORDER_COLOR = 0xFFEEEEEE;
    /**
     * 形状
     */
    private int BORDER_SHAPE;
    /**
     * width:控件的宽<br/>
     * height：控件的高
     */
    private int width, height;
    private int paddingLeft, paddingRight, paddingTop, paddingBottom;

    public RoundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView);
        BORDER_COLOR = a.getColor(R.styleable.RoundImageView_rivBorderColor, 0xFFEEEEEE);
        BORDER_WIDTH = a.getDimensionPixelSize(R.styleable.RoundImageView_rivBorderWidth, 0);
        BORDER_CORNER = a.getDimensionPixelSize(R.styleable.RoundImageView_rivBorderCorner, 0);
        BORDER_SHAPE = a.getInt(R.styleable.RoundImageView_rivBorderShape, 0);
        a.recycle();
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
        paddingLeft = getPaddingLeft();
        paddingRight = getPaddingRight();
        paddingTop = getPaddingTop();
        paddingBottom = getPaddingBottom();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }
        if (width == 0 || height == 0) {
            return;
        }
        Bitmap b = ((BitmapDrawable) drawable).getBitmap();
        if (b == null) {
            return;
        }
        Bitmap bitmap = b.copy(Config.ARGB_8888, true);
        Bitmap roundBitmap = null;
        if (BORDER_SHAPE == RING) {
            //圆环型，去宽高的最小值作为半径(包含边框的宽度)
            roundBitmap = drawRing(bitmap, Math.min(width - paddingLeft - paddingRight, height - paddingTop - paddingBottom));
        } else if (BORDER_SHAPE == ROUND) {
            //圆角型，获取设置的圆角角度
            roundBitmap = drawRound(bitmap, BORDER_CORNER);
        }
        canvas.drawBitmap(roundBitmap, paddingLeft, paddingTop, null);

        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }

    /**
     * 画圆环型
     *
     * @param bitmap Bitmap
     * @param radius 直径
     * @return 返回处理过的Bitmap
     */
    private Bitmap drawRing(Bitmap bitmap, int radius) {
        //对于图片做宽高的缩放处理
        Bitmap scaleBitmap = Bitmap.createScaledBitmap(bitmap, radius, radius, false);
        Bitmap output = Bitmap.createBitmap(radius, radius, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        Rect rect = new Rect(BORDER_WIDTH, BORDER_WIDTH, BORDER_WIDTH + paddingLeft + radius, BORDER_WIDTH + paddingTop + radius);

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(BORDER_COLOR);
        canvas.drawCircle(radius / 2, radius / 2,
                radius / 2 - BORDER_WIDTH / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));// 相交类型，in取相交内部的部分
        canvas.drawBitmap(scaleBitmap, rect, rect, paint);

        // 画外环圆圈
        if (BORDER_WIDTH > 0) {
            paint.reset();
            paint.setColor(BORDER_COLOR);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(BORDER_WIDTH);
            paint.setAntiAlias(true);
            canvas.drawCircle(radius / 2, radius / 2,
                    radius / 2 - BORDER_WIDTH / 2, paint);
        }
        return output;
    }

    /**
     * 画圆角图片
     *
     * @param bitmap
     * @param corner 圆角角度
     * @return 返回处理过的Bitmap
     */
    private Bitmap drawRound(Bitmap bitmap, int corner) {
        int w = getWidth();
        int h = getHeight();
        Bitmap scaleBitmap = Bitmap.createScaledBitmap(bitmap, w - paddingLeft - paddingRight, h - paddingTop - paddingBottom, false);
        Bitmap output = Bitmap.createBitmap(w - paddingLeft - paddingRight, h - paddingTop - paddingBottom, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        Rect rect = new Rect(BORDER_WIDTH - paddingLeft, BORDER_WIDTH - paddingTop, w - BORDER_WIDTH - paddingLeft, h - BORDER_WIDTH - paddingTop);
        RectF rectF = new RectF(BORDER_WIDTH, BORDER_WIDTH, w - BORDER_WIDTH - paddingLeft - paddingRight, h - BORDER_WIDTH - paddingTop - paddingBottom);

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(BORDER_COLOR);
        canvas.drawRoundRect(rectF, corner, corner, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));// 相交类型，in取相交内部的部分
        canvas.drawBitmap(scaleBitmap, rect, rect, paint);

        // 画框
        if (BORDER_WIDTH > 0) {
            paint.reset();
            paint.setColor(BORDER_COLOR);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(BORDER_WIDTH);
            paint.setAntiAlias(true);
            canvas.drawRoundRect(rectF, corner, corner, paint);
        }
        return output;
    }
}
