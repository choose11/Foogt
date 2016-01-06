package com.example.json.foogt.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.json.foogt.R;
import com.example.json.foogt.fragment.HomeFragment;

public class CollectionActivity extends AppCompatActivity {

    private int userId;

    private ActionBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

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
            bar.setTitle(R.string.user_coll);
        }

        userId = getIntent().getIntExtra("userId", -1);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.layout_root, HomeFragment.newInstance(userId, HomeFragment.COLLECTION)).commit();
    }

    public static void actionStart(Context context, int userId) {
        Intent i = new Intent(context, CollectionActivity.class);
        i.putExtra("userId", userId);
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
