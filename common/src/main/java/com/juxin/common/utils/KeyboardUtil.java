package com.juxin.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.media.AudioAttributes;
import android.os.Vibrator;
import android.provider.Settings;
import android.text.Editable;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.juxin.common.R;
import com.juxin.common.widget.SafeEditText;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class KeyboardUtil {
    private Context ctx;
    private Activity act;
    private KeyboardView keyboardView;
    private Keyboard k1;// 字母键盘
    private Keyboard k2;// 数字键盘
    private Keyboard k3;// 符号键盘
    public boolean isnun = false;// 是否数据键盘
    public boolean isupper = false;// 是否大写

    private Resources resources;

    private EditText ed;
    private RelativeLayout rlKeyboard;
    private TextView tvSystemKeyboard;
    private RadioGroup rgChangeKeyboard;
    private RadioButton rbDigit, rbLetter, rbSymbol;

    public KeyboardUtil(Activity act, Context ctx, EditText edit) {
        this.act = act;
        this.ctx = ctx;
        this.ed = edit;
        resources = ctx.getResources();
        k1 = new Keyboard(ctx, R.xml.qwerty);
        k2 = new Keyboard(ctx, R.xml.digit);
        k3 = new Keyboard(ctx, R.xml.symbol);
        keyboardView = (KeyboardView) act.findViewById(R.id.keyboard_view);
        rlKeyboard = (RelativeLayout) act.findViewById(R.id.rl_keyboard);
        keyboardView.setKeyboard(k1);
        keyboardView.setEnabled(true);
        keyboardView.setPreviewEnabled(true);
        keyboardView.setOnKeyboardActionListener(listener);

        rgChangeKeyboard = (RadioGroup) act.findViewById(R.id.rgChangeKeyboard);
        tvSystemKeyboard = (TextView) act.findViewById(R.id.tvSystemKeyboard);
        rbDigit = (RadioButton) act.findViewById(R.id.rb_digit);
        rbLetter = (RadioButton) act.findViewById(R.id.rb_letter);
        rbSymbol = (RadioButton) act.findViewById(R.id.rb_symbol);
        tvSystemKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openKeyBoard();
            }
        });
        rgChangeKeyboard.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_digit) {
                    isnun = true;
                    randomdigkey();
                    keyboardView.setKeyboard(k2);
                } else if (checkedId == R.id.rb_letter) {
                    isnun = false;
                    keyboardView.setKeyboard(k1);
                } else if (checkedId == R.id.rb_symbol) {
                    keyboardView.setKeyboard(k3);
                }
            }
        });
    }

    private OnKeyboardActionListener listener = new OnKeyboardActionListener() {
        @Override
        public void swipeUp() {
        }

        @Override
        public void swipeRight() {
        }

        @Override
        public void swipeLeft() {
        }

        @Override
        public void swipeDown() {
        }

        @Override
        public void onText(CharSequence text) {
        }

        @Override
        public void onRelease(int primaryCode) {
        }

        @Override
        public void onPress(int primaryCode) {
            vibrate(20);
            if (primaryCode == Keyboard.KEYCODE_SHIFT || primaryCode == Keyboard.KEYCODE_DELETE
                    || primaryCode == Keyboard.KEYCODE_DONE || primaryCode == 32 || primaryCode == Keyboard.KEYCODE_MODE_CHANGE
                    || (primaryCode >= 48 && primaryCode <= 57)) {
                keyboardView.setPreviewEnabled(false);
            } else {
                keyboardView.setPreviewEnabled(true);
            }
        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            Editable editable = ed.getText();
            int start = ed.getSelectionStart();
            if (primaryCode == Keyboard.KEYCODE_DONE) {// 完成
                hideKeyboard();
            } else if (primaryCode == Keyboard.KEYCODE_DELETE) {// 回退
                if (editable != null && editable.length() > 0) {
                    if (start > 0) {
                        editable.delete(start - 1, start);
                    }
                }
            } else if (primaryCode == Keyboard.KEYCODE_SHIFT) {// 大小写切换
                changeKey();
                keyboardView.setKeyboard(k1);

            } else if (primaryCode == Keyboard.KEYCODE_MODE_CHANGE) {// 数字键盘切换
                if (isnun) {
                    isnun = false;
                    keyboardView.setKeyboard(k1);
                } else {
                    isnun = true;
                    randomdigkey();
                    keyboardView.setKeyboard(k2);
                }
            } else if (primaryCode == 57419) { // go left
                if (start > 0) {
                    ed.setSelection(start - 1);
                }
            } else if (primaryCode == 57421) { // go right
                if (start < ed.length()) {
                    ed.setSelection(start + 1);
                }
            } else {
                editable.insert(start, Character.toString((char) primaryCode));
            }
        }
    };

    private void randomdigkey() {
        List<Key> keyList = k2.getKeys();
        // 查找出0-9的数字键
        List<Key> newkeyList = new ArrayList<Key>();
        for (int i = 0; i < keyList.size(); i++) {
            if (keyList.get(i).label != null
                    && isNumber(keyList.get(i).label.toString())) {
                newkeyList.add(keyList.get(i));
            }
        }
        // 数组长度
        int count = newkeyList.size();
        // 结果集
        List<KeyModel> resultList = new ArrayList<KeyModel>();
        // 用一个LinkedList作为中介
        LinkedList<KeyModel> temp = new LinkedList<KeyModel>();
        // 初始化temp
        for (int i = 0; i < count; i++) {
            temp.add(new KeyModel(48 + i, i + ""));
        }
        // 取数
        Random rand = new Random();
        for (int i = 0; i < count; i++) {
            int num = rand.nextInt(count - i);
            resultList.add(new KeyModel(temp.get(num).getCode(), temp.get(num)
                    .getLable()));
            temp.remove(num);
        }
        for (int i = 0; i < newkeyList.size(); i++) {
            newkeyList.get(i).label = resultList.get(i).getLable();
            newkeyList.get(i).codes[0] = resultList.get(i).getCode();
        }

        keyboardView.setKeyboard(k2);
    }

    private boolean isNumber(String str) {
        String wordstr = "0123456789.,";
        if (wordstr.indexOf(str) > -1) {
            return true;
        }
        return false;
    }

    /**
     * 键盘大小写切换
     */
    private void changeKey() {
        List<Key> keylist = k1.getKeys();
        if (isupper) {// 大写切换小写
            isupper = false;
            for (Key key : keylist) {
                if (key.label != null && isword(key.label.toString())) {
                    key.label = key.label.toString().toLowerCase();
                    key.codes[0] = key.codes[0] + 32;
                } else if (key.sticky && key.codes[0] == Keyboard.KEYCODE_SHIFT) {
                    key.icon = resources.getDrawable(R.drawable.sym_keyboard_shift_normal);
                }
            }
        } else {// 小写切换大写
            isupper = true;
            for (Key key : keylist) {
                if (key.label != null && isword(key.label.toString())) {
                    key.label = key.label.toString().toUpperCase();
                    key.codes[0] = key.codes[0] - 32;
                } else if (key.sticky && key.codes[0] == Keyboard.KEYCODE_SHIFT) {
                    key.icon = resources.getDrawable(R.drawable.sym_keyboard_shift_press);
                }
            }
        }
    }

    public void showKeyboard() {
        hideKeyBoard();
        if (this.ed instanceof SafeEditText) {
            Settings.System.putInt(this.act.getContentResolver(), Settings.System.TEXT_SHOW_PASSWORD, 0);
        } else {
            Settings.System.putInt(this.act.getContentResolver(), Settings.System.TEXT_SHOW_PASSWORD, 1);
        }
        rbLetter.setChecked(true);
        int visibility = rlKeyboard.getVisibility();
        if (visibility == View.GONE || visibility == View.INVISIBLE) {
            rlKeyboard.setVisibility(View.VISIBLE);
            rlKeyboard.startAnimation(AnimationUtils.loadAnimation(this.act, R.anim.show_keyboard));
        }
    }

    public void hideKeyboard() {
        Settings.System.putInt(this.act.getContentResolver(), Settings.System.TEXT_SHOW_PASSWORD, 1);
        int visibility = rlKeyboard.getVisibility();
        if (visibility == View.VISIBLE) {
            rlKeyboard.setVisibility(View.INVISIBLE);
            rlKeyboard.startAnimation(AnimationUtils.loadAnimation(this.act, R.anim.hide_keyboard));
        }
    }

    private boolean isword(String str) {
        String wordstr = "abcdefghijklmnopqrstuvwxyz";
        if (wordstr.indexOf(str.toLowerCase()) > -1) {
            return true;
        }
        return false;
    }

    /**
     * 打开系统软键盘
     */
    protected void openKeyBoard() {
        hideKeyboard();
        InputMethodManager imm = (InputMethodManager) this.ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 关闭系统软键盘
     */
    protected void hideKeyBoard() {
        InputMethodManager inputMsg = (InputMethodManager) this.ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMsg.isActive()) { // 隐藏软键盘
            View curView = this.act.getCurrentFocus();
            if (curView != null) {
                inputMsg.hideSoftInputFromWindow(curView.getWindowToken(), 0);
            }
        }
    }

    /**
     * 震动
     *
     * @param duration
     */
    protected void vibrate(long duration) {
        Vibrator vibrator = (Vibrator) this.act.getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {
                0, duration
        };
        vibrator.vibrate(pattern, -1);
    }

}
