package com.example.quanlynhapxuat.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.quanlynhapxuat.R;
import com.example.quanlynhapxuat.activity.Employee.ListEmployeeActivity;
import com.example.quanlynhapxuat.activity.EmployeesActivity;
import com.example.quanlynhapxuat.activity.KhachHang.ListKHActivity;
import com.example.quanlynhapxuat.activity.main.LoginActivity;
import com.example.quanlynhapxuat.activity.main.MainActivity;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

public class MoreFragment extends Fragment {

    Button btn_nv, btn_kh, btn_dx;
    ImageView civAvatar;
    TextView tvFullNameLogin;
    MainActivity mainActivity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainActivity= (MainActivity) getActivity();
        return inflater.inflate(R.layout.fragment_more, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            setControl(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
        setEvent();

    }

    private void setControl(View view) throws IOException {
        btn_nv = view.findViewById(R.id.btn_nv);
        btn_kh = view.findViewById(R.id.btn_kh);
        btn_dx = view.findViewById(R.id.btn_dx);
        civAvatar=view.findViewById(R.id.civAvatar);
        tvFullNameLogin=view.findViewById(R.id.tv_fullnameLogin);
        tvFullNameLogin.setText(LoginActivity.nameLogin);


            Glide.with(mainActivity)
                    .load(LoginActivity.urlAvatar)
                    .into(civAvatar);

    }

    private void setEvent() {
        btn_nv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ListEmployeeActivity.class);
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