package com.example.json.foogt.activity;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.json.foogt.R;
import com.example.json.foogt.Sqlite.MySqliteOpenHelper;
import com.example.json.foogt.entity.User;
import com.example.json.foogt.entity.UserInfoMsg;
import com.example.json.foogt.util.HttpCallbackListener;
import com.example.json.foogt.util.HttpUtil;
import com.example.json.foogt.util.IConst;
import com.example.json.foogt.util.LogUtil;
import com.example.json.foogt.util.Utility;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class LoginActivity extends AppCompatActivity {
    private final String TAG = "LoginActivity";
    private int userId;
    private Button loginBtn, registerBtn;
    private EditText countEdit, pwdEdit;
    private String account;
    private String password;

    private MySqliteOpenHelper helper;
    private SQLiteDatabase db;
    private String dbName = "user";
    private int version = 1;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            boolean re = (boolean) msg.obj;
            System.out.println(re);
            if (re == true) {
                // TODO: 2015/12/28 get userId on Login
                MenuActivity.actionStart(LoginActivity.this, userId);
                finish();
            } else {
                System.out.println("没成功啊亲");
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginBtn = (Button) findViewById(R.id.btn_login_login);
        registerBtn = (Button) findViewById(R.id.btn_login_register);
        countEdit = (EditText) findViewById(R.id.edit_login_userName);
        pwdEdit = (EditText) findViewById(R.id.edit_login_pwd);

        helper = new MySqliteOpenHelper(LoginActivity.this, dbName, null, version);
        db = helper.getWritableDatabase();

        //登陆按钮点击事件
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new Thread() {
//                    @Override
//                    public void run() {
//                        account = editText.getText().toString();
//                        password = editText2.getText().toString();
//                        String path = "http://10.25.246.100:8080/Foogt/servlet/LoginServlet?username=" + account + "&password=" + password + "";
//                        System.out.println(path);
//                        try {
//                            URL url = new URL(path);
//                            URLConnection conn = url.openConnection();
//                            conn.setConnectTimeout(5000);
//                            conn.setRequestProperty("User-Agent", "Mozilla/4.0");
//                            InputStream is = conn.getInputStream();
//                            BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
//                            String result = br.readLine();
//                            Message msg = new Message();
//                            msg.obj = result;
//                            handler.sendMessage(msg);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }.start();
                account = countEdit.getText().toString();
                password = pwdEdit.getText().toString();
                if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(password)) {
                    String url = IConst.SERVLET_ADDR + "LoginServlet";
                    String data = "username=" + account + "&" + "password=" + password;
                    HttpUtil.sendHttpRequest(url, "POST", data, new HttpCallbackListener() {
                        @Override
                        public void onFinish(String response) {
                            UserInfoMsg UserInfo = JSON.parseObject(response, UserInfoMsg.class);
                            userId = UserInfo.getUser().getUserId();
                            System.out.println(userId);
                            Message msg = new Message();
                            msg.obj = UserInfo.isResult();
                            handler.sendMessage(msg);


                            /**
                             * 存数据到sqlite
                             */
                            SaveData(account);
                        }

                        @Override
                        public void onError(Exception e) {
                            e.printStackTrace();
                            LogUtil.e(TAG, e.toString());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LoginActivity.this, R.string.http_fail, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity.actionStart(LoginActivity.this);

            }
        });
    }

    /**
     * 保存数据
     */
    public void SaveData(String account) {
        boolean flag = false;
        String testAccount = "select * from user where account=?";
        Cursor c = db.rawQuery(testAccount, new String[]{account});
        c.getColumnCount();
        System.out.println("c = " + c);
        while (c.moveToFirst()) {
            flag = true;
        }
        c.close();
        if (!flag) {
            String sql = "insert into user values(?,?)";
            db.execSQL(sql, new Object[]{account, password});
        }
    }

    /**
     * 对接账号添加
     */

    public static void actionStart(Context context) {
        Intent i = new Intent(context, LoginActivity.class);
        context.startActivity(i);
    }

}
