package com.example.quanlynhapxuat.service;

import com.example.quanlynhapxuat.model.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ProductService {
    @GET("products")
    Call<List<Product>> getAllProduct();

    @GET("products/{id}")
    Call<Product> getProductById(@Path("id") int i);

    @POST("products")
    Call<Product> createProduct(@Body Product product);

    @PUT("products/{id}")
    Call<Product> updateProductById(@Path("id") int id, @Body Product kh);

    @DELETE("products/{id}")
    Call<Product> deleteProductById(@Path("id") int id);
}
