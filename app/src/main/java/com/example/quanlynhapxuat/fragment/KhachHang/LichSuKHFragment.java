package com.example.quanlynhapxuat.fragment.KhachHang;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quanlynhapxuat.R;
import com.example.quanlynhapxuat.adapter.LichSuAdapter;
import com.example.quanlynhapxuat.model.KhachHang;


public class LichSuKHFragment extends Fragment{

    private RecyclerView rcvLS;
    private LichSuAdapter lichSuAdapter;
    private KhachHang dto;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lich_su_k_h, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        assert args != null;
        dto = (KhachHang) args.getSerializable("KH");
        setControl(view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcvLS.setLayoutManager(linearLayoutManager);
        lichSuAdapter = new LichSuAdapter(getContext());
        lichSuAdapter.setData(dto,dto.getDeliveryDockets());
        rcvLS.setAdapter(lichSuAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Lịch Sử");
    }



    private void setControl(View view) {
        rcvLS = view.findViewById(R.id.rcv_ls);
    }

}