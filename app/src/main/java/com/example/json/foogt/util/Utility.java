package com.example.json.foogt.util;

import android.text.TextUtils;

import com.example.json.foogt.entity.User;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mzz on 2015/12/10.
 */
public class Utility {

    public static final String TAG = "Utility";

    /**
     * handle response which is a json contains a boolean result.
     *
     * @param response
     * @return
     */
    public static synchronized boolean handleBooleanResultResponse(String response) {
        String TAG = "handleBooleanResultResponse";
        boolean flag = false;
        if (TextUtils.isEmpty(response)) {
            return flag;
        }
        try {
            LogUtil.d(TAG, response);
            JSONObject jsonObject = new JSONObject(response);
            flag = jsonObject.getBoolean("result");
        } catch (JSONException e) {
            e.printStackTrace();
            LogUtil.e(TAG, e.toString());
        }
        return flag;
    }

    public static User handleUserInfoResultResponse(String response) {
        User u = null;
        try {
            JSONObject jsonObject = new JSONObject(response);
            u = new User(jsonObject.getString("account"), null, jsonObject.getString("username"),
                    jsonObject.getString("userIntro"), jsonObject.getInt("msgCount"),
                    jsonObject.getInt("fansCount"), jsonObject.getInt("focusCount"));
            u.setUserId(jsonObject.getInt("userId"));
        } catch (JSONException e) {
            e.printStackTrace();
            LogUtil.e(TAG, e.toString());
        }
        return u;
    }
}
