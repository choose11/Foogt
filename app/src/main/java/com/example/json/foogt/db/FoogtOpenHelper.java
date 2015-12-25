package com.example.json.foogt.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Mzz on 2015/12/11.
 */
public class FoogtOpenHelper extends SQLiteOpenHelper {
    public FoogtOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static final String CREATE_USER = "create table user(" +
            "id integer primary key autoincrement," +
            "account text not null," +
            "userId integer not null," +
            "username text not null," +
            "userIntro text not null," +
            "msgCount integer not null," +
            "fansCount integer not null," +
            "focusCount integer not null" +
            ")";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
