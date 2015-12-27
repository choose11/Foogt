package com.example.json.foogt.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.json.foogt.R;

public class RegisterActivity extends AppCompatActivity {

    EditText usernameEdit;
    EditText passwordEdit;
    Button registerBtn;

    private ActionBar bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setDisplayShowHomeEnabled(true);
        bar.setHomeButtonEnabled(true);
        bar.setTitle("注册");

        usernameEdit = (EditText) findViewById(R.id.edit_register_username);
        passwordEdit = (EditText) findViewById(R.id.edit_register_pwd);
        registerBtn = (Button) findViewById(R.id.register_button);

        registerBtn.setOnClickListener(new onRegisterButtonClickListener());
        passwordEdit.setOnFocusChangeListener(new onPasswordFocusChangeListener());
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

    private class onRegisterButtonClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {

        }
    }

    // TODO: 2015/12/24 焦点方法检测
    
    private class onPasswordFocusChangeListener implements  View.OnFocusChangeListener{

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            
        }
    }

    // TODO: 2015/12/24 等待登录界面完成对接 
    public static void actionStart(Context context) {
        Intent i = new Intent(context, RegisterActivity.class);
        context.startActivity(i);
    }
}
