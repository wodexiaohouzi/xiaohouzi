package ai.houzi.xiao.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import ai.houzi.xiao.R;
import ai.houzi.xiao.activity.main.MyApplication;
import ai.houzi.xiao.utils.UIUtils;

/**
 * 登录loading
 */
public class DialogLogin extends Dialog {

    private AnimationDrawable animationDrawable;
    private String mText;
    private boolean isCancle;

    public DialogLogin(Context context) {
        this(context, R.style.Dialog);
    }

    public DialogLogin(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    public void show() {
        addContentView(View.inflate(getContext(), R.layout.dialog_login, null), new FrameLayout.LayoutParams(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
        setCanceledOnTouchOutside(true);
        setCancelable(isCancle);
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        lp.y = UIUtils.dip2px(30); // 新位置Y坐标
        lp.width = MyApplication.sWidth - UIUtils.dip2px(10);

        dialogWindow.setAttributes(lp);
        ImageView ivIcon = (ImageView) findViewById(R.id.ivIcon);
        animationDrawable = (AnimationDrawable) ivIcon.getDrawable();
        if (animationDrawable != null) {
            animationDrawable.start();
        }
        TextView tvText = (TextView) findViewById(R.id.tvText);
        tvText.setText(mText);
        super.show();
    }

    @Override
    public void dismiss() {
        if (isShowing() && animationDrawable != null) {
            animationDrawable.start();
        }
        super.dismiss();
    }

    public void setText(String text) {
        this.mText = text;
    }

    public void setCancle(boolean isCancle) {
        this.isCancle = isCancle;
    }
}
