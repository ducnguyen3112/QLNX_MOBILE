package com.example.quanlynhapxuat.service;

import com.example.quanlynhapxuat.model.DeliveryDocket;
import com.example.quanlynhapxuat.model.DeliveryDocketDetail;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface DeliveryDocketDetailService {
    Gson gson=new GsonBuilder().setDateFormat("dd-MM-yyyy HH:mm:ss").create();

    DeliveryDocketDetailService deliveryDocketService=new Retrofit.Builder()
            //http://192.168.0.6:8080/api//
            //https://shoesstation.herokuapp.com/api/
            .baseUrl("https://shoesstation.herokuapp.com/api/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(DeliveryDocketDetailService.class);
    @GET("deliveryDocketDetails")
    Call<List<DeliveryDocketDetail>> getAllDeliveryDocketDetail();
    @POST("deliveryDocketDetails")
    Call<DeliveryDocketDetail> addDeliveryDocketDetail(@Body DeliveryDocketDetail deliveryDocketDetail);
    @PUT("deliveryDocketDetails/{id}")
    Call<DeliveryDocketDetail> upDateDeliveryDocketDetail(@Body DeliveryDocketDetail deliveryDocketDetail,@Path("id") int id);
    @DELETE("deliveryDocketDetails/{id}")
    Call<DeliveryDocketDetail> deleteDeliveryDocketDetail(@Path("id") int id);
}
