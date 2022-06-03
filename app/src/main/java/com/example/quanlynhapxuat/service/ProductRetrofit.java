package com.example.quanlynhapxuat.service;

import com.example.quanlynhapxuat.model.Product;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ProductRetrofit {

    @GET("products")
    Call<ArrayList<Product>> getProducts();

    @GET("products/{id}")
    Call<Product> getProduct(@Path("id") int id);

    @POST("products")
    Call<Product> createNewProduct(@Body Product product);

    @PUT("products/{id}")
    Call<Product> updateProduct(@Path("id") int id, @Body Product product);

}
