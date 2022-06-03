package com.example.quanlynhapxuat.service;

import com.example.quanlynhapxuat.model.Message;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UploadService {
    @Multipart
    @POST("cloudDinary/fileUpload")
    Call<Message> uploadImage(@Part MultipartBody.Part file);
}
