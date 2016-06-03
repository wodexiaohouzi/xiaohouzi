package ai.houzi.xiao.activity.main;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.squareup.okhttp.Request;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ai.houzi.xiao.R;
import ai.houzi.xiao.activity.interfaces.ITianQi;
import ai.houzi.xiao.entity.TianQi;
import ai.houzi.xiao.utils.Logg;
import ai.houzi.xiao.utils.MD5;
import ai.houzi.xiao.utils.OkHttpClientManager;
import ai.houzi.xiao.widget.BigToast;
import ai.houzi.xiao.widget.ClearEditText;
import ai.houzi.xiao.widget.DialogLogin;
import ai.houzi.xiao.widget.TitleBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 注册
 */
public class RegisterActivity extends BaseActivity {
    private TitleBar titleBar;
    private ClearEditText cetPhone, cetPassword;
    private Button btnRegister;
    private DialogLogin dialogLogin;
    private Handler mHandler;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_register);
        dialogLogin = new DialogLogin(this);
        mHandler = new Handler(getMainLooper());
        dialogLogin.setText("注册中...");
        dialogLogin.setCancelable(true);
        titleBar = (TitleBar) findViewById(R.id.titleBar);
        titleBar.setTitleText("注\t册");
        titleBar.setLeftText("登录", this);
        cetPhone = (ClearEditText) findViewById(R.id.cetPhone);
        cetPassword = (ClearEditText) findViewById(R.id.cetPassword);
        btnRegister = (Button) findViewById(R.id.btnRegister);

    }

    @Override
    protected void initListener() {
        btnRegister.setOnClickListener(this);
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
            case R.id.btnRegister:
                String phone = cetPhone.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    showBigToast(BigToast.WRONG, getString(R.string.error_field_required_phone));
                    cetPhone.setShakeAnimation(5);
                    return;
                }
                if (!isPhoneValid(phone)) {
                    showBigToast(BigToast.WRONG, getString(R.string.error_invalid_phone));
                    cetPhone.setShakeAnimation(5);
                    return;
                }
                String password = cetPassword.getText().toString();
                if (TextUtils.isEmpty(password)) {
                    showBigToast(BigToast.WRONG, getString(R.string.error_field_required_password));
                    cetPassword.setShakeAnimation(5);
                    return;
                }
                String isValid = isPasswordValid(password);
                if (!"true".equals(isValid)) {
                    showBigToast(BigToast.WRONG, isValid);
                    cetPassword.setShakeAnimation(5);
                    return;
                }
                register(phone, password);
                break;
        }
    }

    private boolean isPhoneValid(String phone) {
        return phone.matches("^[1][3-8]\\d{9}$");
    }

    private String isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        if (password.length() < 6) {
            //密码长度不能小于6位
            return "密码长度不能小于6位";
        }
        if (password.length() > 12) {
            //密码长度不能大于12位
            return "密码长度不能大于12位";
        }
        if (TextUtils.isDigitsOnly(password)) {
            //密码必须包含数字和字母
            return "密码必须包含数字和字母";
        }
        if (password.matches("^[A-Za-z]+$")) {
            //密码必须包含数字和字母
            return "密码必须包含数字和字母";
        }

        if (!password.matches("[A-Za-z0-9_]{6,12}")) {
            //密码不能包含非法字符
            return "密码不能包含非法字符";
        }
        return "true";
    }

    private void showProgress(boolean show) {
        if (show) {
            dialogLogin.show();
        } else {
            dialogLogin.dismiss();
        }
    }

    private void register(String phone, String password) {
        //http://aihouzi.com/xiaohouzi/user.php?iName=User/register&userName=小侯子&userPhone=15300928917
        String url = IP.HTTP + "/user/user.php";
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("iName", "User/register");
        hashMap.put("userPhone", phone);
        hashMap.put("password", MD5.md5(password));
        OkHttpClientManager.postAsyn(url, hashMap, new OkHttpClientManager.ResultCallback() {

            @Override
            public void onAfter() {
                showProgress(false);
            }

            @Override
            public void onBefore(Request request) {
                showProgress(true);
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
                        setResult(RESULT_OK);
                        finish();
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
