package com.example.quanlynhapxuat.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlynhapxuat.R;
import com.example.quanlynhapxuat.activity.main.MainActivity;
import com.example.quanlynhapxuat.activity.product.NewProductActivity;
import com.example.quanlynhapxuat.adapter.ProductFragmentAdapter;
import com.example.quanlynhapxuat.api.ApiUtils;
import com.example.quanlynhapxuat.model.Product;
import com.example.quanlynhapxuat.model.Product2;
import com.example.quanlynhapxuat.model.RestErrorResponse;
import com.example.quanlynhapxuat.utils.CustomToast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductFragment extends Fragment {
    private ProductFragmentAdapter adapter;
    private ArrayList<Product2> products;

    private TextView tvTotal;
    private RecyclerView rcvProducts;
    private FloatingActionButton flbThemSP;

    public ProductFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);

        //setControl
        tvTotal = view.findViewById(R.id.tvTotal_fragmentProduct);
        rcvProducts = view.findViewById(R.id.rcvProducts_fragmentProduct);
        flbThemSP = view.findViewById(R.id.flbThemSP_fragmentProduct);

        //showProducts
        rcvProducts.setLayoutManager(new LinearLayoutManager((MainActivity) getActivity()));
        adapter = new ProductFragmentAdapter((MainActivity) getContext());
        getProducts();
        //rcvProducts.setAdapter(adapter);

        //setEvents
        flbThemSP.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), NewProductActivity.class);
            startActivity(intent);
        });

        return view;
    }

    private void getProducts() {
        ApiUtils.getProductService().getProducts().enqueue(new Callback<ArrayList<Product2>>() {
            @Override
            public void onResponse(Call<ArrayList<Product2>> call, Response<ArrayList<Product2>> response) {
                if(response.isSuccessful()) {
                    products = response.body();
                    if(products==null) {
                        CustomToast.makeText(getContext(),"Danh sách sản phẩm rỗng!"
                                ,CustomToast.LENGTH_SHORT,CustomToast.SUCCESS).show();
                    }
                    else {
                        adapter.setProducts(products);
                        rcvProducts.setAdapter(adapter);
                        tvTotal.setText(adapter.getItemCount()+"");
                        Log.e("products.size()",products.size()+"");
                    }
                }
                else {
                    try {
                        Gson g = new Gson();
                        RestErrorResponse errorResponse = g.fromJson(response.errorBody().string(),RestErrorResponse.class);
                        CustomToast.makeText(getContext(),"TRY: " + errorResponse.getMessage()
                                ,CustomToast.LENGTH_LONG,CustomToast.ERROR).show();
                    }
                    catch (Exception e) {
                        CustomToast.makeText(getContext(),"CATCH: " + e.getMessage()
                                ,CustomToast.LENGTH_LONG,CustomToast.ERROR).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Product2>> call, Throwable t) {
                CustomToast.makeText(getContext(),"CALL API FAIL!!!"
                        ,CustomToast.LENGTH_LONG,CustomToast.ERROR).show();
            }
        });
    }
}