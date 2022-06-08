package com.example.quanlynhapxuat.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlynhapxuat.R;
import com.example.quanlynhapxuat.activity.DetailEmployeeActivity;
import com.example.quanlynhapxuat.api.ApiUtils;
import com.example.quanlynhapxuat.model.Employee;
import com.example.quanlynhapxuat.utils.CustomToast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeetRecyclerViewAdapter extends RecyclerView.Adapter<EmployeetRecyclerViewAdapter.MyViewHolder> {

    private Context context;
    private List<Employee> list;
    int check = 0;

    public EmployeetRecyclerViewAdapter(@NonNull Context context, @NonNull List<Employee> list) {
        super();
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_employee_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Employee employee = list.get(position);
        holder.name.setText(employee.getFullName());
        holder.status.setClickable(false);
        if (employee.getStatus() == 1) {
            holder.status.setChecked(true);
        } else {
            holder.status.setChecked(false);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailEmployeeActivity.class);
                intent.putExtra("id", employee.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(!list.isEmpty()){
            return list.size();
        }else {
            return 0;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView image;
        Switch status;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            image = view.findViewById(R.id.image);
            status = view.findViewById(R.id.status);
        }
    }

    private void updateEmployee(int id,Employee e) {
        ApiUtils.employeeRetrofit().updateEmployee(id,e).enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(Call<Employee> call, Response<Employee> response) {
                if (response.isSuccessful()) {
                    CustomToast.makeText(context, "Sửa thông tin thành công!!",
                            CustomToast.LENGTH_LONG, CustomToast.SUCCESS).show();
                }
            }

            @Override
            public void onFailure(Call<Employee> call, Throwable t) {
                CustomToast.makeText(context, t.toString(),
                        CustomToast.LENGTH_LONG, CustomToast.ERROR).show();
            }
        });
    }
}
