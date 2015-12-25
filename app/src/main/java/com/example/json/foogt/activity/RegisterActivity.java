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

import com.alibaba.fastjson.JSON;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.json.foogt.R;
import com.example.json.foogt.db.FoogtDB;
import com.example.json.foogt.entity.User;
import com.example.json.foogt.entity.UserInfoMsg;
import com.example.json.foogt.util.HttpCallbackListener;
import com.example.json.foogt.util.HttpUtil;
import com.example.json.foogt.util.IConst;
import com.example.json.foogt.util.LogUtil;
import com.example.json.foogt.util.Utility;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private final String TAG = "RegisterActivity";
    EditText accountEdit;
    EditText passwordEdit;
    Button registerBtn;
    RequestQueue mQueue;

    private ActionBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        bar = getSupportActionBar();
        //// TODO: 2015/12/25 warning
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setDisplayShowHomeEnabled(true);
        bar.setHomeButtonEnabled(true);
        bar.setTitle("注册");

        accountEdit = (EditText) findViewById(R.id.edit_register_account);
        passwordEdit = (EditText) findViewById(R.id.edit_register_pwd);
        registerBtn = (Button) findViewById(R.id.register_button);

        registerBtn.setOnClickListener(new onRegisterButtonClickListener());
        passwordEdit.setOnFocusChangeListener(new onPasswordFocusChangeListener());

        mQueue = Volley.newRequestQueue(RegisterActivity.this);
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
            if (!v.isClickable()) {
                return;
            }
            final String account = accountEdit.getText().toString();
            final String pwd = passwordEdit.getText().toString();
            if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(pwd)) {
                String url = IConst.SERVLET_ADDR + "UserRegister";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                UserInfoMsg userInfo = JSON.parseObject(response, UserInfoMsg.class);
                                if (userInfo.isResult()) {
                                    FoogtDB.getInstance(RegisterActivity.this).saveUser(userInfo.getUser());
                                }
                                finish();
                            }
                        }, new NetErrorListener()) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<>();
                        map.put("account", account);
                        map.put("password", pwd);
                        return map;
                    }
                };
                mQueue.add(stringRequest);
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
                    StringRequest stringRequest = new StringRequest(url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (Utility.handleBooleanResultResponse(response)) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                registerBtn.setClickable(false);
                                                Toast.makeText(RegisterActivity.this, R.string.account_exist, Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }
                            }, new NetErrorListener());
                    mQueue.add(stringRequest);
                }
                LogUtil.d(TAG, "RegisterButton\t" + flag);
                registerBtn.setClickable(flag);
            }
        }
    }

    class NetErrorListener implements Response.ErrorListener {

        @Override
        public void onErrorResponse(VolleyError e) {
            e.printStackTrace();
            LogUtil.e(TAG, e.toString());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(RegisterActivity.this, R.string.http_fail, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public static void actionStart(Context context) {
        Intent i = new Intent(context, RegisterActivity.class);
        context.startActivity(i);
    }
}
