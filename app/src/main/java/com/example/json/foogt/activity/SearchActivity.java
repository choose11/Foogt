package com.example.json.foogt.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.json.foogt.R;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }
    public static void actionStart(Context context) {
        Intent i = new Intent(context, SearchActivity.class);
        context.startActivity(i);
    }
}