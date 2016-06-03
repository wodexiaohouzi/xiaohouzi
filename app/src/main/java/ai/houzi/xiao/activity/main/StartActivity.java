package ai.houzi.xiao.activity.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.QuickContactBadge;
import android.widget.VideoView;

import ai.houzi.xiao.*;

/**
 * 启动液
 */
public class StartActivity extends BaseActivity {
    private static final int TO_MAIN = 0;

    private VideoView mVideoView;
    private Button btnSkip;
    //    private ImageView ivLogo;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TO_MAIN:
                    SharedPreferences login_sucess = getSharedPreferences("user_login", Context.MODE_PRIVATE);
                    String login = login_sucess.getString("Login", null);
                    if ("LOGIN_SUCCESS".equals(login)) {
                        Intent intent = new Intent();
                        intent.setClass(StartActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent();
                        intent.setClass(StartActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_start);
//        ivLogo = (ImageView) findViewById(R.id.ivLogo);
//        Glide.with(this).load(R.drawable.welcome).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(ivLogo);
        mVideoView = (VideoView) findViewById(R.id.mVideoView);
        Uri uri = Uri.parse("android.resource://ai.houzi.xiao/" + R.raw.rabbit);
//        Uri uri = Uri.parse("http://www.flaredup.com/pic/1.mp4");
//        mVideoView.setMediaController(new MediaController(this));//不设置控制布局
        mVideoView.setVideoURI(uri);
        if (savedInstanceState != null) {
            int progress = savedInstanceState.getInt("progress");
            mVideoView.seekTo(progress);
        }
//        mVideoView.start();
//        mVideoView.requestFocus();

        btnSkip = (Button) findViewById(R.id.btnSkip);
        mHandler.sendEmptyMessageDelayed(TO_MAIN, 100);

    }

    @Override
    protected void initDate() {

    }

    @Override
    protected void handleListener(View v) {

    }

    @Override
    protected void initListener() {
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mHandler.sendEmptyMessageDelayed(TO_MAIN, 500);
            }
        });
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVideoView != null && mVideoView.isPlaying() && mVideoView.canPause()) {
                    mVideoView.pause();
                }
                mHandler.sendEmptyMessageDelayed(TO_MAIN, 500);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mVideoView != null && mVideoView.getCurrentPosition() != 0) {
            outState.putInt("progress", mVideoView.getCurrentPosition());
        }
        super.onSaveInstanceState(outState);
    }

    private int mProgress;

    @Override
    protected void onResume() {
        super.onResume();
        if (mVideoView != null && mProgress != 0) {
            mVideoView.seekTo(mProgress);
            mVideoView.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mVideoView != null && mVideoView.isPlaying() && mVideoView.canPause()) {
            mProgress = mVideoView.getCurrentPosition();
            mVideoView.pause();
        }
    }

    @Override
    protected void onDestroy() {
        if (mVideoView != null) {
            mVideoView.stopPlayback();
            mVideoView = null;
        }
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
//        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {//横屏
//            Log.e("111", "======横屏===========");
//        } else {
//            Log.e("111", "======竖屏===========");
//        }
    }

    @Override
    public void onBackPressed() {
    }
}
