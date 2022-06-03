package com.example.quanlynhapxuat.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.quanlynhapxuat.R;
import com.example.quanlynhapxuat.adapter.EmployeetRecyclerViewAdapter;
import com.example.quanlynhapxuat.api.ApiUtils;
import com.example.quanlynhapxuat.model.Employee;
import com.example.quanlynhapxuat.utils.Constants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Employee> list = new ArrayList<>();
    private EmployeetRecyclerViewAdapter mAdapter;
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employees);
        setControl();
        getEmployees();
        handleClickFloatingActionButton();
    }

    private void setControl() {
        recyclerView = findViewById(R.id.recyclerView);
        floatingActionButton = findViewById(R.id.floatingActionButton);
    }

    public void getEmployees() {
        ApiUtils.employeeRetrofit().getEmployees().enqueue(new Callback<ArrayList<Employee>>() {
            @Override
            public void onResponse(Call<ArrayList<Employee>> call, Response<ArrayList<Employee>> response) {
                if (response.isSuccessful()) {
                    list = response.body();
                    mAdapter = new EmployeetRecyclerViewAdapter(EmployeesActivity.this, list);

                    LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(EmployeesActivity.this, RecyclerView.VERTICAL, false);
                    recyclerView.setLayoutManager(horizontalLayoutManager);
                    recyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Employee>> call, Throwable t) {
            }
        });
    }

    private void handleClickFloatingActionButton() {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EmployeesActivity.this, DetailEmployeeActivity.class);
                intent.putExtra("id", 0);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.RESULT_PRODUCT_ACTIVITY:
                list.clear();
                getEmployees();
                break;
            default:
                break;
        }
    }
}