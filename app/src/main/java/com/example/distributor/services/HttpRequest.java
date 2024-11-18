package com.example.distributor.services;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpRequest {
    private APIService service;

    public  HttpRequest(){
        service = new Retrofit.Builder()
                .baseUrl(APIService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(APIService.class);
    }

    public APIService callAPI(){

        return  service;
    }

}
