package com.example.json.foogt.util;

import android.content.Intent;
import android.text.TextUtils;

import com.example.json.foogt.entity.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            u = new User(null, null, jsonObject.getString("username"),
                    jsonObject.getString("userIntro"), jsonObject.getInt("msgCount"),
                    jsonObject.getInt("fansCount"), jsonObject.getInt("focusCount"));
            u.setUserId(jsonObject.getInt("userId"));
        } catch (JSONException e) {
            e.printStackTrace();
            LogUtil.e(TAG, e.toString());
        }
        return u;
    }

    public static User handleUserDataResultResponse(String response) {
        User u = null;
        try {
            JSONObject jsonObject = new JSONObject(response);
            u = new User(jsonObject.getInt("userId"),jsonObject.getString("username"),
                    jsonObject.getString("userIntro"));

        } catch (JSONException e) {
            e.printStackTrace();
            LogUtil.e(TAG, e.toString());
        }
        return u;
    }

    public static User handleUserAccountResultResponse(String response) {
        User u = null;
        try {
            JSONObject jsonObject = new JSONObject(response);
            u = new User(jsonObject.getString("account"),jsonObject.getInt("userId"));

        } catch (JSONException e) {
            e.printStackTrace();
            LogUtil.e(TAG, e.toString());
        }
        return u;
    }

    public static List<Map<String,String>> handleSearchUserRequestResponse(String response){
        String TAG = "handleSearchUserRequestResponse";
        List<Map<String,String>> list = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(response);
            for(int i = 0; i<array.length();i++){
                JSONObject user = array.getJSONObject(i);
                JSONArray jsonArray = user.names();
                Map<String,String> map = new HashMap<>();
                for(int j=0;j<jsonArray.length();j++){
                    map.put(jsonArray.getString(j),user.getString(jsonArray.getString(j)));
                }
                list.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            LogUtil.e(TAG,e.toString());
        }
        return list;
    }
}
