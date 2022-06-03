package com.example.quanlynhapxuat.service;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UploadFileRetrofit {

    @Multipart
    @POST("cloudDinary/fileUpload")
    Call<ResponseBody> fileUpload(@Part MultipartBody.Part image);

}
