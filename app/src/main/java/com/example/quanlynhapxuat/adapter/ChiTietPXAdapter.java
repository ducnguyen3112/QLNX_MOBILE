package com.example.quanlynhapxuat.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.example.quanlynhapxuat.model.DeliveryDocket;
import com.example.quanlynhapxuat.model.DeliveryDocketDetail;
import com.example.quanlynhapxuat.service.DeliveryDocketDetailService;
import com.example.quanlynhapxuat.utils.Convert;
import com.example.quanlynhapxuat.utils.CustomAlertDialog;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChiTietPXAdapter extends RecyclerView.Adapter<ChiTietPXAdapter.ChiTietPXViewHolder> {
    private Context context;
    private List<DeliveryDocketDetail> list;
    public static int status=0;


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
        if (item.getDeliveryDocketId()!=0){
        }
        if (item==null){
            return;
        }
        holder.tv_tensppx.setText("product"+item.getId());
        holder.tv_slsppx.setText(String.valueOf(item.getQuantity()));
        holder.tv_dongiasppx.setText(Convert.currencyFormat(item.getPrice()));
        holder.tv_tonggiasppx.setText(Convert.currencyFormat(item.getPrice()*item.getQuantity()));
        Glide.with(context)
                .load("https://res.cloudinary.com/shoesstation/image/upload/v1652698504/nike-air-force-1-low_pvldic.png")
                .into(holder.ivSPPX);
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