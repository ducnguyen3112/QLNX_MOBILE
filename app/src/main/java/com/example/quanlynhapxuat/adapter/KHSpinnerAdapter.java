package com.example.quanlynhapxuat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.quanlynhapxuat.R;
import com.example.quanlynhapxuat.model.KhachHang;

import java.util.List;

public class KHSpinnerAdapter extends BaseAdapter {
    private Context context;
    public List<KhachHang> khachHangs;

    public KHSpinnerAdapter(Context context,List<KhachHang> khachHangs){
        this.context=context;
        this.khachHangs=khachHangs;
    }
    @Override
    public int getCount() {
        return khachHangs!=null ? khachHangs.size():0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        KhachHang khachHang=khachHangs.get(i);
        View rootView  = LayoutInflater.from(context).inflate(R.layout.item_khachhang_spinner,
                viewGroup,false);
        TextView tvTen=rootView.findViewById(R.id.tv_TenKHSpinner);
        ImageView ivAvatar=rootView.findViewById(R.id.iv_KHSpinner);

        tvTen.setText( khachHang.getFullName());
        if(khachHangs.get(i).getAvatar()!=null){
            Glide.with(context).load(khachHang.getAvatar()).into(ivAvatar);
        }else {
            ivAvatar.setImageResource(R.drawable.custom_person_icon);
        }
        return rootView;
        }
    }
