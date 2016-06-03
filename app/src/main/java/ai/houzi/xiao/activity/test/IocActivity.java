package ai.houzi.xiao.activity.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.juxin.common.ioc.ContentView;
import com.juxin.common.ioc.ViewInject;
import com.juxin.common.ioc.ViewInjectUtils;

import ai.houzi.xiao.R;

/**
 * 注解不能在库中使用。。。
 */
@ContentView(value = R.layout.activity_ioc)
public class IocActivity extends AppCompatActivity {

    @ViewInject(R.id.tv)
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewInjectUtils.inject(this);

        tv.setText("注解成功");
    }

}
