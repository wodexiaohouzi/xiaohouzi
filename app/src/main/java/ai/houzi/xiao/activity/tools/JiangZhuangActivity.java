package ai.houzi.xiao.activity.tools;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ai.houzi.xiao.R;
import ai.houzi.xiao.activity.main.BaseActivity;
import ai.houzi.xiao.utils.ImageUtils;
import ai.houzi.xiao.utils.Logg;
import ai.houzi.xiao.widget.ClearEditText;
import ai.houzi.xiao.widget.StampView;
import ai.houzi.xiao.widget.TitleBar;
import ai.houzi.xiao.widget.ZhongQiTextView;

public class JiangZhuangActivity extends BaseActivity {

    private TitleBar titleBar;
    private Button btnConfirm;
    private ClearEditText cetName, cetReason, cetTitle, cetTeam;

    private View view;
    private TextView tvReason, tvTeam;
    private ZhongQiTextView tvName, tvChengHao;
    private StampView stampView;
    private ImageView ivBg;
    private LinearLayout llParent;
    boolean isFirst = true;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_jiangzhuang);
        titleBar = (TitleBar) findViewById(R.id.titleBar);
        titleBar.setTitleText("发奖中心");
        titleBar.setLeftText("返回", this);

        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        cetName = (ClearEditText) findViewById(R.id.cetName);
        cetReason = (ClearEditText) findViewById(R.id.cetReason);
        cetTitle = (ClearEditText) findViewById(R.id.cetTitle);
        cetTeam = (ClearEditText) findViewById(R.id.cetTeam);

        view = View.inflate(this, R.layout.layout_jiangzhuang, null);
        view = findViewById(R.id.view);
        tvName = (ZhongQiTextView) findViewById(R.id.tvName);
        tvReason = (TextView) findViewById(R.id.tvReason);
        tvChengHao = (ZhongQiTextView) findViewById(R.id.tvChengHao);
        tvTeam = (TextView) findViewById(R.id.tvTeam);
        stampView = (StampView) findViewById(R.id.stampView);
        stampView.setRotation(-45);
        llParent = (LinearLayout) view.findViewById(R.id.llParent);
        ivBg = (ImageView) view.findViewById(R.id.ivBg);
        ivBg.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (isFirst) {
                    isFirst = false;
                    int height = ivBg.getMeasuredHeight();
                    Logg.d(height);
                    ViewGroup.LayoutParams params = llParent.getLayoutParams();
                    params.height = height;
                    llParent.setLayoutParams(params);
                }
            }
        });
    }

    @Override
    protected void initListener() {
        btnConfirm.setOnClickListener(this);
    }

    @Override
    protected void initDate() {

    }

    @Override
    protected void handleListener(View v) {
        switch (v.getId()) {
            case R.id.llLeft:
                finish();
                break;
            case R.id.btnConfirm:
                String name = cetName.getText().toString().trim();
                String reason = cetReason.getText().toString().trim();
                String title = cetTitle.getText().toString().trim();
                String team = cetTeam.getText().toString().trim();

                if (!TextUtils.isEmpty(name)) {
                    tvName.setText("亲爱的 " + name + " 小朋友");
                }
                if (!TextUtils.isEmpty(reason)) {
                    tvReason.setText("\t\t\t\t" + reason + "。特此授予你");
                }
                if (!TextUtils.isEmpty(title)) {
                    tvChengHao.setText(title);
                }
                if (!TextUtils.isEmpty(team)) {
                    tvTeam.setText("颁发单位：" + team);
                }

                Bitmap bitmap = ImageUtils.screenShot(view, null);

                Dialog dialog = new Dialog(this, R.style.Dialog);
                ImageView imageView = new ImageView(this);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setAdjustViewBounds(true);
                imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                imageView.setImageBitmap(bitmap);
                dialog.addContentView(imageView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                dialog.show();
                break;
        }
    }
}