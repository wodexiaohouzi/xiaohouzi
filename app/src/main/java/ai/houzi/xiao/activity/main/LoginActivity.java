package ai.houzi.xiao.activity.main;

import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ai.houzi.xiao.R;
import ai.houzi.xiao.adapter.CommonAdapter;
import ai.houzi.xiao.adapter.ViewHolder;
import ai.houzi.xiao.db.DBManager;
import ai.houzi.xiao.impls.MyTextWatcher;
import ai.houzi.xiao.utils.Final;
import ai.houzi.xiao.utils.Logg;
import ai.houzi.xiao.utils.MD5;
import ai.houzi.xiao.utils.NetWorkUtils;
import ai.houzi.xiao.utils.SharedPerUtil;
import ai.houzi.xiao.widget.DialogLogin;
import ai.houzi.xiao.widget.RoundImageView;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mPhoneView;
    private EditText mPasswordView;
    private RoundImageView rivUserHead;

    private SharedPreferences login_success;
    private DialogLogin dialogLogin;
    private TextView tvNewUser;

    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dialogLogin = new DialogLogin(this);
        dialogLogin.setText("登录中...");
        dbManager = new DBManager(this);
        login_success = getSharedPreferences(Final.USER_LOGIN, Context.MODE_PRIVATE);
        rivUserHead = (RoundImageView) findViewById(R.id.rivUserHead);
        // Set up the login form.
        mPhoneView = (AutoCompleteTextView) findViewById(R.id.phone);
        populateAutoComplete();
        mPhoneView.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 11) {
                    String userHead = login_success.getString("user_" + s.toString(), null);
                    if (!TextUtils.isEmpty(userHead)) {
                        Glide.with(MyApplication.context).load(userHead).asBitmap().error(R.mipmap.default_head).into(rivUserHead);
                    }
                } else if (s.length() == 10 || s.length() == 12) {
                    rivUserHead.setImageResource(R.mipmap.default_head);
                }
            }
        });

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_ACTION_DONE) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        tvNewUser = (TextView) findViewById(R.id.tvNewUser);
        tvNewUser.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(intent, Final.REGISTER);
            }
        });
        initAutoComplete();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Final.REGISTER:
                    toMain();
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mPhoneView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mPhoneView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String phone = mPhoneView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required_password));
            focusView = mPasswordView;
            cancel = true;
        } else if (!"true".equals(isPasswordValid(password))) {
            mPasswordView.setError(isPasswordValid(password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(phone)) {
            mPhoneView.setError(getString(R.string.error_field_required_phone));
            focusView = mPhoneView;
            cancel = true;
        } else if (!isPhoneValid(phone)) {
            mPhoneView.setError(getString(R.string.error_invalid_phone));
            focusView = mPhoneView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            if (NetWorkUtils.isNetworkAvailable(this)) {
                mAuthTask = new UserLoginTask(phone, password);
                mAuthTask.execute((Void) null);
            } else {
                String pwd = login_success.getString("password_" + phone, null);
                showProgress(false);
                if (MD5.md5(password).equals(pwd)) {
                    login_success.edit().putString("Login", "LOGIN_SUCCESS").apply();
                    login_success.edit().putString("password_" + mPhoneView.getText().toString(), MD5.md5(mPasswordView.getText().toString())).apply();
                    toMain();
                } else {
                    Toast.makeText(this, "网络异常，请检查网络！", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    private boolean isPhoneValid(String phone) {
        //TODO: Replace this with your own logic
        return phone.matches("^[1][3-8]\\d{9}$");
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
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

    /**
     * Shows the progress UI and hides the login form.
     */
    private void showProgress(boolean show) {
        if (show) {
            dialogLogin.show();
        } else {
            dialogLogin.dismiss();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }


    private void addEmailsToAutoComplete(final List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mPhoneView.setAdapter(adapter);
        mPhoneView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Logg.e(emailAddressCollection.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Logg.e("onNothingSelected");
            }
        });
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            if (success) {
                login_success.edit().putString("Login", "LOGIN_SUCCESS").apply();
                login_success.edit().putString("password_" + mPhoneView.getText().toString(), MD5.md5(mPasswordView.getText().toString())).apply();
                toMain();
            } else {
                showProgress(false);
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    private void toMain() {
        saveHistory(mPhoneView.getText().toString().trim());
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 把指定AutoCompleteTextView中内容保存到sharedPreference中指定的字符段
     *
     * @param text 要操作保存的text
     */
    private void saveHistory(String text) {
        String longhistory = SharedPerUtil.getLoginHistory();
        if (!longhistory.contains(text + ",")) {
            StringBuilder sb = new StringBuilder(longhistory);
            sb.insert(0, text + ",");
            SharedPerUtil.setLoginHistory(sb.toString());
        }
    }

    private void initAutoComplete() {
        String longhistory = SharedPerUtil.getLoginHistory();
        if (TextUtils.isEmpty(longhistory)) {
            return;
        }
        final String[] hisArrays = longhistory.split(",");
        Logg.i(hisArrays.length);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_list_item_1, hisArrays);
//        //只保留最近的50条的记录
//        if (hisArrays.length > 5) {
//            String[] newArrays = new String[5];
//            System.arraycopy(hisArrays, 0, newArrays, 0, 5);
//            adapter = new ArrayAdapter<String>(this,
//                    android.R.layout.simple_dropdown_item_1line, newArrays);
//        }
//        mPhoneView.setAdapter(adapter);
////        mPhoneView.setDropDownHeight(350);
//        mPhoneView.setThreshold(0);
////        mPhoneView.setCompletionHint("最近的5条记录");
        mPhoneView.setText(hisArrays.length > 0 ? hisArrays[0] : "");
        mPhoneView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Logg.e(hasFocus);
                AutoCompleteTextView view = (AutoCompleteTextView) v;
                if (hasFocus) {
                    showPhoneList(mPhoneView, Arrays.asList(hisArrays));
                }
            }
        });
    }

    private PopupWindow phonePopup;
    private HistoryAdapter historyAdapter;

    private void showPhoneList(View v, List<String> phones) {
        ListView list = new ListView(LoginActivity.this);
        historyAdapter = new HistoryAdapter(LoginActivity.this, phones);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (phonePopup != null) {
                    phonePopup.dismiss();
                }
                ViewHolder holder = (ViewHolder) view.getTag();
                mPhoneView.setText(((TextView) holder.getView(R.id.userPhone)).getText().toString());
                mPhoneView.setSelection(mPhoneView.getText().length());
            }
        });
        list.setAdapter(historyAdapter);

        phonePopup = new PopupWindow(LoginActivity.this);
        phonePopup.setContentView(list);
        phonePopup.setWidth(v.getWidth());
        phonePopup.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // stockpopup.setBackgroundDrawable(getResources().getDrawable(
        // R.drawable.shape_border_text_line));

        // 需要设置一下此参数，点击外边可消失
        phonePopup.setBackgroundDrawable(new ColorDrawable(
                getResources().getColor(android.R.color.transparent)));
        // 设置点击窗口外边窗口消失
        phonePopup.setOutsideTouchable(true);
        // 设置弹出窗体需要软键盘，
        phonePopup.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        // 再设置模式，和Activity的一样，覆盖。
        phonePopup.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        phonePopup.showAsDropDown(v, 0, 5);
    }

    private long mExitTime;

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private class HistoryAdapter extends CommonAdapter<String> {

        public HistoryAdapter(Context context, List<String> datas) {
            super(context, R.layout.item_login_account, datas);
        }

        @Override
        public void convert(ViewHolder holder, String s) {
            holder.setText(R.id.userPhone, s);
        }
    }
}

