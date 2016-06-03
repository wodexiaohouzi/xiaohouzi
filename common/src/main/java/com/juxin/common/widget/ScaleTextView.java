package com.juxin.common.widget;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

/**
 * Created by zhenxixianzai on 2015/5/5.
 */
public class ScaleTextView extends TextView {
    private static final int DEFAULT_DEGREES = 0;
    //    private int mDegrees;
    private Camera mCamera;
    private Matrix mMaxtrix;

    public ScaleTextView(Context context) {
        super(context, null);
    }

    public ScaleTextView(Context context, AttributeSet attrs) {
        super(context, attrs, android.R.attr.textViewStyle);
        this.setGravity(Gravity.CENTER);
//        TypedArray a = context.obtainStyledAttributes(attrs,
//                R.styleable.RotateTextView);
//        mDegrees = a.getDimensionPixelSize(R.styleable.RotateTextView_degree,
//                DEFAULT_DEGREES);
//        a.recycle();

        mCamera = new Camera();
        mMaxtrix = new Matrix();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        canvas.save();
//        canvas.translate(getCompoundPaddingLeft(), getExtendedPaddingTop());
        canvas.scale(1.0f, this.getHeight() / 2f, 0, this.getHeight() / 2f);
        canvas.restore();
//        mCamera.save();
//        mCamera.translate(0.0f, 0.0f, 20);
//        mCamera.rotateY(20);
//        mCamera.getMatrix(mMaxtrix);
//        mCamera.restore();
//        mMaxtrix.preTranslate(-20, -getHeight() / 2);
//        mMaxtrix.postTranslate(20, getHeight() / 2);
//        canvas.drawBitmap(mBitmap, mMaxtrix, null);
    }

//    public void setDegrees(int degrees) {
//        mDegrees = degrees;
//    }
}