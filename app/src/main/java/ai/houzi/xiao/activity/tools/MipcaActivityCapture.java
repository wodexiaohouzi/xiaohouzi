package ai.houzi.xiao.activity.tools;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.mining.app.zxing.camera.CameraManager;
import com.mining.app.zxing.decoding.CaptureActivityHandler;
import com.mining.app.zxing.decoding.InactivityTimer;
import com.mining.app.zxing.view.ViewfinderView;
import com.qqalbum.imagepicker.ui.PhotoWallActivity;

import java.io.IOException;
import java.util.Vector;

import ai.houzi.xiao.R;
import ai.houzi.xiao.activity.main.BaseActivity;
import ai.houzi.xiao.activity.user.MyQrcodeActivity;
import ai.houzi.xiao.utils.Logg;
import ai.houzi.xiao.widget.BigToast;
import ai.houzi.xiao.widget.TitleBar;

/**
 * Initial the camera
 *
 * @author Ryan.Tang
 */
public class MipcaActivityCapture extends BaseActivity implements Callback {
    private static final String TAG = MipcaActivityCapture.class.getSimpleName();
    private static final int ALBUM = 101;

    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;

    private TitleBar titleBar;
    private LinearLayout llAlbum, llLight, llQRcode;
    private TextView tvLight;
    private ImageView ivLight;
    private Camera camera;
    private boolean isLight;

    private Camera.Parameters mParameters;
    private Intent intent;


    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_capture);
        intent = new Intent();
        titleBar = (TitleBar) findViewById(R.id.titleBar);
        titleBar.setLeftText("  ", this);
        titleBar.setTitleText("扫一扫");
        llAlbum = (LinearLayout) findViewById(R.id.llAlbum);
        llLight = (LinearLayout) findViewById(R.id.llLight);
        llQRcode = (LinearLayout) findViewById(R.id.llQRcode);
        tvLight = (TextView) findViewById(R.id.tvLight);
        ivLight = (ImageView) findViewById(R.id.ivLight);
        initListener();
        //ViewUtil.addTopView(getApplicationContext(), this, R.string.scan_card);
        CameraManager.init(getApplication());
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);

        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            llLight.setEnabled(false);
        }
    }

    @Override
    protected void initListener() {
        llAlbum.setOnClickListener(this);
        llLight.setOnClickListener(this);
        llQRcode.setOnClickListener(this);
    }

    @Override
    protected void initDate() {

    }

    @Override
    protected void handleListener(View v) {
        switch (v.getId()) {
            case R.id.llLeft:
                MipcaActivityCapture.this.finish();
                break;
            case R.id.llAlbum:
//                showBigToast("打开相册");
                //跳转至最终的选择图片页面
                intent.setClass(MipcaActivityCapture.this, PhotoWallActivity.class);
                intent.putExtra(PhotoWallActivity.CHOICE_COUNT, PhotoWallActivity.SINGLE);
                startActivityForResult(intent, ALBUM);
                overridePendingTransition(R.anim.in_from_bottom, 0);
                break;
            case R.id.llLight:
                if (camera == null) {
                    camera = CameraManager.getCamera();
                }
                if (camera == null) {
                    showBigToast(BigToast.WRONG, "开启闪光灯失败");
                    return;
                }
                if (mParameters == null) {
                    mParameters = CameraManager.getCamera().getParameters();
                }
                if (!isLight) {
                    //设置照相机参数，FLASH_MODE_TORCH  持续的亮灯，FLASH_MODE_ON 只闪一下
                    mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    CameraManager.getCamera().setParameters(mParameters);
                    camera.startPreview();  //开启预览
                    isLight = true;
                    tvLight.setText("关灯");
                    ivLight.setImageResource(R.mipmap.zxing_light_pressed);
                } else {
                    mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    CameraManager.getCamera().setParameters(mParameters);
                    camera.startPreview();  //开启预览
//                    camera.stopPreview();  //停止预览
//                    camera.release();   //关掉照相机
                    isLight = false;
                    tvLight.setText("开灯");
                    ivLight.setImageResource(R.drawable.selector_zxing_light);
                }
                break;
            case R.id.llQRcode:
//                showBigToast("打开我的二维码");
                intent.setClass(MipcaActivityCapture.this, MyQrcodeActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ALBUM:
                    if (data != null) {
                        int code = data.getIntExtra(PhotoWallActivity.CODE, 0);
                        if (code == 100) {
                            String photo_path = data.getStringExtra(PhotoWallActivity.PATH);
                            Result result = QrTools.scanningImage(photo_path);
                            if (result == null) {
                                showBigToast("图片格式有误");
                            } else {
                                Logg.i(result.toString());
//                                String recode = QrTools.recode(result.toString());
                                handle(result, false);
                            }
                        }
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    /**
     * 处理扫描结果
     *
     * @param result
     * @param barcode
     */

    public void handleDecode(Result result, Bitmap barcode) {
//        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        handle(result, true);

    }

    private void handle(Result result, boolean isFinish) {
        String resultString = result.getText();
        if (resultString.equals("")) {
            Toast.makeText(MipcaActivityCapture.this, "Scan failed!", Toast.LENGTH_SHORT).show();
        } else {
            if (resultString.startsWith("http://") || resultString.startsWith("https://")) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                Uri uri = Uri.parse(resultString);
                intent.setData(uri);
                startActivity(intent);
                if (isFinish) {
                    MipcaActivityCapture.this.finish();
                }
            } else {
                intent.setClass(MipcaActivityCapture.this, QrResultActivity.class);
                intent.putExtra("content", resultString);
                startActivity(intent);
            }
        }
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (Exception ioe) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(MipcaActivityCapture.this, decodeFormats,
                    characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };
}