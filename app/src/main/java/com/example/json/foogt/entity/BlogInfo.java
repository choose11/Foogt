package com.example.json.foogt.entity;

import java.util.Date;

/**
 * Created by Mzz on 2015/12/28.
 */
public class BlogInfo {
    private String username;
    private Date postTime;
    private String msg;

    public BlogInfo() {
    }

    public BlogInfo(String username, Date postTime, String msg) {
        this.username = username;
        this.postTime = postTime;
        this.msg = msg;
    }

    public Date getPostTime() {
        return postTime;
    }

    public void setPostTime(Date postTime) {
        this.postTime = postTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
