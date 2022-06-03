package com.example.quanlynhapxuat.utils;

import com.example.quanlynhapxuat.api.ApiUtils;
import com.example.quanlynhapxuat.model.KhachHang;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Convert {
    public static String  dateToString(Date date){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = dateFormat.format(date);
        return strDate;
    }
    public static String currencyFormat(int value){
        DecimalFormat formatter = new DecimalFormat("###,###,###,###");
        return formatter.format(value);
    }
    public static Map<Integer,String> customerMap(){
        Map<Integer,String> customerMap=new HashMap<>();

        ApiUtils.getKhachHangService().getAllKH().enqueue(new Callback<List<KhachHang>>() {
            @Override
            public void onResponse(Call<List<KhachHang>> call, Response<List<KhachHang>> response) {
                if (response.isSuccessful()){
                    List<KhachHang> list=new ArrayList<>();
                    list=response.body();
                    for (KhachHang item:
                         list) {
                        customerMap.put(item.getId(),item.getFullName());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<KhachHang>> call, Throwable t) {

            }
        });
        return customerMap;
    }
}
