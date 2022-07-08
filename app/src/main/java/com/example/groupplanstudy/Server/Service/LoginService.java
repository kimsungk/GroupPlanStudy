package com.example.groupplanstudy.Server.Service;

import com.example.groupplanstudy.Server.DTO.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface LoginService {

    //추가
    @POST("api/accounts")
    Call<ResponseBody> createUser(@Body User user);

    //수정
    @PUT("api/accounts/{id}")
    Call<ResponseBody> updateUser(@Body User user, @Path("id") Long uid);

    //삭제
    @DELETE("api/accounts/{id}")
    Call<ResponseBody> deleteUser(@Path("id") Long uid);
}
