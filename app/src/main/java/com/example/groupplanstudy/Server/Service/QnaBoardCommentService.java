package com.example.groupplanstudy.Server.Service;

import com.example.groupplanstudy.Server.DTO.APIMessage;
import com.example.groupplanstudy.Server.DTO.QnaBoardCommentDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface QnaBoardCommentService
{
    @GET("api/groom/{grId}/qnaboard/{bid}/comment")
    Call<APIMessage> getAllQnaCommentList(@Path("grId") long grId, @Path("bid") long bid);

    @POST("api/groom/{grId}/qnaboard/{bid}/comment/{uid}")
    Call<QnaBoardCommentDto> createQnaComment(
            @Path("grId") long grId, @Path("bid") long bid, @Path("uid") long uid,
            @Body QnaBoardCommentDto qnaBoardCommentDto);

    @DELETE("api/groom/{grId}/qnaboard/{bid}/comment/{cid}")
    Call<APIMessage> deleteQnacommentByCid(@Path("grId") long grId, @Path("bid") long bid, @Path("cid") long cid);

}
