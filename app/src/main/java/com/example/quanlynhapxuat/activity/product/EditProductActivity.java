package com.example.quanlynhapxuat.activity.product;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.quanlynhapxuat.R;
import com.example.quanlynhapxuat.api.ApiUtils;
import com.example.quanlynhapxuat.model.Message;
import com.example.quanlynhapxuat.model.Product2;
import com.example.quanlynhapxuat.model.RealPathUtil;
import com.example.quanlynhapxuat.utils.Convert;
import com.example.quanlynhapxuat.utils.CustomToast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProductActivity extends AppCompatActivity {
    Product2 product2;

    private ImageView ivImage;
    private EditText etName;
    private SwitchCompat swStatus;
    private TextView tvCreateAt, tvPrice, tvInventory;
    private Button btnCancel, btnSave;

    private Uri mUri;
    Drawable drawable;
    private static final int REQUEST_CODE_FOLDER = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_edit);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle==null) {
            CustomToast.makeText(EditProductActivity.this,"Không thể tải sản phẩm",
                    CustomToast.LENGTH_LONG,CustomToast.ERROR).show();
            finish();
        }
        product2 = new Product2(bundle.getInt("id",0),
                bundle.getString("name","name not found"),
                bundle.getString("createAt","1999-01-01 01:01:01"),
                bundle.getInt("status",0),
                bundle.getString("image",null),
                bundle.getFloat("price",69),
                bundle.getInt("inventory",69));

        setControl();
        setValues();
        setEvent();
    }

    private void setControl() {
        ivImage = findViewById(R.id.ivImage_activityEditProduct);
        etName  = findViewById(R.id.etName_activityEditProduct);
        swStatus  = findViewById(R.id.swStatus_activityEditProduct);
        tvCreateAt  = findViewById(R.id.tvCreatedAt_activityEditProduct);
        tvPrice  = findViewById(R.id.tvPrice_activityEditProduct);
        tvInventory = findViewById(R.id.tvInventory_activityEditProduct);
        btnCancel = findViewById(R.id.btnCancel_activityEditProduct);
        btnSave = findViewById(R.id.btnSave_activityEditProduct);
    }

    private void setValues() {
        if(product2.getImage()!=null) {
            Glide.with(EditProductActivity.this).
                    load(product2.getImage()).
                    circleCrop().
                    into(ivImage);
        }
        etName.setText(product2.getName());
        if(product2.getStatus()==1) {
            swStatus.setChecked(true);
        }
        else {
            swStatus.setChecked(false);
        }
        tvCreateAt.setText(product2.getCreatedAt());
        tvPrice.setText(Convert.currencyFormat(product2.getPrice()) +"VND");
        tvInventory.setText(product2.getInventory()+"");
    }

    private void setEvent() {
        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] arrayPermissions = {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                };
                ActivityCompat.requestPermissions(EditProductActivity.this, arrayPermissions, REQUEST_CODE_FOLDER);
            }
        });

        btnCancel.setOnClickListener(view -> {
            finish();
        });

        btnSave.setOnClickListener(view -> {
            String tenSP = etName.getText().toString().trim();
            if(tenSP.length() < 5 || tenSP.length() > 30) {
                CustomToast.makeText(EditProductActivity.this,"Tên sản phẩm phải lớn hơn 5 và nhỏ hơn 30 kí tự",
                        CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            }
            else {
                product2.setName(tenSP);
                if(swStatus.isChecked()) {
                    product2.setStatus(1);
                }
                else {
                    product2.setStatus(0);
                }
            }
            if(mUri!=null) {
                String strReadPath = RealPathUtil.getRealPath(EditProductActivity.this, mUri);
                File file = new File(strReadPath);
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
                ApiUtils.getUploadService().uploadImage(body).enqueue(new Callback<Message>() {
                    @Override
                    public void onResponse(Call<Message> call, Response<Message> response) {
                        if(response.isSuccessful()) {
                            Message message = response.body();
                            product2.setImage(message.getMessage());

                            ApiUtils.getProductService().putProduct(product2.getId(),product2).enqueue(new Callback<Product2>() {
                                @Override
                                public void onResponse(Call<Product2> call, Response<Product2> response) {
                                    if(response.isSuccessful()) {
                                        CustomToast.makeText(EditProductActivity.this,"Cập nhật thành công!",
                                                CustomToast.LENGTH_SHORT, CustomToast.SUCCESS).show();
                                        Log.e("Yes image: ", "success");
                                    }
                                }

                                @Override
                                public void onFailure(Call<Product2> call, Throwable t) {
                                    CustomToast.makeText(EditProductActivity.this,"CALL API FAILED!",
                                            CustomToast.LENGTH_LONG, CustomToast.ERROR).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<Message> call, Throwable t) {
                        CustomToast.makeText(EditProductActivity.this,"CALL API FAILED!",
                                CustomToast.LENGTH_LONG, CustomToast.ERROR).show();
                    }
                });
            }
            else {
                ApiUtils.getProductService().putProduct(product2.getId(),product2).enqueue(new Callback<Product2>() {
                    @Override
                    public void onResponse(Call<Product2> call, Response<Product2> response) {
                        if(response.isSuccessful()) {
                            CustomToast.makeText(EditProductActivity.this,"Cập nhật thành công!",
                                    CustomToast.LENGTH_SHORT, CustomToast.SUCCESS).show();
                            Log.e("Yes image: ", "success");
                        }
                    }

                    @Override
                    public void onFailure(Call<Product2> call, Throwable t) {
                        CustomToast.makeText(EditProductActivity.this,"CALL API FAILED!",
                                CustomToast.LENGTH_LONG, CustomToast.ERROR).show();
                    }
                });
            }
            finish();
        });
    }

    private ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    Uri uri = data.getData();
                    mUri = uri;
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(uri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        ivImage.setImageBitmap(bitmap);
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
}