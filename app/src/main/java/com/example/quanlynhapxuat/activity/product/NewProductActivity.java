package com.example.quanlynhapxuat.activity.product;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.Manifest;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.quanlynhapxuat.R;
import com.example.quanlynhapxuat.model.Product2;
import com.example.quanlynhapxuat.utils.CustomToast;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.IOException;
import java.util.List;

import gun0912.tedbottompicker.TedBottomPicker;

public class NewProductActivity extends AppCompatActivity {
    Product2 product2;

    private ImageView ivAnhSP;
    private EditText etTenSP;
    private SwitchCompat swTrangThai;
    private Button btnHuy, btnTao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_new);

        //setControl
        ivAnhSP = findViewById(R.id.ivAnhSP_activityNewProduct);
        etTenSP = findViewById(R.id.etTenSP_activityNewProduct);
        swTrangThai = findViewById(R.id.swTrangThai_activityNewProduct);
        btnHuy = findViewById(R.id.btnHuy_activityNewProduct);
        btnTao = findViewById(R.id.btnTao_activityNewProduct);

        //setEvent
        btnHuy.setOnClickListener(view -> {
            finish();
        });
        btnTao.setOnClickListener(view -> {
            //TODO
        });
        ivAnhSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermission();
            }
        });
    }

    private void requestPermission() {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                imagePicker();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                CustomToast.makeText(NewProductActivity.this,"Permission Denied"
                        ,CustomToast.LENGTH_LONG,CustomToast.ERROR).show();
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }

    private void imagePicker() {
        TedBottomPicker.OnImageSelectedListener listener = new TedBottomPicker.OnImageSelectedListener() {
            @Override
            public void onImageSelected(Uri uri) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                    ivAnhSP.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        TedBottomPicker tedBottomPicker = new TedBottomPicker.Builder(NewProductActivity.this)
                .setOnImageSelectedListener(listener)
                .create();
        tedBottomPicker.show(getSupportFragmentManager());
    }
}