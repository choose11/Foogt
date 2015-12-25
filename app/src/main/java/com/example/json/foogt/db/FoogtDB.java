package com.example.json.foogt.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.json.foogt.entity.User;

/**
 * Created by Mzz on 2015/12/14.
 */
public class FoogtDB {
    public static final String DB_NAME = "Foogt";
    public static final int VERSION = 1;
    private static FoogtDB foogtDB;
    private SQLiteDatabase db;

    private FoogtDB(Context context) {
        FoogtOpenHelper dbHelper = new FoogtOpenHelper(context, DB_NAME, null, VERSION);
        db = dbHelper.getWritableDatabase();
    }

    public synchronized static FoogtDB getInstance(Context context) {
        if (foogtDB == null) {
            foogtDB = new FoogtDB(context);
        }
        return foogtDB;
    }

    public boolean saveUser(User user) {
        if (user == null) {
            return false;
        }
        db.delete("user", "userId=?", new String[]{user.getUserId() + ""});
        ContentValues contentValues = new ContentValues();
        contentValues.put("account", user.getAccount());
        contentValues.put("userId", user.getUserId());
        contentValues.put("username", user.getUsername());
        contentValues.put("userIntro", user.getUserIntro());
        contentValues.put("msgCount", user.getMsgCount());
        contentValues.put("fansCount", user.getFansCount());
        contentValues.put("focusCount", user.getFocusCount());
        return db.insert("user", null, contentValues) != -1;
    }

    private void close(Cursor cursor) {
        if (cursor != null) {
            cursor.close();
        }
    }
}
