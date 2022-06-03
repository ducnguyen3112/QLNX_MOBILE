package com.example.quanlynhapxuat.activity.KhachHang;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlynhapxuat.R;
import com.example.quanlynhapxuat.adapter.ChiTietLishSuAdapter;
import com.example.quanlynhapxuat.model.DeliveryDocket;

public class ChiTietActivity extends AppCompatActivity {

    private RecyclerView rcvCTLS;
    private ChiTietLishSuAdapter adapter;
    private DeliveryDocket dto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet);
        setControl();
        Intent intent = getIntent();
        dto = (DeliveryDocket) intent.getSerializableExtra("DD");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvCTLS.setLayoutManager(linearLayoutManager);
        adapter = new ChiTietLishSuAdapter(ChiTietActivity.this);
        adapter.setData(dto.getDeliveryDocketDetails());
        rcvCTLS.setAdapter(adapter);
    }

    private void setControl() {
        rcvCTLS = findViewById(R.id.rcv_ctls);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}