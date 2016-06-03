package ai.houzi.xiao.activity.user;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.juxin.common.ioc.ContentView;
import com.juxin.common.ioc.ViewInject;
import com.juxin.common.ioc.ViewInjectUtils;
import com.squareup.okhttp.Request;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ai.houzi.xiao.R;
import ai.houzi.xiao.activity.main.BaseActivity;
import ai.houzi.xiao.activity.main.IP;
import ai.houzi.xiao.activity.main.MyApplication;
import ai.houzi.xiao.utils.Logg;
import ai.houzi.xiao.utils.MD5;
import ai.houzi.xiao.utils.OkHttpClientManager;
import ai.houzi.xiao.widget.BigToast;
import ai.houzi.xiao.widget.TitleBar;

/**
 * 编辑资料
 */
@ContentView(value = R.layout.activity_edit_info)
public class EditInfoActivity extends BaseActivity {

    @ViewInject(R.id.titleBar)
    private TitleBar titleBar;
    @ViewInject(R.id.llUserName)
    private LinearLayout llUserName;
    @ViewInject(R.id.etUserName)
    private EditText etUserName;
    @ViewInject(R.id.llSex)
    private LinearLayout llSex;
    @ViewInject(R.id.etSex)
    private EditText etSex;
    @ViewInject(R.id.llAge)
    private LinearLayout llAge;
    @ViewInject(R.id.etAge)
    private EditText etAge;
    @ViewInject(R.id.llConstellation)
    private LinearLayout llConstellation;
    @ViewInject(R.id.etConstellation)
    private EditText etConstellation;
    @ViewInject(R.id.llJob)
    private LinearLayout llJob;
    @ViewInject(R.id.etJob)
    private EditText etJob;
    @ViewInject(R.id.llCompany)
    private LinearLayout llCompany;
    @ViewInject(R.id.etCompany)
    private EditText etCompany;
    @ViewInject(R.id.llSchool)
    private LinearLayout llSchool;
    @ViewInject(R.id.etSchool)
    private EditText etSchool;
    @ViewInject(R.id.llSite)
    private LinearLayout llSite;
    @ViewInject(R.id.etSite)
    private EditText etSite;
    @ViewInject(R.id.llHomeTown)
    private LinearLayout llHomeTown;
    @ViewInject(R.id.etHomeTown)
    private EditText etHomeTown;
    @ViewInject(R.id.llMailBox)
    private LinearLayout llMailBox;
    @ViewInject(R.id.etMailBox)
    private EditText etMailBox;
    @ViewInject(R.id.llSignature)
    private LinearLayout llSignature;
    @ViewInject(R.id.etSignature)
    private EditText etSignature;
    @ViewInject(R.id.llInterest)
    private LinearLayout llInterest;


    @Override
    protected void initView(Bundle savedInstanceState) {
        ViewInjectUtils.inject(this);
        titleBar = (TitleBar) findViewById(R.id.titleBar);
        titleBar.setTitleText("编辑资料");
        titleBar.setLeftText("取消", this);
        titleBar.setRighText("完成", this);
    }

    @Override
    protected void initListener() {
    }

    @Override
    protected void initDate() {
    }

    @Override
    protected void handleListener(View v) {
        switch (v.getId()) {
            case R.id.llLeft:
                finish();
                break;
            case R.id.tvRight:
                String userPhone = MyApplication.userPhone;
                String userName = etUserName.getText().toString();
                String sex = etSex.getText().toString();
                String age = etAge.getText().toString();
                String constellation = etConstellation.getText().toString();
                String job = etJob.getText().toString();
                String company = etCompany.getText().toString();
                String school = etSchool.getText().toString();
                String site = etSite.getText().toString();
                String hometown = etHomeTown.getText().toString();
                String mailbox = etMailBox.getText().toString();
                String signature = etSignature.getText().toString();

                editinfo(userPhone, userName, "男".equals(sex) ? "1" : "0", age.replace("岁", ""), constellation, job, company, school, site, hometown, mailbox, signature);
                break;
        }
    }

    private void editinfo(String userPhone, String userName, String sex, String age, String constellation,
                          String job, String company, String school, String site, String hometown, String mailbox, String signature) {
        //http://aihouzi.com/xiaohouzi/user.php?iName=User/register&userName=小侯子&userPhone=15300928917
        String url = IP.HTTP + "/user/user.php";
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("iName", "User/editinfo");
        hashMap.put("userPhone", userPhone);
        hashMap.put("userName", userName);
        hashMap.put("sex", sex);
        hashMap.put("age", age);
        hashMap.put("constellation", constellation);
        hashMap.put("job", job);
        hashMap.put("company", company);
        hashMap.put("school", school);
        hashMap.put("site", site);
        hashMap.put("hometown", hometown);
        hashMap.put("mailbox", mailbox);
        hashMap.put("signature", signature);
        OkHttpClientManager.postAsyn(url, hashMap, new OkHttpClientManager.ResultCallback() {

            @Override
            public void onAfter() {
                hideLoadingDialog();
            }

            @Override
            public void onBefore(Request request) {
                showLoadingDialog(true);
            }

            @Override
            public void onError(Request request, Exception e) {
            }

            @Override
            public void onResponse(String result) {
                try {
                    Logg.e(result);
                    JSONObject object = new JSONObject(result);
                    int code = object.optInt("code");
                    String message = object.optString("message");
                    if (code == 200) {
                        showBigToast(message);
                    } else {
                        showBigToast(BigToast.WRONG, message);
                    }
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