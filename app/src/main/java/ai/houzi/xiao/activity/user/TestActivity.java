package ai.houzi.xiao.activity.user;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import java.util.List;

import ai.houzi.xiao.R;
import ai.houzi.xiao.activity.main.BaseActivity;
import ai.houzi.xiao.utils.Logg;
import ai.houzi.xiao.widget.TitleBar;

public class TestActivity extends BaseActivity {
    private TitleBar titleBar;

    @Override
    protected void initView(Bundle savedInstanceState) {
        titleBar = new TitleBar(this);
        titleBar.setBackgroundColor(getResources().getColor(R.color.blue));
        setContentView(titleBar);
        titleBar.setTitleText("测试");
        titleBar.setLeftText("返回", this);
    }

    @Override
    protected void initListener() {
    }


    @Override
    protected void initDate() {
        ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningTasks = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : runningTasks) {
            if (info == null) {
                Logg.i("===info==null====");
                continue;
            }
            Logg.i("===processName====" + info.processName + "\n"
                    + "===importanceReasonComponent====" + info.importanceReasonComponent + "\n"
                    + "===pid====" + info.pid + "\n");
        }
    }

    @Override
    protected void handleListener(View v) {
        switch (v.getId()) {
            case R.id.llLeft:
                finish();
                break;
        }
    }

}
