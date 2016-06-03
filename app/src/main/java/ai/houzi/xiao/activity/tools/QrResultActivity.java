package ai.houzi.xiao.activity.tools;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import ai.houzi.xiao.R;
import ai.houzi.xiao.activity.main.BaseActivity;
import ai.houzi.xiao.widget.TitleBar;

/**
 * 扫描结果
 */
public class QrResultActivity extends BaseActivity {
    private TitleBar titleBar;
    private TextView tvContent;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_qrresult);
        titleBar = (TitleBar) findViewById(R.id.titleBar);
        titleBar.setTitleText("扫描结果");
        titleBar.setLeftText("返回", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvContent = (TextView) findViewById(R.id.tvContent);
        String content = getIntent().getStringExtra("content");
        tvContent.setText(content);
    }

    @Override
    protected void initListener() {
    }

    @Override
    protected void initDate() {

    }

    @Override
    protected void handleListener(View v) {

    }
}
