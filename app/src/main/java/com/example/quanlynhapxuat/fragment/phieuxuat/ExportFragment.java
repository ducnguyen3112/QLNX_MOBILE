package com.example.quanlynhapxuat.fragment.phieuxuat;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlynhapxuat.R;
import com.example.quanlynhapxuat.activity.main.LoginActivity;
import com.example.quanlynhapxuat.activity.main.MainActivity;
import com.example.quanlynhapxuat.adapter.KhachHangAdapter;
import com.example.quanlynhapxuat.adapter.PhieuXuatAdapter;
import com.example.quanlynhapxuat.model.DeliveryDocket;
import com.example.quanlynhapxuat.model.KhachHang;
import com.example.quanlynhapxuat.service.DeliveryDocketService;
import com.example.quanlynhapxuat.utils.Convert;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ExportFragment extends Fragment {

    private RecyclerView rcvPX;
    private SearchView searchView;
    private PhieuXuatAdapter phieuXuatAdapter;
    private View mView;
    private TextView tvTongSLPX,tvTongGiaTriPX;
    private MainActivity mainActivity;
    private ImageButton btnAddpx;
    private List<DeliveryDocket> deliveryDockets;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView= inflater.inflate(R.layout.fragment_export, container, false);
        mainActivity= (MainActivity) getActivity();
        rcvPX=mView.findViewById(R.id.rcv_px);
        tvTongSLPX=mView.findViewById(R.id.tv_tongslpx);
        tvTongGiaTriPX=mView.findViewById(R.id.tv_tonggiatripx);
        searchView=mView.findViewById(R.id.sv_px);
        btnAddpx=mView.findViewById(R.id.btn_addpx);
        searchView.clearFocus();
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(mainActivity);
        rcvPX.setLayoutManager(linearLayoutManager);
        phieuXuatAdapter=new PhieuXuatAdapter();
        getAllExport();
        rcvPX.setAdapter(phieuXuatAdapter);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                phieuXuatAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                phieuXuatAdapter.getFilter().filter(newText);
                return true;
            }
        });
        btnAddpx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.goToAddExport();
            }
        });


        return mView;
    }



    public void getAllExport(){
        DeliveryDocketService.deliveryDocketService.getAllDeliveryDocket().enqueue(new Callback<List<DeliveryDocket>>() {
            @Override
            public void onResponse(Call<List<DeliveryDocket>> call, Response<List<DeliveryDocket>> response) {
                if (response.isSuccessful()) {
                    List<DeliveryDocket> list = response.body();
                    if (list.isEmpty()){
                        Toast.makeText(mainActivity,"Danh sách rỗng!",
                                Toast.LENGTH_SHORT).show();
                    }else{
                        deliveryDockets=list;
                        tvTongSLPX.setText(String.valueOf(list.size()));
                       tvTongGiaTriPX.setText(Convert.currencyFormat(phieuXuatAdapter.getGiatriTatCaPX(list)));
                        phieuXuatAdapter.setData(list, deliveryDocket -> mainActivity.goToDeliveryDetail(deliveryDocket));


                    }
                }else{
                    Toast.makeText(mainActivity,"Lỗi! Không thể lấy danh sách!",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<DeliveryDocket>> call, Throwable t) {
                Log.e("exportfragment", t.getMessage() );
            }
        });
    }


}