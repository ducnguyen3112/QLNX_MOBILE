package com.example.quanlynhapxuat.activity.ReceivedDocket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quanlynhapxuat.R;
import com.example.quanlynhapxuat.activity.main.LoginActivity;
import com.example.quanlynhapxuat.adapter.ReceivedDocketDetailAdapter;
import com.example.quanlynhapxuat.api.ApiUtils;
import com.example.quanlynhapxuat.model.Product;
import com.example.quanlynhapxuat.model.Product2;
import com.example.quanlynhapxuat.model.ReceivedDocket;
import com.example.quanlynhapxuat.model.ReceivedDocketDetail;
import com.example.quanlynhapxuat.model.RestErrorResponse;
import com.example.quanlynhapxuat.utils.CustomAlertDialog;
import com.example.quanlynhapxuat.utils.CustomToast;
import com.google.gson.Gson;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ReceivedDocketDetailActivity extends AppCompatActivity {
    private ReceivedDocket receivedDocket;
    private ReceivedDocketDetailAdapter rddAdapter;

    private TextView tvTittle, tvMaPN, tvMaNV, tvTongGiaTri;
    private EditText etNgayDat, etNhaCungCap;
    private ImageView ivDatePicker;
    private Button btnThemSP, btnTaoPhieuNhap, btnHuy;
    private RecyclerView rcvListChiTietPN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received_docket_detail);

        //setControl
        tvTittle = findViewById(R.id.tvTittle_activityRDD);
        tvMaPN = findViewById(R.id.tvMaPN_activityRDD);
        tvMaNV = findViewById(R.id.tvMaNV_activityRDD);
        tvTongGiaTri = findViewById(R.id.tvTongGiaTri_activityRDD);
        etNgayDat = findViewById(R.id.etNgayDat_activityRDD);
        etNhaCungCap = findViewById(R.id.etNhaCungCap_activityRDD);
        ivDatePicker = findViewById(R.id.ivDatePicker_activityRDD);
        btnThemSP = findViewById(R.id.btnThemSanPham_activityRDD);
        btnHuy = findViewById(R.id.btnHuy_activityRDD);
        btnTaoPhieuNhap = findViewById(R.id.btnTaoPhieuNhap_activityRDD);
        rcvListChiTietPN = findViewById(R.id.rcvListChiTietPN_activityRDD);

        //get&showList
        rddAdapter = new ReceivedDocketDetailAdapter(ReceivedDocketDetailActivity.this);
        rcvListChiTietPN.setLayoutManager(new LinearLayoutManager(ReceivedDocketDetailActivity.this));
        rcvListChiTietPN.setAdapter(rddAdapter);



        //
        Intent intent = getIntent();
        int maPN = intent.getIntExtra("maPN",-99);
        if(maPN<0) {
            CustomToast.makeText(this,"maPN = " + maPN,CustomToast.LENGTH_LONG,CustomToast.CONFUSING).show();
        }
        else if(maPN==0){
            // --  tạo phiếu nhập
            int maNV = LoginActivity.idLogin;
            tvMaPN.setText(maPN+"");
            tvMaNV.setText(maNV+"");
            tvTongGiaTri.setText(NumberFormat.getNumberInstance(Locale.US).format(rddAdapter.getTotalList())+"VND");

            receivedDocket = new ReceivedDocket();
        }
        else {
            // -- sửa phiếu nhập
            etNgayDat.setEnabled(false);
            etNhaCungCap.setEnabled(false);
            ivDatePicker.setVisibility(View.GONE);
            getReceivedDocket(maPN);
        }

        //setEvent
        ivDatePicker.setOnClickListener(view -> {
            dateTimePicker();
        });

        btnThemSP.setOnClickListener(view -> {
            themSP(maPN);
        });

        btnTaoPhieuNhap.setOnClickListener(view -> {
            if(rddAdapter.getItemCount()==0) {
                CustomToast.makeText(this,"Phiếu nhập chưa có sản phẩm!"
                        ,CustomToast.LENGTH_SHORT,CustomToast.WARNING).show();
                return;
            }

            taoPhieuNhap(maPN);
            if(maPN==0) {
                CustomToast.makeText(ReceivedDocketDetailActivity.this,"Thêm phiếu nhập thành công!"
                        ,CustomToast.LENGTH_SHORT,CustomToast.SUCCESS).show();
            }
            else {
                CustomToast.makeText(ReceivedDocketDetailActivity.this,"Cập nhật phiếu nhập thành công!"
                        ,CustomToast.LENGTH_SHORT,CustomToast.SUCCESS).show();
            }
            finish();
        });

        btnHuy.setOnClickListener(view -> {
            huy();
        });
    }

    private void themSP(int maPN) {
        Dialog dialog = rddAdapter.getDialogThemSP(ReceivedDocketDetailActivity.this);

        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        rddAdapter.btnHuy_dialogThemSP.setOnClickListener(view -> {
            dialog.cancel();
        });

        rddAdapter.btnThem_dialogThemSP.setOnClickListener(view -> {
            if(rddAdapter.etSL_dialogThemSP.getText().toString().isEmpty()) {
                CustomToast.makeText(ReceivedDocketDetailActivity.this,"Vui lòng nhập số lượng!"
                        ,CustomToast.LENGTH_LONG,CustomToast.WARNING).show();
            }
            else if(rddAdapter.etDonGia_dialogThemSP.getText().toString().isEmpty()) {
                CustomToast.makeText(ReceivedDocketDetailActivity.this,"Vui lòng nhập đơn giá!"
                        ,CustomToast.LENGTH_LONG,CustomToast.WARNING).show();
            }
            else {
                Product2 product = rddAdapter.getProduct2(rddAdapter.getSelectedProductID());

                ReceivedDocketDetail rdd = new ReceivedDocketDetail();

                rdd.setId(0);
                rdd.setProductId(product.getId());
                rdd.setReceivedDocketId(maPN);
                rdd.setQuantity(Integer.parseInt(rddAdapter.etSL_dialogThemSP.getText().toString()));
                rdd.setPrice(Integer.parseInt(rddAdapter.etDonGia_dialogThemSP.getText().toString()));

                rdd.toString();

                rddAdapter.addRDDList(rdd);

                capNhatDuLieu();
                dialog.cancel();
            }
        });
    }

    private void taoPhieuNhap(int maPN) {
        String ngayDat = etNgayDat.getText().toString();
        if(ngayDat.equals("")) {
            CustomToast.makeText(this,"Không được để trống ngày đặt!"
                    ,CustomToast.LENGTH_SHORT,CustomToast.WARNING).show();
            return;
        }

        String nhaCungCap = etNhaCungCap.getText().toString();
        if(nhaCungCap.equals("")) {
            CustomToast.makeText(this,"Không được để trống nhà cung cấp!"
                    ,CustomToast.LENGTH_SHORT,CustomToast.WARNING).show();
            return;
        }

        receivedDocket = new ReceivedDocket(maPN,ngayDat,Integer.parseInt(tvMaNV.getText().toString()),1,nhaCungCap,null);

        if(maPN==0) {
            ApiUtils.getReceivedDocketService().postReceivedDocket(receivedDocket).enqueue(new Callback<ReceivedDocket>() {

                @Override
                public void onResponse(Call<ReceivedDocket> call, Response<ReceivedDocket> response) {
                    if(response.isSuccessful()) {
                        Log.e("postRD: ","Thêm phiếu nhập thành công!");
                        deleteInventotyInOldRDD();
                        for(ReceivedDocketDetail item : rddAdapter.getRddList()) {
                            item.setReceivedDocketId(response.body().getId());
                            Log.e("item.setReceivedDocketId: ",item.getReceivedDocketId()+"");
                            if(item.getId()==0) {
                                postReceivedDocketDetail(item);
                            }
                            else {
                                putReceivedDocketDetail(item);
                            }
                        }
                    }
                    else {
                        try {
                            Gson g = new Gson();
                            RestErrorResponse errorResponse = g.fromJson(response.errorBody().string(),RestErrorResponse.class);
                            CustomToast.makeText(ReceivedDocketDetailActivity.this,"TRY: " + errorResponse.getMessage()
                                    ,CustomToast.LENGTH_LONG,CustomToast.ERROR).show();
                        }
                        catch (Exception e) {
                            CustomToast.makeText(ReceivedDocketDetailActivity.this,"CATCH: " + e.getMessage()
                                    ,CustomToast.LENGTH_LONG,CustomToast.ERROR).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ReceivedDocket> call, Throwable t) {
                    CustomToast.makeText(ReceivedDocketDetailActivity.this,"CALL API FAIL!!!"
                            ,CustomToast.LENGTH_LONG,CustomToast.ERROR).show();
                }
            });
        }
        else {
            deleteInventotyInOldRDD();
            for(ReceivedDocketDetail item : rddAdapter.getRddList()) {
                if(item.getId()==0) {
                    postReceivedDocketDetail(item);
                }
                else {
                    putReceivedDocketDetail(item);
                }
            }
        }
    }

    private void  huy() {
        CustomAlertDialog alertDialog = new CustomAlertDialog(this);
        //alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //DisplayMetrics metrics=getResources().getDisplayMetrics();
        //alertDialog.getWindow().setLayout((7*metrics.widthPixels)/8, WindowManager.LayoutParams.WRAP_CONTENT);
        alertDialog.setCancelable(false);
        alertDialog.show();

        alertDialog.setMessage("Nếu thoát những Thay đổi/Thêm mới sẽ không được lưu!\nVẫn thoát?");
        alertDialog.setBtnPositive("Thoát");
        alertDialog.setBtnNegative("Không");

        alertDialog.btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        alertDialog.btnNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });
    }

    private void dateTimePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener onDateSetListener = (datePicker, i1, i2, i3) -> {
            calendar.set(Calendar.YEAR, i1);
            calendar.set(Calendar.MONTH, i2);
            calendar.set(Calendar.DAY_OF_MONTH, i3);

            TimePickerDialog.OnTimeSetListener onTimeSetListener = (timePicker, i4, i5) -> {
                calendar.set(Calendar.HOUR_OF_DAY, i4);
                calendar.set(Calendar.MINUTE, i5);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                etNgayDat.setText(simpleDateFormat.format(calendar.getTime()));
            };

            new TimePickerDialog(this,onTimeSetListener
                    ,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true)
                    .show();
        };

        new DatePickerDialog(this,onDateSetListener
                ,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    private void getReceivedDocket(int maPN) {
        ApiUtils.getReceivedDocketService().getReceivedDocket(maPN).enqueue(new Callback<ReceivedDocket>() {
            @Override
            public void onResponse(Call<ReceivedDocket> call, Response<ReceivedDocket> response) {
                if(response.isSuccessful()) {
                    ReceivedDocket rd = response.body();
                    if(rd==null) {
                        CustomToast.makeText(ReceivedDocketDetailActivity.this,"rd==null"
                                ,CustomToast.LENGTH_LONG,CustomToast.ERROR).show();
                        finish();
                    }
                    else {
                        rddAdapter.setRddList(rd.getReceivedDocketDetails());
                        tvTittle.setText("SỬA PHIẾU NHẬP");
                        tvMaPN.setText(rd.getId()+"");
                        tvMaNV.setText(rd.getEmployee_id()+"");
                        etNgayDat.setText(rd.getCreatedAt());
                        etNhaCungCap.setText(rd.getSupplier_name());
                        tvTongGiaTri.setText(NumberFormat.getNumberInstance(Locale.US).format(rddAdapter.getTotalList())+"VND");
                        btnTaoPhieuNhap.setText("CẬP NHẬT");
                    }
                }
                else {
                    try {
                        Gson g = new Gson();
                        RestErrorResponse errorResponse = g.fromJson(response.errorBody().string(),RestErrorResponse.class);
                        CustomToast.makeText(ReceivedDocketDetailActivity.this,"TRY: " + errorResponse.getMessage()
                                ,CustomToast.LENGTH_LONG,CustomToast.ERROR).show();
                    }
                    catch (Exception e) {
                        CustomToast.makeText(ReceivedDocketDetailActivity.this,"CATCH: " + e.getMessage()
                                ,CustomToast.LENGTH_LONG,CustomToast.ERROR).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ReceivedDocket> call, Throwable t) {
                CustomToast.makeText(ReceivedDocketDetailActivity.this,"CALL API FAIL!!!"
                        ,CustomToast.LENGTH_LONG,CustomToast.ERROR).show();
            }
        });
    }

    private void capNhatDuLieu() {
        rddAdapter.notifyDataSetChanged();
        tvTongGiaTri.setText(NumberFormat.getNumberInstance(Locale.US).format(rddAdapter.getTotalList())+"VND");
    }

    private void postReceivedDocketDetail(ReceivedDocketDetail item) {
        ApiUtils.getReceivedDocketService().postReceivedDocketDetail(item).enqueue(new Callback<ReceivedDocketDetail>() {
            @Override
            public void onResponse(Call<ReceivedDocketDetail> call, Response<ReceivedDocketDetail> response) {
                if(response.isSuccessful()) {
                    Log.e("postRDD","SUCCESSFUL");
                    //update product.inventory
                    //putProduct(item);
                }
                else {
                    try {
                        Gson g = new Gson();
                        RestErrorResponse errorResponse = g.fromJson(response.errorBody().string(),RestErrorResponse.class);
                        Log.e("TRY: ",errorResponse.getMessage());
                    }
                    catch (Exception e) {
                        Log.e("CATCH: ", e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ReceivedDocketDetail> call, Throwable t) {
                CustomToast.makeText(ReceivedDocketDetailActivity.this,"CALL API FAIL!!!"
                        ,CustomToast.LENGTH_LONG,CustomToast.ERROR).show();
            }
        });
    }

    private void putReceivedDocketDetail(ReceivedDocketDetail item) {
        ApiUtils.getReceivedDocketService().putReceivedDocketDetail(item.getId(),item).enqueue(new Callback<ReceivedDocketDetail>() {
            @Override
            public void onResponse(Call<ReceivedDocketDetail> call, Response<ReceivedDocketDetail> response) {
                if(response.isSuccessful()) {
                    Log.e("putRDD","SUCCESSFUL");
                    putProduct(item);
                }
                else {
                    try {
                        Gson g = new Gson();
                        RestErrorResponse errorResponse = g.fromJson(response.errorBody().string(),RestErrorResponse.class);
                        CustomToast.makeText(ReceivedDocketDetailActivity.this,"TRY: " + errorResponse.getMessage()
                                ,CustomToast.LENGTH_LONG,CustomToast.ERROR).show();
                        errorResponse.getMessage();

                    }
                    catch (Exception e) {
                        CustomToast.makeText(ReceivedDocketDetailActivity.this,"CATCH: " + e.getMessage()
                                ,CustomToast.LENGTH_LONG,CustomToast.ERROR).show();
                        e.getMessage();
                    }
                }
            }

            @Override
            public void onFailure(Call<ReceivedDocketDetail> call, Throwable t) {
                CustomToast.makeText(ReceivedDocketDetailActivity.this,"CALL API FAIL!!!"
                        ,CustomToast.LENGTH_LONG,CustomToast.ERROR).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        capNhatDuLieu();
    }

    private void putProduct(ReceivedDocketDetail item) {
        ApiUtils.getProductService().getProduct(item.getProductId()).enqueue(new Callback<Product2>() {
            @Override
            public void onResponse(Call<Product2> call, Response<Product2> response) {
                if(response.isSuccessful()) {
                    Product2 product2 = response.body();
                    int a = product2.getInventory()+item.getQuantity();
                    Log.e("a",a+"");
                    product2.setInventory(a);
                    product2.setPrice(item.getPrice());
                    ApiUtils.getProductService().putProduct(product2.getId(),product2).enqueue(new Callback<Product2>() {
                        @Override
                        public void onResponse(Call<Product2> call, Response<Product2> response) {
                            if(response.isSuccessful()) {
                                Product2 product21 = response.body();
                                Log.e("edit inventory",product21.toString());
                            }
                            else {
                                Log.e("edit inventory","failed");
                            }
                        }

                        @Override
                        public void onFailure(Call<Product2> call, Throwable t) {
                            Log.e("edit inventory","CALL API FAIL");
                        }
                    });
                }
                else {
                    Log.e("edit inventory: get product","failed");
                }
            }

            @Override
            public void onFailure(Call<Product2> call, Throwable t) {
                Log.e("edit inventory: get product","CALL API FAIL");
            }
        });
    }

    private void deleteInventotyInOldRDD() {
        if(rddAdapter.getRddListOld()==null) {
            return;
        }
        for(ReceivedDocketDetail item : rddAdapter.getRddListOld()) {
            ApiUtils.getProductService().getProduct(item.getProductId()).enqueue(new Callback<Product2>() {
                @Override
                public void onResponse(Call<Product2> call, Response<Product2> response) {
                    if(response.isSuccessful()) {
                        Product2 product2 = response.body();
                        int a = product2.getInventory()-item.getQuantity();
                        ApiUtils.getProductService().putProduct(product2.getId(),product2).enqueue(new Callback<Product2>() {
                            @Override
                            public void onResponse(Call<Product2> call, Response<Product2> response) {
                                if(response.isSuccessful()) {
                                    Product2 product21 = response.body();
                                    Log.e("deleteInventotyInOldRDD",product21.toString());
                                }
                                else {
                                    Log.e("deleteInventotyInOldRDD","failed");
                                }
                            }

                            @Override
                            public void onFailure(Call<Product2> call, Throwable t) {
                                Log.e("deleteInventotyInOldRDD","CALL API FAIL");
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<Product2> call, Throwable t) {
                    Log.e("deleteInventotyInOldRDD: get product","CALL API FAIL");
                }
            });
        }
    }
}