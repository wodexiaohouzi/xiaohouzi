package ai.houzi.xiao.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.EmbossMaskFilter;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import ai.houzi.xiao.R;

/**
 * Created by hp on 2016/6/2.
 */
public class Test extends View {

    private Paint mPaint;
    private Bitmap bitmap;

    public Test(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.background_menu_head);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //设置光源的方向
        float[] direction = new float[]{1, 1, 1};
        //设置要应用的环境光亮度
        float light = 0.4f;
        //选择要应用的反射等级
        float specular = 6;
        //向蒙版应用一定级别的模糊
        float blur = 3.5f;
        EmbossMaskFilter emboss = new EmbossMaskFilter(direction, light, specular, blur);
        //应用蒙版
        if (canvas.isHardwareAccelerated()) {
            mPaint.setMaskFilter(emboss);
        }

        if (bitmap == null) {
            return;
        }
        canvas.drawBitmap(bitmap, 0, 0, mPaint);
    }
}
