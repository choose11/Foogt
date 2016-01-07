package com.example.json.foogt.activity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.json.foogt.ActivityCollector.ActivityCollector;
import com.example.json.foogt.R;
import com.example.json.foogt.Sqlite.MySqliteOpenHelper;
import com.example.json.foogt.entity.UserInfoMsg;
import com.example.json.foogt.util.HttpCallbackListener;
import com.example.json.foogt.util.HttpUtil;
import com.example.json.foogt.util.IConst;
import com.example.json.foogt.util.LogUtil;

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
    SharedPreferences preferences;


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
        ActivityCollector.addActivity(this);
        loginBtn = (Button) findViewById(R.id.btn_login_login);
        registerBtn = (Button) findViewById(R.id.btn_login_register);
        countEdit = (EditText) findViewById(R.id.edit_login_userName);
        pwdEdit = (EditText) findViewById(R.id.edit_login_pwd);
        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);

        helper = new MySqliteOpenHelper(LoginActivity.this, dbName, null, version);
        db = helper.getWritableDatabase();

        //登陆按钮点击事件

        loginBtn.setOnClickListener(new OnSubmitListener());

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

    class OnSubmitListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
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
                        Message msg = new Message();
                        msg.obj = UserInfo.isResult();
                        /**
                         * 存数据到sqlite
                         */
                        SaveData(account);
                        /**
                         * 保存用户userId到本地，方便下次登陆
                         */
                        saveSharedPreferencesUserId(userId);

                        handler.sendMessage(msg);
                        //handler一定要放在上面两个函数后面执行，否则会先启动主界面再传值到文件里！！！！
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
    }

    private void saveSharedPreferencesUserId(int userId){
        preferences = getSharedPreferences("userId"
                , MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("userId", userId);
        // 提交修改
        /*
         *apply()与commit()区别：  apply没有返回值而commit返回boolean表明修改是否提交成功 ，
         * apply是将修改数据原子提交到内存, 而后异步真正提交到硬件磁盘, 而commit是同步的提交到硬件磁盘
         * apply方法不会提示任何失败的提示。
         */
        editor.apply();
        //editor.commit();
    }
}
