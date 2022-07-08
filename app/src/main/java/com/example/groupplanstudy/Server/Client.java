package com.example.groupplanstudy.Server;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {
    public static Retrofit retrofit;

    //
    public static Retrofit getClient(){
        if(retrofit == null) {

            retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.45.243:8866")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
