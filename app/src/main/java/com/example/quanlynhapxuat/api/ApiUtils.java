package com.example.quanlynhapxuat.api;

import com.example.quanlynhapxuat.service.EmployeeRetrofit;
import com.example.quanlynhapxuat.service.KhachHangService;
import com.example.quanlynhapxuat.service.ProductRetrofit;
import com.example.quanlynhapxuat.service.ProductService;
import com.example.quanlynhapxuat.service.ReceivedDocketService;
import com.example.quanlynhapxuat.service.UploadFileRetrofit;
import com.example.quanlynhapxuat.service.UploadService;

public class ApiUtils {

    //public static final String baseURL = "http://10.200.0.157:8080/api/";
    public static final String baseURL = "https://shoesstation.herokuapp.com/api/";

    public static KhachHangService getKhachHangService() {
        return RetrofitClient.getClient(baseURL).create(KhachHangService.class);
    }

    public static ReceivedDocketService getReceivedDocketService() {
        return RetrofitClient.getClient(baseURL).create(ReceivedDocketService.class);
    }

    public static ProductRetrofit productRetrofit() {
        return RetrofitClient.getClient(baseURL).create(ProductRetrofit.class);
    }

    public static EmployeeRetrofit employeeRetrofit() {
        return RetrofitClient.getClient(baseURL).create(EmployeeRetrofit.class);
    }

    public static ProductService getProductService() {
        return RetrofitClient.getClient(baseURL).create(ProductService.class);
    }

    public static UploadService getUploadService() {
        return RetrofitClient.getClient(baseURL).create(UploadService.class);
    }

}

