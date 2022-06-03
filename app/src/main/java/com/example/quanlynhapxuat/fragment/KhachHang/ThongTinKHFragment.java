package com.example.quanlynhapxuat.fragment.KhachHang;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.bumptech.glide.Glide;
import com.example.quanlynhapxuat.R;
import com.example.quanlynhapxuat.activity.KhachHang.AddKHActivity;
import com.example.quanlynhapxuat.activity.KhachHang.ListKHActivity;
import com.example.quanlynhapxuat.activity.KhachHang.ProfileKHActivity;
import com.example.quanlynhapxuat.activity.KhachHang.UpdateKHActivity;
import com.example.quanlynhapxuat.model.KhachHang;


public class ThongTinKHFragment extends Fragment {

    private ImageView ivTTAvatar;
    private EditText edtTTName, edtTTAdress, edtTTPhone, edtTTEmail;
    private Button btnCancelTT;
    private KhachHang dto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_thong_tin_k_h, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Thông Tin Khách Hàng");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        dto = (KhachHang) args.getSerializable("KH");
        setControl(view);
        if (dto.getAvatar() != null) {
            Glide.with(getContext())
                    .load(dto.getAvatar())
                    .circleCrop()
                    .into(ivTTAvatar);
        }
        edtTTName.setText(dto.getFullName());
        edtTTAdress.setText(dto.getAddress());
        edtTTPhone.setText(dto.getPhoneNumber());
        edtTTEmail.setText(dto.getEmail());
        setEvent();
    }

    private void setControl(View view) {
        ivTTAvatar = view.findViewById(R.id.ivTTAvatar);
        edtTTName = view.findViewById(R.id.edtTTName);
        edtTTAdress = view.findViewById(R.id.edtTTAdress);
        edtTTPhone = view.findViewById(R.id.edtTTPhone);
        edtTTEmail = view.findViewById(R.id.edtTTEmail);
        btnCancelTT = view.findViewById(R.id.btnCancelTT);
    }

    private void setEvent() {
        btnCancelTT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ListKHActivity.class);
                startActivity(intent);
            }
        });
    }
}