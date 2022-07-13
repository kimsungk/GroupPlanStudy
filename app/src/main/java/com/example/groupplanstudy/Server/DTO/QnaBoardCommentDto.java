package com.example.groupplanstudy.Server.DTO;

import java.util.Date;

public class QnaBoardCommentDto
{
    private long cid;
    private long uid;
    private User userDto;
    private String content;
    private Date regdate;

    //bid
    private GroupQnaDto qnaBoardDto;

    public QnaBoardCommentDto() {
    }

    public QnaBoardCommentDto(long cid, long uid, User userDto, String content, Date regdate, GroupQnaDto qnaBoardDto) {
        this.cid = cid;
        this.uid = uid;
        this.userDto = userDto;
        this.content = content;
        this.regdate = regdate;
        this.qnaBoardDto = qnaBoardDto;
    }

    public void setCid(long cid) {
        this.cid = cid;
    }

    public void setUserDto(User userDto) {
        this.userDto = userDto;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setRegdate(Date regdate) {
        this.regdate = regdate;
    }

    public void setQnaBoardDto(GroupQnaDto qnaBoardDto) {
        this.qnaBoardDto = qnaBoardDto;
    }

    public User getUserDto() {
        return userDto;
    }

    public long getCid() {
        return cid;
    }

    public long getUid() {
        return uid;
    }

    public String getContent() {
        return content;
    }

    public Date getRegdate() {
        return regdate;
    }

    public GroupQnaDto getQnaBoardDto() {
        return qnaBoardDto;
    }
}
