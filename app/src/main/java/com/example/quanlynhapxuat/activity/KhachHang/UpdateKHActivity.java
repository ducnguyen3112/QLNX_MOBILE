package com.example.quanlynhapxuat.activity.KhachHang;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.example.quanlynhapxuat.R;
import com.example.quanlynhapxuat.api.ApiUtils;
import com.example.quanlynhapxuat.model.KhachHang;
import com.example.quanlynhapxuat.model.Message;
import com.example.quanlynhapxuat.model.RealPathUtil;
import com.example.quanlynhapxuat.utils.CustomToast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateKHActivity extends AppCompatActivity {

    private ImageView ivEditAvatar, ivEditAvatarOld;
    private TextView txtEditChooseImgae;
    private EditText edtEditNameKH, edtEditAddressKH, edtEditPhoneKH, edtEditEmailKH;
    private Button btnEditKH, btnCancelEditKH;
    private static final int REQUEST_CODE_FOLDER = 123;
    private KhachHang dto;
    private Uri mUri;
    private Drawable drawable;
    private Drawable avatarOld;

    private Drawable drawableFromUrl(String url) throws IOException {
        Bitmap x;
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.connect();
        InputStream input = connection.getInputStream();
        x = BitmapFactory.decodeStream(input);
        return new BitmapDrawable(Resources.getSystem(), x);
    }


    private ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
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
                            ivEditAvatar.setImageBitmap(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_khactivity);
        setControl();
        getAndSetIntentData();
        setEvent();
        Log.e("e", ivEditAvatar.getDrawable() + "");
    }

    void getAndSetIntentData() {
        Intent intent = getIntent();
        dto = (KhachHang) intent.getSerializableExtra("KH");
        if (dto.getAvatar() != null) {
            Glide.with(UpdateKHActivity.this)
                    .load(dto.getAvatar())
                    .circleCrop()
                    .into(ivEditAvatar);
        } else {
            ivEditAvatar.setImageDrawable(drawable);
        }
        edtEditNameKH.setText(dto.getFullName());
        edtEditAddressKH.setText(dto.getAddress());
        edtEditPhoneKH.setText(dto.getPhoneNumber());
        edtEditEmailKH.setText(dto.getEmail());
    }

    private void setControl() {
        drawable = AppCompatResources.getDrawable(this, R.drawable.ic_baseline_account_circle);
        ivEditAvatar = findViewById(R.id.ivEditAvatar);
        ivEditAvatarOld = findViewById(R.id.ivEditAvatarOld);
        txtEditChooseImgae = findViewById(R.id.txtEditChooseImgae);
        edtEditNameKH = findViewById(R.id.edtEditNameKH);
        edtEditAddressKH = findViewById(R.id.edtEditAddressKH);
        edtEditPhoneKH = findViewById(R.id.edtEditPhoneKH);
        edtEditEmailKH = findViewById(R.id.edtEditEmailKH);
        btnEditKH = findViewById(R.id.btnEditKH);
        btnCancelEditKH = findViewById(R.id.btnCancelEditKH);
        txtEditChooseImgae = findViewById(R.id.txtEditChooseImgae);
    }


    private void setEvent() {
        txtEditChooseImgae.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] arrayPermissions = {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                };
                ActivityCompat.requestPermissions(UpdateKHActivity.this, arrayPermissions, REQUEST_CODE_FOLDER);
            }
        });

        btnEditKH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validationKH()) {
                    String fullName = edtEditNameKH.getText().toString().trim();
                    String address = edtEditAddressKH.getText().toString().trim();
                    String phoneNumber = edtEditPhoneKH.getText().toString().trim();
                    String email = edtEditEmailKH.getText().toString().trim();
                    dto.setFullName(fullName);
                    dto.setAddress(address);
                    dto.setPhoneNumber(phoneNumber);
                    dto.setEmail(email);
                    if (ivEditAvatar.getDrawable().equals(drawable)) {
                        ApiUtils.getKhachHangService().updateKHById(dto.getId(), dto).enqueue(new Callback<KhachHang>() {
                            @Override
                            public void onResponse(Call<KhachHang> call, Response<KhachHang> response) {
                                if (response.isSuccessful()) {
                                    CustomToast.makeText(UpdateKHActivity.this, "Cập nhật thành công",
                                            CustomToast.LENGTH_LONG, CustomToast.SUCCESS).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<KhachHang> call, Throwable t) {
                                CustomToast.makeText(UpdateKHActivity.this, "Cập nhật thất bại",
                                        CustomToast.LENGTH_LONG, CustomToast.ERROR).show();
                                Log.e("Error", t.getMessage());
                            }
                        });
                    }else if(mUri == null) {
                        ApiUtils.getKhachHangService().updateKHById(dto.getId(), dto).enqueue(new Callback<KhachHang>() {
                            @Override
                            public void onResponse(Call<KhachHang> call, Response<KhachHang> response) {
                                if (response.isSuccessful()) {
                                    CustomToast.makeText(UpdateKHActivity.this, "Cập nhật thành công",
                                            CustomToast.LENGTH_LONG, CustomToast.SUCCESS).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<KhachHang> call, Throwable t) {
                                CustomToast.makeText(UpdateKHActivity.this, "Cập nhật thất bại",
                                        CustomToast.LENGTH_LONG, CustomToast.ERROR).show();
                                Log.e("Error", t.getMessage());
                            }
                        });
                    }

                    else {
                        String strReadPath = RealPathUtil.getRealPath(UpdateKHActivity.this, mUri);
                        File file = new File(strReadPath);
                        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

                        ApiUtils.getUploadService().uploadImage(body).enqueue(new Callback<Message>() {
                            @Override
                            public void onResponse(Call<Message> call, Response<Message> response) {
                                Message message = response.body();
                                dto.setAvatar(message.getMessage());
                                ApiUtils.getKhachHangService().updateKHById(dto.getId(), dto).enqueue(new Callback<KhachHang>() {
                                    @Override
                                    public void onResponse(Call<KhachHang> call, Response<KhachHang> response) {
                                        if (response.isSuccessful()) {
                                            CustomToast.makeText(UpdateKHActivity.this, "Cập nhật thành công",
                                                    CustomToast.LENGTH_LONG, CustomToast.SUCCESS).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<KhachHang> call, Throwable t) {
                                        CustomToast.makeText(UpdateKHActivity.this, "Cập nhật thất bại",
                                                CustomToast.LENGTH_LONG, CustomToast.ERROR).show();
                                        Log.e("Error", t.getMessage());
                                    }
                                });
                            }

                            @Override
                            public void onFailure(Call<Message> call, Throwable t) {
                                CustomToast.makeText(UpdateKHActivity.this, "Thêm avatar thất bại",
                                        CustomToast.LENGTH_LONG, CustomToast.ERROR).show();
                                Log.e("Error", t.getMessage());
                            }
                        });
                    }
                }

            }
        });
        btnCancelEditKH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdateKHActivity.this, ProfileKHActivity.class);
                intent.putExtra("Check", 1);
                startActivity(intent);
            }
        });
    }

    boolean validationKH() {
        String fullName = edtEditNameKH.getText().toString().trim();
        String address = edtEditAddressKH.getText().toString().trim();
        String phoneNumber = edtEditPhoneKH.getText().toString().trim();
        String email = edtEditEmailKH.getText().toString().trim();

        if (fullName.isEmpty()) {
            CustomToast.makeText(UpdateKHActivity.this, "Không được để trống trường tên",
                    CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            return false;
        } else if (fullName.length() < 2) {
            CustomToast.makeText(UpdateKHActivity.this, "Tên khách hàng tối thiểu 2 ký tự",
                    CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            return false;
        } else if (fullName.length() > 30) {
            CustomToast.makeText(UpdateKHActivity.this, "Tên khách hàng không 30 ký tự",
                    CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            return false;
        } else if (address.isEmpty()) {
            CustomToast.makeText(UpdateKHActivity.this, "Không được để trống trường địa chỉ",
                    CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            return false;
        } else if (address.length() < 2) {
            CustomToast.makeText(UpdateKHActivity.this, "Địa chỉ tối thiểu 2 ký tự",
                    CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            return false;
        } else if (phoneNumber.length() > 12) {
            CustomToast.makeText(UpdateKHActivity.this, "Số điện thoại tối đa 11 số",
                    CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            return false;
        } else if (phoneNumber.length() < 10) {
            CustomToast.makeText(UpdateKHActivity.this, "Số điện thoại tối thiểu 10 số",
                    CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            return false;
        } else if (email.isEmpty()) {
            CustomToast.makeText(UpdateKHActivity.this, "Không được để trống trường email",
                    CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            return false;
        } else if (email.length() < 2) {
            CustomToast.makeText(UpdateKHActivity.this, "Email tối thiểu 2 ký tự",
                    CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            return false;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(UpdateKHActivity.this, ListKHActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_FOLDER)
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                activityResultLauncher.launch(Intent.createChooser(intent, "Select Avatar"));
            }
    }
}