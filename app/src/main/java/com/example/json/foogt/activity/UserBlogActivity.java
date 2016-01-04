package com.example.json.foogt.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
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
import com.example.json.foogt.entity.BlogInfo;
import com.example.json.foogt.util.IConst;
import com.example.json.foogt.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class UserBlogActivity extends AppCompatActivity {

    private static final String TAG = "MyBlog";
    private int userId;
    private ArrayList<BlogInfo> list;
    private RecyclerView rv;
    private SwipeRefreshLayout sw;
    private MBlogAdapter adapter;
    private RequestQueue mQueue;
    private int currentPage;
    private int lastVisibleItem;
    private LinearLayoutManager layoutManager;
    private boolean havaMoreBlogs;
    private ActionBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_blog);

        bar = getSupportActionBar();

        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setDisplayShowHomeEnabled(true);
            bar.setHomeButtonEnabled(true);
            bar.setTitle(R.string.user_own_blog);
        }

        userId = getIntent().getIntExtra("userId", -1);
        LogUtil.i("userId", userId + "");
        //userId = 1;

        mQueue = Volley.newRequestQueue(this);
        havaMoreBlogs = true;

        rv = (RecyclerView) findViewById(R.id.rv_User_msg);
        sw = (SwipeRefreshLayout) findViewById(R.id.layout_userBlog_swipe);

        layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);
        list = new ArrayList<>();
        adapter = new MBlogAdapter(list, null);
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
            havaMoreBlogs = true;
        }
        if (!havaMoreBlogs) {
            return;
        }
        if (!sw.isRefreshing()) {
            sw.setRefreshing(true);
        }

        String url = IConst.SERVLET_ADDR + "GetUserOwnBlogs?userId=" + userId + "&page=" + page;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                List<BlogInfo> results = JSON.parseObject(response, new TypeReference<List<BlogInfo>>() {
                });
                LogUtil.i(TAG, results.size() + "");
                if (results.size() > 0) {
                    currentPage = page;
                    for (BlogInfo b : results) {
                        list.add(b);
                    }
                } else {
                    havaMoreBlogs = false;
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
                        Toast.makeText(UserBlogActivity.this, R.string.http_fail, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        mQueue.add(stringRequest);
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

    /**
     * 登录后对接
     */

    public static void actionStart(Context context, int userId) {
        Intent i = new Intent(context, UserBlogActivity.class);
        i.putExtra("userId", userId);
        context.startActivity(i);
    }
}
