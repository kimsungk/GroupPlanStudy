package com.example.groupplanstudy.Server.Service;

import com.example.groupplanstudy.Server.DTO.APIMessage;
import com.example.groupplanstudy.Server.DTO.GroupQnaDto;
import com.example.groupplanstudy.Server.DTO.GroupRoomDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface GroupRoomService {

    //Qna 글쓰기
    @POST("/api/groom/{grId}/qnaboard/{uid}")
    Call<GroupQnaDto> writeQna(@Body GroupQnaDto groupQnaDto,@Path("grId")Long grId,@Path("uid") long uid);

    //Qna 전체보기
    @GET("/api/groom/{grId}/qnaboard")
    Call<APIMessage> getGroupQnaByGrId(@Path("grId") Long grId);

    //GroupRoomDto 가져오기
    @GET("/api/groom/{grId}")
    Call<GroupRoomDto> getGroupRoom(@Path("grId") Long grId);


    //Qna 상세보기
//    @GET("/{grId}/qnaboard/{bid}")
}