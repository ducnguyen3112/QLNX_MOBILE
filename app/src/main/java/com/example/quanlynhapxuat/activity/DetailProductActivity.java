package com.example.quanlynhapxuat.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.quanlynhapxuat.R;
import com.example.quanlynhapxuat.activity.KhachHang.AddKHActivity;
import com.example.quanlynhapxuat.api.ApiUtils;
import com.example.quanlynhapxuat.model.Message;
import com.example.quanlynhapxuat.model.Product;
import com.example.quanlynhapxuat.model.RealPathUtil;
import com.example.quanlynhapxuat.utils.Constants;
import com.example.quanlynhapxuat.utils.CustomToast;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailProductActivity extends AppCompatActivity {
    TextInputLayout name;
    Switch status;
    ImageView image;
    Toolbar toolbar;
    boolean is_update = false;
    int check;
    Button add, cancel;
    int id;
    Product product;
    private Uri mUri;
    private Drawable drawable;
    private TextView txtChooseImgae;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);
        setControl();
        id = getIntent().getIntExtra("id", 0);
        if (id != 0) {
            toolbar.setTitle("Sửa thông tin");
            is_update = true;
            setDisplayInfoProduct(id);
            add.setText("Sửa");
        } else {
            check = 1;
            status.setChecked(true);
        }
        handleClickButtonAdd();
        handleClickButtonCancel();
        handleSwitch();
        hanleClickUploadFile();
        setActionBar();
    }

    private void setActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void handleSwitch() {
        status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    check = 1;
                } else {
                    check = 0;
                }
            }
        });
    }

    public void setDisplayInfoProduct(int id) {
        ApiUtils.productRetrofit().getProduct(id).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                product = response.body();
                name.getEditText().setText(product.getName());
                if (product.getStatus() == 1) {
                    status.setChecked(true);
                } else {
                    status.setChecked(false);
                }
                if (product.getImage() != null) {
                    Glide.with(DetailProductActivity.this).load(product.getImage()).into(image);
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                CustomToast.makeText(DetailProductActivity.this, t.toString(),
                        CustomToast.LENGTH_LONG, CustomToast.ERROR).show();
            }
        });
    }

    private void setControl() {
        name = findViewById(R.id.name);
        status = findViewById(R.id.status);
        drawable = AppCompatResources.getDrawable(this, R.drawable.ic_baseline_account_circle);
        image = findViewById(R.id.image);
        image.setImageDrawable(drawable);
        toolbar = findViewById(R.id.toolbar);
        add = findViewById(R.id.add);
        cancel = findViewById(R.id.cancel);
        txtChooseImgae = findViewById(R.id.txtChooseImgae);
    }

    private ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        Uri uri = data.getData();
                        mUri = uri;
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(uri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            image.setImageBitmap(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    public void hanleClickUploadFile() {
        txtChooseImgae.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] arrayPermissions = {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                };
                ActivityCompat.requestPermissions(DetailProductActivity.this, arrayPermissions, Constants.MY_CODE);
            }
        });
    }

    private void handleClickButtonAdd() {
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameProduct = name.getEditText().getText().toString().trim();
                Date date = new Date();
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateString = df.format(date);
                if (checkInput(nameProduct)) {
                    String realPath = RealPathUtil.getRealPath(DetailProductActivity.this, mUri);
                    File file = new File(realPath);
                    RequestBody requestBodyImage = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    MultipartBody.Part muPart = MultipartBody.Part.createFormData("image", file.getName(), requestBodyImage);
                    Product product = new Product(nameProduct, check, dateString);
                    if (is_update == false) {
                        ApiUtils.getUploadService().uploadImage(muPart).enqueue(new Callback<Message>() {
                            @Override
                            public void onResponse(Call<Message> call, Response<Message> response) {
                                Message message = response.body();
                                product.setImage(message.getMessage());
                                Log.d("TAG", message.getMessage());
                                addProduct(product);
                                finish();
                            }

                            @Override
                            public void onFailure(Call<Message> call, Throwable t) {
                                CustomToast.makeText(DetailProductActivity.this, t.toString(),
                                        CustomToast.LENGTH_LONG, CustomToast.ERROR).show();
                            }
                        });
                    } else {
                        ApiUtils.getUploadService().uploadImage(muPart).enqueue(new Callback<Message>() {
                            @Override
                            public void onResponse(Call<Message> call, Response<Message> response) {
                                Message message = response.body();
                                product.setImage(message.getMessage());
                                Log.d("TAG", message.getMessage());
                                updateProduct(id, product);
                                finish();
                            }

                            @Override
                            public void onFailure(Call<Message> call, Throwable t) {
                                CustomToast.makeText(DetailProductActivity.this, t.toString(),
                                        CustomToast.LENGTH_LONG, CustomToast.ERROR).show();
                            }
                        });
                    }
                }
            }
        });
    }

    private void handleClickButtonCancel() {
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void addProduct(Product product) {
        ApiUtils.productRetrofit().createNewProduct(product).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful()) {
                    CustomToast.makeText(DetailProductActivity.this, "Thêm thành công!!",
                            CustomToast.LENGTH_LONG, CustomToast.SUCCESS).show();
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                CustomToast.makeText(DetailProductActivity.this, "Vui lòng thử lại!!",
                        CustomToast.LENGTH_LONG, CustomToast.ERROR).show();
            }
        });
    }

    private void updateProduct(int id, Product product) {
        ApiUtils.productRetrofit().updateProduct(id, product).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful()) {
                    CustomToast.makeText(DetailProductActivity.this, "Sửa thành công!!",
                            CustomToast.LENGTH_LONG, CustomToast.SUCCESS).show();
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                CustomToast.makeText(DetailProductActivity.this, t.toString(),
                        CustomToast.LENGTH_LONG, CustomToast.ERROR).show();
            }
        });
    }

    boolean checkInput(String nameProduct) {
        if (nameProduct.isEmpty()) {
            CustomToast.makeText(DetailProductActivity.this, "Vui lòng nhập tên sản phẩm!!",
                    CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Constants.MY_CODE)
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                activityResultLauncher.launch(Intent.createChooser(intent, "Select Avatar"));
            }
    }

}
