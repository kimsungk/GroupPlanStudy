package com.example.groupplanstudy.Server.Service;

import com.example.groupplanstudy.Server.DTO.APIMessage;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenGroupService
{
    @GET("api/opengroups")
    Call<APIMessage> getGroupRooms();

    @GET("api/opengroups")
    Call<APIMessage> getGroupRoomsByTitle(@Query("title")String title);
}
