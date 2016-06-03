package com.qqalbum.imagepicker.ui;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.qqalbum.imagepicker.R;
import com.qqalbum.imagepicker.customviews.MyTitleBar;

import java.util.ArrayList;

/**
 * private void imageBrower(int position, String[] urls) {
 * Intent intent = new Intent(mContext, ImagePagerActivity.class);
 * // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
 * intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
 * intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
 * mContext.startActivity(intent);
 * }
 */
public class ImagePagerActivity extends AppCompatActivity {
    private static final String STATE_POSITION = "STATE_POSITION";
    public static final String EXTRA_IMAGE_INDEX = "image_index";
    public static final String EXTRA_IMAGE_URLS = "image_urls";
    public static final String NEED_SELECT = "needSelect";
    public static final String SELECT = "select";

    private HackyViewPager mPager;
    private int pagerPosition;
    private MyTitleBar myTitleBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_detail_pager);

        pagerPosition = getIntent().getIntExtra(EXTRA_IMAGE_INDEX, 0);
//        String[] urls = getIntent().getStringArrayExtra(EXTRA_IMAGE_URLS);
        ArrayList<String> urls = getIntent().getStringArrayListExtra(EXTRA_IMAGE_URLS);

        mPager = (HackyViewPager) findViewById(R.id.pager);
        ImagePagerAdapter mAdapter = new ImagePagerAdapter(
                getSupportFragmentManager(), urls);
        mPager.setAdapter(mAdapter);
        myTitleBar = (MyTitleBar) findViewById(R.id.titleBar);

        CharSequence text = getString(R.string.viewpager_indicator, 1, mPager
                .getAdapter().getCount());
        myTitleBar.setTitleText(text);
        myTitleBar.setLeftText(getString(R.string.main_back), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        boolean needSelect = getIntent().getBooleanExtra(NEED_SELECT, false);
        if (needSelect) {
            boolean select = getIntent().getBooleanExtra(SELECT, false);
            myTitleBar.setRightCheckBox(0, null);
            myTitleBar.setRightChecked(select);
        }
        // 更新下标
        mPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageSelected(int arg0) {
                CharSequence text = getString(R.string.viewpager_indicator,
                        arg0 + 1, mPager.getAdapter().getCount());
                myTitleBar.setTitleText(text);
            }

        });
        if (savedInstanceState != null) {
            pagerPosition = savedInstanceState.getInt(STATE_POSITION);
        }

        mPager.setCurrentItem(pagerPosition);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_POSITION, mPager.getCurrentItem());
    }

    public void visibleTitle() {
        if (myTitleBar.isShown()) {
            myTitleBar.setVisibility(View.INVISIBLE);
            myTitleBar.startAnimation(AnimationUtils.loadAnimation(this, R.anim.out_from_top));
        } else {
            myTitleBar.setVisibility(View.VISIBLE);
            myTitleBar.startAnimation(AnimationUtils.loadAnimation(this, R.anim.in_from_top));
        }
    }

    public void colorChange(ImageDetailFragment fragment) {
        // 用来提取颜色的Bitmap
        Bitmap bitmap = ((BitmapDrawable) fragment.getBackground()).getBitmap();
        // Palette的部分
        Palette.generateAsync(bitmap, new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                Palette.Swatch swatch = palette.getVibrantSwatch();
                if (swatch == null) {
                    return;
                }
                myTitleBar.setBackgroundColor(swatch.getRgb());
            }
        });
    }

    private class ImagePagerAdapter extends FragmentStatePagerAdapter {

        public ArrayList<String> fileList;

        public ImagePagerAdapter(FragmentManager fm, ArrayList<String> fileList) {
            super(fm);
            this.fileList = fileList;
        }

        @Override
        public int getCount() {
            return fileList == null ? 0 : fileList.size();
        }

        @Override
        public Fragment getItem(int position) {
            String url = fileList.get(position);
            ImageDetailFragment imageDetailFragment = ImageDetailFragment.newInstance(url);
//            colorChange(imageDetailFragment);
            return imageDetailFragment;
        }

    }
}