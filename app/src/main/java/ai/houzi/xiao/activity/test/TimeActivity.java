package ai.houzi.xiao.activity.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import ai.houzi.xiao.R;
import ai.houzi.xiao.widget.PickerView;
import ai.houzi.xiao.widget.wheelview.WheelMain;

public class TimeActivity extends Activity {
    private View timePicker1;
    private WheelMain wheelMain;
    private PickerView pickerView;
    private List<String> citys = new ArrayList<>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_time);
        timePicker1 = findViewById(R.id.timePicker1);
        wheelMain = new WheelMain(timePicker1);
        wheelMain.initDateTimePicker();

        for (int i = 0; i < 20; i++) {
            citys.add("第" + (i + 1) + "个城市");
        }
        pickerView = (PickerView) findViewById(R.id.pickerView);
        pickerView.setData(citys);
    }

}