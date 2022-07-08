package com.example.groupplanstudy.Server;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {
    public static Retrofit retrofit;

    //
    public static Retrofit getClient(){
        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.219.108:8866")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }
}
