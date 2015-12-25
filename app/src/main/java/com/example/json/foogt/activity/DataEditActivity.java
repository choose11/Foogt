package com.example.json.foogt.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.json.foogt.R;

public class DataEditActivity extends AppCompatActivity {

    private ActionBar bar;
    private Button savebtn;
    private TextView nicknameTxt, introTxt;
    private EditText nicknameEdit, introEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_edit);
        savebtn = (Button) findViewById(R.id.btn_data_edit_save);
        nicknameTxt = (TextView) findViewById(R.id.txt_data_edit_nickname);
        introTxt = (TextView) findViewById(R.id.txt_data_edit_intro);

        bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setDisplayShowHomeEnabled(true);
        bar.setHomeButtonEnabled(true);
        bar.setTitle(R.string.changeIntroduction);

        // Intent i = new Intent();
        // nicknameTxt.setText(i.getStringExtra("nickname"));
        // introTxt.setText(i.getStringExtra("introduction"));

        // TODO: 2015/12/24 按钮的方法未完成
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        nicknameTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LinearLayout nickname = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_nickname, null);
                new AlertDialog.Builder(DataEditActivity.this)
                        .setTitle("编辑昵称")
                        .setView(nickname)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                nicknameEdit = (EditText) nickname.findViewById(R.id.edit_nickname);
                                String name = nicknameEdit.getText().toString();
                                System.out.println("name = " + name);
                                Toast.makeText(getApplicationContext(), name, Toast.LENGTH_LONG).show();
                                nicknameTxt.setText(name);

                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                                //显示对话框
                        .create().show();
            }
        });

        introTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LinearLayout introduction = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_introduction, null);
                new AlertDialog.Builder(DataEditActivity.this)
                        .setTitle("简介")
                        .setView(introduction)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                introEdit = (EditText) introduction.findViewById(R.id.edit_introduction);
                                introTxt.setText(introEdit.getText().toString());
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                                //显示对话框
                        .create().show();
            }
        });
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

    // TODO: 2015/12/24 等待对接
    public static void actionStart(Context context) {
        Intent i = new Intent(context, DataEditActivity.class);
        context.startActivity(i);
    }

}
