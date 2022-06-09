package com.example.quanlynhapxuat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlynhapxuat.R;
import com.example.quanlynhapxuat.api.ApiUtils;
import com.example.quanlynhapxuat.model.DeliveryDocket;
import com.example.quanlynhapxuat.model.DeliveryDocketDetail;
import com.example.quanlynhapxuat.model.KhachHang;
import com.example.quanlynhapxuat.utils.Convert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhieuXuatAdapter extends RecyclerView.Adapter<PhieuXuatAdapter.PXViewHolder>
implements Filterable {


    private List<DeliveryDocket> deliveryDockets;
    private List<DeliveryDocket> deliveryDocketOlds;
    private IclickItemListener itemListener;
    private Map<Integer,String> khachHangMap;

    public PhieuXuatAdapter() {
    }
    public interface IclickItemListener{
        void onclickItem(DeliveryDocket deliveryDocket);
    }


    public void setData(List<DeliveryDocket> list,IclickItemListener listener ) {
        this.deliveryDocketOlds = list;
        this.deliveryDockets = list;
        this.itemListener=listener;
        this.khachHangMap=Convert.customerMap();

    }
    public class PXViewHolder extends RecyclerView.ViewHolder{
        private TextView tvGiaPX,tvMaPX,tvTenKH,tvNgay,tvStatus;

        public PXViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGiaPX=itemView.findViewById(R.id.tv_giatripx);
            tvMaPX=itemView.findViewById(R.id.tv_mapx);
            tvTenKH=itemView.findViewById(R.id.tv_tenkhpx);
            tvNgay=itemView.findViewById(R.id.tv_ngaypx);
            tvStatus=itemView.findViewById(R.id.tv_trangthai);
        }
    }
    @NonNull
    @Override
    public PXViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_phieuxuat,parent,false);
        return new PXViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PXViewHolder holder, int position) {
        DeliveryDocket deliveryDocket=deliveryDockets.get(position);
        if (deliveryDocket==null){
            return;
        }
        int id =deliveryDocket.getId();
        holder.tvMaPX.setText(String.valueOf(id));
        holder.tvNgay.setText(deliveryDocket.getCreatedAt());
        if(deliveryDocket.getStatus()==1){
            holder.tvStatus.setText("Đang xử lí");
        }else if(deliveryDocket.getStatus()==2){
            holder.tvStatus.setText("Hoàn thành");
        }else if(deliveryDocket.getStatus()==0) {
            holder.tvStatus.setText("Đã hủy");
        }
        holder.tvNgay.setText(deliveryDocket.getCreatedAt());
        int tong=0;
        if (deliveryDockets.get(position).getDeliveryDocketDetails()!=null) {
            for (DeliveryDocketDetail item : deliveryDockets.get(position).getDeliveryDocketDetails()) {
                tong += item.getPrice()*item.getQuantity();
            }
        }
        holder.tvGiaPX.setText(Convert.currencyFormat(tong));
        ApiUtils.getKhachHangService().getKHById(deliveryDocket.getCustomerId()).enqueue(new Callback<KhachHang>() {
            @Override
            public void onResponse(Call<KhachHang> call, Response<KhachHang> response) {
                KhachHang khachHang=response.body();
                holder.tvTenKH.setText(khachHang.getFullName());
            }

            @Override
            public void onFailure(Call<KhachHang> call, Throwable t) {

            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemListener.onclickItem(deliveryDocket);
                ChiTietPXAdapter.status=0;
            }
        });

    }

    @Override
    public int getItemCount() {
        if (deliveryDockets !=null){
            return deliveryDockets.size();
        }
        return 0;
    }

    public int getGiatriTatCaPX(List<DeliveryDocket> deliveryDockets){
        int tongList=0;
        int tong;
        for (int i = 0; i <deliveryDockets.size(); i++) {
            tong=0;
            for (int j = 0; j < deliveryDockets.get(i).getDeliveryDocketDetails().size(); j++) {
                tong+=deliveryDockets.get(i).getDeliveryDocketDetails().get(j).getPrice();
            }
            tongList+=tong;
        }
        return tongList;
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSeach=constraint.toString();
                if (strSeach.isEmpty()){
                    deliveryDockets=deliveryDocketOlds;
                }else{
                    List<DeliveryDocket> list=new ArrayList<>();
                    for (DeliveryDocket item: deliveryDocketOlds){
                        if (khachHangMap.get(item.getCustomerId()).toLowerCase().contains(strSeach.toLowerCase())){
                            list.add(item);
                        }
                    }
                    deliveryDockets=list;
                }
                FilterResults filterResults=new FilterResults();
                filterResults.values=deliveryDockets;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                deliveryDockets= (List<DeliveryDocket>) results.values;
                notifyDataSetChanged();
            }
        };
    }

}
