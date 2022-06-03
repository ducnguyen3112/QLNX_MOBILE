package com.example.quanlynhapxuat.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlynhapxuat.R;
import com.example.quanlynhapxuat.activity.KhachHang.ChiTietActivity;
import com.example.quanlynhapxuat.model.DeliveryDocket;
import com.example.quanlynhapxuat.model.KhachHang;

import java.text.SimpleDateFormat;
import java.util.List;


public class LichSuAdapter extends RecyclerView.Adapter<LichSuAdapter.LichSuViewHolder> {

    private Context context;
    private KhachHang kh;
    private List<DeliveryDocket>  list;

    public LichSuAdapter(Context context) {
        this.context = context;
    }


    public void setData(KhachHang kh,List<DeliveryDocket> list) {
        this.kh = kh;
        this.list = list;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public LichSuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dd, parent, false);
        return new LichSuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LichSuViewHolder holder, int position) {
        DeliveryDocket dto = list.get(position);
        if (dto == null) {
            return;
        }
        holder.tv_idDD.setText(dto.getId() + "");
        holder.tv_dateDD.setText(dto.getCreatedAt());
        if(dto.getStatus() == 1) {
            holder.tv_statusDD.setText("Hoàn thành");
        }else {
            holder.tv_statusDD.setText("Đang xử lý");
        }

        holder.tv_totalDD.setText(dto.getTotal() + "");
        holder.lichSuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChiTietActivity.class);
                intent.putExtra("DD", dto);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    public class LichSuViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_idDD, tv_dateDD, tv_statusDD, tv_totalDD, tv_nameKH;
        private CardView lichSuLayout;


        public LichSuViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_idDD = itemView.findViewById(R.id.tv_idDD);
            tv_dateDD = itemView.findViewById(R.id.tv_dateDD);
            tv_statusDD = itemView.findViewById(R.id.tv_statusDD);
            tv_totalDD = itemView.findViewById(R.id.tv_totalDD);
            lichSuLayout = itemView.findViewById(R.id.lichSuLayout);
        }
    }
}
