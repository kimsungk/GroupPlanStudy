package com.example.groupplanstudy.Server.DTO;

public class GroupMemberDto2 {

    private long gmId;
    private GroupRoomDto groupRoomDto;
    private long uid;
    private GroupRole role;

    public GroupMemberDto2() {
    }

    public GroupMemberDto2(long gmId, GroupRoomDto groupRoomDto, long uid, GroupRole role) {
        this.gmId = gmId;
        this.groupRoomDto = groupRoomDto;
        this.uid = uid;
        this.role = role;
    }



    public long getGmId() {
        return gmId;
    }

    public void setGmId(long gmId) {
        this.gmId = gmId;
    }

    public GroupRoomDto getGroupRoomDto() {
        return groupRoomDto;
    }

    public void setGroupRoomDto(GroupRoomDto groupRoomDto) {
        this.groupRoomDto = groupRoomDto;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public GroupRole getRole() {
        return role;
    }

    public void setRole(GroupRole role) {
        this.role = role;
    }
}
