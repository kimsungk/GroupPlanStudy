package com.example.groupplanstudy.Server.DTO;

public class GroupMemberDto
{
    private long grId;
    private long uid;
    private String name;
    private String intro;
    private GroupRole role;

    public GroupMemberDto() {
    }

    public GroupMemberDto(long grId, long uid, String name, GroupRole role) {
        this.grId = grId;
        this.uid = uid;
        this.name = name;
        this.role = role;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GroupRole getRole() {
        return role;
    }

    public void setRole(GroupRole role) {
        this.role = role;
    }
}
