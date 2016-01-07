package com.example.json.foogt.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
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
import com.example.json.foogt.util.IConst;
import com.example.json.foogt.util.LogUtil;
import com.example.json.foogt.util.Utility;

import java.text.SimpleDateFormat;

public class CommentBlogActivity extends AppCompatActivity implements Response.ErrorListener {
    private static final String TAG = "CommentBlogActivity";

    private BlogInfo msg;
    private int userId;
    private Button submit;
    private EditText comment;
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_blog);
        Intent i = getIntent();
        String msgJson = i.getStringExtra("blogInfo");
        msg = JSON.parseObject(msgJson, BlogInfo.class);
        userId = i.getIntExtra("userId", -1);

        submit = (Button) findViewById(R.id.btn_submit);
        comment = (EditText) findViewById(R.id.edit_comment);

        TextView blogUserName = (TextView) findViewById(R.id.txt_blog_name);
        TextView blogPostTime = (TextView) findViewById(R.id.txt_blog_time);
        TextView blogMsg = (TextView) findViewById(R.id.txt_blog_msg);
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
            bar.setTitle(R.string.comment);
        }

        blogUserName.setText(msg.getUsername());
        blogPostTime.setText(new SimpleDateFormat("MM.dd kk:mm").format(msg.getPostTime()));
        blogMsg.setText(msg.getMsg());

        submit.setOnClickListener(new OnSubmitClickListener());
        mQueue = Volley.newRequestQueue(this);
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

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getApplicationContext(), R.string.http_fail, Toast.LENGTH_SHORT).show();
    }

    public static void actionStart(Context context, BlogInfo msg, int userId) {
        Intent i = new Intent(context, CommentBlogActivity.class);
        i.putExtra("blogInfo", JSON.toJSON(msg).toString());
        i.putExtra("userId", userId);
        context.startActivity(i);
    }

    private class OnSubmitClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO: 2016/1/4 msg length
            String comm = comment.getText().toString();
            if (TextUtils.isEmpty(comm)) {
                return;
            }
            String url = IConst.SERVLET_ADDR + "Comment?userId=" + userId + "&msgAuthorId=" + msg.getAuthorId() + "&msgId=" + msg.getMsgId() + "&comment=" + comm;
            StringRequest stringRequest = new StringRequest(url, new OnCommentListener(), CommentBlogActivity.this);
            LogUtil.d(TAG, url);
            mQueue.add(stringRequest);
        }
    }

    private class OnCommentListener implements Response.Listener<String> {
        @Override
        public void onResponse(String response) {
            if (Utility.handleBooleanResultResponse(response)) {
                Toast.makeText(getApplicationContext(), R.string.comment_success, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), R.string.comment_failed, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
