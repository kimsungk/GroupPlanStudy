package com.example.groupplanstudy.Server.Service;

import com.example.groupplanstudy.Server.DTO.APIMessage;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GroupMemberService
{
    @GET("api/groupmember/applyMembers/{grId}")
    Call<APIMessage> getApplyMember(@Path("grId") long grId);

    @GET("api/groupmember/groupMembers/{grId}")
    Call<APIMessage> getGroupMember(@Path("grId") long grId);
}
