package com.example.quanlynhapxuat.activity.KhachHang;

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
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;

import com.example.quanlynhapxuat.R;
import com.example.quanlynhapxuat.api.ApiUtils;
import com.example.quanlynhapxuat.model.KhachHang;
import com.example.quanlynhapxuat.model.Message;
import com.example.quanlynhapxuat.model.RealPathUtil;
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

public class AddKHActivity extends AppCompatActivity {

    private ImageView ivAvatar;
    private TextView txtChooseImgae;
    private EditText edtAddNameKH, edtAddAddressKH, edtAddPhoneKH, edtAddEmailKH;
    private Button btnAddKH, btnCancelAddKH;
    private static final int REQUEST_CODE_FOLDER = 123;
    private Uri mUri;
    private Drawable drawable;

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
                            ivAvatar.setImageBitmap(bitmap);
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
        setContentView(R.layout.activity_add_khactivity);
        setControl();
        setEvent();
    }

    private void setControl() {
        drawable = AppCompatResources.getDrawable(this, R.drawable.ic_baseline_account_circle);
        ivAvatar = findViewById(R.id.ivAvatar);
        ivAvatar.setImageDrawable(drawable);
        edtAddNameKH = findViewById(R.id.edtAddNameKH);
        edtAddAddressKH = findViewById(R.id.edtAddAddressKH);
        edtAddPhoneKH = findViewById(R.id.edtAddPhoneKH);
        edtAddEmailKH = findViewById(R.id.edtAddEmailKH);
        btnAddKH = findViewById(R.id.btnAddKH);
        btnCancelAddKH = findViewById(R.id.btnCancelAddKH);
        txtChooseImgae = findViewById(R.id.txtChooseImgae);
    }


    private void setEvent() {
        txtChooseImgae.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] arrayPermissions = {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                };
                ActivityCompat.requestPermissions(AddKHActivity.this, arrayPermissions, REQUEST_CODE_FOLDER);
            }
        });


        btnAddKH.setOnClickListener(view -> {
            if (validationKH()) {
                String fullName = edtAddNameKH.getText().toString().trim();
                String phoneNumber = edtAddPhoneKH.getText().toString().trim();
                String address = edtAddAddressKH.getText().toString().trim();
                String email = edtAddEmailKH.getText().toString().trim();
                KhachHang kh = new KhachHang(fullName, phoneNumber, address, email);
                if (ivAvatar.getDrawable().equals(drawable)) {
                    ApiUtils.getKhachHangService().createKH(kh).enqueue(new Callback<KhachHang>() {
                        @Override
                        public void onResponse(Call<KhachHang> call, Response<KhachHang> response) {
                            if (response.isSuccessful()) {
                                CustomToast.makeText(AddKHActivity.this, "Thêm khách hàng thành công",
                                        CustomToast.LENGTH_LONG, CustomToast.SUCCESS).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<KhachHang> call, Throwable t) {
                            CustomToast.makeText(AddKHActivity.this, "Thêm khách hàng thất bại",
                                    CustomToast.LENGTH_LONG, CustomToast.ERROR).show();
                            Log.e("Error", t.getMessage());
                        }
                    });
                } else {
                    String strReadPath = RealPathUtil.getRealPath(AddKHActivity.this, mUri);
                    File file = new File(strReadPath);
                    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
                    ApiUtils.getUploadService().uploadImage(body).enqueue(new Callback<Message>() {
                        @Override
                        public void onResponse(Call<Message> call, Response<Message> response) {
                            Message message = response.body();
                            kh.setAvatar(message.getMessage());
                            ApiUtils.getKhachHangService().createKH(kh).enqueue(new Callback<KhachHang>() {
                                @Override
                                public void onResponse(Call<KhachHang> call, Response<KhachHang> response) {
                                    if (response.isSuccessful()) {
                                        CustomToast.makeText(AddKHActivity.this, "Thêm khách hàng thành công",
                                                CustomToast.LENGTH_LONG, CustomToast.SUCCESS).show();
                                    }
                                }
                                @Override
                                public void onFailure(Call<KhachHang> call, Throwable t) {
                                    CustomToast.makeText(AddKHActivity.this, "Thêm khách hàng thất bại",
                                            CustomToast.LENGTH_LONG, CustomToast.ERROR).show();
                                    Log.e("Error", t.getMessage());
                                }
                            });
                        }

                        @Override
                        public void onFailure(Call<Message> call, Throwable t) {
                            CustomToast.makeText(AddKHActivity.this, "Thêm avatar thất bại",
                                    CustomToast.LENGTH_LONG, CustomToast.ERROR).show();
                            Log.e("Error", t.getMessage());
                        }
                    });
                }


            }

        });
        btnCancelAddKH.setOnClickListener(view -> {
            Intent intent = new Intent(AddKHActivity.this, ListKHActivity.class);
            startActivity(intent);
        });
    }

    boolean validationKH() {
        String fullName = edtAddNameKH.getText().toString().trim();
        String address = edtAddAddressKH.getText().toString().trim();
        String phoneNumber = edtAddPhoneKH.getText().toString().trim();
        String email = edtAddEmailKH.getText().toString().trim();

        if (fullName.isEmpty()) {
            CustomToast.makeText(AddKHActivity.this, "Không được để trống trường tên",
                    CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            return false;
        } else if (fullName.length() < 2) {
            CustomToast.makeText(AddKHActivity.this, "Tên khách hàng tối thiểu 2 ký tự",
                    CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            return false;
        } else if (fullName.length() > 30) {
            CustomToast.makeText(AddKHActivity.this, "Tên khách hàng không 30 ký tự",
                    CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            return false;
        } else if (address.isEmpty()) {
            CustomToast.makeText(AddKHActivity.this, "Không được để trống trường địa chỉ",
                    CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            return false;
        } else if (address.length() < 2) {
            CustomToast.makeText(AddKHActivity.this, "Địa chỉ tối thiểu 2 ký tự",
                    CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            return false;
        } else if (phoneNumber.length() > 12) {
            CustomToast.makeText(AddKHActivity.this, "Số điện thoại tối đa 11 số",
                    CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            return false;
        } else if (phoneNumber.length() < 10) {
            CustomToast.makeText(AddKHActivity.this, "Số điện thoại tối thiểu 10 số",
                    CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            return false;
        } else if (email.isEmpty()) {
            CustomToast.makeText(AddKHActivity.this, "Không được để trống trường email",
                    CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            return false;
        } else if (email.length() < 2) {
            CustomToast.makeText(AddKHActivity.this, "Email tối thiểu 2 ký tự",
                    CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            return false;
        }
        return true;
    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AddKHActivity.this, ListKHActivity.class);
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