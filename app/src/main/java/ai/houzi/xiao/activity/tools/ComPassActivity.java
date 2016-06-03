package ai.houzi.xiao.activity.tools;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import ai.houzi.xiao.R;
import ai.houzi.xiao.activity.main.BaseActivity;
import ai.houzi.xiao.utils.Logg;

/**
 * 电子罗盘 方向传感器
 */
public class ComPassActivity extends BaseActivity implements SensorEventListener {
    private ImageView ivCompass;
    private float currentDegree;
    private SensorManager sm;
    private float[] accelerometerValues = new float[3];
    private float[] magneticFieldValues = new float[3];
    private final float[] values = new float[3];
    private final float[] Rs = new float[9];

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_compass);
        ivCompass = (ImageView) findViewById(R.id.ivCompass);
        // 传感器管理器
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        // 注册传感器(Sensor.TYPE_ORIENTATION(方向传感器);SENSOR_DELAY_FASTEST(0毫秒延迟);
        // SENSOR_DELAY_GAME(20,000毫秒延迟)、SENSOR_DELAY_UI(60,000毫秒延迟))
        sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST);
        sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_FASTEST);
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
    public void onSensorChanged(SensorEvent event) {//传感器报告新的值(方向改变)
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accelerometerValues = event.values;
        }
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            magneticFieldValues = event.values;
            Logg.d("====重力加速度=====" + magneticFieldValues[0]);
        }
        //调用getRotaionMatrix获得变换矩阵R[]
        SensorManager.getRotationMatrix(Rs, null, accelerometerValues, magneticFieldValues);
        SensorManager.getOrientation(Rs, values);
        //经过SensorManager.getOrientation(R, values);得到的values值为弧度
        //转换为角度
        values[0] = (float) Math.toDegrees(values[0]);
        Logg.d("====方向=====" + values[0]);

        RotateAnimation ra = new RotateAnimation(currentDegree, -values[0],
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        //旋转过程持续时间
        ra.setDuration(10);
        //罗盘图片使用旋转动画
        ivCompass.startAnimation(ra);
        currentDegree = -values[0];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {//传感器精度的改变

    }

    @Override
    //注意activity暂停的时候释放
    protected void onPause() {
        super.onPause();
        sm.unregisterListener(this);
    }
}
