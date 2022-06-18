package com.example.quanlynhapxuat.service;

import com.example.quanlynhapxuat.model.DeliveryDocket;
import com.example.quanlynhapxuat.model.Statistics;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public interface StatisticService {

    Gson gson=new GsonBuilder().setDateFormat("dd-MM-yyyy HH:mm:ss").create();
    StatisticService statisticService= new Retrofit.Builder()
           // .baseUrl("https://shoesstation.herokuapp.com/api/")
            .baseUrl("http://192.168.2.29:8080/api/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(StatisticService.class);

    @GET("statistics")
    Call<Statistics> getStatistics();
}
