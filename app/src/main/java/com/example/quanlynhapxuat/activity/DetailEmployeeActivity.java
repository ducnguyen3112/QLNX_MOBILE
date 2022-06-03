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
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.quanlynhapxuat.R;
import com.example.quanlynhapxuat.activity.KhachHang.AddKHActivity;
import com.example.quanlynhapxuat.api.ApiUtils;
import com.example.quanlynhapxuat.model.Employee;
import com.example.quanlynhapxuat.model.KhachHang;
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
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailEmployeeActivity extends AppCompatActivity {

    TextInputLayout name, sdt, address, pass;
    DatePicker datePicker;
    Toolbar toolbar;
    ImageView image;
    Switch status;
    Button add, cancel;

    private boolean is_update = false;
    private int check;
    private int id = 0;
    private Drawable drawable;

    private TextView txtChooseImgae;
    private Uri mUri;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_employee);
        setControl();
        id = getIntent().getIntExtra("id", 0);
        if (id > 0) {
            displayInfo(id);
            toolbar.setTitle("Sửa thông tin");
            is_update = true;
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

    private void setControl() {
        name = findViewById(R.id.name);
        sdt = findViewById(R.id.sdt);
        address = findViewById(R.id.address);
        pass = findViewById(R.id.pass);
        add = findViewById(R.id.add);
        cancel = findViewById(R.id.cancel);

        datePicker = findViewById(R.id.datePicker);
        toolbar = findViewById(R.id.toolbar);
        image = findViewById(R.id.image);
        status = findViewById(R.id.status);
        drawable = AppCompatResources.getDrawable(this, R.drawable.ic_baseline_account_circle);
        image.setImageDrawable(drawable);
        txtChooseImgae = findViewById(R.id.txtChooseImgae);
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

    public void displayInfo(int id) {
        ApiUtils.employeeRetrofit().getEmployee(id).enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(Call<Employee> call, Response<Employee> response) {
                if (response.isSuccessful()) {
                    Employee employee = response.body();
                    name.getEditText().setText(employee.getFullName());
                    if (employee.getStatus() == 1) {
                        status.setChecked(true);
                    } else {
                        status.setChecked(false);
                    }
                    if (employee.getAvatar() != null) {
                        Glide.with(DetailEmployeeActivity.this).load(employee.getAvatar()).into(image);
                    }
                    sdt.getEditText().setText(employee.getPhoneNumber());
                    address.getEditText().setText(employee.getAddress());
                    pass.getEditText().setText(employee.getPassword());
                    try {
                        String str[] = employee.getDateOfBirth().split(" ");
                        String strB[] = str[0].split("-");
                        int year = Integer.parseInt(strB[0]);

                        int month = Integer.parseInt(strB[1]);
                        int dayOfMonth = Integer.parseInt(strB[2]);

                        datePicker.updateDate(year, (month - 1), dayOfMonth);

                    } catch (Exception e) {
                        CustomToast.makeText(DetailEmployeeActivity.this, e.toString(),
                                CustomToast.LENGTH_LONG, CustomToast.ERROR).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Employee> call, Throwable t) {
                CustomToast.makeText(DetailEmployeeActivity.this, t.toString(),
                        CustomToast.LENGTH_LONG, CustomToast.ERROR).show();
            }
        });
    }

    public void hanleClickUploadFile() {
        txtChooseImgae.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] arrayPermissions = {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                };
                ActivityCompat.requestPermissions(DetailEmployeeActivity.this, arrayPermissions, Constants.MY_CODE);
            }
        });
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

    public void handleClickButtonAdd() {
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameStr = name.getEditText().getText().toString().trim();
                String phoneStr = sdt.getEditText().getText().toString().trim();
                String addressStr = address.getEditText().getText().toString().trim();
                String passStr = pass.getEditText().getText().toString().trim();
                String dayStr = datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth() + " 00:00:00";
                Employee e = new Employee(nameStr, addressStr, dayStr, phoneStr, 1, passStr, check);
                if (checkInput(nameStr, addressStr, datePicker.getYear(), phoneStr, passStr)) {
                    String realPath = RealPathUtil.getRealPath(DetailEmployeeActivity.this, mUri);
                    File file = new File(realPath);
                    RequestBody requestBodyImage = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    MultipartBody.Part muPart = MultipartBody.Part.createFormData("avatar", file.getName(), requestBodyImage);
                    if (is_update == false) {
                        ApiUtils.getUploadService().uploadImage(muPart).enqueue(new Callback<Message>() {
                            @Override
                            public void onResponse(Call<Message> call, Response<Message> response) {
                                Message message = response.body();
                                e.setAvatar(message.getMessage());
                                Log.d("TAG", message.getMessage());
                                addEmployee(e);
                            }

                            @Override
                            public void onFailure(Call<Message> call, Throwable t) {
                                CustomToast.makeText(DetailEmployeeActivity.this, t.toString(),
                                        CustomToast.LENGTH_LONG, CustomToast.ERROR).show();
                            }
                        });
                        Intent intent = new Intent(DetailEmployeeActivity.this, EmployeesActivity.class);
                        startActivityForResult(intent, Constants.RESULT_PRODUCT_ACTIVITY);
                    } else {
                        ApiUtils.getUploadService().uploadImage(muPart).enqueue(new Callback<Message>() {
                            @Override
                            public void onResponse(Call<Message> call, Response<Message> response) {
                                Message message = response.body();
                                e.setAvatar(message.getMessage());
                                Log.d("TAG", message.getMessage());
                                updateEmployee(id, e);
                            }

                            @Override
                            public void onFailure(Call<Message> call, Throwable t) {
                                CustomToast.makeText(DetailEmployeeActivity.this, t.toString(),
                                        CustomToast.LENGTH_LONG, CustomToast.ERROR).show();
                            }
                        });
                        Intent intent = new Intent(DetailEmployeeActivity.this, EmployeesActivity.class);
                        startActivityForResult(intent, Constants.RESULT_PRODUCT_ACTIVITY);
                    }
                }
            }
        });
    }

    private void addEmployee(Employee e) {
        ApiUtils.employeeRetrofit().createEmployee(e).enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(Call<Employee> call, Response<Employee> response) {
                if (response.isSuccessful()) {
                    CustomToast.makeText(DetailEmployeeActivity.this, "Thêm thành công!!",
                            CustomToast.LENGTH_LONG, CustomToast.SUCCESS).show();
                }
            }

            @Override
            public void onFailure(Call<Employee> call, Throwable t) {
                CustomToast.makeText(DetailEmployeeActivity.this, t.toString(),
                        CustomToast.LENGTH_LONG, CustomToast.ERROR).show();
            }
        });
    }

    private void updateEmployee(int id, Employee e) {
        ApiUtils.employeeRetrofit().updateEmployee(id, e).enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(Call<Employee> call, Response<Employee> response) {
                if (response.isSuccessful()) {
                    CustomToast.makeText(DetailEmployeeActivity.this, "Sửa thông tin thành công!!",
                            CustomToast.LENGTH_LONG, CustomToast.SUCCESS).show();
                }
            }

            @Override
            public void onFailure(Call<Employee> call, Throwable t) {
                CustomToast.makeText(DetailEmployeeActivity.this, t.toString(),
                        CustomToast.LENGTH_LONG, CustomToast.ERROR).show();
            }
        });
    }

    private void handleClickButtonCancel() {
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailEmployeeActivity.this, EmployeesActivity.class);
                startActivityForResult(intent, Constants.RESULT_PRODUCT_ACTIVITY);
            }
        });
    }

    boolean checkInput(String nameStr, String addressStr, int year, String phoneStr, String passStr) {
        if (nameStr.isEmpty()) {
            CustomToast.makeText(DetailEmployeeActivity.this, "Vui lòng nhập họ tên!!",
                    CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            return false;
        } else if (phoneStr.isEmpty()) {
            CustomToast.makeText(DetailEmployeeActivity.this, "Vui lòng nhập số điện thoại!!",
                    CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            return false;
        } else if (addressStr.isEmpty()) {
            CustomToast.makeText(DetailEmployeeActivity.this, "Vui lòng nhập địa chỉ!!",
                    CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            return false;
        } else if (passStr.isEmpty()) {
            CustomToast.makeText(DetailEmployeeActivity.this, "Vui lòng nhập mật khẩu!!",
                    CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            return false;
        } else if ((2022 - year) < 18) {
            CustomToast.makeText(DetailEmployeeActivity.this, "Tuổi nhân viên >=  18 tuổi!!",
                    CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            return false;
        }
        return true;
    }
}