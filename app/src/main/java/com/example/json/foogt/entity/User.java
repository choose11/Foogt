package com.example.json.foogt.entity;

public class User {
    private int userId;
    private String account;
    private String password;
    private String username;
    private String userIntro;
    private int msgCount;
    private int fansCount;
    private int focusCount;

    public User() {
    }

    /**
     * For register. default username is account.
     *
     * @param account
     * @param password
     */
    public User(String account, String password) {
        this(account, password, account, "Nothing", 0, 0, 0);
    }

    public User(String account, String password, String username,
                String userIntro, int msgCount, int fansCount, int focusCount) {
        super();
        this.account = account;
        this.password = password;
        this.username = username;
        this.userIntro = userIntro;
        this.msgCount = msgCount;
        this.fansCount = fansCount;
        this.focusCount = focusCount;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserIntro() {
        return userIntro;
    }

    public void setUserIntro(String userIntro) {
        this.userIntro = userIntro;
    }

    public int getMsgCount() {
        return msgCount;
    }

    public void setMsgCount(int msgCount) {
        this.msgCount = msgCount;
    }

    public int getFansCount() {
        return fansCount;
    }

    public void setFansCount(int fansCount) {
        this.fansCount = fansCount;
    }

    public int getFocusCount() {
        return focusCount;
    }

    public void setFocusCount(int focusCount) {
        this.focusCount = focusCount;
    }

}
