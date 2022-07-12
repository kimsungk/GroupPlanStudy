package com.example.groupplanstudy.Server.DTO;

import com.example.groupplanstudy.Server.DTO.Applicable;
import com.example.groupplanstudy.Server.DTO.User;

import java.io.Serializable;

public class GroupRoomDto implements Serializable
{
    private long grId;
    private User user;
    private String title;
    private String introduce;
    // OPEN , CLOSED
    private Applicable applicable;
    private int memberLimit;

    public GroupRoomDto() {
    }

    public GroupRoomDto(long grId, User user, String title, String introduce, Applicable applicable, int memberLimit) {
        this.grId = grId;
        this.user = user;
        this.title = title;
        this.introduce = introduce;
        this.applicable = applicable;
        this.memberLimit = memberLimit;
    }

    public void setGrId(long grId) {
        this.grId = grId;
    }

    public void setUserDto(User user) {
        this.user = user;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public void setMemberLimit(int memberLimit) {
        this.memberLimit = memberLimit;
    }

    public long getGrId() {
        return grId;
    }

    public User getUserDto() {
        return user;
    }

    public String getTitle() {
        return title;
    }

    public String getIntroduce() {
        return introduce;
    }

    public int getMemberLimit() {
        return memberLimit;
    }

    public Applicable getApplicable() {
        return applicable;
    }

    public void setApplicable(Applicable applicable) {
        this.applicable = applicable;
    }
}
