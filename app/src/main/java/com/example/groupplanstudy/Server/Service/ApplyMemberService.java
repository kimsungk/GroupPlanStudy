package com.example.groupplanstudy.Server.Service;

import com.example.groupplanstudy.Server.DTO.APIMessage;
import com.example.groupplanstudy.Server.DTO.ApplyMemberDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.HTTP;
import retrofit2.http.POST;

public interface ApplyMemberService
{
    @POST("api/applymember")
    Call<APIMessage> applyGroupRoom(@Body ApplyMemberDto applyMemberDto);

    @POST("api/applymember/allow")
    Call<APIMessage> allowGroupMember(@Body ApplyMemberDto applyMemberDto);

    @POST("api/applymember/refuse")
    //@DELETE("api/applymember/refuse")
    Call<APIMessage> refuseGroupMember(@Body ApplyMemberDto applyMemberDto);
}
