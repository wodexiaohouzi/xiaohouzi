package com.juxin.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Selection;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.juxin.common.widget.MyTitleBar;

/**
 * Created by hp on 2016/5/10.
 */
public class ReNameActivity extends AppCompatActivity implements View.OnClickListener {
    private MyTitleBar myTitleBar;
    private EditText tvReName;
    private TextView tvErrorMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rename);

        myTitleBar = (MyTitleBar) findViewById(R.id.myTitleBar);
        myTitleBar.setTitleText("重命名");
        myTitleBar.setLeftText("取消", this);
        myTitleBar.setRighText("保存", this);
        tvReName = (EditText) findViewById(R.id.tvReName);
        tvErrorMsg = (TextView) findViewById(R.id.tvErrorMsg);
        String name = getIntent().getStringExtra("name");
        tvReName.setText(name);
        tvReName.setHighlightColor(0xFF17B4EB);
        tvReName.setSelection(0, name.lastIndexOf("."));

        tvReName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() < 1 || TextUtils.isEmpty(s.toString().substring(0, s.toString().indexOf(".")))) {
                    myTitleBar.setRightEnabled(false);
                    tvErrorMsg.setText("文件名不允许为空");
                } else {
                    myTitleBar.setRightEnabled(true);
                    tvErrorMsg.setText("");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.llLeft) {
            finish();
        } else if (v.getId() == R.id.tvRight) {
            Intent intent = new Intent();
            intent.putExtra("name", tvReName.getText().toString().trim());
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
