package com.example.json.foogt.activity;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.json.foogt.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class LoginActivity extends AppCompatActivity {
    private Button button,button2;
    private EditText editText,editText2;
    private String account;
    private String password;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String re=msg.obj.toString();
            System.out.println(re);
            if (re.equals("登陆成功")){
                System.out.println("成功啦");
            }else{
                System.out.println("没成功啊亲");
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        button= (Button) findViewById(R.id.button);
        button2= (Button) findViewById(R.id.button2);
        editText= (EditText) findViewById(R.id.editText);
        editText2= (EditText) findViewById(R.id.editText2);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(){
                    @Override
                    public void run() {
                        account=editText.getText().toString();
                        password=editText2.getText().toString();
                        String path="http://10.25.246.100:8080/Foogt/servlet/LoginServlet?username="+account+"&password="+password+"";
                        System.out.println(path);
                        try {
                            URL url=new URL(path);
                            URLConnection conn=url.openConnection();
                            conn.setConnectTimeout(5000);
                            conn.setRequestProperty("User-Agent", "Mozilla/4.0");
                            InputStream is=conn.getInputStream();
                            BufferedReader br=new BufferedReader(new InputStreamReader(is,"utf-8"));
                            String result=br.readLine();
                            Message msg=new Message();
                            msg.obj=result;
                            handler.sendMessage(msg);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                }.start();



            }
        });

    }
}
