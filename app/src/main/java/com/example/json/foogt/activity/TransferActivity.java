package com.example.json.foogt.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.json.foogt.R;
import com.example.json.foogt.entity.BlogInfo;
import com.example.json.foogt.util.HttpCallbackListener;
import com.example.json.foogt.util.HttpUtil;
import com.example.json.foogt.util.IConst;
import com.example.json.foogt.util.LogUtil;
import com.example.json.foogt.util.Utility;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TransferActivity extends AppCompatActivity {

    private BlogInfo msg;
    private int userId;
    private Button repost;
    private EditText comment;
    private String content;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String re= msg.obj.toString();
            System.out.println(re);
            if (re.equals("s")){
                Toast.makeText(TransferActivity.this,"转发微博成功",Toast.LENGTH_LONG).show();
                finish();
            }else if(re.equals("f")){
                Toast.makeText(TransferActivity.this,"转发微博失败",Toast.LENGTH_LONG).show();
                finish();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        Intent i = getIntent();
        String msgJson = i.getStringExtra("blogInfo");
        msg = JSON.parseObject(msgJson, BlogInfo.class);
        userId = i.getIntExtra("userId", -1);
        repost = (Button) findViewById(R.id.btn_repost1);
        comment = (EditText) findViewById(R.id.edit_repost);

        TextView blogUserName = (TextView) findViewById(R.id.txt_blog_name);
        TextView blogPostTime = (TextView) findViewById(R.id.txt_blog_time);
        TextView blogMsg = (TextView) findViewById(R.id.txt_blog_msg);

        blogUserName.setText(msg.getUsername());
        blogPostTime.setText(new SimpleDateFormat("MM.dd hh:mm").format(msg.getPostTime()));
        blogMsg.setText(msg.getMsg());

        repost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content =comment.getText().toString()+"\n"+"@"+msg.getUsername()+":"+msg.getMsg();
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
                String timeT=sdf.format(new Date());
                String url=IConst.SERVLET_ADDR+"MsgInfo";
                String data="userId="+userId+"&content="+content+"&type=2&commentCount=0&transferCount=0&timeT="+timeT;
                HttpUtil.sendHttpRequest(url, "POST", data, new HttpCallbackListener() {
                    @Override
                    public void onFinish(String response) {
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


    public static void actionStart(Context context, BlogInfo msg, int userId) {
        Intent i = new Intent(context, TransferActivity.class);
        i.putExtra("blogInfo", JSON.toJSON(msg).toString());
        i.putExtra("userId",userId);
        context.startActivity(i);
    }






}
