package com.example.quanlynhapxuat.activity.Employee;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quanlynhapxuat.R;
import com.example.quanlynhapxuat.api.ApiUtils;
import com.example.quanlynhapxuat.model.Employee;
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

public class AddEmployeeActivity extends AppCompatActivity {

    private ImageView ivAvatarE;
    private TextView txtChooseImageE;
    private EditText edtAddNameE, edtAddAddressE, edtAddPhoneE;
    private Button btnAddE, btnCancelAddE;
    DatePicker datePicker;
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
                            ivAvatarE.setImageBitmap(bitmap);
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
        setContentView(R.layout.activity_add_employee);
        setControl();
        setEvent();
    }


    private void setControl() {
        drawable = AppCompatResources.getDrawable(this, R.drawable.ic_baseline_account_circle);
        ivAvatarE = findViewById(R.id.ivAvatarE);
        ivAvatarE.setImageDrawable(drawable);
        datePicker = findViewById(R.id.datePicker);
        edtAddNameE = findViewById(R.id.edtAddNameE);
        edtAddAddressE = findViewById(R.id.edtAddAddressE);
        edtAddPhoneE = findViewById(R.id.edtAddPhoneE);
        btnAddE = findViewById(R.id.btnAddE);
        btnCancelAddE = findViewById(R.id.btnCancelAddE);
        txtChooseImageE = findViewById(R.id.txtChooseImageE);
    }

    private void setEvent() {
        txtChooseImageE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] arrayPermissions = {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                };
                ActivityCompat.requestPermissions(AddEmployeeActivity.this, arrayPermissions, REQUEST_CODE_FOLDER);
            }
        });


        btnAddE.setOnClickListener(view -> {
            if (validationE()) {
                String fullName = edtAddNameE.getText().toString().trim();
                String phoneNumber = edtAddPhoneE.getText().toString().trim();
                String address = edtAddAddressE.getText().toString().trim();
                int status = 1;
                int role = 1;
                String password = "123456789";
                String dateOfBirth = datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth();
                Employee employee = new Employee(fullName, address, dateOfBirth, phoneNumber, role, password, status);

                if (ivAvatarE.getDrawable().equals(drawable)) {
                    ApiUtils.employeeRetrofit().createEmployee(employee).enqueue(new Callback<Employee>() {
                        @Override
                        public void onResponse(Call<Employee> call, Response<Employee> response) {
                            if (response.isSuccessful()) {
                                CustomToast.makeText(AddEmployeeActivity.this, "Thêm nhân viên thành công",
                                        CustomToast.LENGTH_LONG, CustomToast.SUCCESS).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Employee> call, Throwable t) {
                            CustomToast.makeText(AddEmployeeActivity.this, "Thêm nhân viên thất bại",
                                    CustomToast.LENGTH_LONG, CustomToast.ERROR).show();
                            Log.e("Error", t.getMessage());
                        }
                    });
                } else {
                    String strReadPath = RealPathUtil.getRealPath(AddEmployeeActivity.this, mUri);
                    File file = new File(strReadPath);
                    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
                    ApiUtils.getUploadService().uploadImage(body).enqueue(new Callback<Message>() {
                        @Override
                        public void onResponse(Call<Message> call, Response<Message> response) {
                            Message message = response.body();
                            employee.setAvatar(message.getMessage());
                            ApiUtils.employeeRetrofit().createEmployee(employee).enqueue(new Callback<Employee>() {
                                @Override
                                public void onResponse(Call<Employee> call, Response<Employee> response) {
                                    if (response.isSuccessful()) {
                                        CustomToast.makeText(AddEmployeeActivity.this, "Thêm nhân viên thành công",
                                                CustomToast.LENGTH_LONG, CustomToast.SUCCESS).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Employee> call, Throwable t) {
                                    CustomToast.makeText(AddEmployeeActivity.this, "Thêm nhân viên thất bại",
                                            CustomToast.LENGTH_LONG, CustomToast.ERROR).show();
                                    Log.e("Error", t.getMessage());
                                }
                            });
                        }

                        @Override
                        public void onFailure(Call<Message> call, Throwable t) {
                            CustomToast.makeText(AddEmployeeActivity.this, "Thêm avatar thất bại",
                                    CustomToast.LENGTH_LONG, CustomToast.ERROR).show();
                            Log.e("Error", t.getMessage());
                        }
                    });
                }
            }
        });

        btnCancelAddE.setOnClickListener(view -> {
            Intent intent = new Intent(AddEmployeeActivity.this, ListEmployeeActivity.class);
            startActivity(intent);
        });
    }

    boolean validationE() {
        String fullName = edtAddNameE.getText().toString().trim();
        String address = edtAddAddressE.getText().toString().trim();
        String phoneNumber = edtAddPhoneE.getText().toString().trim();

        if (fullName.isEmpty()) {
            CustomToast.makeText(AddEmployeeActivity.this, "Không được để trống trường tên",
                    CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            return false;
        } else if (fullName.length() < 2) {
            CustomToast.makeText(AddEmployeeActivity.this, "Tên nhân viên tối thiểu 2 ký tự",
                    CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            return false;
        } else if (fullName.length() > 30) {
            CustomToast.makeText(AddEmployeeActivity.this, "Tên nhân viên không 30 ký tự",
                    CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            return false;
        } else if (address.isEmpty()) {
            CustomToast.makeText(AddEmployeeActivity.this, "Không được để trống trường địa chỉ",
                    CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            return false;
        } else if (address.length() < 2) {
            CustomToast.makeText(AddEmployeeActivity.this, "Địa chỉ tối thiểu 2 ký tự",
                    CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            return false;
        } else if (phoneNumber.length() > 12) {
            CustomToast.makeText(AddEmployeeActivity.this, "Số điện thoại tối đa 11 số",
                    CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            return false;
        } else if (phoneNumber.length() < 10) {
            CustomToast.makeText(AddEmployeeActivity.this, "Số điện thoại tối thiểu 10 số",
                    CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            return false;
        }
        return true;
    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AddEmployeeActivity.this, ListEmployeeActivity.class);
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