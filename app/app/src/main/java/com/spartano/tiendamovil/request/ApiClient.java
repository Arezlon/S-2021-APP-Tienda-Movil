package com.spartano.tiendamovil.request;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static ApiClient api = null;
    //private static final String PATH="http://192.168.0.107:45455/"; //Diego
    //private static final String PATH="http://192.168.0.108:45455/"; //Sebastian

    private static MyRetrofit myRetrofit;

    public static MyRetrofit getRetrofit(){
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(PATH)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        myRetrofit = retrofit.create(MyRetrofit.class);
        return myRetrofit;
    }

    public interface MyRetrofit {
        //
    }

    public static ApiClient getApi(){
        if (api == null){
            api = new ApiClient();
        }
        return api;
    }

}