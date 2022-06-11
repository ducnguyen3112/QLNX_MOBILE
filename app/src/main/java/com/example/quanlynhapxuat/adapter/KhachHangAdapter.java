package com.example.quanlynhapxuat.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.quanlynhapxuat.R;

import com.example.quanlynhapxuat.activity.KhachHang.AddKHActivity;
import com.example.quanlynhapxuat.activity.KhachHang.ListKHActivity;
import com.example.quanlynhapxuat.activity.KhachHang.ProfileKHActivity;
import com.example.quanlynhapxuat.activity.KhachHang.UpdateKHActivity;
import com.example.quanlynhapxuat.api.ApiUtils;
import com.example.quanlynhapxuat.model.KhachHang;
import com.example.quanlynhapxuat.utils.CustomAlertDialog;
import com.example.quanlynhapxuat.utils.CustomToast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class KhachHangAdapter extends RecyclerView.Adapter<KhachHangAdapter.KhachHangViewHolder> implements Filterable {

    private Activity activity;
    private Context context;
    private List<KhachHang> list;
    private List<KhachHang> listOld;

    public KhachHangAdapter(Activity activity, Context context) {
        this.activity = activity;
        this.context = context;
    }

    public void setData(List<KhachHang> list) {
        this.list = list;
        this.listOld = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public KhachHangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kh, parent, false);
        return new KhachHangViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KhachHangViewHolder holder, int position) {
        KhachHang kh = list.get(position);
        if (kh == null) {
            return;
        }

        if (kh.getAvatar() != null) {
            Glide.with(context)
                    .load(kh.getAvatar())
                    .circleCrop()
                    .into(holder.iv_KH);
        }
        holder.tv_tenKH.setText(kh.getFullName());
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProfileKHActivity.class);
                intent.putExtra("KH", kh);
                activity.startActivityForResult(intent, 1);
            }
        });



        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateKHActivity.class);
                intent.putExtra("KH", kh);
                activity.startActivityForResult(intent, 1);
            }
        });

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialog(kh.getId());
            }
        });
    }

    void confirmDialog(int id) {
        CustomAlertDialog alertDialog = new CustomAlertDialog(context);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().setLayout((7 * ListKHActivity.width) / 8, WindowManager.LayoutParams.WRAP_CONTENT);
        alertDialog.show();
        alertDialog.btnPositive.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                ApiUtils.getKhachHangService().deleteKHById(id).enqueue(new Callback<KhachHang>() {
                    @Override
                    public void onResponse(Call<KhachHang> call, Response<KhachHang> response) {
                        CustomToast.makeText(context, "Xoá thành công",
                                CustomToast.LENGTH_SHORT, CustomToast.SUCCESS).show();

                    }
                    @Override
                    public void onFailure(Call<KhachHang> call, Throwable t) {
                        CustomToast.makeText(context, "Xoá thất bại",
                                CustomToast.LENGTH_SHORT, CustomToast.ERROR).show();
                        Log.e("error", t.getMessage());
                    }
                });
                ApiUtils.getKhachHangService().getAllKH().enqueue(new Callback<List<KhachHang>>() {
                    @Override
                    public void onResponse(Call<List<KhachHang>> call, Response<List<KhachHang>> response) {
                        Log.e("H", response.body().toString());
                        setData(response.body());
                    }
                    @Override
                    public void onFailure(Call<List<KhachHang>> call, Throwable t) {

                    }
                });
                alertDialog.cancel();

            }
        });
        alertDialog.btnNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });
        alertDialog.setMessage("Bạn muốn xóa khách hàng: " + id + " ?");
        alertDialog.setBtnPositive("Xóa");
        alertDialog.setBtnNegative("Hủy");
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String str =  charSequence.toString();
                if(str.isEmpty()) {
                    list = listOld;
                }else {
                    List<KhachHang> listSearch = new ArrayList<>();
                    for(KhachHang item: listOld) {
                        if(item.getFullName().toLowerCase().contains(str.toLowerCase())) {
                            listSearch.add(item);
                        }
                    }

                    list = listSearch;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = list;

                return  filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                list = (List<KhachHang>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class KhachHangViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_tenKH;
        private ImageView iv_KH;
        private CardView mainLayout;
        private ImageView ivEdit, ivDelete;


        public KhachHangViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_KH = (ImageView) itemView.findViewById(R.id.iv_KH);
            tv_tenKH = itemView.findViewById(R.id.tv_tenKH);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            ivEdit = itemView.findViewById(R.id.ivEdit);
            ivDelete = itemView.findViewById(R.id.ivDelete);
        }
    }
}
