package ai.houzi.xiao.activity.user;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import ai.houzi.xiao.R;
import ai.houzi.xiao.activity.main.BaseActivity;
import ai.houzi.xiao.activity.main.MyApplication;
import ai.houzi.xiao.utils.FilePath;
import ai.houzi.xiao.utils.QRCodeUtil;
import ai.houzi.xiao.utils.UIUtils;
import ai.houzi.xiao.widget.MyDialog;
import ai.houzi.xiao.widget.RoundImageView;
import ai.houzi.xiao.widget.TitleBar;

/**
 * 我的二维码
 */
public class MyQrcodeActivity extends BaseActivity {
    private TitleBar titleBar;
    private RoundImageView rivUserHead;
    private ImageView ivQRcode;
    private TextView tvUserName, tvTip;
    private LinearLayout llContent;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_myqrcode);
        titleBar = (TitleBar) findViewById(R.id.titleBar);
        titleBar.setTitleText("我的二维码");
        titleBar.setLeftText("返回", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ivQRcode = (ImageView) findViewById(R.id.ivQRcode);
        rivUserHead = (RoundImageView) findViewById(R.id.rivUserHead);
        tvUserName = (TextView) findViewById(R.id.tvUserName);
        tvTip = (TextView) findViewById(R.id.tvTip);
        llContent = (LinearLayout) findViewById(R.id.llContent);
    }

    @Override
    protected void initListener() {
        llContent.setOnClickListener(this);
    }

    @Override
    protected void initDate() {
        create();
    }

    private MyDialog myDialog;
    private View dialogView;

    @Override
    protected void handleListener(View v) {
        switch (v.getId()) {
            case R.id.tvShare:
                showBigToast("分享有待开发");
                break;
            case R.id.tvSwitch:
                showBigToast("换个样式有待开发");
                break;
            case R.id.tvSave:
                myDialog.dismiss();
                FilePath.saveLayout2File(llContent, new FilePath.SaveFileListener() {
                    @Override
                    public void onSaveFile(boolean saveOk, String photoUrl) {
                        if (saveOk) {
                            showBigToast("保存成功：\n" + photoUrl);
                        } else {
                            showBigToast("保存失败，请重试！");
                        }
                    }
                });
                break;
            case R.id.llContent:
                if (dialogView == null) {
                    dialogView = View.inflate(MyQrcodeActivity.this, R.layout.dialog_arcode, null);
                    TextView tvShare = (TextView) dialogView.findViewById(R.id.tvShare);
                    TextView tvSwitch = (TextView) dialogView.findViewById(R.id.tvSwitch);
                    TextView tvSave = (TextView) dialogView.findViewById(R.id.tvSave);
                    TextView tvCancle = (TextView) dialogView.findViewById(R.id.tvCancle);
                    tvShare.setOnClickListener(this);
                    tvSwitch.setOnClickListener(this);
                    tvSave.setOnClickListener(this);
                    tvCancle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            myDialog.dismiss();
                        }
                    });
                }
                if (myDialog == null) {
                    myDialog = new MyDialog(MyQrcodeActivity.this)
                            .addView(dialogView)
                            .setWindow(0, UIUtils.dip2px(8), MyApplication.sWidth - UIUtils.dip2px(16), 0, 0.9f);
                }
                if (myDialog != null) {
                    myDialog.show();
                }
                break;
        }
    }

    /**
     * 生成二维码
     */
    private void create() {
        String filePath = FilePath.imagePath
                + "qr_" + MyApplication.userId + ".jpg";
        titleBar.showProgressBar(true);
        QRCodeUtil.createQRcode(this, filePath, getString(R.string.share_content), R.mipmap.head, new QRCodeUtil.QRCodeListener() {
            @Override
            public void onQRCode(boolean isSuccess, Bitmap qrBitmap, String filePath) {
                titleBar.showProgressBar(false);
                if (isSuccess && qrBitmap != null) {
                    Glide.with(MyApplication.context).load(filePath).into(ivQRcode);
                } else {
                    showBigToast("二维码生成失败，\n请重新生成");
                }
            }
        });
    }
}
