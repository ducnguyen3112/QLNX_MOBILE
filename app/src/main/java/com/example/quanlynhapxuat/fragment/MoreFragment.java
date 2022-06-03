package com.example.quanlynhapxuat.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.quanlynhapxuat.R;
import com.example.quanlynhapxuat.activity.EmployeesActivity;
import com.example.quanlynhapxuat.activity.KhachHang.ListKHActivity;

public class MoreFragment extends Fragment {

    Button btn_nv, btn_kh, btn_nh, btn_bc, btn_dx;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_more, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setControl(view);
        setEvent();
    }

    private void setControl(View view) {
        btn_nv = view.findViewById(R.id.btn_nv);
        btn_kh = view.findViewById(R.id.btn_kh);
        btn_nh = view.findViewById(R.id.btn_nh);
        btn_bc = view.findViewById(R.id.btn_bc);
        btn_dx = view.findViewById(R.id.btn_dx);

    }

    private void setEvent() {
        btn_nv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EmployeesActivity.class);
                startActivity(intent);
            }
        });

        btn_kh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ListKHActivity.class);
                startActivity(intent);
            }
        });

    }
}