package ai.houzi.xiao.activity.test;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;

import com.juxin.common.utils.KeyboardUtil;
import com.juxin.common.widget.SafeEditText;

import ai.houzi.xiao.R;
import ai.houzi.xiao.widget.TitleBar;

public class KeyBoardActivity extends Activity implements View.OnClickListener {
    private Context ctx;
    private Activity act;
    private EditText edit, edit2;
    private SafeEditText edit1;
    private TitleBar titleBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard);
        ctx = this;
        act = this;
        titleBar = (TitleBar) findViewById(R.id.titleBar);
        titleBar.setTitleText("自定义键盘");
        titleBar.setLeftText("返回", this);

        edit = (EditText) this.findViewById(R.id.edit);
        edit2 = (EditText) this.findViewById(R.id.edit2);
        edit.setInputType(InputType.TYPE_NULL);

        edit1 = (SafeEditText) this.findViewById(R.id.edit1);

        edit.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int inputback = edit.getInputType();
                new KeyboardUtil(act, ctx, edit).showKeyboard();
                edit.setInputType(inputback);
                return false;
            }
        });
        edit1.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                edit1.setText("");
                int inputback = edit1.getInputType();
                edit1.setInputType(InputType.TYPE_NULL);
                new KeyboardUtil(act, ctx, edit1).showKeyboard();
                edit1.setInputType(inputback);
                return false;
            }
        });
        edit2.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                edit2.setText("");
                int inputback = edit2.getInputType();
                edit2.setInputType(InputType.TYPE_NULL);
                new KeyboardUtil(act, ctx, edit2).showKeyboard();
                edit2.setInputType(inputback);
                return false;
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llLeft:
                finish();
                break;
        }
    }
}