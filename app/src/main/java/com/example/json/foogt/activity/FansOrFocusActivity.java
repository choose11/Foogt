package com.example.json.foogt.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.json.foogt.R;
import com.example.json.foogt.adapter.MBlogAdapter;
import com.example.json.foogt.adapter.MFriendAdapter;
import com.example.json.foogt.entity.BlogInfo;
import com.example.json.foogt.entity.User;
import com.example.json.foogt.util.IConst;
import com.example.json.foogt.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class FansOrFocusActivity extends AppCompatActivity {

    private ActionBar bar;
    private int userId;
    private String type = "";
    private ArrayList<User> list;
    private RecyclerView rv;
    private SwipeRefreshLayout sw;
    private MFriendAdapter adapter;
    private RequestQueue mQueue;
    private int currentPage;
    private int lastVisibleItem;
    private LinearLayoutManager layoutManager;
    private boolean havaMoreFriends;
    private String TAG = "FansOrFocusActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fans_or_focus);

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);

        bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setDisplayShowHomeEnabled(true);
            bar.setHomeButtonEnabled(true);

        }

        userId = getIntent().getIntExtra("UserId", -1);

        if (getIntent().getStringExtra("Type").equals("粉丝")) {
            bar.setTitle(R.string.fans);
        } else {
            bar.setTitle(R.string.focus);
        }

        mQueue = Volley.newRequestQueue(this);
        havaMoreFriends = true;

        rv = (RecyclerView) findViewById(R.id.rv_fansOrFocus_intro);
        sw = (SwipeRefreshLayout) findViewById(R.id.layout_userFansOrFocus_swipe);

        layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);
        list = new ArrayList<>();
        adapter = new MFriendAdapter(list);
        rv.setAdapter(adapter);


        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                    if (lastVisibleItem + 1 == adapter.getItemCount()) {
                        load(currentPage + 1);
                    }
                }
            }
        });
        sw.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                load(0);
            }
        });
        currentPage = 0;
        load(currentPage);
    }


    /**
     * method  --  load();
     */
    public void load(final int page) {
        if (page == 0) {
            list.clear();
            havaMoreFriends = true;
        }
        if (!havaMoreFriends) {
            return;
        }
        if (!sw.isRefreshing()) {
            sw.setRefreshing(true);
        }

        String url;
        if (getIntent().getStringExtra("Type").equals("粉丝")) {
            url = IConst.SERVLET_ADDR + "GetFans?userId=" + userId + "&page=" + page;
        } else {
            url = IConst.SERVLET_ADDR + "GetFocus?userId=" + userId + "&page=" + page;
        }
        LogUtil.d(TAG, url);
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                List<User> results = JSON.parseObject(response, new TypeReference<List<User>>() {
                });
                LogUtil.i(TAG, results.size() + "");
                if (results.size() > 0) {
                    currentPage = page;
                    for (User b : results) {
                        list.add(b);
                    }
                } else {
                    havaMoreFriends = false;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        if (sw.isRefreshing()) {
                            sw.setRefreshing(false);
                        }
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(FansOrFocusActivity.this, R.string.http_fail, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        mQueue.add(stringRequest);
    }


    /**
     * 登录后对接
     */

    public static void actionStart(Context context, int userId, String type) {
        Intent i = new Intent(context, FansOrFocusActivity.class);
        i.putExtra("UserId", userId);
        i.putExtra("Type", type);
        context.startActivity(i);
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
}
