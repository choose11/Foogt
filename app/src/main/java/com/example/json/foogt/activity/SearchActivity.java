package com.example.json.foogt.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.json.foogt.R;
import com.example.json.foogt.entity.User;
import com.example.json.foogt.util.HttpCallbackListener;
import com.example.json.foogt.util.HttpUtil;
import com.example.json.foogt.util.IConst;
import com.example.json.foogt.util.Utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    private ImageButton searchUserBtn;
    private EditText inEdit;
    private ListView resultLv;
    private SimpleAdapter adapter;
    private ActionBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchUserBtn = (ImageButton) findViewById(R.id.ibtn_search_search);
        inEdit = (EditText) findViewById(R.id.edit_search_in);
        resultLv = (ListView) findViewById(R.id.lv_search_result);

        bar = getSupportActionBar();

        if(bar!=null){
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setDisplayShowHomeEnabled(true);
            bar.setHomeButtonEnabled(true);
            bar.setTitle(R.string.search);
        }

        searchUserBtn.setOnClickListener(new onSearchButtonClickListener());
    }

    public class onSearchButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
             /*
                progressDialog
                 */
            final ProgressDialog progressDialog = new ProgressDialog(SearchActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
                /*
                servlet
                 */
            String url = IConst.SERVLET_ADDR + "SearchUserServlet";
            String data = "keyword=" + inEdit.getText().toString();
            System.out.println("data = " + data);
            HttpUtil.sendHttpRequest(url, "POST", data, new HttpCallbackListener() {
                @Override
                public void onFinish(final String response) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            List<Map<String, String>> data = Utility.handleSearchUserRequestResponse(response);
                           /*
                           输出测试
                            */
                            for (Map<String, String> m : data
                                    ) {
                                System.out.println("m = " + m);
                            }
                            if (data.size() > 0) {
                                adapter = new SimpleAdapter(SearchActivity.this,
                                        data,
                                        R.layout.item_user,
                                        new String[]{"username"},
                                        new int[]{R.id.txt_search_username});
                                // TODO: 2015/12/28 微博搜索的内容未填充满
                                resultLv.setAdapter(adapter);
                                resultLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Toast.makeText(SearchActivity.this, "测试", Toast.LENGTH_LONG).show();
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(SearchActivity.this, R.string.no_this_user, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });


                }

                @Override
                public void onError(Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SearchActivity.this, R.string.network_is_bad, Toast.LENGTH_LONG).show();
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

    public static void actionStart(Context context) {
        Intent i = new Intent(context, SearchActivity.class);
        context.startActivity(i);
    }
}
