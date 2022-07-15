package com.example.groupplanstudy.Server.Service;

import com.example.groupplanstudy.Server.DTO.APIMessage;
import com.example.groupplanstudy.Server.DTO.User;

import java.util.List;

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
    Call<APIMessage> updateUser(@Body User user, @Path("id") long uid);

    //삭제
    @DELETE("api/accounts/{id}")
    Call<ResponseBody> deleteUser(@Path("id") long uid);

    //로그인
    @POST("api/accounts/login")
    Call<APIMessage> loginUser(@Body User user);

    @GET("api/accounts")
    Call<List<User>> userList();
}
