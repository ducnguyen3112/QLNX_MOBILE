package com.example.quanlynhapxuat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlynhapxuat.R;
import com.example.quanlynhapxuat.api.ApiUtils;
import com.example.quanlynhapxuat.model.DeliveryDocketDetail;
import com.example.quanlynhapxuat.model.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChiTietLishSuAdapter extends RecyclerView.Adapter<ChiTietLishSuAdapter.ChiTietLishSuViewHolder> {

    private Context context;
    private List<DeliveryDocketDetail> list;

    public ChiTietLishSuAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<DeliveryDocketDetail> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ChiTietLishSuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ddd, parent, false);
        return new ChiTietLishSuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChiTietLishSuViewHolder holder, int position) {
        DeliveryDocketDetail dto = list.get(position);
        if (dto == null) {
            return;
        }
        String id = dto.getId() + "";
        String price = ((float) dto.getPrice()) + "";
        String quanity = dto.getQuantity() + "";
        String total = (dto.getPrice() * dto.getQuantity()) + "";
        ApiUtils.getProductService().getProductById(dto.getProductId()).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                Product product = response.body();
                holder.tv_nameProductDDD.setText(product.getName());
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {

            }
        });
        holder.tv_idDDD.setText(id);
        holder.tv_priceDDD.setText(price);
        holder.tv_quanityDDD.setText(quanity);
        holder.tv_totalDDD.setText(total);

    }


    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    public class ChiTietLishSuViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_idDDD, tv_nameProductDDD, tv_priceDDD, tv_totalDDD,tv_quanityDDD;


        public ChiTietLishSuViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_idDDD = itemView.findViewById(R.id.tv_idDDD);
            tv_nameProductDDD = itemView.findViewById(R.id.tv_nameProductDDD);
            tv_quanityDDD = itemView.findViewById(R.id.tv_quanityDDD);
            tv_priceDDD = itemView.findViewById(R.id.tv_priceDDD);
            tv_totalDDD = itemView.findViewById(R.id.tv_totalDDD);
        }
    }
}
