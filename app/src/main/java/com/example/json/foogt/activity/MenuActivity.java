package com.example.json.foogt.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.example.json.foogt.ActivityCollector.ActivityCollector;
import com.example.json.foogt.R;
import com.example.json.foogt.entity.User;
import com.example.json.foogt.fragment.CommentFragment;
import com.example.json.foogt.fragment.HomeFragment;
import com.example.json.foogt.util.BitmapCache;
import com.example.json.foogt.util.HttpCallbackListener;
import com.example.json.foogt.util.HttpUtil;
import com.example.json.foogt.util.IConst;
import com.example.json.foogt.util.LogUtil;
import com.example.json.foogt.util.Utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final int SELECT_PIC_BY_PICK_PHOTO = 1;
    private int userId;
    private String mPicturePath;

    private ImageView dataEditImg;
    private NetworkImageView headImg;
    private ProgressDialog pd;

    private int lastLoginUserId;
    private TextView fansTxt, focusTxt, countMsgTxt, userNameTxt, UserIntroMin;

    CollectionPagerAdapter mPagerAdapter;
    ViewPager mViewPager;
    private ImageLoader mImageLoader;
    private RequestQueue mQueue;
    private Context mContext;

    SharedPreferences preferences;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        ActivityCollector.addActivity(this);

        mContext = this;

        lastLoginUserId = getSharedPreferencesUserId();
        if (lastLoginUserId == -1) {
            userId = -1;
        } else {

            userId = lastLoginUserId;
            //userId = getIntent().getIntExtra("userId", -1);

        }

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
        if (userId == -1) {
            fab.setVisibility(View.GONE);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                if (userId != -1) {
                    getUserData(userId);//从服务器拿到原始数据,做一次刷新
                } else {
                    Toast.makeText(MenuActivity.this, "Login First!!!!!", Toast.LENGTH_LONG).show();
                }

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
        headImg = (NetworkImageView) headerView.findViewById(R.id.img_menu_user);

        OnDrawerItemClickListener listener = new OnDrawerItemClickListener();

        mQueue = Volley.newRequestQueue(this);
        mImageLoader = new ImageLoader(mQueue, BitmapCache.getInstance());
        //Set Head IMG
        headImg.setDefaultImageResId(R.drawable.picture);
        headImg.setErrorImageResId(R.drawable.search);
        headImg.setImageUrl(IConst.SERVLET_ADDR + "GetHeadIMG?uid=" + userId, mImageLoader);

        if (userId != -1) {
            dataEditImg.setOnClickListener(listener);
            fansTxt.setOnClickListener(listener);
            focusTxt.setOnClickListener(listener);
            countMsgTxt.setOnClickListener(listener);
            headImg.setOnClickListener(new SetHeadImgListener());
        }
    }

    private void setupViewpager(ViewPager viewPager) {
        mPagerAdapter = new CollectionPagerAdapter(getSupportFragmentManager());
        mPagerAdapter.addFragment(HomeFragment.newInstance(userId,HomeFragment.HOME), getResources().getString(R.string.home));
        mPagerAdapter.addFragment(CommentFragment.newInstance(userId), getResources().getString(R.string.comment));
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
                    //  toolbar.setTitle(R.string.home);
                    mViewPager.setCurrentItem(0);
                    break;
                case R.id.action_comment:
                    //  toolbar.setTitle(R.string.comment);
                    mViewPager.setCurrentItem(1);
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
     * 返回键
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            // super.onBackPressed();
            /**
             * 清除掉前面所有的活动
             */
            LogUtil.d("mune", ActivityCollector.activities.size() + "");
            ActivityCollector.finishAll();
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
     * @return boolean
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (userId != -1) {
            if (id == R.id.nav_search) {
                SearchActivity.actionStart(MenuActivity.this, userId);
            } else if (id == R.id.nav_collection) {
                CollectionActivity.actionStart(MenuActivity.this, userId);
            } else if (id == R.id.nav_send) {
                SendBlogActivity.actionStart(MenuActivity.this, userId);
            } else if (id == R.id.nav_manage) {
                ChangeUserActivity.actionStart(MenuActivity.this, userId);
            }
        } else {
            LoginActivity.actionStart(MenuActivity.this);

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            String path = convertUriToPath(uri);
            LogUtil.d("URI", uri.toString());
            if (!Utility.isImageFileExtension(MimeTypeMap.getFileExtensionFromUrl(path))) {
                alert("不是有效的PNG文件！");
                return;
            }
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(this
                        .getContentResolver().openInputStream(uri));
                if (bitmap.getByteCount() > 4 * 1024 * 1024) { //max size 4MB
                    alert("图片文件过大！");
                    return;
                }
                mPicturePath = path;
                LogUtil.d("MenuActivity", path);
                doPhoto(mPicturePath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                LogUtil.e("MenuActivity", e.toString());
            }
        }
    }

    /**
     * 登录后对接
     */

    public static void actionStart(Context context, int userId) {
        Intent i = new Intent(context, MenuActivity.class);
        i.putExtra("userId", userId);
        LogUtil.d("MenuActivity", "当前启动的userId：" + userId);
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
                    UserBlogActivity.actionStart(MenuActivity.this, userId);
                    break;
                /**
                 * 点击进入粉丝界面
                 */
                case R.id.txt_user_fans:
                    FansOrFocusActivity.actionStart(MenuActivity.this, userId, "粉丝");
                    break;
                /**
                 * 点击进入关注界面
                 */
                case R.id.txt_user_focus:
                    FansOrFocusActivity.actionStart(MenuActivity.this, userId, "关注");
                    break;

            }

        }
    }

    /*
    刚刚跳转时从服务器得到数据
     */
    public void getUserData(final int userId) {
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


    private class SetHeadImgListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            pickPhoto();
        }
    }

    private void pickPhoto() {
        Intent intent = new Intent();
        // 如果要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
        intent.setType("image/png");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, SELECT_PIC_BY_PICK_PHOTO);
    }

    /**
     * 选择图片后，获取图片的路径
     */
    private void doPhoto(String picPath) {
        // 如果图片非空将其上传到服务器
        if (picPath != null) {
/*          暂时无用，预览图片用
//          BitmapFactory.Options option = new BitmapFactory.Options();
//          压缩图片:表示缩略图大小为原始图片大小的几分之一，1为原图
//          option.inSampleSize = 1;
//          根据图片的SDCard路径读出Bitmap
//          Bitmap bm = BitmapFactory.decodeFile(picPath, option);
//          显示在图片控件上
//          picImg.setImageBitmap(bm);
*/
            final File file = new File(picPath);
            final String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    MimeTypeMap.getFileExtensionFromUrl(picPath));
            try {
                pd = ProgressDialog.show(mContext, null, "正在上传图片，请稍候...");
                int res = HttpUtil.postFileToURL(file, mimeType, new URL(IConst.SERVLET_ADDR + "UploadImg?userId=" + userId), "pic",
                        new HttpCallbackListener() {
                            @Override
                            public void onFinish(String response) {
                                if (Utility.handleBooleanResultResponse(response)) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            //reload img without cache
                                            headImg.setImageUrl(IConst.SERVLET_ADDR + "GetHeadIMG?uid=" + userId + "&random=" + Math.random(), mImageLoader);
                                            Toast.makeText(MenuActivity.this, getString(R.string.upload_img_success) + "," + getString(R.string.refresh_to_show), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onError(Exception e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MenuActivity.this, R.string.http_fail, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                LogUtil.d("result", res + "");
            } catch (MalformedURLException e) {
                e.printStackTrace();
                LogUtil.e("UploadImg", e.toString());
            } finally {
                pd.dismiss();
            }
        } else {
            Toast.makeText(this, "选择图片文件不正确", Toast.LENGTH_LONG).show();
        }

    }

    private String convertUriToPath(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
        if (cursor == null) {
            alert("图片不存在或尚未加入相册索引！");
            return null;
        }
        int colunm_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(colunm_index);
        Log.i("MenuActivity", "PicPath: " + path);
        return path;
    }

    private void alert(String msg) {
        Dialog dialog = new AlertDialog.Builder(this).setTitle("提示")
                .setMessage(msg)
                .setPositiveButton("确定", null).create();
        dialog.show();
    }

    private int getSharedPreferencesUserId() {
        int lastUserId;
        File f = new File("data/data/com.example.json.foogt/shared_prefs/userId.xml");
        if (!f.exists()) {
            preferences = getSharedPreferences("userId"
                    , MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("userId", -1);
            editor.apply();
            lastUserId = preferences.getInt("userId", 0);
            System.out.println("取出刚存的lastUserId = " + lastUserId);
        } else {
            preferences = getSharedPreferences("userId"
                    , MODE_PRIVATE);
            // 读取SharedPreferences里的数据
            lastUserId = preferences.getInt("userId", 0);
            System.out.println("取出上次存的lastUserId = " + lastUserId);
        }
        return lastUserId;
    }
}

