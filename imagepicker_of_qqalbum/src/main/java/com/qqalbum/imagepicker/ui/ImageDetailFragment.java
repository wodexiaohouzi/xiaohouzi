package com.qqalbum.imagepicker.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.qqalbum.imagepicker.R;
import com.qqalbum.imagepicker.customviews.GifView;
import com.qqalbum.imagepicker.photoview.PhotoViewAttacher;
import com.qqalbum.imagepicker.utils.GaoSI;

public class ImageDetailFragment extends Fragment {
    private String mImageUrl;
    private ImageView mImageView;
    private ProgressBar progressBar;
    private PhotoViewAttacher mAttacher;

    public static ImageDetailFragment newInstance(String imageUrl) {
        final ImageDetailFragment f = new ImageDetailFragment();

        final Bundle args = new Bundle();
        args.putString("url", imageUrl);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageUrl = getArguments() != null ? getArguments().getString("url") : null;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.image_detail_fragment, container, false);
        mImageView = (ImageView) v.findViewById(R.id.image);
        mAttacher = new PhotoViewAttacher(mImageView);

//        mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
//
//            @Override
//            public void onPhotoTap(View arg0, float arg1, float arg2) {
//                getActivity().finish();
//            }
//        });
        mAttacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ImagePagerActivity) getActivity()).visibleTitle();
            }
        });

        progressBar = (ProgressBar) v.findViewById(R.id.loading);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        progressBar.setVisibility(View.VISIBLE);
        Glide.with(getActivity()).load(mImageUrl).asBitmap().into(new ImageViewTarget<Bitmap>(mImageView) {
            @Override
            protected void setResource(Bitmap resource) {
                mImageView.setImageBitmap(resource);
                mAttacher.update();
                progressBar.setVisibility(View.GONE);
                if (ImageDetailFragment.this.isVisible()) {
                    ((ImagePagerActivity) getActivity()).colorChange(ImageDetailFragment.this);
                }
            }
        });
    }

    /**
     * 提供当前Fragment的主色调的Bitmap对象,供Palette解析颜色
     *
     * @return
     */
    public Drawable getBackground() {
        return mImageView.getDrawable();
    }

}
