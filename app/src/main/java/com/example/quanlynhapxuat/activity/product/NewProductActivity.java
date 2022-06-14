package com.example.quanlynhapxuat.activity.product;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.quanlynhapxuat.R;
import com.example.quanlynhapxuat.activity.Employee.AddEmployeeActivity;
import com.example.quanlynhapxuat.activity.ReceivedDocket.ReceivedDocketDetailActivity;
import com.example.quanlynhapxuat.api.ApiUtils;
import com.example.quanlynhapxuat.model.Message;
import com.example.quanlynhapxuat.model.Product2;
import com.example.quanlynhapxuat.model.RealPathUtil;
import com.example.quanlynhapxuat.model.RestErrorResponse;
import com.example.quanlynhapxuat.utils.Convert;
import com.example.quanlynhapxuat.utils.CustomToast;
import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import gun0912.tedbottompicker.TedBottomPicker;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewProductActivity extends AppCompatActivity {
    Product2 product2;

    private ImageView ivAnhSP;
    private EditText etTenSP;
    private SwitchCompat swTrangThai;
    private Button btnHuy, btnTao;

    private Uri mUri;
    Drawable drawable;
    private static final int REQUEST_CODE_FOLDER = 123;

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

        //dafaultValues
        drawable = AppCompatResources.getDrawable(this, R.drawable.ic_add_plus_svgrepo_com);
        ivAnhSP.setImageDrawable(drawable);
        swTrangThai.setChecked(true);

        //setEvent
        btnHuy.setOnClickListener(view -> {
            finish();
        });
        btnTao.setOnClickListener(view -> {
            taoSanPham();
        });
        ivAnhSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] arrayPermissions = {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                };
                ActivityCompat.requestPermissions(NewProductActivity.this, arrayPermissions, REQUEST_CODE_FOLDER);
            }
        });
    }

    private ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        Uri uri = data.getData();
                        mUri = uri;
                        Log.e("mURI",mUri.toString());
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(uri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            ivAnhSP.setImageBitmap(bitmap);
                        }
                        catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if((requestCode == REQUEST_CODE_FOLDER) && (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            activityResultLauncher.launch(Intent.createChooser(intent, "Select Image"));
        }
    }

    private void taoSanPham() {
        String tenSP = etTenSP.getText().toString().trim();
        if(tenSP.length() < 5 || tenSP.length() > 30) {
            CustomToast.makeText(NewProductActivity.this,"Tên sản phẩm phải lớn hơn 5 và nhỏ hơn 30 kí tự",
                    CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
        }
        else {
            Product2 product2 = new Product2();
            product2.setName(tenSP);
            if(swTrangThai.isChecked()) {
                product2.setStatus(1);
            }
            else {
                product2.setStatus(0);
            }
            Date date = new Date();
            product2.setCreatedAt(Convert.dateToString(date));
            Log.e("product2.getCreateAt",product2.getCreatedAt());

            if(ivAnhSP.getDrawable().equals(drawable)) {
                ApiUtils.getProductService().postProduct2(product2).enqueue(new Callback<Product2>() {
                    @Override
                    public void onResponse(Call<Product2> call, Response<Product2> response) {
                        if(response.isSuccessful()) {
                            CustomToast.makeText(NewProductActivity.this,"Thêm sản phẩm thành công!",
                                    CustomToast.LENGTH_SHORT, CustomToast.SUCCESS).show();
                        }
                        else {
                            try {
                                Gson g = new Gson();
                                RestErrorResponse errorResponse = g.fromJson(response.errorBody().string(),RestErrorResponse.class);
                                CustomToast.makeText(NewProductActivity.this,"TRY: " + errorResponse.getMessage()
                                        ,CustomToast.LENGTH_LONG,CustomToast.ERROR).show();
                            }
                            catch (Exception e) {
                                CustomToast.makeText(NewProductActivity.this,"CATCH: " + e.getMessage()
                                        ,CustomToast.LENGTH_LONG,CustomToast.ERROR).show();
                            }
                        }
                        Log.e("No image: ", "success");
                    }

                    @Override
                    public void onFailure(Call<Product2> call, Throwable t) {
                        CustomToast.makeText(NewProductActivity.this,"CALL API FAIL!!!"
                                ,CustomToast.LENGTH_LONG,CustomToast.ERROR).show();
                        Log.e("No image: ", "fail");

                    }
                });
            }
            else {
                String strReadPath = RealPathUtil.getRealPath(NewProductActivity.this, mUri);
                File file = new File(strReadPath);
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
                ApiUtils.getUploadService().uploadImage(body).enqueue(new Callback<Message>() {
                    @Override
                    public void onResponse(Call<Message> call, Response<Message> response) {
                        if(response.isSuccessful()) {
                            Message message = response.body();
                            product2.setImage(message.getMessage());
                            Log.e("image: ", "success");

                            ApiUtils.getProductService().postProduct2(product2).enqueue(new Callback<Product2>() {
                                @Override
                                public void onResponse(Call<Product2> call, Response<Product2> response) {
                                    if(response.isSuccessful()) {
                                        CustomToast.makeText(NewProductActivity.this,"Thêm sản phẩm thành công!",
                                                CustomToast.LENGTH_SHORT, CustomToast.SUCCESS).show();
                                        Log.e("Yes image: ", "success");

                                    }
                                    else {
                                        try {
                                            Gson g = new Gson();
                                            RestErrorResponse errorResponse = g.fromJson(response.errorBody().string(),RestErrorResponse.class);
                                            CustomToast.makeText(NewProductActivity.this,"TRY: " + errorResponse.getMessage()
                                                    ,CustomToast.LENGTH_LONG,CustomToast.ERROR).show();
                                        }
                                        catch (Exception e) {
                                            CustomToast.makeText(NewProductActivity.this,"CATCH: " + e.getMessage()
                                                    ,CustomToast.LENGTH_LONG,CustomToast.ERROR).show();
                                        }
                                        Log.e("Yes image: ", "fail1");
                                    }
                                }

                                @Override
                                public void onFailure(Call<Product2> call, Throwable t) {
                                    CustomToast.makeText(NewProductActivity.this,"CALL API FAIL!"
                                            ,CustomToast.LENGTH_LONG,CustomToast.ERROR).show();
                                    Log.e("Yes image: ", "fail2");
                                }
                            });
                        }
                        else {
                            try {
                                Gson g = new Gson();
                                RestErrorResponse errorResponse = g.fromJson(response.errorBody().string(),RestErrorResponse.class);
                                CustomToast.makeText(NewProductActivity.this,"TRY: " + errorResponse.getMessage()
                                        ,CustomToast.LENGTH_LONG,CustomToast.ERROR).show();
                            }
                            catch (Exception e) {
                                CustomToast.makeText(NewProductActivity.this,"CATCH: " + e.getMessage()
                                        ,CustomToast.LENGTH_LONG,CustomToast.ERROR).show();
                            }
                            Log.e("image: ", "fail0");
                        }
                    }

                    @Override
                    public void onFailure(Call<Message> call, Throwable t) {
                        CustomToast.makeText(NewProductActivity.this,"CALL API FAIL!"
                                ,CustomToast.LENGTH_LONG,CustomToast.ERROR).show();
                        Log.e("image: ", "fai5");
                    }
                });
            }
            finish();
        }
    }
}