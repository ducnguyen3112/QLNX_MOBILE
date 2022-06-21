package com.example.quanlynhapxuat.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.quanlynhapxuat.R;
import com.example.quanlynhapxuat.activity.main.LoginActivity;
import com.example.quanlynhapxuat.activity.main.MainActivity;
import com.example.quanlynhapxuat.api.ApiUtils;
import com.example.quanlynhapxuat.model.DeliveryDocket;
import com.example.quanlynhapxuat.model.DeliveryDocketDetail;
import com.example.quanlynhapxuat.model.Product;
import com.example.quanlynhapxuat.service.DeliveryDocketDetailService;
import com.example.quanlynhapxuat.service.ProductService;
import com.example.quanlynhapxuat.utils.Convert;
import com.example.quanlynhapxuat.utils.CustomAlertDialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ChiTietPXAdapter extends RecyclerView.Adapter<ChiTietPXAdapter.ChiTietPXViewHolder> {
    private Context context;
    private List<DeliveryDocketDetail> list;
    public static int status=0;
    List<Product> productList;
    Map<Integer,Product> productMap;


    public void setData(List<DeliveryDocketDetail> list,Context context ) {
        this.list = list;
        this.context=context;
    }

    @NonNull
    @Override
    public ChiTietPXViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=null;
        if (status==0){
            view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ctpx,
                    parent,false);
        }else{
            view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_taoctpx,
                    parent,false);
        }

        return new ChiTietPXViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChiTietPXViewHolder holder, int position) {
        DeliveryDocketDetail item= list.get(position);

        if (item==null){
            return;
        }
        ApiUtils.getProductService().getAllProduct().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()){
                    productMap=new HashMap<>();
                    productList=response.body();
                    for (Product p :
                            productList) {
                        productMap.put(p.getId(),p);

                    }
                    holder.tv_tensppx.setText(productMap.get(item.getProductId()).getName());
                    Glide.with(context)
                            .load(productMap.get(item.getProductId()).getImage())
                            .into(holder.ivSPPX);
                }else{
                    Log.e("chitietpx", "khoong co product" );
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.e("chitietpx", t.getMessage() );
            }
        });

        holder.tv_slsppx.setText(String.valueOf(item.getQuantity()));
        holder.tv_dongiasppx.setText(Convert.currencyFormat(item.getPrice()));
        holder.tv_tonggiasppx.setText(Convert.currencyFormat(item.getPrice()*item.getQuantity()));
        if (status==1) {
            holder.ivDeletectpx.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.getDeliveryDocketId() == 0) {
                        list.remove(item);
                        notifyDataSetChanged();
                        Toast.makeText(context, "List size: " + list.size(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if (list !=null){
            return list.size();
        }
        return 0;
    }
    public class ChiTietPXViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_tensppx,tv_tonggiasppx,tv_dongiasppx,tv_slsppx;
        private ImageView ivSPPX,ivDeletectpx;

        public ChiTietPXViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_tensppx=itemView.findViewById(R.id.tv_tensppx);
            tv_tonggiasppx=itemView.findViewById(R.id.tv_tonggiasppx);
            tv_dongiasppx=itemView.findViewById(R.id.tv_dongiasppx);
            tv_slsppx=itemView.findViewById(R.id.tv_sl_sp_px);
            ivSPPX=itemView.findViewById(R.id.iv_sppx);
            ivDeletectpx=itemView.findViewById(R.id.iv_delete_ctpx);
        }
    }
}