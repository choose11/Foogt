package com.example.json.foogt.entity;

import java.util.Date;

/**
 * Created by Mzz on 2015/12/28.
 */
public class BlogInfo implements Comparable<BlogInfo> {
    private int authorId;
    private int msgId;
    private String username;
    private Date postTime;
    private String msg;
    private boolean collected;

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

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int autorId) {
        this.authorId = autorId;
    }

    public boolean isCollected() {
        return collected;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
    }

    @Override
    public int compareTo(BlogInfo another) {
        //时间越早，越往后排
        return another.getPostTime().compareTo(this.postTime);
    }
}
