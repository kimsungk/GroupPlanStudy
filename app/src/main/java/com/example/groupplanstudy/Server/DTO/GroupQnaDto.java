package com.example.groupplanstudy.Server.DTO;

import java.util.Date;

public class GroupQnaDto {
    private Long bid;

    private GroupRoomDto groupRoom;

    private User user;

    private String title;
    private String content;

    private Date regdate;

    public GroupQnaDto(long bid, GroupRoomDto groupRoom, User user, String title, String content, Date regdate){
        this.bid = bid;
        this.groupRoom = groupRoom;
        this.user = user;
        this.title = title;
        this.content = content;
        this.regdate =regdate;

    }

    public Long getBid() {
        return bid;
    }

    public void setBid(Long bid) {
        this.bid = bid;
    }

    public GroupRoomDto getGroupRoom() {
        return groupRoom;
    }

    public void setGroupRoom(GroupRoomDto groupRoom) {
        this.groupRoom = groupRoom;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getRegdate() {
        return regdate;
    }

    public void setRegdate(Date regdate) {
        this.regdate = regdate;
    }
}
