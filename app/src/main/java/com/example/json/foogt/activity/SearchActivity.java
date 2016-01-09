package com.example.json.foogt.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.json.foogt.R;
import com.example.json.foogt.util.HttpCallbackListener;
import com.example.json.foogt.util.HttpUtil;
import com.example.json.foogt.util.IConst;
import com.example.json.foogt.util.LogUtil;
import com.example.json.foogt.util.Utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    private List<Map<String, String>> list;
    private RequestQueue mQueue;
    private int userId;

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
        View root = findViewById(R.id.layout_root);

        bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setDisplayShowHomeEnabled(true);
            bar.setHomeButtonEnabled(true);
            bar.setTitle(R.string.search);
        }

        searchUserBtn.setOnClickListener(new onSearchButtonClickListener());

        list = new ArrayList<>();
        adapter = new SimpleAdapter(SearchActivity.this,
                list,
                R.layout.item_user,
                new String[]{"username"},
                new int[]{R.id.txt_search_username});

        resultLv.setAdapter(adapter);

        userId = getIntent().getIntExtra("userId", -1);
        resultLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               int friendId = Integer.parseInt(list.get(position).get("userId"));
                PeopleActivity.actionStart(SearchActivity.this,userId,friendId);
            }
        });
        resultLv.setOnItemLongClickListener(new FocusUserListener());
        mQueue = Volley.newRequestQueue(SearchActivity.this);

        if (PreferenceManager.getDefaultSharedPreferences(SearchActivity.this).getBoolean("SnackBar_first", true)) {
            Snackbar.make(root, R.string.long_press_to_focus, Snackbar.LENGTH_LONG).setAction(R.string.do_not_show_again, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences.Editor sf = PreferenceManager.getDefaultSharedPreferences(SearchActivity.this).edit();
                    sf.putBoolean("SnackBar_first", false);
                    sf.apply();
                }
            }).show();
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

    public static void actionStart(Context context, int userId) {
        Intent i = new Intent(context, SearchActivity.class);
        i.putExtra("userId", userId);
        context.startActivity(i);
    }

    private class onSearchButtonClickListener implements View.OnClickListener {

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
            HttpUtil.sendHttpRequest(url, "POST", data, new HttpCallbackListener() {
                @Override
                public void onFinish(final String response) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();

                            List<Map<String, String>> data = Utility.handleSearchUserRequestResponse(response);
                            if (data.size() > 0) {
                                list.clear();
                                list.addAll(data);
                                adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(SearchActivity.this, R.string.no_this_user, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

                @Override
                public void onError(Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            Toast.makeText(SearchActivity.this, R.string.network_is_bad, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        }

    }

    private class FocusUserListener implements AdapterView.OnItemLongClickListener {

        @Override
        public boolean onItemLongClick(final AdapterView<?> parent, View view, int position, long id) {
            int focusUserId = Integer.parseInt(list.get(position).get("userId"));
            String url = IConst.SERVLET_ADDR + "Focus?currentUserId=" + userId + "&focusUserId=" + focusUserId;
            StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    boolean result = Utility.handleBooleanResultResponse(response);
                    if (result) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Snackbar.make(parent, R.string.focus_user_success, Snackbar.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    LogUtil.e("SearchActivity", error.toString());
                }
            });
            mQueue.add(stringRequest);
            return true;
        }
    }
}
