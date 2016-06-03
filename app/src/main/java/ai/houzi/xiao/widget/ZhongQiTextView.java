package ai.houzi.xiao.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * ZhongQi字体的TextView
 */
public class ZhongQiTextView extends TextView {
    public ZhongQiTextView(Context context) {
        super(context);
        init();
    }

    public ZhongQiTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ZhongQiTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/zhongqi.ttf"));
    }
}
