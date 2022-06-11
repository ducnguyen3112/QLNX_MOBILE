package com.example.quanlynhapxuat.activity.Employee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.quanlynhapxuat.R;
import com.example.quanlynhapxuat.activity.KhachHang.AddKHActivity;
import com.example.quanlynhapxuat.activity.KhachHang.ListKHActivity;
import com.example.quanlynhapxuat.adapter.EmployeeAdapter;
import com.example.quanlynhapxuat.api.ApiUtils;
import com.example.quanlynhapxuat.model.Employee;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListEmployeeActivity extends AppCompatActivity {

    private RecyclerView rcvE;
    private EmployeeAdapter employeeAdapter;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_employee);
        setControl();
        setEvent();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvE.setLayoutManager(linearLayoutManager);
        employeeAdapter = new EmployeeAdapter(ListEmployeeActivity.this, ListEmployeeActivity.this);
        rcvE.setAdapter(employeeAdapter);
        getAllE();
    }

    public void getAllE() {
        ApiUtils.employeeRetrofit().getEmployees().enqueue(new Callback<List<Employee>>() {
            @Override
            public void onResponse(Call<List<Employee>> call, Response<List<Employee>> response) {
                if (response.isSuccessful()) {
                    List<Employee> list = response.body();
                    employeeAdapter.setData(list);
                }
            }

            @Override
            public void onFailure(Call<List<Employee>> call, Throwable t) {
                Log.e("failure", t.getLocalizedMessage());
            }
        });
    }

    private void setControl() {
        rcvE = findViewById(R.id.rcv_e);
    }


    private void setEvent() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list_employee, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.actionSearch).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                employeeAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                employeeAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionAdd:
                Intent intent = new Intent(ListEmployeeActivity.this, AddEmployeeActivity.class);
                startActivity(intent);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}