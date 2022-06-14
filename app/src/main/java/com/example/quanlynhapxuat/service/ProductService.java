package com.example.quanlynhapxuat.service;

import com.example.quanlynhapxuat.model.Product;
import com.example.quanlynhapxuat.model.Product2;

import java.util.ArrayList;
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

    @GET("products")
    Call<ArrayList<Product2>> getProducts();

    @GET("products/{id}")
    Call<Product> getProductById(@Path("id") int i);

    @POST("products")
    Call<Product> createProduct(@Body Product product);

    @POST("products")
    Call<Product2> postProduct2(@Body Product2 product2);

    @PUT("products/{id}")
    Call<Product> updateProductById(@Path("id") int id, @Body Product kh);

    @PUT("products/{id}")
    Call<Product2> putProduct(@Path("id") int id, @Body Product2 product2);

    @DELETE("products/{id}")
    Call<Product> deleteProductById(@Path("id") int id);
}
