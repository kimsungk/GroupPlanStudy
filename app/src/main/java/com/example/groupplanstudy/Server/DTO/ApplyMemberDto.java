package com.example.groupplanstudy.Server.DTO;

public class ApplyMemberDto
{
    private long grId;
    private long uid;

    public ApplyMemberDto() {
    }

    public ApplyMemberDto(long grId, long uid) {
        this.grId = grId;
        this.uid = uid;
    }

    public long getGrId() {
        return grId;
    }

    public void setGrId(long grId) {
        this.grId = grId;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }
}
