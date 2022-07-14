package com.example.groupplanstudy.Server.Service;

import com.example.groupplanstudy.Server.DTO.APIMessage;
import com.example.groupplanstudy.Server.DTO.GroupRoomDto;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface OpenGroupService
{
    @GET("api/opengroups")
    Call<APIMessage> getGroupRooms();

    @GET("api/opengroups")
    Call<APIMessage> getGroupRoomsByTitle(@Query("title")String title);

    @POST("api/groom/{id}")
    Call<GroupRoomDto> createGroupRoom(@Body GroupRoomDto groupRoomDto, @Path("id") long uid);

}
