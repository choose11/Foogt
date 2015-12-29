package com.example.json.foogt.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.json.foogt.R;

public class ChangeUserActivity extends AppCompatActivity {
    private ActionBar bar;
    private ListView userlv;
    private Button quitBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user);
        userlv = (ListView)findViewById(R.id.lv_changeUser_user);
        quitBtn = (Button)findViewById(R.id.btn_change_user_quit);
        //Intent i = getIntent();
        //int userId = i.getExtras().getInt("UserId");

        quitBtn.setOnClickListener(new onQuitClickListener());

        bar = getSupportActionBar();
        if(bar!=null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setDisplayShowHomeEnabled(true);
            bar.setHomeButtonEnabled(true);
            bar.setTitle(R.string.account_management);
        }


    }
        public class onQuitClickListener implements View.OnClickListener{
            @Override
            public void onClick(View v) {

            }
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
    public static void actionStart(Context context,int userId) {
        Intent i = new Intent(context, ChangeUserActivity.class);
        i.putExtra("UserId",userId);
        context.startActivity(i);
    }
}
