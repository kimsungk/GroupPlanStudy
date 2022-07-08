package com.example.groupplanstudy.Server.DTO;

public class User {
    private Long uid;
    private String email;
    private String password;
    private String introduce;
    private String nickname;

    public User() {
    }

    public User(String email, String password, String introduce, String nickname) {
        this.email = email;
        this.password = password;
        this.introduce = introduce;
        this.nickname = nickname;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
