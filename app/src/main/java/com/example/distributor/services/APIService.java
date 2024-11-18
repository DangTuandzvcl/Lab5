package com.example.distributor.services;

import com.example.distributor.models.Distributor;
import com.example.distributor.models.Response;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {
    String BASE_URL = "http://192.168.1.30:3001/";

    @GET("/distributors/list")
    Call<Response<ArrayList<Distributor>>> getListDistributor();

    @POST("/distributors/add")
    Call<Response<Distributor>> addDistributor(@Body Distributor distributor);

    @PUT("/distributors/update/{id}")
    Call<Response<Distributor>> editDistributor(@Path("id") String id, @Body Distributor distributor);

    @DELETE("/distributors/delete/{id}")
    Call<Response<Distributor>> deleteDistributor(@Path("id") String id);
    @GET("/distributors/search")
    Call<Response<ArrayList<Distributor>>> searchDistributor(@Query("key") String key);
}
