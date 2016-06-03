package ai.houzi.xiao.activity.main;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.bumptech.glide.Glide;
import com.qqalbum.imagepicker.ui.PhotoWallActivity;
import com.squareup.okhttp.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ai.houzi.xiao.R;
import ai.houzi.xiao.activity.interfaces.ITianQi;
import ai.houzi.xiao.activity.tools.BaiduMapActivity;
import ai.houzi.xiao.activity.tools.JiangZhuangActivity;
import ai.houzi.xiao.activity.tools.MipcaActivityCapture;
import ai.houzi.xiao.activity.tools.ScreenShotActivity;
import ai.houzi.xiao.activity.tools.WeatherActivity;
import ai.houzi.xiao.activity.tools.ZipTestActivity;
import ai.houzi.xiao.activity.user.ListViewTestActivity;
import ai.houzi.xiao.activity.user.MyAutographActivity;
import ai.houzi.xiao.activity.user.MyInfoActivity;
import ai.houzi.xiao.activity.user.MyQrcodeActivity;
import ai.houzi.xiao.activity.user.SettingActivity;
import ai.houzi.xiao.activity.user.TopicActivity;
import ai.houzi.xiao.entity.TianQi;
import ai.houzi.xiao.fragment.SuperAwesomeCardFragment;
import ai.houzi.xiao.utils.Final;
import ai.houzi.xiao.utils.Format;
import ai.houzi.xiao.utils.Logg;
import ai.houzi.xiao.utils.OkHttpClientManager;
import ai.houzi.xiao.utils.SystemTools;
import ai.houzi.xiao.utils.UIUtils;
import ai.houzi.xiao.widget.BigToast;
import ai.houzi.xiao.widget.ElasticScrollView;
import ai.houzi.xiao.widget.PagerSlidingTabStrip;
import ai.houzi.xiao.widget.RoundImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Intent intent;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ShareActionProvider mShareActionProvider;
    private PagerSlidingTabStrip mPagerSlidingTabStrip;
    private ViewPager mViewPager;
    private Toolbar mToolbar;
    /*-----菜单部分------*/
    private ElasticScrollView esv;
    private RoundImageView rivUserHead;
    private TextView tvUserName, tvAutograph, tvTemperature, tvCity;
    private ImageView ivQRcode;
    private LinearLayout llUserLv, llResume, llAutograph, llHuiYuan, llWallet, llZhuangBan, llSetting, llTheme, llWeather;

    public LocationClient mLocationClient = null;
    public final BDLocationListener myListener = new MyLocationListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intent = new Intent();
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        initViews();
        initListener();
        initLocation();
    }

    private void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        // toolbar.setLogo(R.drawable.ic_launcher);
        mToolbar.setTitle("小侯子");// 标题的文字需在setSupportActionBar之前，不然会无效
        // toolbar.setSubtitle("副标题");
        setSupportActionBar(mToolbar);
        /* 这些通过ActionBar来设置也是一样的，注意要在setSupportActionBar(toolbar);之后，不然就报错了 */
        // getSupportActionBar().setTitle("标题");
        // getSupportActionBar().setSubtitle("副标题");
        // getSupportActionBar().setLogo(R.drawable.ic_launcher);

		/* 菜单的监听可以在toolbar里设置，也可以像ActionBar那样，通过下面的两个回调方法来处理 */
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_share:
                        Toast.makeText(MainActivity.this, "action_share", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /* findView */
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open,
                R.string.drawer_close);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mPagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        mPagerSlidingTabStrip.setViewPager(mViewPager);
        mPagerSlidingTabStrip.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                colorChange(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        initTabsValue();

        //--------------------菜单部分---------------------
        esv = (ElasticScrollView) findViewById(R.id.esv);
        esv.setMoveMode(ElasticScrollView.Mode.TOP);
        rivUserHead = (RoundImageView) findViewById(R.id.rivUserHead);
        tvUserName = (TextView) findViewById(R.id.tvUserName);
        tvAutograph = (TextView) findViewById(R.id.tvAutograph);
        tvTemperature = (TextView) findViewById(R.id.tvTemperature);
        tvCity = (TextView) findViewById(R.id.tvCity);
        ivQRcode = (ImageView) findViewById(R.id.ivQRcode);
        llUserLv = (LinearLayout) findViewById(R.id.llUserLv);
        llResume = (LinearLayout) findViewById(R.id.llResume);
        llAutograph = (LinearLayout) findViewById(R.id.llAutograph);
        llHuiYuan = (LinearLayout) findViewById(R.id.llHuiYuan);
        llWallet = (LinearLayout) findViewById(R.id.llWallet);
        llZhuangBan = (LinearLayout) findViewById(R.id.llZhuangBan);
        llSetting = (LinearLayout) findViewById(R.id.llSetting);
        llTheme = (LinearLayout) findViewById(R.id.llTheme);
        llWeather = (LinearLayout) findViewById(R.id.llWeather);

        mHandler.sendEmptyMessageDelayed(0, 2000);
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    initData();
                    break;
                case 1://地图定位成功后去查对应城市的天气
//                    initData();
                    okhttp(MyApplication.locationCity.replace("市", ""));
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void initData() {
        mLocationClient.start();
        String JSON = getResources().getString(R.string.user_content);
        try {
            JSONObject object = new JSONObject(JSON);
            JSONObject user = object.getJSONObject("user");
            String userId = user.optString("userId");
            String userName = user.optString("userName");
            String userAutograph = user.optString("userAutograph");
            String userHeadUrl = user.optString("userHeadUrl");
            int userLv = user.optInt("userLv");
            String userPhone = user.optString("userPhone");
            String userQRcode = user.optString("userQRcode");
            String city = user.optString("city");
            String temperatrue = user.optString("temperatrue");
            MyApplication.userId = userId;
            MyApplication.userPhone = userPhone;
            tvUserName.setText(userName);
//            圆形头像要获取Bitmap，需要添加.asBitmap()
            Glide.with(this).load(userHeadUrl).asBitmap().error(R.mipmap.default_head).into(rivUserHead);
            tvAutograph.setText(userAutograph);
            tvCity.setText(city);
            tvTemperature.setText(temperatrue);
            Glide.with(this).load(userQRcode).error(R.mipmap.default_qr_code).into(ivQRcode);
            int[] lvs = Format.getUserLv(userLv);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ImageView iv;
            llUserLv.removeAllViews();
            for (int i = 0; i < lvs[0]; i++) {
                iv = new ImageView(this);
                iv.setLayoutParams(layoutParams);
                iv.setImageResource(R.mipmap.lv_wangguan);
                llUserLv.addView(iv);
            }
            for (int i = 0; i < lvs[1]; i++) {
                iv = new ImageView(this);
                iv.setLayoutParams(layoutParams);
                iv.setImageResource(R.mipmap.lv_taiyang);
                llUserLv.addView(iv);
            }
            for (int i = 0; i < lvs[2]; i++) {
                iv = new ImageView(this);
                iv.setLayoutParams(layoutParams);
                iv.setImageResource(R.mipmap.lv_yueliang);
                llUserLv.addView(iv);
            }
            for (int i = 0; i < lvs[3]; i++) {
                iv = new ImageView(this);
                iv.setLayoutParams(layoutParams);
                iv.setImageResource(R.mipmap.lv_xingxing);
                llUserLv.addView(iv);
            }
            SharedPreferences user_login = getSharedPreferences(Final.USER_LOGIN, Context.MODE_PRIVATE);
            user_login.edit().putString("user_" + userPhone, userHeadUrl).apply();

        } catch (JSONException e) {
            Logg.d(JSON);
            Logg.e(e.getMessage());
            e.printStackTrace();
        }
    }

    private void initListener() {
        llResume.setOnClickListener(this);
        ivQRcode.setOnClickListener(this);
        llAutograph.setOnClickListener(this);
        llHuiYuan.setOnClickListener(this);
        llWallet.setOnClickListener(this);
        llZhuangBan.setOnClickListener(this);
        llSetting.setOnClickListener(this);
        llTheme.setOnClickListener(this);
        llWeather.setOnClickListener(this);
        rivUserHead.setOnClickListener(this);
    }

    /**
     * mPagerSlidingTabStrip默认值配置
     */
    private void initTabsValue() {
        // 底部游标颜色
        mPagerSlidingTabStrip.setIndicatorColor(Color.BLUE);
        // tab的分割线颜色
        mPagerSlidingTabStrip.setDividerColor(Color.TRANSPARENT);
        // tab背景
        mPagerSlidingTabStrip.setBackgroundColor(Color.parseColor("#4876FF"));
        // tab底线高度
        mPagerSlidingTabStrip.setUnderlineHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                1, getResources().getDisplayMetrics()));
        // 游标高度
        mPagerSlidingTabStrip.setIndicatorHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                5, getResources().getDisplayMetrics()));
        // 选中的文字颜色
        mPagerSlidingTabStrip.setSelectedTextColor(Color.WHITE);
        // 正常文字颜色
        mPagerSlidingTabStrip.setTextColor(Color.BLACK);
    }

    /**
     * 界面颜色的更改
     */
    @SuppressLint("NewApi")
    private void colorChange(int position) {
        // 用来提取颜色的Bitmap
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                SuperAwesomeCardFragment.getBackgroundBitmapPosition(position));
        // Palette的部分
        Palette.generateAsync(bitmap, new Palette.PaletteAsyncListener() {
            /**
             * 提取完之后的回调方法
             */
            @Override
            public void onGenerated(Palette palette) {
                Palette.Swatch vibrant = palette.getVibrantSwatch();
                if (vibrant == null) {
                    return;
                }
                /* 界面颜色UI统一性处理,看起来更Material一些 */
                mPagerSlidingTabStrip.setBackgroundColor(vibrant.getRgb());
                mPagerSlidingTabStrip.setTextColor(vibrant.getTitleTextColor());
                // 其中状态栏、游标、底部导航栏的颜色需要加深一下，也可以不加，具体情况在代码之后说明
                mPagerSlidingTabStrip.setIndicatorColor(colorBurn(vibrant.getRgb()));

                mToolbar.setBackgroundColor(vibrant.getRgb());
                if (android.os.Build.VERSION.SDK_INT >= 21) {
                    Window window = getWindow();
                    // 很明显，这两货是新API才有的。
                    window.setStatusBarColor(colorBurn(vibrant.getRgb()));
                    window.setNavigationBarColor(colorBurn(vibrant.getRgb()));
                }
            }
        });
    }

    /**
     * 颜色加深处理
     *
     * @param RGBValues RGB的值，由alpha（透明度）、red（红）、green（绿）、blue（蓝）构成，
     *                  Android中我们一般使用它的16进制，
     *                  例如："#FFAABBCC",最左边到最右每两个字母就是代表alpha（透明度）、
     *                  red（红）、green（绿）、blue（蓝）。每种颜色值占一个字节(8位)，值域0~255
     *                  所以下面使用移位的方法可以得到每种颜色的值，然后每种颜色值减小一下，在合成RGB颜色，颜色就会看起来深一些了
     * @return
     */
    private int colorBurn(int RGBValues) {
        int alpha = RGBValues >> 24;
        int red = RGBValues >> 16 & 0xFF;
        int green = RGBValues >> 8 & 0xFF;
        int blue = RGBValues & 0xFF;
        red = (int) Math.floor(red * (1 - 0.1));
        green = (int) Math.floor(green * (1 - 0.1));
        blue = (int) Math.floor(blue * (1 - 0.1));
        return Color.rgb(red, green, blue);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        /* ShareActionProvider配置 */
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menu
                .findItem(R.id.action_share));
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
//        intent.setType("text/*");
        mShareActionProvider.setShareIntent(intent);

        MenuItem item = menu.findItem(R.id.action_menu);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                showOptionsMenu();
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private Dialog dialog;

    private void showOptionsMenu() {
        if (dialog == null) {
            dialog = new Dialog(this, R.style.Dialog);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.header_btn_more_pop);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
            Window window = dialog.getWindow();
            window.setLayout(UIUtils.dip2px(150), ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setWindowAnimations(R.style.header_options_menu_pop); //设置窗口弹出动画
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.dimAmount = 0.3f;
            attributes.gravity = Gravity.RIGHT | Gravity.TOP;
            attributes.x = UIUtils.dip2px(5);
            attributes.y = UIUtils.dip2px(50);
            dialog.findViewById(R.id.llScan).setOnClickListener(this);
            dialog.findViewById(R.id.llCalendar).setOnClickListener(this);
            dialog.findViewById(R.id.llSystemSetting).setOnClickListener(this);
            dialog.findViewById(R.id.llBaiDuMap).setOnClickListener(this);
            dialog.findViewById(R.id.llEdit).setOnClickListener(this);
        }
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_menu:
                Toast.makeText(MainActivity.this, "action_settings", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_share:
                Toast.makeText(MainActivity.this, "action_share", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llResume:
                intent.setClass(MainActivity.this, MyInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.ivQRcode:
                intent.setClass(MainActivity.this, MyQrcodeActivity.class);
                startActivity(intent);
                break;
            case R.id.llAutograph:
                intent.setClass(MainActivity.this, MyAutographActivity.class);
                startActivity(intent);
                break;
            case R.id.llHuiYuan:
                intent.setClass(MainActivity.this, ListViewTestActivity.class);
                startActivity(intent);
                break;
            case R.id.llWallet:
                intent.setClass(MainActivity.this, ScreenShotActivity.class);
                startActivity(intent);
                break;
            case R.id.llZhuangBan:
                intent.setClass(MainActivity.this, JiangZhuangActivity.class);
                startActivity(intent);
                break;
            case R.id.llSetting:
                intent.setClass(MainActivity.this, SettingActivity.class);
                startActivityForResult(intent, Final.SETTING);
                break;
            case R.id.llTheme:
                intent.setClass(MainActivity.this, TopicActivity.class);
                startActivity(intent);
                break;
            case R.id.rivUserHead:
                intent.setClass(MainActivity.this, PhotoWallActivity.class);
                intent.putExtra(PhotoWallActivity.CHOICE_COUNT, PhotoWallActivity.SINGLE);
                startActivityForResult(intent, Final.ALBUM);
                break;
            case R.id.llScan://扫一扫
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                intent.setClass(MainActivity.this, MipcaActivityCapture.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.llCalendar://系统日历
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                SystemTools.calendar(this);
                break;
            case R.id.llSystemSetting://进入系统设置
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                Intent intent1 = new Intent();
                intent1.setAction(Settings.ACTION_SETTINGS);
                startActivity(intent1);
                break;
            case R.id.llBaiDuMap://进入百度地图
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                intent.setClass(MainActivity.this, BaiduMapActivity.class);
                startActivity(intent);
                break;
            case R.id.llEdit://编辑列表
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
//                intent.setClass(MainActivity.this, DragEditActivity.class);//编辑列表
//                startActivity(intent);
//                intent.setClass(MainActivity.this, ComPassActivity.class);//指南针
//                startActivity(intent);
                intent.setClass(MainActivity.this, ZipTestActivity.class);//压缩和解压
                startActivity(intent);
                break;
            case R.id.llWeather://进入浏览器的天气预报
//                Intent intent2 = new Intent();
//                intent2.setAction(Intent.ACTION_VIEW);
//                Uri uri = Uri.parse(Final.WEATHER_BROWSER);
//                intent2.setData(uri);
//                startActivity(intent2);
                intent.setClass(MainActivity.this, WeatherActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Final.ALBUM:
                    if (data != null) {
                        int code = data.getIntExtra(PhotoWallActivity.CODE, 0);
                        if (code == 100) {
                            String photo_path = data.getStringExtra(PhotoWallActivity.PATH);
                            Glide.with(MainActivity.this).load(photo_path).asBitmap().error(R.mipmap.default_head).into(rivUserHead);
                        }
                    }
                    break;
                case Final.SETTING:
                    finish();
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /* ***************FragmentPagerAdapter***************** */
    public class MyPagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = {"分类", "主页", "热门推荐", "热门收藏", "本月热榜", "今日热榜", "专栏", "随机"};

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {
            return SuperAwesomeCardFragment.newInstance(position);
        }
    }

    private long mExitTime;

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
//        int span = 1000;
//        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    private class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location.getLocType() == BDLocation.TypeGpsLocation || location.getLocType() == BDLocation.TypeNetWorkLocation) {
                MyApplication.locationCityKey = location.getCityCode();
                MyApplication.locationCity = location.getCity();
                mHandler.sendEmptyMessage(1);
            }
            //Receive Location
            StringBuilder sb = new StringBuilder(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());// 单位度
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果

                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());// 位置语义化信息
            List<Poi> list = location.getPoiList();// POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
            Logg.d("BaiduLocationApiDem", sb.toString());
        }
    }

    private void okhttp(String city) {

        String url = "http://wthrcdn.etouch.cn/weather_mini";
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("city", city);
//        Retrofit.Builder builder = new Retrofit.Builder();
//        Retrofit retrofit = builder
//                .baseUrl("http://wthrcdn.etouch.cn/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        ITianQi iTianQi = retrofit.create(ITianQi.class);
//        Call<TianQi> call = iTianQi.getTianQi("西安");
//        call.enqueue(new Callback<TianQi>() {
//            @Override
//            public void onResponse(Call<TianQi> call, Response<TianQi> response) {
//                Logg.e("=====Retrofit=====" + response.body().toString());
//            }
//
//            @Override
//            public void onFailure(Call<TianQi> call, Throwable t) {
//                Logg.e("=====onFailure=====" + t.getMessage());
//            }
//        });
        OkHttpClientManager.getAsyn(url, hashMap, new OkHttpClientManager.ResultCallback() {

            @Override
            public void onError(Request request, Exception e) {
            }

            @Override
            public void onResponse(String result) {
                try {
                    Logg.i(result);
                    JSONObject object = new JSONObject(result);
                    JSONObject data = object.getJSONObject("data");
                    String city = data.optString("city");
                    String wendu = data.optString("wendu");
                    tvCity.setText(city);
                    tvTemperature.setText(wendu);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onException(String exception) {
            }
        });
    }
}