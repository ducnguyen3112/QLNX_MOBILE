package com.example.quanlynhapxuat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.quanlynhapxuat.R;
import com.example.quanlynhapxuat.model.Product;

import java.util.ArrayList;

public class SPSpinnerAdapter extends BaseAdapter {
    private Context context;
    public ArrayList<Product> productList;

    public SPSpinnerAdapter(Context context, ArrayList<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @Override
    public int getCount() {
        return productList==null ? 0 : productList.size();
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
        View v = LayoutInflater.from(context).inflate(R.layout.item_spinner_sp,viewGroup,false);
        TextView tvTenSP = v.findViewById(R.id.tvTenSP_itemSpinnerSP);

        Product product = productList.get(i);
        tvTenSP.setText(product.getName());

        return v;
    }
}
