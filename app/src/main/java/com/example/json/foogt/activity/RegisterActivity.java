package com.example.json.foogt.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.json.foogt.R;
import com.example.json.foogt.entity.User;
import com.example.json.foogt.util.HttpCallbackListener;
import com.example.json.foogt.util.HttpUtil;
import com.example.json.foogt.util.IConst;
import com.example.json.foogt.util.LogUtil;
import com.example.json.foogt.util.Utility;

public class RegisterActivity extends AppCompatActivity {

    private final String TAG = "RegisterActivity";
    EditText accountEdit;
    EditText passwordEdit;
    Button registerBtn;

    private ActionBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setDisplayShowHomeEnabled(true);
        bar.setHomeButtonEnabled(true);
        bar.setTitle("注册");

        accountEdit = (EditText) findViewById(R.id.edit_register_account);
        passwordEdit = (EditText) findViewById(R.id.edit_register_pwd);
        registerBtn = (Button) findViewById(R.id.register_button);

        registerBtn.setOnClickListener(new onRegisterButtonClickListener());
        passwordEdit.setOnFocusChangeListener(new onPasswordFocusChangeListener());
    }

    /*
   点击按钮结束当前界面
    */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        //return super.onOptionsItemSelected(item);
        return true;
    }

    private class onRegisterButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            String account = accountEdit.getText().toString();
            String pwd = passwordEdit.getText().toString();
            if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(pwd)) {
                String url = IConst.SERVLET_ADDR + "UserRegister";
                String data = "account=" + account + "&" + "password=" + pwd;
                HttpUtil.sendHttpRequest(url, "POST", data, new HttpCallbackListener() {
                    @Override
                    public void onFinish(String response) {
                        if(Utility.handleBooleanResultResponse(response)){
                            User u =Utility.handleUserInfoResultResponse(response);
                            // TODO: 2015/12/24 save user info
//                            finish();
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                        LogUtil.e(TAG, e.toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(RegisterActivity.this, R.string.http_fail, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        }
    }

    private class onPasswordFocusChangeListener implements View.OnFocusChangeListener {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                //check user exist
                boolean flag = true;
                final String account = accountEdit.getText().toString();
                if (TextUtils.isEmpty(account)) {
                    flag = false;
                }
                if (flag) {
                    // account not empty
                    String url = IConst.SERVLET_ADDR + "CheckAccountExist?account=" + account;
                    HttpUtil.sendHttpRequest(url, "GET", null, new HttpCallbackListener() {
                        @Override
                        public void onFinish(String response) {
                            // TODO: 2015/12/24 to be optimized to free server
                            if (Utility.handleBooleanResultResponse(response)) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // TODO: 2015/12/24 confirm no conflict with out of if(flag)
                                        // TODO: 2015/12/24 button still clickable
                                        accountEdit.setClickable(false);
                                        Toast.makeText(RegisterActivity.this, R.string.account_exist, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            e.printStackTrace();
                            LogUtil.e(TAG, e.toString());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(RegisterActivity.this, R.string.http_fail, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }
                LogUtil.d(TAG,"Button\t"+flag);
                accountEdit.setClickable(flag);
            }
        }
    }

    // TODO: 2015/12/24 等待登录界面完成对接 
    public static void actionStart(Context context) {
        Intent i = new Intent(context, RegisterActivity.class);
        context.startActivity(i);
    }
}
