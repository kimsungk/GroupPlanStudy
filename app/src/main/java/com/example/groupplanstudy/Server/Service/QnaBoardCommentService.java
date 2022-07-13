package com.example.groupplanstudy.Server.Service;

import com.example.groupplanstudy.Server.DTO.APIMessage;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface QnaBoardCommentService
{
    @GET("api/groom/{grId}/qnaboard/{bid}/comment")
    Call<APIMessage> getAllQnaCommentList(@Path("grId") long grId, @Path("bid") long bid);

}
