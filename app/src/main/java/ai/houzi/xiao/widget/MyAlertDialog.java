package ai.houzi.xiao.widget;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import ai.houzi.xiao.R;
import ai.houzi.xiao.utils.UIUtils;

/**
 * 提示框
 */
public class MyAlertDialog extends DialogFragment {

    private TextView tvTitle, tvMessage;
    private TextView negativeButton, positiveButton;
    private String mTitle, mMsg, mNegativeText, mPositiveText;
    private boolean mCancle;

    private void assignViews(View view) {
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        tvMessage = (TextView) view.findViewById(R.id.tvMessage);
        negativeButton = (TextView) view.findViewById(R.id.negativeButton);
        positiveButton = (TextView) view.findViewById(R.id.positiveButton);

        tvTitle.setVisibility(View.GONE);
        tvMessage.setVisibility(View.GONE);
        negativeButton.setVisibility(View.GONE);

        initListener();
    }

    private void initListener() {
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (negativeListener != null) {
                    negativeListener.onNegativeClick(v);
                }
            }
        });
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (positiveListener != null) {
                    positiveListener.onPositiveClick(v);
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout(UIUtils.dip2px(300), getDialog().getWindow().getAttributes().height);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.myalert_dialog, container);
        assignViews(view);
        setCancelable(mCancle);
        initData();
        return view;
    }

    private void initData() {
        if (!TextUtils.isEmpty(mTitle)) {
            tvTitle.setText(mTitle);
            tvTitle.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(mMsg)) {
            tvMessage.setText(mMsg);
            tvMessage.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(mNegativeText)) {
            negativeButton.setText(mNegativeText);
            negativeButton.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(mPositiveText)) {
            positiveButton.setText(mPositiveText);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * 设置标题
     */
    public MyAlertDialog setTitle(String title) {
        mTitle = title;
        return this;
    }

    /**
     * 设置内容
     */
    public MyAlertDialog setMessage(String msg) {
        mMsg = msg;
        return this;
    }

    public MyAlertDialog setCancle(boolean isCancle) {
        mCancle = isCancle;
        return this;
    }

    private OnNegativeClickListener negativeListener;

    public interface OnNegativeClickListener {
        void onNegativeClick(View v);
    }

    public MyAlertDialog setNegativeListener(String text, OnNegativeClickListener listener) {
        this.mNegativeText = text;
        this.negativeListener = listener;
        return this;
    }

    private OnPositiveClickListener positiveListener;

    public interface OnPositiveClickListener {
        void onPositiveClick(View v);
    }

    public MyAlertDialog setPositiveListener(String text, OnPositiveClickListener listener) {
        this.mPositiveText = text;
        this.positiveListener = listener;
        return this;
    }

    public void showDialog(FragmentManager manager, String tag) {
        if (!isAdded()) {
            show(manager, tag);
        }
    }
}

