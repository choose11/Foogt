package com.example.json.foogt.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SyncStatusObserver;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.json.foogt.R;
import com.example.json.foogt.util.HttpCallbackListener;
import com.example.json.foogt.util.HttpUtil;
import com.example.json.foogt.util.IConst;
import com.example.json.foogt.util.LogUtil;

import java.sql.Date;
import java.sql.SQLOutput;
import java.text.SimpleDateFormat;

public class SendBlogActivity extends AppCompatActivity {
    private Button send_btn;
    private EditText contentEdit;
    private ImageView addImg;
    private int userId;
    private String content, timeT;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String re = msg.obj.toString();
            if (re.equals("s")) {
                Toast.makeText(SendBlogActivity.this, "发送微博成功", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(SendBlogActivity.this, "发送微博失败", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_blog);
        contentEdit = (EditText) findViewById(R.id.edit_send_content);
        addImg = (ImageView) findViewById(R.id.img_picture_add);
        send_btn = (Button) findViewById(R.id.send);
        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            // TODO: 2016/1/6 listener
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setDisplayShowHomeEnabled(true);
            bar.setHomeButtonEnabled(true);
            bar.setTitle(R.string.send_blog);
        }
        userId = getIntent().getIntExtra("userId", -1);
        System.out.println(userId);

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2016/1/4 msg length
                content = contentEdit.getText().toString();
                System.out.println(content);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
                timeT = sdf.format(new java.util.Date());
                System.out.println(timeT);

                String url = IConst.SERVLET_ADDR + "MsgInfo";
                String data = "userId=" + userId + "&content=" + content + "&type=0&commentCount=0&transferCount=0&timeT=" + timeT;
                System.out.println(data);
                HttpUtil.sendHttpRequest(url, "POST", data, new HttpCallbackListener() {
                    @Override
                    public void onFinish(String response) {
                        LogUtil.d("SendBlog", response);
                        Message msg = new Message();
                        msg.obj = response;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
            }
        });
    }

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

    public static void actionStart(Context context, int userId) {
        Intent i = new Intent(context, SendBlogActivity.class);
        i.putExtra("userId", userId);
        context.startActivity(i);
    }
}
