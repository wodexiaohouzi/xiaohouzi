package ai.houzi.xiao.activity.user;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.qqalbum.imagepicker.customviews.PictureGridView;
import com.qqalbum.imagepicker.ui.AddImageActivity;
import com.qqalbum.imagepicker.ui.PhotoWallActivity;

import ai.houzi.xiao.R;
import ai.houzi.xiao.activity.main.BaseActivity;
import ai.houzi.xiao.widget.TitleBar;

/**
 * 话题
 */
public class TopicActivity extends BaseActivity {
    private TitleBar titleBar;
    private PictureGridView pGridView;
    private LinearLayout llMenuTopic, llMood, llChuanTu;
    private ImageView ivRight;
    private View cancle;
    private Handler mHandler;

    String[] urls = {"http://b.hiphotos.baidu.com/album/pic/item/caef76094b36acafe72d0e667cd98d1000e99c5f.jpg?psign=e72d0e667cd98d1001e93901213fb80e7aec54e737d1b867",
            "http://h.hiphotos.baidu.com/zhidao/pic/item/9f510fb30f2442a7c6010ec8d243ad4bd113024b.jpg",
            "http://img1.3lian.com/2015/w7/98/d/22.jpg",
            "http://img1.3lian.com/2015/w7/90/d/1.jpg",
            "http://pic.58pic.com/58pic/11/19/56/80d58PICzng.jpg",
            "http://pic41.nipic.com/20140520/18505720_144032556135_2.jpg",
            "http://pic39.nipic.com/20140226/18071023_152929941000_2.jpg"};

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_topic);
        titleBar = (TitleBar) findViewById(R.id.titleBar);
        titleBar.setTitleText("话题");
        titleBar.setLeftText("返回", this);
        titleBar.setRighIcon(R.drawable.selector_addimage, this);
        pGridView = (PictureGridView) findViewById(R.id.pGridView);
        llMenuTopic = (LinearLayout) findViewById(R.id.llMenuTopic);
        llMood = (LinearLayout) findViewById(R.id.llMood);
        llChuanTu = (LinearLayout) findViewById(R.id.llChuanTu);
        cancle = findViewById(R.id.cancle);

        ivRight = (ImageView) titleBar.findViewById(R.id.ivRight);
        mHandler = new Handler(getMainLooper());
    }

    @Override
    protected void initListener() {
        llMood.setOnClickListener(this);
        llChuanTu.setOnClickListener(this);
        cancle.setOnClickListener(this);
    }

    @Override
    protected void initDate() {
        pGridView.setUrls(urls);

    }

    @Override
    protected void handleListener(View v) {
        switch (v.getId()) {
            case R.id.llLeft:
                finish();
                break;
            case R.id.ivRight:
                if (ivRight.getRotation() == 0) {
                    ObjectAnimator.ofFloat(ivRight, View.ROTATION, 0f, -45f).setDuration(400).start();
                    showView();
                } else {
                    ObjectAnimator.ofFloat(ivRight, View.ROTATION, -45f, 0f).setDuration(400).start();
                    dismissView();
                }
                break;
            case R.id.llMood:
                ObjectAnimator.ofFloat(ivRight, View.ROTATION, -45f, 0f).setDuration(400).start();
                dismissView();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent();
                        intent.setClass(TopicActivity.this, AddImageActivity.class);
                        startActivity(intent);
                    }
                }, 400);
                break;
            case R.id.llChuanTu:
                ObjectAnimator.ofFloat(ivRight, View.ROTATION, -45f, 0f).setDuration(400).start();
                dismissView();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent();
                        intent.setClass(TopicActivity.this, PhotoWallActivity.class);
                        intent.putExtra(PhotoWallActivity.CHOICE_COUNT, PhotoWallActivity.MULTI);
                        startActivity(intent);
                        overridePendingTransition(R.anim.in_from_bottom, 0);
                    }
                }, 400);
                break;
            case R.id.llSigniIn:
                ObjectAnimator.ofFloat(ivRight, View.ROTATION, -45f, 0f).setDuration(400).start();
                dismissView();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showBigToast("有待开发");
                    }
                }, 400);
                break;
            case R.id.llShuiYin:
                ObjectAnimator.ofFloat(ivRight, View.ROTATION, -45f, 0f).setDuration(400).start();
                dismissView();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showBigToast("有待开发");
                    }
                }, 400);
                break;
            case R.id.llVideo:
                ObjectAnimator.ofFloat(ivRight, View.ROTATION, -45f, 0f).setDuration(400).start();
                dismissView();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showBigToast("有待开发");
                    }
                }, 400);
                break;
            case R.id.cancle:
                ObjectAnimator.ofFloat(ivRight, View.ROTATION, -45f, 0f).setDuration(400).start();
                dismissView();
                break;
        }
    }

    //显示下拉菜单
    private void showView() {
        if (!llMenuTopic.isShown()) {
            ObjectAnimator.ofFloat(llMenuTopic, View.TRANSLATION_Y, -llMenuTopic.getHeight(), 0f).setDuration(300).start();
            llMenuTopic.setVisibility(View.VISIBLE);

            ObjectAnimator.ofFloat(cancle, View.ALPHA, 0.0f, 0.4f).setDuration(300).start();
            cancle.setVisibility(View.VISIBLE);
        }
    }

    //隐藏下拉菜单
    private void dismissView() {
        if (llMenuTopic.isShown()) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(llMenuTopic, View.TRANSLATION_Y, 0f, -llMenuTopic.getHeight()).setDuration(300);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    llMenuTopic.setVisibility(View.INVISIBLE);
                    cancle.setVisibility(View.INVISIBLE);
                }
            });
            ObjectAnimator.ofFloat(cancle, View.ALPHA, 0.4f, 0.0f).setDuration(300).start();
            animator.start();
        }
    }
}
