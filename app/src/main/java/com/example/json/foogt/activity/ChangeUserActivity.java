package com.example.json.foogt.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.json.foogt.R;

public class ChangeUserActivity extends AppCompatActivity {
    private ActionBar bar;
    private ListView userlv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user);
        userlv = (ListView)findViewById(R.id.lv_changeUser_user);


        bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setDisplayShowHomeEnabled(true);
        bar.setHomeButtonEnabled(true);
        bar.setTitle(R.string.account_management);
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
    public static void actionStart(Context context) {
        Intent i = new Intent(context, ChangeUserActivity.class);
        context.startActivity(i);
    }
}
