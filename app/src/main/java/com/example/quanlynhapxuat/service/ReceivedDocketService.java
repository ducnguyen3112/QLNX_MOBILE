package com.example.quanlynhapxuat.service;

import com.example.quanlynhapxuat.model.ReceivedDocket;
import com.example.quanlynhapxuat.model.ReceivedDocketDetail;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ReceivedDocketService {

    Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy HH:mm:ss").create();

    ReceivedDocketService RECEIVED_DOCKET_SERVICE = new Retrofit.Builder()
            //.baseUrl("https://shoesstation.herokuapp.com/api/")
            .baseUrl("http://10.200.0.157:8080/api/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ReceivedDocketService.class);

    @GET("receivedDockets/")
    Call<ArrayList<ReceivedDocket>> getReceivedDocketList();

    @GET("receivedDocketDetails/")
    Call<ArrayList<ReceivedDocketDetail>> getReceivedDocketDetailList();

    @POST("receivedDockets/")
    Call<ReceivedDocket> postReceivedDocket(@Body ReceivedDocket receivedDocket);

    @GET("receivedDockets/{id}/")
    Call<ReceivedDocket> getReceivedDocket(@Path("id") int maPN);

    @PUT("receivedDockets/{id}")
    Call<ReceivedDocket> putReceivedDocket(@Path("id") int maPN, @Body ReceivedDocket receivedDocket);

    @POST("receivedDocketDetails/")
    Call<ReceivedDocketDetail> postReceivedDocketDetail(@Body ReceivedDocketDetail receivedDocketDetail);

    @PUT("receivedDocketDetails/{id}")
    Call<ReceivedDocketDetail> putReceivedDocketDetail(@Path("id") int maCTPN, @Body ReceivedDocketDetail receivedDocketDetail);
}
