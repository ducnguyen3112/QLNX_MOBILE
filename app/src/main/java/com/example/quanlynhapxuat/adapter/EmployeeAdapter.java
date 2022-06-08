package com.example.quanlynhapxuat.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.example.quanlynhapxuat.activity.Employee.ProfileEmployeeActivity;
import com.example.quanlynhapxuat.activity.Employee.UpdateEmployeeActivity;
import com.example.quanlynhapxuat.activity.KhachHang.ListKHActivity;
import com.example.quanlynhapxuat.activity.KhachHang.ProfileKHActivity;
import com.example.quanlynhapxuat.activity.KhachHang.UpdateKHActivity;
import com.example.quanlynhapxuat.api.ApiUtils;
import com.example.quanlynhapxuat.model.Employee;
import com.example.quanlynhapxuat.model.KhachHang;
import com.example.quanlynhapxuat.utils.CustomAlertDialog;
import com.example.quanlynhapxuat.utils.CustomToast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> implements Filterable {

    private Activity activity;
    private Context context;
    private List<Employee> list;
    private List<Employee> listOld;

    public EmployeeAdapter(Activity activity, Context context) {
        this.activity = activity;
        this.context = context;
    }

    public void setData(List<Employee> list) {
        this.list = list;
        this.listOld = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_employee, parent, false);
        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position) {
        Employee e = list.get(position);
        if (e == null) {
            return;
        }

        if (e.getAvatar() != null) {
            Glide.with(context)
                    .load(e.getAvatar())
                    .circleCrop()
                    .into(holder.iv_E);
        }
        holder.tv_tenE.setText(e.getFullName());

        holder.mainELayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProfileEmployeeActivity.class);
                intent.putExtra("E", e.getId());
                activity.startActivityForResult(intent, 1);
            }
        });

        holder.ivEEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateEmployeeActivity.class);
                intent.putExtra("E", e.getId());
                activity.startActivityForResult(intent, 1);
            }
        });


//        holder.ivEDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(e.getDeliveryDockets().size() > 0) {
//                    CustomToast.makeText(context, "Nhân Viên Có Đơn Hàng Không Thể Xoá",
//                            CustomToast.LENGTH_SHORT, CustomToast.ERROR).show();
//                }else {
//                    confirmDialog(e.getId());
//                }
//
//            }
//        });
    }

//    void confirmDialog(int id) {
//        CustomAlertDialog alertDialog = new CustomAlertDialog(context);
//        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        alertDialog.getWindow().setLayout((7 * ListKHActivity.width) / 8, WindowManager.LayoutParams.WRAP_CONTENT);
//        alertDialog.show();
//        alertDialog.btnPositive.setOnClickListener(new View.OnClickListener() {
//            @SuppressLint("NotifyDataSetChanged")
//            @Override
//            public void onClick(View view) {
//                ApiUtils.employeeRetrofit().deleteEmployeeById(id).enqueue(new Callback<Employee>() {
//                    @Override
//                    public void onResponse(Call<Employee> call, Response<Employee> response) {
//                        CustomToast.makeText(context, "Xoá thành công",
//                                CustomToast.LENGTH_SHORT, CustomToast.SUCCESS).show();
//
//                    }
//                    @Override
//                    public void onFailure(Call<Employee> call, Throwable t) {
//                        CustomToast.makeText(context, "Xoá thất bại",
//                                CustomToast.LENGTH_SHORT, CustomToast.ERROR).show();
//                        Log.e("error", t.getMessage());
//                    }
//                });
//                ApiUtils.employeeRetrofit().getEmployees().enqueue(new Callback<List<Employee>>() {
//                    @Override
//                    public void onResponse(Call<List<Employee>> call, Response<List<Employee>> response) {
//                        Log.e("H", response.body().toString());
//                        setData(response.body());
//                    }
//                    @Override
//                    public void onFailure(Call<List<Employee>> call, Throwable t) {
//
//                    }
//                });
//                alertDialog.cancel();
//
//            }
//        });
//        alertDialog.btnNegative.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                alertDialog.cancel();
//            }
//        });
//        alertDialog.setMessage("Bạn muốn xóa nhân viên: " + id + " ?");
//        alertDialog.setBtnPositive("Xóa");
//        alertDialog.setBtnNegative("Hủy");
//    }

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
                String str = charSequence.toString();
                if (str.isEmpty()) {
                    list = listOld;
                } else {
                    List<Employee> listSearch = new ArrayList<>();
                    for (Employee item : listOld) {
                        if (item.getFullName().toLowerCase().contains(str.toLowerCase())) {
                            listSearch.add(item);
                        }
                    }

                    list = listSearch;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = list;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                list = (List<Employee>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class EmployeeViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_tenE;
        private ImageView iv_E;
        private CardView mainELayout;
        private ImageView ivEEdit, ivEDelete;


        public EmployeeViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_E = (ImageView) itemView.findViewById(R.id.iv_E);
            tv_tenE = itemView.findViewById(R.id.tv_tenE);
            mainELayout = itemView.findViewById(R.id.mainELayout);
            ivEEdit = itemView.findViewById(R.id.ivEEdit);
//            ivEDelete = itemView.findViewById(R.id.ivEDelete);
        }
    }
}
