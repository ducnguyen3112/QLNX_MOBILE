package com.example.quanlynhapxuat.activity.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.quanlynhapxuat.R;
import com.example.quanlynhapxuat.fragment.HomeFragment;
import com.example.quanlynhapxuat.fragment.ProductFragment;
import com.example.quanlynhapxuat.fragment.phieuxuat.AddExportFragment;
import com.example.quanlynhapxuat.fragment.phieuxuat.ExportDetailFragment;
import com.example.quanlynhapxuat.fragment.phieuxuat.ExportFragment;
import com.example.quanlynhapxuat.fragment.phieunhap.ImportFragment;
import com.example.quanlynhapxuat.fragment.MoreFragment;
import com.example.quanlynhapxuat.model.DeliveryDocket;
import com.example.quanlynhapxuat.model.DeliveryDocketDetail;
import com.example.quanlynhapxuat.service.DeliveryDocketService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;
    HomeFragment homeFragment=new HomeFragment();
    ExportFragment exportFragment=new ExportFragment();
    ImportFragment importFragment = new ImportFragment();
    MoreFragment moreFragment = new MoreFragment();
    ExportDetailFragment exportDetailFragment=new ExportDetailFragment();
    ProductFragment productFragment = new ProductFragment();
    List<DeliveryDocketDetail> mDeliveryDocketDetails=new ArrayList<>();
    public static int width;

    public List<DeliveryDocketDetail> getmDeliveryDocketDetails() {
        return mDeliveryDocketDetails;
    }

    public void setmDeliveryDocketDetails(List<DeliveryDocketDetail> mDeliveryDocketDetails) {
        this.mDeliveryDocketDetails = mDeliveryDocketDetails;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics metrics=getResources().getDisplayMetrics();
        width=metrics.widthPixels;
        setContentView(R.layout.activity_main);
        initViews();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_main,homeFragment).commit();

        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_main,homeFragment).commit();
                        return true;
                    case R.id.menu_export:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_main,exportFragment).commit();
                        return true;
                    case R.id.menu_import:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_main,importFragment).commit();
                        return true;
                    case R.id.menu_more:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_main, moreFragment).commit();
                        return true;
                    case R.id.menu_products:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_main, productFragment).commit();
                        return true;
                }
                return false;
            }
        });
    }


    private void initViews(){
        bottomNav=findViewById(R.id.bottom_navigation);
    }

    public void goToDeliveryDetail(DeliveryDocket deliveryDocket){
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        ExportDetailFragment exportDetailFragment=new ExportDetailFragment();
        Bundle bundle=new Bundle();
        bundle.putSerializable("deliveryDocket",deliveryDocket);
        exportDetailFragment.setArguments(bundle);
        DeliveryDocketService.deliveryDocketService.getAllDeliveryDetailsInDelivery(deliveryDocket.getId()).enqueue(new Callback<List<DeliveryDocketDetail>>() {
            @Override
            public void onResponse(Call<List<DeliveryDocketDetail>> call, Response<List<DeliveryDocketDetail>> response) {
                if (response.isSuccessful()){
                    List<DeliveryDocketDetail> deliveryDocketDetails=response.body();
                    if (deliveryDocketDetails!=null){
                        mDeliveryDocketDetails=deliveryDocketDetails;
                        exportDetailFragment.setArguments(bundle);
                        fragmentTransaction.replace(R.id.frame_main,exportDetailFragment);
                        fragmentTransaction.addToBackStack(ExportDetailFragment.TAG);
                        fragmentTransaction.commit();
                    }else{
                        Toast.makeText(MainActivity.this,"!!!Call không thành công!",
                                Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(MainActivity.this,"không thể get dữ liễu!",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<DeliveryDocketDetail>> call, Throwable t) {
                Toast.makeText(MainActivity.this,"không thể get dữ liễu 2  !"+t.getMessage(),
                        Toast.LENGTH_SHORT).show();

            }
        });
    }
    public void goToAddExport(){
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        AddExportFragment addExportFragment=new AddExportFragment();
        fragmentTransaction.replace(R.id.frame_main,addExportFragment);
        fragmentTransaction.addToBackStack(AddExportFragment.TAG);
        fragmentTransaction.commit();
    }
}