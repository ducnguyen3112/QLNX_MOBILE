package com.example.quanlynhapxuat.fragment.phieunhap;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.quanlynhapxuat.R;
import com.example.quanlynhapxuat.activity.ReceivedDocket.ReceivedDocketDetailActivity;
import com.example.quanlynhapxuat.activity.main.MainActivity;
import com.example.quanlynhapxuat.adapter.ReceivedDocketAdapter;
import com.example.quanlynhapxuat.api.ApiUtils;
import com.example.quanlynhapxuat.model.ReceivedDocket;
import com.example.quanlynhapxuat.model.RestErrorResponse;
import com.example.quanlynhapxuat.utils.Convert;
import com.example.quanlynhapxuat.utils.CustomToast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImportFragment extends Fragment {
    private ReceivedDocketAdapter receivedDocketAdapter;
    private ArrayList<ReceivedDocket> receivedDocketList;

    private RecyclerView rcvListPhieuNhap;
    private TextView tvSLPhieuNhap;
    private TextView tvTotal;
    private FloatingActionButton flbThemPhieuNhap;

    public ImportFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_import, container, false);

        //setControl
        tvSLPhieuNhap = view.findViewById(R.id.tvSLPhieuNhap_fragmentImport);
        tvTotal = view.findViewById(R.id.tvTotal_fragmentImport );
        flbThemPhieuNhap = view.findViewById(R.id.flbThemPhieuNhap_fragmentImport);
        rcvListPhieuNhap = view.findViewById(R.id.rcvListPhieuNhap_fragmentImport);

        //get&showList
        receivedDocketAdapter = new ReceivedDocketAdapter(getActivity());
        rcvListPhieuNhap.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcvListPhieuNhap.setAdapter(receivedDocketAdapter);

        capNhatDuLieu();


        //setEvent
        flbThemPhieuNhap.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(),ReceivedDocketDetailActivity.class);
            intent.putExtra("maPN", 0); //m?? phi???u nh???p 0 l?? th??m m???i
            startActivity(intent);
        });

        return view;
    }

    private void capNhatDuLieu() {
        getReceivedDocketList();
        receivedDocketAdapter.notifyDataSetChanged();
    }

    private void getReceivedDocketList() {
        ApiUtils.getReceivedDocketService().getReceivedDocketList().enqueue(new Callback<ArrayList<ReceivedDocket>>() {
            @Override
            public void onResponse(Call<ArrayList<ReceivedDocket>> call, Response<ArrayList<ReceivedDocket>> response) {
                if(response.isSuccessful()) {
                    receivedDocketList = response.body();
                    if(receivedDocketList==null) {
                        CustomToast.makeText((MainActivity) getActivity(),"Danh s??ch phi???u nh???p r???ng!"
                                ,CustomToast.LENGTH_SHORT,CustomToast.SUCCESS).show();
                    }
                    else {
                        Collections.reverse(receivedDocketList);
                        receivedDocketAdapter.setReceivedDocketList(receivedDocketList);
                        tvSLPhieuNhap.setText(receivedDocketAdapter.getItemCount()+"");
                        tvTotal.setText(Convert.currencyFormat(receivedDocketAdapter.getTotalList())+" VND");

                    }
                }
                else {
                    try {
                        Gson g = new Gson();
                        RestErrorResponse errorResponse = g.fromJson(response.errorBody().string(),RestErrorResponse.class);
                        CustomToast.makeText((MainActivity) getActivity(),"TRY: " + errorResponse.getMessage()
                                ,CustomToast.LENGTH_LONG,CustomToast.ERROR).show();
                    }
                    catch (Exception e) {
                        CustomToast.makeText((MainActivity) getActivity(),"CATCH: " + e.getMessage()
                                ,CustomToast.LENGTH_LONG,CustomToast.ERROR).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ReceivedDocket>> call, Throwable t) {
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        capNhatDuLieu();
    }
}