package com.example.json.foogt.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
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
import com.example.json.foogt.entity.User;
import com.example.json.foogt.util.HttpCallbackListener;
import com.example.json.foogt.util.HttpUtil;
import com.example.json.foogt.util.IConst;
import com.example.json.foogt.util.Utility;

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
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setDisplayShowHomeEnabled(true);
            bar.setHomeButtonEnabled(true);
            bar.setTitle(R.string.changeIntroduction);
        }

        //Intent i = getIntent();
        //int userId = i.getExtras().getInt("UserId");
        getUsername(12);

        // TODO: 2015/12/24 按钮的方法未完成
        savebtn.setOnClickListener( new onSaveButtonClickListener());

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
                                if(introTxt.getText().toString().length()  > 140){
                                    savebtn.setEnabled(false);
                                    System.out.println("introTxt.getText().toString().length() = " + introTxt.getText().toString().length());
                                    Toast.makeText(DataEditActivity.this,"超过140个字",Toast.LENGTH_LONG).show();
                                }
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
    public class onSaveButtonClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            String url = IConst.SERVLET_ADDR + "DataEditServlet";
            // TODO: 2015/12/28 userId 使用传递来的值
            String data = "userId="+12+"&"+"userName="+nicknameTxt.getText().toString()+"&"+"userIntro="+introTxt.getText().toString();
            HttpUtil.sendHttpRequest(url, "POST", data, new HttpCallbackListener() {
                @Override
                public void onFinish(final String response) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(DataEditActivity.this, response+"修改成功", Toast.LENGTH_LONG).show();
                        }
                    });
                    MenuActivity.actionStart(DataEditActivity.this);
                }

                @Override
                public void onError(Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(DataEditActivity.this, R.string.network_is_bad, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
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

    // TODO: 2015/12/24 等待对接
    public static void actionStart(Context context, int userId) {
        Intent i = new Intent(context, DataEditActivity.class);
        i.putExtra("UserId", userId);
        context.startActivity(i);
    }

    /*
    刚刚跳转时从服务器得到数据
     */
    public void getUsername(int userId) {
        String url = IConst.SERVLET_ADDR + "SearchUserDataServlet";
        String data = "userId=" + userId;
        HttpUtil.sendHttpRequest(url, "POST", data, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                User user = Utility.handleUserDataResultResponse(response);
                final String nick = user.getUsername();
                final String intro = user.getUserIntro();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        nicknameTxt.setText(nick);
                        introTxt.setText(intro);
                    }
                });

            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(DataEditActivity.this, R.string.network_is_bad, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}
