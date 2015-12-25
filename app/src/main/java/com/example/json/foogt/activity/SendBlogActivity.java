package com.example.json.foogt.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.json.foogt.R;

public class SendBlogActivity extends AppCompatActivity {

    private EditText contentEdit;
    private ImageView addImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_blog);
        contentEdit = (EditText)findViewById(R.id.edit_send_content);
        addImg = (ImageView)findViewById(R.id.img_picture_add);

    }

    public static void actionStart(Context context) {
        Intent i = new Intent(context, SendBlogActivity.class);
        context.startActivity(i);
    }
}
