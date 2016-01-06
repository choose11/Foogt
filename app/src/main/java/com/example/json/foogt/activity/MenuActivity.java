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
import android.support.v4.app.FragmentTransaction;
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
import android.widget.Toast;

import com.example.json.foogt.ActivityCollector.ActivityCollector;
import com.example.json.foogt.R;
import com.example.json.foogt.entity.User;
import com.example.json.foogt.fragment.CommentFragment;
import com.example.json.foogt.fragment.HomeFragment;
import com.example.json.foogt.util.HttpCallbackListener;
import com.example.json.foogt.util.HttpUtil;
import com.example.json.foogt.util.IConst;
import com.example.json.foogt.util.LogUtil;
import com.example.json.foogt.util.Utility;

import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.ManagerFactoryParameters;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ImageView dataEditImg;
    private int userId;

    private TextView fansTxt, focusTxt, countMsgTxt, userNameTxt, UserIntroMin;

    CollectionPagerAdapter mPagerAdapter;
    ViewPager mViewPager;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        ActivityCollector.addActivity(this);

        userId = getIntent().getIntExtra("userId", -1);
        //userId=1;

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
        mViewPager = (ViewPager) findViewById(R.id.pager);
        if (mViewPager != null) {
            setupViewpager(mViewPager);
        }

        /*
        圆形按钮
         */
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                SendBlogActivity.actionStart(MenuActivity.this, userId);
            }
        });
        /*
        toggle切换键
         */
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {

            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                getUserData(userId);//从服务器拿到原始数据,做一次刷新
            }
        };

        drawer.setDrawerListener(toggle);
        toggle.syncState();//左上角按钮

        /*
        侧面的菜单的详细的。
         */
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // TODO: 2015/12/25 ，粉丝，关注，未完成。
        /*
        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_menu);
        添加导航试图，有这行就在xml文件中去掉app:headerLayout="@layout/nav_header_menu"，
        否则将出现两个R.layout.nav_header_menu!!!!!
         */
        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_menu);
        dataEditImg = (ImageView) headerView.findViewById(R.id.img_user_data_edit);
        fansTxt = (TextView) headerView.findViewById(R.id.txt_user_fans);
        focusTxt = (TextView) headerView.findViewById(R.id.txt_user_focus);
        countMsgTxt = (TextView) headerView.findViewById(R.id.txt_user_count);
        userNameTxt = (TextView) headerView.findViewById(R.id.txt_menu_userName);
        UserIntroMin = (TextView) headerView.findViewById(R.id.txt_menu_userIntro);


        OnDrawerItemClickListener listener = new OnDrawerItemClickListener();
        dataEditImg.setOnClickListener(listener);
        fansTxt.setOnClickListener(listener);
        focusTxt.setOnClickListener(listener);
        countMsgTxt.setOnClickListener(listener);


    }

    private void setupViewpager(ViewPager viewPager) {
        mPagerAdapter = new CollectionPagerAdapter(getSupportFragmentManager());
        mPagerAdapter.addFragment(HomeFragment.newInstance(userId,HomeFragment.HOME), getResources().getString(R.string.home));
        mPagerAdapter.addFragment(CommentFragment.newInstance(), getResources().getString(R.string.comment));
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                change(position);
            }
        });
        viewPager.setAdapter(mPagerAdapter);
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
                    // TODO: 2015/12/29 未实现点击跳转

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
        toolbar.setTitle(mPagerAdapter.getPageTitle(position));
    }

    /*
  自定义page
   */
    public class CollectionPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public CollectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
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

    /**
     * 跳转至各个界面
     *
     * @param item selected NavigationItem
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_search) {
            SearchActivity.actionStart(MenuActivity.this, userId);
        } else if (id == R.id.nav_collection) {
            CollectionActivity.actionStart(MenuActivity.this, userId);
        } else if (id == R.id.nav_send) {
            SendBlogActivity.actionStart(MenuActivity.this, userId);
        } else if (id == R.id.nav_manage) {
            ChangeUserActivity.actionStart(MenuActivity.this, userId);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * 登录后对接
     */

    public static void actionStart(Context context, int userId) {
        Intent i = new Intent(context, MenuActivity.class);
        i.putExtra("userId", userId);
        System.out.println(userId);
        context.startActivity(i);
    }


    public class OnDrawerItemClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                /**
                 * 修改资料页面跳转
                 */
                case R.id.img_user_data_edit:
                    DataEditActivity.actionStart(MenuActivity.this, userId);
                    /**
                     * 重新获取到drawer 在跳转之后关闭侧面菜单，详见onNavigationItemSelected
                     */
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                    break;
                /**
                 * 点击进入自己的微博
                 *
                 */
                case R.id.txt_user_count:
                    UserBlogActivity.actionStart(MenuActivity.this,userId);
                    LogUtil.i("hehehe", "sdfsdfsfas");
                    break;
                /**
                 * 点击进入粉丝界面
                 */
                case R.id.txt_user_fans:
                    FansOrFocusActivity.actionStart(MenuActivity.this, userId, "粉丝");
                    LogUtil.i("fensi", "sdfsdfsfas");
                    break;
                /**
                 * 点击进入关注界面
                 */
                case R.id.txt_user_focus:
                    FansOrFocusActivity.actionStart(MenuActivity.this, userId, "关注");
                    LogUtil.i("guanzhu", "sdfsdfsfas");
                    break;

            }

        }
    }

    /*
    刚刚跳转时从服务器得到数据
     */
    public void getUserData(int userId) {
        String url = IConst.SERVLET_ADDR + "SearchUserDataServlet";
        String data = "userId=" + userId;
        HttpUtil.sendHttpRequest(url, "POST", data, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                User user = Utility.handleUserInfoResultResponse(response);
                final String name = user.getUsername();
                final String intro = user.getUserIntro();
                final int msgCount = user.getMsgCount();
                final int fansCount = user.getFansCount();
                final int focusCount = user.getFocusCount();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (fansCount > 10000) {
                            fansTxt.setText("粉丝:" + " " + (fansCount / 10000) + "万");
                        } else {
                            fansTxt.setText("粉丝:" + " " + fansCount);
                        }
                        if (focusCount > 10000) {
                            focusTxt.setText("关注:" + " " + (focusCount / 10000) + "万");
                        } else {
                            focusTxt.setText("关注:" + " " + focusCount);
                        }
                        if (msgCount > 10000) {
                            countMsgTxt.setText("微博:" + " " + (msgCount / 10000) + "万");
                        } else {
                            countMsgTxt.setText("微博:" + " " + msgCount);
                        }
                        userNameTxt.setText(name);
                        if (intro.length() > 40) {
                            UserIntroMin.setText(intro.substring(0, 36) + "...");
                        } else {
                            UserIntroMin.setText(intro);
                        }
                    }
                });

            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MenuActivity.this, R.string.network_is_bad, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}
