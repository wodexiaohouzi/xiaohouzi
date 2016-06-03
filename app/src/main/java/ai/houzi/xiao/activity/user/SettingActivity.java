package ai.houzi.xiao.activity.user;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import ai.houzi.xiao.R;
import ai.houzi.xiao.activity.main.BaseActivity;
import ai.houzi.xiao.utils.Final;
import ai.houzi.xiao.widget.TitleBar;

/**
 * 设置
 */
public class SettingActivity extends BaseActivity {
    private TitleBar titleBar;
    private RelativeLayout rlAccount, rlDaren, rlMessage, rlRecord, rlLinkMan, rlSuo, rlJanCe, rlFunction, rlWiFi, rlAbout;
//    private ElasticScrollView esv;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_setting);
        titleBar = (TitleBar) findViewById(R.id.titleBar);
        titleBar.setTitleText("设置");
        titleBar.setLeftText("返回", this);

        rlAccount = (RelativeLayout) findViewById(R.id.rlAccount);
        rlDaren = (RelativeLayout) findViewById(R.id.rlDaren);
        rlMessage = (RelativeLayout) findViewById(R.id.rlMessage);
        rlRecord = (RelativeLayout) findViewById(R.id.rlRecord);
        rlLinkMan = (RelativeLayout) findViewById(R.id.rlLinkMan);
        rlSuo = (RelativeLayout) findViewById(R.id.rlSuo);
        rlJanCe = (RelativeLayout) findViewById(R.id.rlJanCe);
        rlFunction = (RelativeLayout) findViewById(R.id.rlFunction);
        rlWiFi = (RelativeLayout) findViewById(R.id.rlWiFi);
        rlAbout = (RelativeLayout) findViewById(R.id.rlAbout);
//        esv = (ElasticScrollView) findViewById(R.id.esv);
    }

    @Override
    protected void initListener() {
        rlAccount.setOnClickListener(this);
        rlDaren.setOnClickListener(this);
        rlMessage.setOnClickListener(this);
        rlRecord.setOnClickListener(this);
        rlLinkMan.setOnClickListener(this);
        rlSuo.setOnClickListener(this);
        rlJanCe.setOnClickListener(this);
        rlFunction.setOnClickListener(this);
        rlWiFi.setOnClickListener(this);
        rlAbout.setOnClickListener(this);
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
            case R.id.rlAccount:
                Intent intent = new Intent(this, AccountManageActivity.class);
                startActivityForResult(intent, Final.ACCOUNT_MANAGE);
                break;
            case R.id.rlDaren:
                // android:exported="true"所要调整的activity必须有这个属性
                Intent mIntent = new Intent();
                ComponentName mComp = new ComponentName("com.juxin.jfcc", "com.juxin.jfcc.activity.login.RegisterActivity");//注意AcitivityName(目标应用程序)要完整的，带包名的PackageName的
                try {
                    mIntent.setComponent(mComp);
                    startActivity(mIntent);
                } catch (Exception e) {
                    showBigToast("未找到可用应用程序！");
                }
                break;
            case R.id.rlMessage:
                break;
            case R.id.rlRecord:
                break;
            case R.id.rlLinkMan:
                break;
            case R.id.rlSuo:
                break;
            case R.id.rlJanCe:
                break;
            case R.id.rlFunction:
                break;
            case R.id.rlWiFi:
                break;
            case R.id.rlAbout:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Final.ACCOUNT_MANAGE:
                    setResult(RESULT_OK);
                    finish();
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
