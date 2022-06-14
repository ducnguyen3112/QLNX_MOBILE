package com.example.quanlynhapxuat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlynhapxuat.R;
import com.example.quanlynhapxuat.model.Product;
import com.example.quanlynhapxuat.model.Product2;

import java.util.ArrayList;

public class ProductFragmentAdapter extends RecyclerView.Adapter<ProductFragmentAdapter.ProductFragmentViewHolder> {
    private Context context;
    private ArrayList<Product2> products;

    public ProductFragmentAdapter(Context context) {
        this.context = context;
    }

    public void setProducts(ArrayList<Product2> products) {
        this.products = products;
    }

    @NonNull
    @Override
    public ProductFragmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product,parent,false);
        return new ProductFragmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductFragmentViewHolder holder, int position) {
        Product2 product = products.get(position);
        if(product==null) {
            return;
        }

        holder.tvTenSP.setText(product.getName());
        if(product.getStatus()==1) {
            holder.swTrangThai.setChecked(true);
        }
        else {
            holder.swTrangThai.setChecked(false);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
            }
        });
    }

    @Override
    public int getItemCount() {
        return products == null ?
                0 : products.size();
    }

    public class ProductFragmentViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivAnhSP;
        private TextView tvTenSP;
        private Switch swTrangThai;

        public ProductFragmentViewHolder(@NonNull View itemView) {
            super(itemView);

            ivAnhSP = itemView.findViewById(R.id.ivAnhSP_itemProduct);
            tvTenSP = itemView.findViewById(R.id.tvTenSP_itemProduct);
            swTrangThai = itemView.findViewById(R.id.swTrangThai_itemProduct);
        }
    }
}
