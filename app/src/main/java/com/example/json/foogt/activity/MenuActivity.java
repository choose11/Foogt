package com.example.json.foogt.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.json.foogt.R;
import com.example.json.foogt.fragment.CommentFragment;
import com.example.json.foogt.fragment.HomeFragment;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ImageView dataEditImg;
    private TextView fansTxt, focusTxt;
    private int userId;
    static final int NUM_ITEMS = 2;
    CollectionPagerAdapter mPagerAdapter;
    ViewPager mViewPager;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        userId = getIntent().getIntExtra("userId", -1);

        //dataEditImg = (ImageView) findViewById(R.id.img_user_data_edit);
        //fansTxt = (TextView) findViewById(R.id.txt_user_fans);
        // focusTxt=(TextView)findViewById(R.id.txt_user_focus);

        /*
        Toolbar最上层的一栏
         */
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.home);
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(onMenuItemClick);
        /*
        pageAdapter适配器
         */
        mPagerAdapter = new CollectionPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                change(position);
            }
        });


        /*
        圆形按钮
         */
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                SendBlogActivity.actionStart(MenuActivity.this,userId);
            }
        });
        /*
        toggle切换键
         */
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        /*
        侧面的菜单的详细的。
         */
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // TODO: 2015/12/25 修改个人信息，粉丝，关注，跳转失败。
        // headerLayout linearLayoutUser = (headerLayout)drawer.findViewById(R.id.linear_user);
        //dataEditImg = (ImageView) linearLayoutUser.findViewById(R.id.img_user_data_edit);


    }

    /*
    点击toolbar改变title
     */
    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

            switch (item.getItemId()) {
                case R.id.action_home:
                    toolbar.setTitle(R.string.home);

                    break;
                case R.id.action_comment:
                    toolbar.setTitle(R.string.comment);

                    break;
            }
            return true;
        }
    };


    public void change(int position) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        switch (position) {
            case 0:
                toolbar.setTitle(R.string.home);
                break;
            case 1:
                toolbar.setTitle(R.string.comment);
                break;
        }
    }

    /*
  自定义page
   */
    public class CollectionPagerAdapter extends FragmentPagerAdapter {

        public CollectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = HomeFragment.newInstance(userId);
                    break;
                case 1:
                    fragment = CommentFragment.newInstance();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
        //return super.onCreateOptionsMenu(menu);
    }

    /*
        返回键
         */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewPager.clearOnPageChangeListeners();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_search) {
            SearchActivity.actionStart(MenuActivity.this);
        } else if (id == R.id.nav_collection) {
            CollectionActivity.actionStart(MenuActivity.this);
        } else if (id == R.id.nav_send) {
            SendBlogActivity.actionStart(MenuActivity.this,userId);
        } else if (id == R.id.nav_manage) {
            ChangeUserActivity.actionStart(MenuActivity.this);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /*
    登录后对接
     */

    public static void actionStart(Context context, int userId) {
        Intent i = new Intent(context, MenuActivity.class);
        i.putExtra("userId", userId);
        System.out.println(userId);
        context.startActivity(i);
    }


}
