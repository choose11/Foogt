package com.example.json.foogt.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Looper;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.example.json.foogt.R;
import com.example.json.foogt.adapter.MBlogAdapter;
import com.example.json.foogt.entity.BlogInfo;
import com.example.json.foogt.entity.User;
import com.example.json.foogt.fragment.HomeFragment;
import com.example.json.foogt.util.HttpCallbackListener;
import com.example.json.foogt.util.HttpUtil;
import com.example.json.foogt.util.IConst;
import com.example.json.foogt.util.LogUtil;
import com.example.json.foogt.util.Utility;

import java.util.ArrayList;

public class PeopleActivity extends AppCompatActivity {
    private ActionBar bar;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    private DrawerLayout drawerLayout;
    private CoordinatorLayout rootLayout;

    //Need this to set the title of the app bar
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private ViewPager mPager;
    private MBlogAdapter Bdapter;
    private YourPagerAdapter mAdapter;
    private TabLayout mTabLayout;
    private ArrayList<BlogInfo> list;
    private RecyclerView rv;
    private SwipeRefreshLayout sw;
    private TextView fansTxt, focusTxt;

    private int userId,friendId;
    public String peopleIntro;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people);

         userId = getIntent().getIntExtra("userId", -1);
         friendId = getIntent().getIntExtra("FriendId",friendId);
        //friendId=1;

        initToolbar();
        initInstances(friendId);


        rootLayout = (CoordinatorLayout) findViewById(R.id.rootLayout);
        mTabLayout = (TabLayout) findViewById(R.id.tab_people_tab);
        mAdapter = new YourPagerAdapter(getSupportFragmentManager());//// TODO: 2016/1/4 TEST
        mPager = (ViewPager) findViewById(R.id.view_pager_people);
        mPager.setAdapter(mAdapter);
        //Notice how the Tab Layout links with the Pager Adapter
        mTabLayout.setTabsFromPagerAdapter(mAdapter);

        //Notice how The Tab Layout adn View Pager object are linked
        mTabLayout.setupWithViewPager(mPager);
        mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_people);
        setSupportActionBar(toolbar);
    }

    private void initInstances(int friendId) {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        getFriendName(friendId);
       // collapsingToolbarLayout.setTitle("Design Library");//getFriendName已经设置了
        fansTxt = (TextView)findViewById(R.id.txt_people_fans);
        focusTxt = (TextView)findViewById(R.id.txt_people_focus);

        bar = getSupportActionBar();
        if (collapsingToolbarLayout != null && bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setDisplayShowHomeEnabled(true);
            bar.setHomeButtonEnabled(true);

        }

    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    class YourPagerAdapter extends FragmentStatePagerAdapter {

        public YourPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position==0){
                PeopleActivity.MyFragment myFragment = PeopleActivity.MyFragment.newInstance(friendId);
                return myFragment;
            }else {
               HomeFragment myFragment = HomeFragment.newInstance(userId,friendId,2);
                return myFragment;
            }
        }

        @Override
        public int getItemPosition(Object object) {
         return PagerAdapter.POSITION_NONE;

        }

        @Override
        public int getCount() { return 2; }
        @Override
        public CharSequence getPageTitle(int position) {
            System.out.println("position = " + position);
            String title;
            if (position == 0) {
                title= "资料";
            } else {
                title= "微博" ;
            }
            return title;
        }
    }

    /**
     * 资料界面
     */
    public static class MyFragment extends Fragment{

        //public static final java.lang.String ARG_PAGE1 = "arg_page";
        public static final String FriendId ="FriendId";
        public static MyFragment newInstance(int friendId) {
            MyFragment myFragment = new MyFragment();
            Bundle arguments = new Bundle();
            arguments.putInt(FriendId, friendId);
            myFragment.setArguments(arguments);
            return myFragment;
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
           // return super.onCreateView(inflater, container, savedInstanceState);
            Bundle arguments = getArguments();

            int friendId = arguments.getInt(FriendId);
            // TODO: 2016/1/4 recycleView 的用处
            RecyclerView recyclerView = new RecyclerView(getActivity());
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            TextView txtIntro = new TextView(getActivity());
            String txt = getActivity().getIntent().getStringExtra("People");
            if(txt!=null){
                if(txt.equals("Nothing")){
                    txtIntro.setText("他很懒什么都没有留下");
                }else {
                    txtIntro.setText(txt);
                }
            }else{
               // Log.i("People Intro:","txt is null");
            }
            txtIntro.setTextSize(30);
            txtIntro.setPadding(20,15,20,15);

            //return recyclerView;
            return txtIntro;
        }
    }
    /**
     * 微博界面
     */
  /*  public static class BlogFragment extends Fragment {
        public static final java.lang.String ARG_PAGE = "arg_page";
        private ArrayList<BlogInfo> list;
        public BlogFragment() {

        }

        public static BlogFragment newInstance(int pageNumber) {
            BlogFragment blogFragment = new BlogFragment();
            Bundle arguments = new Bundle();
            arguments.putInt(ARG_PAGE, pageNumber + 1);
            blogFragment.setArguments(arguments);
            return blogFragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            Bundle arguments = getArguments();
            int pageNumber = arguments.getInt(ARG_PAGE);
            RecyclerView recyclerView = new RecyclerView(getActivity());

            recyclerView.setAdapter(new MBlogAdapter(list,null));// TODO: 2016/1/4 list!!!!!!!
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            return recyclerView;
        }
    }*/

    /**
     * 点击按钮结束当前界面
     * @param item
     * @return
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

    public static void actionStart(Context context, int userId, int friendId) {
        Intent i = new Intent(context, PeopleActivity.class);
        i.putExtra("UserId", userId);
        i.putExtra("FriendId", friendId);
        context.startActivity(i);
    }

    /**
     * 拿到好友的名字设置title
      * @param friendId
     */
    public void getFriendName(int friendId) {
        String url = IConst.SERVLET_ADDR + "SearchUserDataServlet";
        String data = "userId=" + friendId;
        HttpUtil.sendHttpRequest(url, "POST", data, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                User user = Utility.handleUserInfoResultResponse(response);
                final String nick = user.getUsername();
                final int fansCount = user.getFansCount();
                final int focusCount = user.getFocusCount();
                 peopleIntro = user.getUserIntro();
                getIntent().putExtra("People", peopleIntro);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();

                        collapsingToolbarLayout.setTitle(nick);
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
                    }
                });

            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PeopleActivity.this, R.string.network_is_bad, Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

    }
}



