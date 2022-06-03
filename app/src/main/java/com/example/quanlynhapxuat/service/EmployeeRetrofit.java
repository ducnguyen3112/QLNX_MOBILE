package com.example.quanlynhapxuat.service;

import com.example.quanlynhapxuat.model.Employee;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface EmployeeRetrofit {

    @GET("employees")
    Call<ArrayList<Employee>> getEmployees();

    @GET("employees/{id}")
    Call<Employee> getEmployee(@Path("id") int id);

    @POST("employees")
    Call<Employee> createEmployee(@Body Employee employee);

    @PUT("employees/{id}")
    Call<Employee> updateEmployee(@Path("id") int id, @Body Employee employee);
}
