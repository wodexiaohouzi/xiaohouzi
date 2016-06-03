package com.juxin.common.widget;

import android.content.Context;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

/**
 * 用这个控件，说明输入密码不要延迟提示（可以与自定义键盘配合使用）
 */
public class SafeEditText extends EditText {

    public SafeEditText(Context context) {
        this(context, null);
    }

    public SafeEditText(final Context context, AttributeSet attrs) {
        super(context, attrs);

        setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Settings.System.putInt(context.getContentResolver(), Settings.System.TEXT_SHOW_PASSWORD, 0);
                } else {
                    Settings.System.putInt(context.getContentResolver(), Settings.System.TEXT_SHOW_PASSWORD, 1);
                }
            }
        });
    }

}
