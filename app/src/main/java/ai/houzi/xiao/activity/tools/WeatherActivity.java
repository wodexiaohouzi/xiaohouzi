package ai.houzi.xiao.activity.tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dd.CircularProgressButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Request;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ai.houzi.xiao.R;
import ai.houzi.xiao.activity.main.BaseActivity;
import ai.houzi.xiao.activity.main.MyApplication;
import ai.houzi.xiao.utils.Final;
import ai.houzi.xiao.utils.Logg;
import ai.houzi.xiao.utils.OkHttpClientManager;

/**
 * 天气预报
 * Created by hp on 2016/4/13.
 */
public class WeatherActivity extends BaseActivity {
    private TextView tvCity, tvWendu, tvState;
    private ImageView ivMore;
    Gson gson;
    ArrayList<Forecast> forecasts;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_weather);
        tvCity = (TextView) findViewById(R.id.tvCity);
        tvWendu = (TextView) findViewById(R.id.tvWendu);
        tvState = (TextView) findViewById(R.id.tvState);
        ivMore = (ImageView) findViewById(R.id.ivMore);

        gson = new Gson();
    }

    @Override
    protected void initListener() {

        final CircularProgressButton circularButton1 = (CircularProgressButton) findViewById(R.id.circularButton1);
        circularButton1.setIndeterminateProgressMode(true);
        circularButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (circularButton1.getProgress() == 0) {
                    circularButton1.setProgress(50);
                } else if (circularButton1.getProgress() == 100) {
                    circularButton1.setProgress(0);
                } else {
                    circularButton1.setProgress(100);
                }
            }
        });

        final CircularProgressButton circularButton2 = (CircularProgressButton) findViewById(R.id.circularButton2);
        circularButton2.setIndeterminateProgressMode(true);
        circularButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (circularButton2.getProgress() == 0) {
                    circularButton2.setProgress(50);
                } else if (circularButton2.getProgress() == -1) {
                    circularButton2.setProgress(0);
                } else {
                    circularButton2.setProgress(-1);
                }
            }
        });

    }

    @Override
    protected void initDate() {
        getWeather(MyApplication.locationCity.replace("市", ""));
        getBitmapColor();
    }

    private void getBitmapColor() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.background_menu_head);
        //取图片上的颜色
        Palette.generateAsync(bitmap, new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                Palette.Swatch swatch = palette.getVibrantSwatch(); //充满活力的色板
                if (swatch != null) {
                    int rgb = swatch.getRgb();
                    int titleTextColor = swatch.getTitleTextColor();
                    int bodyTextColor = swatch.getBodyTextColor();
                    //HSL色彩模式
                    // hsl[0] is Hue [0 .. 360)色相
                    //hsl[1] is Saturation [0...1]饱和度
                    //hsl[2] is Lightness [0...1]明度
                    float[] hsl = swatch.getHsl();
                    colorBurn(rgb);
                    colorBurn(titleTextColor);
                    colorBurn(bodyTextColor);
                    Logg.d(hsl[0] + "==" + hsl[1] + "==" + hsl[2]);
                }
            }
        });
    }

    private int colorBurn(int RGBValues) {
        int alpha = RGBValues >> 24;
        int red = RGBValues >> 16 & 0xFF;
        int green = RGBValues >> 8 & 0xFF;
        int blue = RGBValues & 0xFF;
        Logg.d(alpha + "==" + red + "==" + green + "==" + blue);
        return Color.rgb(red, green, blue);
    }

    @Override
    protected void handleListener(View v) {

    }

    private void getWeather(String city) {
//        String url = "http://wthrcdn.etouch.cn/weather_mini";
        Map<String, String> hashMap = new HashMap<>();
//        hashMap.put("city", city);
        OkHttpClientManager.getAsyn(Final.WEATHER_FROM_CITYNAME + city, hashMap, new OkHttpClientManager.ResultCallback() {

            @Override
            public void onError(Request request, Exception e) {
            }

            @Override
            public void onResponse(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    JSONObject data = object.getJSONObject("data");
                    String city = data.optString("city");
                    String wendu = data.optString("wendu");
                    tvCity.setText(city);
                    tvWendu.setText(wendu);

                    Result jsonObj = gson.fromJson(data.optString("forecast"), new TypeToken<Forecast>() {
                    }.getType());
                    forecasts = jsonObj.forecast;
                    tvState.setText(forecasts.get(0).type);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onException(String exception) {
            }
        });
    }

    private class Result {
        ArrayList<Forecast> forecast;
    }

    private class Forecast {
        public String fengxiang;//"fengxiang": "东北风",
        public String fengli;//"fengli": "微风级",
        public String high;//"high": "高温 25℃",
        public String type;//"type": "多云",
        public String low;//"low": "低温 15℃",
        public String date;//"date": "13日星期三"
    }
}
