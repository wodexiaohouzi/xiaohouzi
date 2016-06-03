package ai.houzi.xiao.activity.user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import ai.houzi.xiao.R;
import ai.houzi.xiao.activity.main.BaseActivity;
import ai.houzi.xiao.activity.main.LoginActivity;
import ai.houzi.xiao.utils.Final;
import ai.houzi.xiao.widget.MyAlertDialog;
import ai.houzi.xiao.widget.TitleBar;

/**
 * 账号管理
 * Created by hp on 2016/4/7.
 */
public class AccountManageActivity extends BaseActivity {
    private RelativeLayout rlQuit;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_account_manage);
        TitleBar titleBar = (TitleBar) findViewById(R.id.titleBar);
        titleBar.setTitleText("账号管理");
        titleBar.setLeftText("设置", this);
        titleBar.setRighText("编辑", this);

        rlQuit = (RelativeLayout) findViewById(R.id.rlQuit);
    }

    @Override
    protected void initListener() {
        rlQuit.setOnClickListener(this);
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
            case R.id.tvRight:

                break;
            case R.id.rlQuit:
                MyAlertDialog dialog = new MyAlertDialog().setTitle("退出当前账号")
                        .setMessage("退出可能会使你的登录记录归零，达人图标变灰，确认退出？")
                        .setNegativeListener("取消", null)
                        .setPositiveListener("退出", new MyAlertDialog.OnPositiveClickListener() {
                            @Override
                            public void onPositiveClick(View v) {
                                quit();
                            }
                        });
                dialog.showDialog(getFragmentManager(), AccountManageActivity.class.getSimpleName());
                break;
        }
    }

    private void quit() {
        SharedPreferences user_login = getSharedPreferences(Final.USER_LOGIN, MODE_PRIVATE);
//        user_login.edit().clear().apply();
        user_login.edit().putString("Login", "LOGIN_FAIL").apply();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        setResult(RESULT_OK);
        finish();
    }
}
