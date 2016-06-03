package ai.houzi.xiao.activity.main;

import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.Timer;
import java.util.TimerTask;

import ai.houzi.xiao.R;
import ai.houzi.xiao.widget.BigToast;

/**
 * Activity基类
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    private BigToast mBigToast;
    private Dialog loading_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBigToast = new BigToast(this);
        initView(savedInstanceState);
        initListener();
        initDate();
    }

    /**
     * 初始化View
     *
     * @param savedInstanceState
     */
    protected abstract void initView(Bundle savedInstanceState);

    /**
     * 初始化事件监听
     */
    protected abstract void initListener();

    /**
     * 初始化数据
     */
    protected abstract void initDate();

    /**
     * 处理View的点击事件
     *
     * @param v 点击的View对象
     */
    protected abstract void handleListener(View v);

    @Override
    public void onClick(View v) {
        handleListener(v);
    }

    /**
     * 默认RIGHT
     *
     * @param text
     */
    protected void showBigToast(String text) {
        mBigToast.setContent(text);
        mBigToast.showToast();
    }

    protected void showBigToast(int resId, String text) {
        mBigToast.setIcon(resId);
        showBigToast(text);
    }

    /**
     * 打开软键盘
     *
     * @param time 延迟打开时间（毫秒）
     */
    protected void openKeyBoard(int time) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, time);
    }

    /**
     * 关闭软键盘
     */
    protected void hideKeyBoard() {
        InputMethodManager inputMsg = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMsg.isActive()) { // 隐藏软键盘
            View curView = this.getCurrentFocus();
            if (curView != null) {
                inputMsg.hideSoftInputFromWindow(curView.getWindowToken(), 0);
            }
        }
    }


    /**
     * 震动
     *
     * @param duration
     */
    protected void vibrate(long duration) {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {
                0, duration
        };
        vibrator.vibrate(pattern, -1);
    }

    /**
     * 显示加载动画
     */
    protected void showLoadingDialog(boolean b) {
        if (loading_dialog == null) {
            loading_dialog = new Dialog(BaseActivity.this,
                    R.style.loading_dialog);
            loading_dialog.setContentView(R.layout.loading_dialog_anim);
            loading_dialog.setCanceledOnTouchOutside(false);
        }
        if (loading_dialog != null && !loading_dialog.isShowing()) {
            loading_dialog.setCancelable(b);
            loading_dialog.show();
        }
    }

    /**
     * 隐藏加载动画
     */
    public void hideLoadingDialog() {
        if (loading_dialog != null && loading_dialog.isShowing()) {
            loading_dialog.dismiss();
        }
    }
}
