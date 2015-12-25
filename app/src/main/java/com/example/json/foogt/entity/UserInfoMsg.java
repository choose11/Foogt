package com.example.json.foogt.entity;

/**
 * Created by Mzz on 2015/12/25.
 */
public class UserInfoMsg {
    boolean result;
    User user;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
