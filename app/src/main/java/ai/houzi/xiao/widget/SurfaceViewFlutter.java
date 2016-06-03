package ai.houzi.xiao.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import ai.houzi.xiao.R;
import ai.houzi.xiao.utils.FilePath;
import ai.houzi.xiao.utils.MD5;
import ai.houzi.xiao.utils.UIUtils;

/**
 * 嵌套在可滑动的页面中会出现闪烁，用FlutterView替代
 * Created by hp on 2016/3/3.
 */
@Deprecated
public class SurfaceViewFlutter extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private SurfaceHolder mHolder;
    private Canvas mCanvas;
    /**
     * 用于绘制的线程
     */
    private Thread mThread;
    /**
     * 线程控制器
     */
    private boolean isRunning;
    //-------------------------上边时基本的-----------------
    private Paint mPaint;
    /**
     * 从控件中获取到的Drawable
     */
    private Drawable mDrawable;
    private Bitmap mBitmap;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            long startTime = System.currentTimeMillis();
            draw();
            long endTime = System.currentTimeMillis();
            if (endTime - startTime < 100) {
                handler.sendEmptyMessageDelayed(0, 100 - (endTime - startTime));
            } else {
                handler.sendEmptyMessage(0);
            }
            super.handleMessage(msg);
        }
    };

    public SurfaceViewFlutter(Context context) {
        this(context, null);
    }

    public SurfaceViewFlutter(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SurfaceViewFlutter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (isInEditMode()) {
            return;
        }
        mHolder = getHolder();//holder中包含Canvas和对于生命周期的管理
        mHolder.addCallback(this);//添加一个回调，生命周期
        /**可获得焦点*/
        setFocusable(true);
        setFocusableInTouchMode(true);
        /**设置屏幕常亮*/
        setKeepScreenOn(true);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SurfaceViewFlutter);
        mDrawable = a.getDrawable(R.styleable.SurfaceViewFlutter_svf);
        a.recycle();
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
            setMeasuredDimension(widthSize, 1080 * widthSize / 1920);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isRunning = true;
        mThread = new Thread(this);
        mThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isRunning = false;
    }

    @Override
    public void run() {
        if (mBitmap == null || mBitmap.isRecycled()) {
            if (mDrawable == null) {
                drawText("小侯子，我爱你");
                if (!TextUtils.isEmpty(imageUrl)) {
                    new DownLoadThread(imageUrl).start();
                }
                return;
            }
            Bitmap bitmap = ((BitmapDrawable) mDrawable).getBitmap();
            if (bitmap == null) {
                drawText("小侯子，我爱你");
                if (!TextUtils.isEmpty(imageUrl)) {
                    new DownLoadThread(imageUrl).start();
                }
                return;
            }
            mBitmap = Bitmap.createScaledBitmap(bitmap, getWidth() + UIUtils.dip2px(100), getHeight() + UIUtils.dip2px(100), false);
            if (bitmap != null) {
                bitmap.recycle();
            }
        }
        handler.sendEmptyMessageDelayed(0, 100);
    }

    private void drawText(String text) {
        try {
            mCanvas = mHolder.lockCanvas(new Rect(0, 0, getWidth(), getHeight()));
            if (mCanvas != null) {
                mPaint.setTextSize(UIUtils.dip2px(30));
                mPaint.setColor(Color.GRAY);
                mCanvas.drawARGB(250, 192, 192, 192);
                mCanvas.drawText(text, getWidth() / 2 - mPaint.measureText(text) / 2, getHeight() / 2 + UIUtils.dip2px(15), mPaint);
            }
        } catch (Exception e) {

        } finally {
            if (mCanvas != null) {
                mHolder.unlockCanvasAndPost(mCanvas);
            }
        }
    }

    private int xIndex, yIndex = 0;
    private int local = 1;//1:左上角,2：右上角,3：右下角,4：左下角
    private int distance = UIUtils.dip2px(100);

    private void draw() {
        if (mBitmap == null || mBitmap.isRecycled()) {
            return;
        }
        try {
            mCanvas = mHolder.lockCanvas();
            if (mCanvas != null) {
                switch (local) {
                    case 1:
                        if (xIndex < distance) {
                            mCanvas.translate(-xIndex, 0);
                            xIndex++;
                            mCanvas.drawBitmap(mBitmap, 0, 0, mPaint);
                        } else {
                            mCanvas.translate(-distance, 0);
                            mCanvas.drawBitmap(mBitmap, 0, 0, mPaint);
                            local = 2;
                            xIndex = 0;
                        }
                        break;
                    case 2:
                        if (yIndex < distance) {
                            mCanvas.translate(-distance, -yIndex);
                            yIndex++;
                            mCanvas.drawBitmap(mBitmap, 0, 0, mPaint);
                        } else {
                            mCanvas.translate(-distance, -distance);
                            mCanvas.drawBitmap(mBitmap, 0, 0, mPaint);
                            local = 3;
                            yIndex = 0;
                        }
                        break;
                    case 3:
                        if (xIndex < distance) {
                            mCanvas.translate(xIndex - distance, -distance);
                            xIndex++;
                            mCanvas.drawBitmap(mBitmap, 0, 0, mPaint);
                        } else {
                            mCanvas.translate(0, -distance);
                            mCanvas.drawBitmap(mBitmap, 0, 0, mPaint);
                            local = 4;
                            xIndex = 0;
                        }
                        break;
                    case 4:
                        if (yIndex < distance) {
                            mCanvas.translate(0, yIndex - distance);
                            yIndex++;
                            mCanvas.drawBitmap(mBitmap, 0, 0, mPaint);
                        } else {
                            mCanvas.translate(0, 0);
                            mCanvas.drawBitmap(mBitmap, 0, 0, mPaint);
                            local = 1;
                            yIndex = 0;
                        }
                        break;
                }
            }
        } catch (Exception e) {

        } finally {
            if (mCanvas != null) {
                mHolder.unlockCanvasAndPost(mCanvas);
            }
        }
    }

    /**
     * 设置图片资源
     *
     * @param resId 图片资源id
     */
    public void setFlutterResoure(int resId) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);
        mBitmap = Bitmap.createScaledBitmap(bitmap, getWidth() + UIUtils.dip2px(100), getHeight() + UIUtils.dip2px(100), false);
        if (mThread != null) {
            mThread.run();
        }
    }

    /**
     * 设置Bitmap
     *
     * @param bitmap 图片Bitmap
     */
    public void setFlutterBitmap(Bitmap bitmap) {
        mBitmap = Bitmap.createScaledBitmap(bitmap, getWidth() + UIUtils.dip2px(100), getHeight() + UIUtils.dip2px(100), false);
        if (mThread != null) {
            mThread.run();
        }
    }

    private String filePath;
    private String imageUrl;

    /**
     * 设置图片地址
     *
     * @param urlString 图片地址
     */
    public void setFlutterUrl(String urlString) {
        this.imageUrl = urlString;
        filePath = FilePath.imagePath + "/" + MD5.md5(urlString) + urlString.substring(urlString.charAt('.'));
        File dir = new File(FilePath.imagePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(filePath);
        if (file.exists()) {
            mBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(file.getPath()), getWidth() + UIUtils.dip2px(100), getHeight() + UIUtils.dip2px(100), false);
            if (mThread != null) {
                mThread.run();
            }
        } else {
            new DownLoadThread(urlString).start();
        }
    }

    class DownLoadThread extends Thread implements Runnable {

        private String urlString;

        public DownLoadThread(String url) {
            this.urlString = url;
        }

        @Override
        public void run() {
            try {
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.connect();
                InputStream inputStream = conn.getInputStream();
                writeStream(inputStream);
                Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                mDrawable = new BitmapDrawable(bitmap);
                if (bitmap != null) {
                    mBitmap = Bitmap.createScaledBitmap(bitmap, getWidth() + UIUtils.dip2px(100), getHeight() + UIUtils.dip2px(100), false);
                    if (mThread != null) {
                        mThread.run();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void writeStream(InputStream is) {
            try {
                FileOutputStream fos = new FileOutputStream(new File(filePath));
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

