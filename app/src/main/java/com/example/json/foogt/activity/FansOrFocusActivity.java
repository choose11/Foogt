package com.example.json.foogt.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.json.foogt.R;

public class FansOrFocusActivity extends AppCompatActivity {
    private ListView showFansOrFocusLv;
    private ActionBar bar;
    private String type="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fans_or_focus);
        showFansOrFocusLv = (ListView) findViewById(R.id.lv_fansOrFocus_show);


        bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setDisplayShowHomeEnabled(true);
            bar.setHomeButtonEnabled(true);

        }

//        Intent i = getIntent();

        if(getIntent().getStringExtra("Type").equals("粉丝")){
            bar.setTitle(R.string.fans);
        }else{
            bar.setTitle(R.string.focus);
        }
    }

    /**
     * 登录后对接
     */

    public static void actionStart(Context context, int userId,String type) {
        Intent i = new Intent(context, FansOrFocusActivity.class);
        i.putExtra("UserId", userId);
        i.putExtra("Type",type);
        context.startActivity(i);
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
}
