package com.example.quanlynhapxuat.service;

import com.example.quanlynhapxuat.model.KhachHang;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface KhachHangService {
    @GET("customers")
    Call<List<KhachHang>> getAllKH();

    @GET("customers/{id}")
    Call<KhachHang> getKHById(@Path("id") int i);

    @POST("customers")
    Call<KhachHang> createKH(@Body KhachHang kh);

    @PUT("customers/{id}")
    Call<KhachHang> updateKHById(@Path("id") int id, @Body KhachHang kh);

    @DELETE("customers/{id}")
    Call<KhachHang> deleteKHById(@Path("id") int id);
}
