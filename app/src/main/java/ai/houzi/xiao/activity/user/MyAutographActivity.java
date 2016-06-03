package ai.houzi.xiao.activity.user;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import ai.houzi.xiao.R;
import ai.houzi.xiao.activity.main.BaseActivity;
import ai.houzi.xiao.activity.main.MyApplication;

/**
 * 我的签名
 * Created by hp on 2016/3/25.
 */
public class MyAutographActivity extends BaseActivity {
    private ImageView imageView;

    String url = "http://b.hiphotos.baidu.com/zhidao/pic/item/a08b87d6277f9e2ffea334581d30e924b999f3c8.jpg";
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Glide.with(MyApplication.context).load(url).into(imageView);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_myautograph);

        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageResource(R.mipmap.background_menu_head);
        mHandler.sendEmptyMessageDelayed(0, 3000);
        Button mButton = (Button) findViewById(R.id.mButton1);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBigToast("我点击了");
            }
        });
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

    @Override
    public void onBackPressed() {
        finish();
    }

}
