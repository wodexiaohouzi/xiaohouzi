package ai.houzi.xiao.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout.LayoutParams;

import ai.houzi.xiao.R;


/**
 * MyDialog,从下边弹出的菜单
 *
 * @author xiaolei
 */
//private View menu;
//public MyDialog dialog = null;
//
//private void showMenu() {
//        menu = MyApplication.inflater.inflate(R.layout.custom_board, null);
//
//        menu.findViewById(R.id.wechat).setOnClickListener(this);
//        menu.findViewById(R.id.wechat_circle).setOnClickListener(this);
//        menu.findViewById(R.id.qq).setOnClickListener(this);
//        menu.findViewById(R.id.sina).setOnClickListener(this);
//        menu.findViewById(R.id.cancle).setOnClickListener(this);
//
//        if (dialog == null) {
//        dialog = new MyDialog(this, R.style.Dialog);
//        }
//        dialog.addView(menu);
//        dialog.setWindow(0, 0, MyApplication.sWidth, 0, 1.0f);
//        dialog.show();
//        }
//
//private void dismissMenu() {
//        dialog.dismiss();
//        }
public class MyDialog extends Dialog {
    private Dialog mDialog;
    private View mView;
    private int mX;
    private int mY;
    private int mWidth;
    private int mHeight;
    private float mAlpha;
    private Context mContext;

    public MyDialog(Context context) {
        super(context);
        init(context);
    }

    public MyDialog(Context context, int theme) {
        super(context, theme);
        init(context);
    }

    private void init(Context context) {
        mView = new View(context);
        this.mContext = context;
    }

    /**
     * @param view :The view in the dialog to display
     */
    public MyDialog addView(View view) {
        this.mView = view;
        return this;
    }

    /**
     * @param x      :On the x axis offset
     * @param y      ：On the y axis offset
     * @param width  :The width of the dialog
     * @param height :The height of the dialog
     * @param alpha  :The alpha of the dialog
     */
    public MyDialog setWindow(int x, int y, int width, int height, float alpha) {
        this.mX = x;
        this.mY = y;
        this.mWidth = width;
        this.mHeight = height;
        this.mAlpha = alpha;
        return this;
    }

    public void show() {
        if (mDialog == null) {
            mDialog = new Dialog(mContext, R.style.Dialog);
            mDialog.addContentView(mView, new LayoutParams(
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                    android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
            Window dialogWindow = mDialog.getWindow();
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            dialogWindow.setWindowAnimations(R.style.dialogWindowAnim);
            dialogWindow.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
            lp.x = mX; // 新位置X坐标
            lp.y = mY; // 新位置Y坐标
            lp.width = mWidth; // 宽度
            // lp.height = mHeight; // 高度
            lp.alpha = mAlpha; // 透明度

            dialogWindow.setAttributes(lp);
        }

        if (mDialog != null) {
            mDialog.show();
        }
    }

    public void dismiss() {
        mDialog.dismiss();
    }

}