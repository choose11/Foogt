package com.example.json.foogt.activity;

import android.accounts.Account;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.json.foogt.ActivityCollector.ActivityCollector;
import com.example.json.foogt.R;
import com.example.json.foogt.Sqlite.MySqliteOpenHelper;
import com.example.json.foogt.entity.User;
import com.example.json.foogt.entity.UserInfoMsg;
import com.example.json.foogt.util.HttpCallbackListener;
import com.example.json.foogt.util.HttpUtil;
import com.example.json.foogt.util.IConst;
import com.example.json.foogt.util.LogUtil;
import com.example.json.foogt.util.Utility;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChangeUserActivity extends AppCompatActivity {
    private ActionBar bar;
    private ListView userlv;
    private Button quitBtn, addBtn;
    private MySqliteOpenHelper helper;
    private SQLiteDatabase db;
    private final String TAG = "ChangeUserActivity";
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user);
        ActivityCollector.addActivity(this);
        userlv = (ListView) findViewById(R.id.lv_changeUser_user);
        quitBtn = (Button) findViewById(R.id.btn_change_user_quit);
        addBtn = (Button) findViewById(R.id.btn_change_user_add);

        userId = getIntent().getIntExtra("UserId", -1);

        helper = new MySqliteOpenHelper(ChangeUserActivity.this, "user", null, 1);
        db = helper.getWritableDatabase();

        showUser(userlv);
        userlv.setOnItemClickListener(new OnUserItemClickListener());

        OnClickListener listener = new OnClickListener();
        quitBtn.setOnClickListener(listener);
        addBtn.setOnClickListener(listener);

        bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setDisplayShowHomeEnabled(true);
            bar.setHomeButtonEnabled(true);
            bar.setTitle(R.string.account_management);
        }
    }

    /**
     * 点击列表选项，切换登陆。
     */
    public class OnUserItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            TextView accShow = (TextView) view.findViewById(R.id.txt_search_username);
            String account = accShow.getText().toString();
            String sql = "select * from user where account = ?";
            Cursor c = db.rawQuery(sql, new String[]{account});
            while (c.moveToNext()) {
                String accountSql = c.getString(c.getColumnIndex("account"));
                String pwd = c.getString(c.getColumnIndex("pwd"));
                String url = IConst.SERVLET_ADDR + "LoginServlet";
                String data = "username=" + accountSql + "&" + "password=" + pwd;
                reLogin(url, data);
            }
            c.close();
        }
    }

    /**
     * 点击监听器
     */
    public class OnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.btn_change_user_add:
                    LoginActivity.actionStart(ChangeUserActivity.this);
                    finish();
                    break;
                case R.id.btn_change_user_quit:
                    getAccount(userId);
                    break;
            }
        }
    }

    /**
     * 从服务器拿到登录名
     */
    private void getAccount(int userId) {

        String url = IConst.SERVLET_ADDR + "SearchUserAccount";
        String data = "userId=" + userId+"";
        // TODO: 2016/1/3 "post可以  GET不行（服务器java.lang.NumberFormatException: null）userd值不对"
        HttpUtil.sendHttpRequest(url, "POST", data, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                User user = Utility.handleUserAccountResultResponse(response);
                final String account = user.getAccount();
                judgeNext(account);
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ChangeUserActivity.this, R.string.network_is_bad, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }

    /**
     * 判断是否还有账号存在
     */
    private void judgeNext(String account) {
        if (!checkOtherUser(account)) {
            ActivityCollector.finishAll();
            LoginActivity.actionStart(ChangeUserActivity.this);

        } else {
            loginNext(account);
        }
    }

    /**
     * 再次登录的连接网络的方法
     */
    public void reLogin(String url, String data) {
        HttpUtil.sendHttpRequest(url, "POST", data, new HttpCallbackListener() {
            @Override
            public void onFinish(final String response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        UserInfoMsg UserInfo = JSON.parseObject(response, UserInfoMsg.class);
                        int userId = UserInfo.getUser().getUserId();
                        Toast.makeText(ChangeUserActivity.this, userId + "" + "已登陆", Toast.LENGTH_LONG).show();
                        ActivityCollector.finishAll();
                        MenuActivity.actionStart(ChangeUserActivity.this, userId);
                    }
                });
            }


            @Override
            public void onError(Exception e) {
                LogUtil.e(TAG, e.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ChangeUserActivity.this, R.string.http_fail, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    /**
     * list中是还有账号，返回true，进行操作。若为false，跳转至Login
     */
    private Boolean checkOtherUser(String account) {
        boolean flag = false;
        deleteOldUser(account);
        String sql = "select * from user";
        Cursor c = db.rawQuery(sql, null);
        while (c.moveToNext()) {
            flag = true;
        }
        c.close();
        return flag;
    }

    /**
     * 退出当前且删除掉用户，再获取数据库里第1条数据登录
     */
    private void loginNext(String account) {

        //deleteOldUser(account);

        String sql = "select * from user";
        Cursor c = db.rawQuery(sql, null);


        while (c.moveToFirst()) {
                String account1 = c.getString(c.getColumnIndex("account"));
                String pwd = c.getString(c.getColumnIndex("pwd"));
                String url = IConst.SERVLET_ADDR + "LoginServlet";
                System.out.println("url = " + url);
                String data = "username=" + account1 + "&" + "password=" + pwd;
                reLogin(url, data);
            break;
        }
        c.close();
    }

    /**
     * 显示list
     */
    public void showUser(ListView lv) {
        String sql = "select * from user";
        Cursor c = db.rawQuery(sql, null);
        //List<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        List<HashMap<String, String>> data = new ArrayList<>();

        while (c.moveToNext()) {
            String account = c.getString(c.getColumnIndex("account"));
            String pwd = c.getString(c.getColumnIndex("pwd"));
            //HashMap<String, String> u = new HashMap<String, String>();
            HashMap<String, String> u = new HashMap<>();
            u.put("account", account);
            u.put("pwd", pwd);
            data.add(u);
        }
        SimpleAdapter adapter = new SimpleAdapter(ChangeUserActivity.this,
                data, R.layout.item_user,
                new String[]{"account"},
                new int[]{R.id.txt_search_username});
        lv.setAdapter(adapter);
        c.close();
    }

    /**
     * 删除上一个的登录账号
     */
    public void deleteOldUser(String account) {
        String delete = "delete from user where account=?";
        db.execSQL(delete, new Object[]{account});

    }

    /**
     * 点击按钮结束当前界面
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

    /**
     * 登录对接
     */
    public static void actionStart(Context context, int userId) {
        Intent i = new Intent(context, ChangeUserActivity.class);
        i.putExtra("UserId", userId);
        context.startActivity(i);
    }

    @Override
    public void finish() {
        super.finish();
    }
}
