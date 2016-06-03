package ai.houzi.xiao.activity.tools;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;

import ai.houzi.xiao.R;
import ai.houzi.xiao.activity.main.BaseActivity;
import ai.houzi.xiao.utils.FilePath;
import ai.houzi.xiao.utils.ImageUtils;
import ai.houzi.xiao.widget.TitleBar;

public class ScreenShotActivity extends BaseActivity {

    private TitleBar titleBar;
    private ImageView img_display;
    private Button bt_screenshot;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_screenshot);
        titleBar = (TitleBar) findViewById(R.id.titleBar);
        titleBar.setTitleText("截图");
        titleBar.setLeftText("返回", this);

        img_display = (ImageView) findViewById(R.id.img_display);
        bt_screenshot = (Button) findViewById(R.id.bt_screenshot);
    }

    @Override
    protected void initListener() {
        bt_screenshot.setOnClickListener(this);
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
            case R.id.bt_screenshot:
                Bitmap bitmap = ImageUtils.screenShot(getWindow().getDecorView(), FilePath.imagePath + File.separator + "screenshot.png");
                img_display.setImageBitmap(bitmap);

//                Glide.with(this).load(ImageUtils.screenShot(getWindow().getDecorView())).into(img_display);
                break;
        }
    }
}