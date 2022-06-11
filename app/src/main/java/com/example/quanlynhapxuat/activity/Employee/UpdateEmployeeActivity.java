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
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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

public class UpdateEmployeeActivity extends AppCompatActivity {

    private ImageView ivEditAvatarE;
    private TextView txtEditChooseImageE;
    private EditText edtEditNameE, edtEditAddressE, edtEditPhoneE, edtEditPassWordE;
    private Button btnEditE, btnCancelEditE;
    private static final int REQUEST_CODE_FOLDER = 123;
    private Employee dto, dto1;
    private Uri mUri;
    private Drawable drawable;
    Switch edtStatus;
    DatePicker datePickerEdit;
//    RadioButton rdbNV, rdbAd;


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
                            ivEditAvatarE.setImageBitmap(bitmap);
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
        setContentView(R.layout.activity_update_employee);
        setControl();
        getAndSetIntentData();
        setEvent();
    }

    private void setControl() {
        drawable = AppCompatResources.getDrawable(this, R.drawable.ic_baseline_account_circle);
        ivEditAvatarE = findViewById(R.id.ivEditAvatarE);
        txtEditChooseImageE = findViewById(R.id.txtEditChooseImageE);
        edtEditNameE = findViewById(R.id.edtEditNameE);
        edtEditAddressE = findViewById(R.id.edtEditAddressE);
        edtEditPhoneE = findViewById(R.id.edtEditPhoneE);
        edtEditPassWordE = findViewById(R.id.edtEditPassWordE);
        btnEditE = findViewById(R.id.btnEditE);
        btnCancelEditE = findViewById(R.id.btnCancelEditE);
        edtStatus = findViewById(R.id.edtStatus);
        datePickerEdit = findViewById(R.id.datePickerEdit);
//        rdbNV = findViewById(R.id.radio_user);
//        rdbAd = findViewById(R.id.radio_admin);
    }

    private void setEvent() {
        txtEditChooseImageE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] arrayPermissions = {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                };
                ActivityCompat.requestPermissions(UpdateEmployeeActivity.this, arrayPermissions, REQUEST_CODE_FOLDER);
            }
        });

        btnEditE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (validationKH()) {
                Employee employee = new Employee(dto.getFullName(), dto.getAddress(), dto.getPhoneNumber(), dto.getDateOfBirth(), dto.getRole(), dto.getPassword(), dto.getStatus(), dto.getAvatar());
                employee.setDeliveryDockets(dto.getDeliveryDockets());

                String fullName = edtEditNameE.getText().toString().trim();
                String address = edtEditAddressE.getText().toString().trim();
                String phoneNumber = edtEditPhoneE.getText().toString().trim();
                String password = edtEditPassWordE.getText().toString().trim();
                Boolean status = edtStatus.isChecked();
                String dateOfBirth = datePickerEdit.getYear() + "-" + (datePickerEdit.getMonth() + 1) + "-" + datePickerEdit.getDayOfMonth();
                employee.setFullName(fullName);
                employee.setAddress(address);
                employee.setPhoneNumber(phoneNumber);
                employee.setPassword(password);
                employee.setDateOfBirth(dateOfBirth);
                if (status) {
                    employee.setStatus(1);
                } else {
                    employee.setStatus(0);
                }


                if (ivEditAvatarE.getDrawable().equals(drawable)) {
                    ApiUtils.employeeRetrofit().updateEmployee(dto.getId(), employee).enqueue(new Callback<Employee>() {
                        @Override
                        public void onResponse(Call<Employee> call, Response<Employee> response) {
                            if (response.isSuccessful()) {
                                CustomToast.makeText(UpdateEmployeeActivity.this, "Cập nhật thành công",
                                        CustomToast.LENGTH_LONG, CustomToast.SUCCESS).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Employee> call, Throwable t) {
                            CustomToast.makeText(UpdateEmployeeActivity.this, "Cập nhật thất bại",
                                    CustomToast.LENGTH_LONG, CustomToast.ERROR).show();
                            Log.e("Error", t.getMessage());
                        }
                    });
                } else if (mUri == null) {
                    ApiUtils.employeeRetrofit().updateEmployee(dto.getId(), employee).enqueue(new Callback<Employee>() {
                        @Override
                        public void onResponse(Call<Employee> call, Response<Employee> response) {
                            if (response.isSuccessful()) {
                                CustomToast.makeText(UpdateEmployeeActivity.this, "Cập nhật thành công",
                                        CustomToast.LENGTH_LONG, CustomToast.SUCCESS).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Employee> call, Throwable t) {
                            CustomToast.makeText(UpdateEmployeeActivity.this, "Cập nhật thất bại",
                                    CustomToast.LENGTH_LONG, CustomToast.ERROR).show();
                            Log.e("Error", t.getMessage());
                        }
                    });
                } else {
                    String strReadPath = RealPathUtil.getRealPath(UpdateEmployeeActivity.this, mUri);
                    File file = new File(strReadPath);
                    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

                    ApiUtils.getUploadService().uploadImage(body).enqueue(new Callback<Message>() {
                        @Override
                        public void onResponse(Call<Message> call, Response<Message> response) {
                            Message message = response.body();
                            employee.setAvatar(message.getMessage());
                            ApiUtils.employeeRetrofit().updateEmployee(dto.getId(), employee).enqueue(new Callback<Employee>() {
                                @Override
                                public void onResponse(Call<Employee> call, Response<Employee> response) {
                                    if (response.isSuccessful()) {
                                        CustomToast.makeText(UpdateEmployeeActivity.this, "Cập nhật thành công",
                                                CustomToast.LENGTH_LONG, CustomToast.SUCCESS).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Employee> call, Throwable t) {
                                    CustomToast.makeText(UpdateEmployeeActivity.this, "Cập nhật thất bại",
                                            CustomToast.LENGTH_LONG, CustomToast.ERROR).show();
                                    Log.e("Error", t.getMessage());
                                }
                            });
                        }

                        @Override
                        public void onFailure(Call<Message> call, Throwable t) {
                            CustomToast.makeText(UpdateEmployeeActivity.this, "Thêm avatar thất bại",
                                    CustomToast.LENGTH_LONG, CustomToast.ERROR).show();
                            Log.e("Error", t.getMessage());
                        }
                    });
                }
            }

//            }
        });
        btnCancelEditE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdateEmployeeActivity.this, ListEmployeeActivity.class);
                startActivity(intent);
            }
        });
    }

    void getAndSetIntentData() {
        Intent intent = getIntent();
        int dtoId = (Integer) intent.getSerializableExtra("E");

        ApiUtils.employeeRetrofit().getEmployee(dtoId).enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(Call<Employee> call, Response<Employee> response) {
                if (response.isSuccessful()) {
                    dto = response.body();
                    if (dto.getAvatar() != null) {
                        Glide.with(UpdateEmployeeActivity.this)
                                .load(dto.getAvatar())
                                .circleCrop()
                                .into(ivEditAvatarE);
                    } else {
                        ivEditAvatarE.setImageDrawable(drawable);
                    }
                    edtEditNameE.setText(dto.getFullName());
                    edtEditAddressE.setText(dto.getAddress());
                    edtEditPhoneE.setText(dto.getPhoneNumber());
                    edtEditPassWordE.setText(dto.getPassword());
                    edtStatus.setChecked(dto.getStatus() == 1);
                    String str[] = dto.getDateOfBirth().split(" ");
                    String strB[] = str[0].split("-");
                    int year = Integer.parseInt(strB[0]);
                    int month = Integer.parseInt(strB[1]);
                    int dayOfMonth = Integer.parseInt(strB[2]);
                    datePickerEdit.updateDate(year, (month - 1), dayOfMonth);
                }
            }

            @Override
            public void onFailure(Call<Employee> call, Throwable t) {

            }
        });


    }

//    boolean validationKH() {
//        String fullName = edtEditNameKH.getText().toString().trim();
//        String address = edtEditAddressKH.getText().toString().trim();
//        String phoneNumber = edtEditPhoneKH.getText().toString().trim();
//        String email = edtEditEmailKH.getText().toString().trim();
//
//        if (fullName.isEmpty()) {
//            CustomToast.makeText(UpdateKHActivity.this, "Không được để trống trường tên",
//                    CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
//            return false;
//        } else if (fullName.length() < 2) {
//            CustomToast.makeText(UpdateKHActivity.this, "Tên khách hàng tối thiểu 2 ký tự",
//                    CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
//            return false;
//        } else if (fullName.length() > 30) {
//            CustomToast.makeText(UpdateKHActivity.this, "Tên khách hàng không 30 ký tự",
//                    CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
//            return false;
//        } else if (address.isEmpty()) {
//            CustomToast.makeText(UpdateKHActivity.this, "Không được để trống trường địa chỉ",
//                    CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
//            return false;
//        } else if (address.length() < 2) {
//            CustomToast.makeText(UpdateKHActivity.this, "Địa chỉ tối thiểu 2 ký tự",
//                    CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
//            return false;
//        } else if (phoneNumber.length() > 12) {
//            CustomToast.makeText(UpdateKHActivity.this, "Số điện thoại tối đa 11 số",
//                    CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
//            return false;
//        } else if (phoneNumber.length() < 10) {
//            CustomToast.makeText(UpdateKHActivity.this, "Số điện thoại tối thiểu 10 số",
//                    CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
//            return false;
//        } else if (email.isEmpty()) {
//            CustomToast.makeText(UpdateKHActivity.this, "Không được để trống trường email",
//                    CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
//            return false;
//        } else if (email.length() < 2) {
//            CustomToast.makeText(UpdateKHActivity.this, "Email tối thiểu 2 ký tự",
//                    CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
//            return false;
//        }
//
//        return true;
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(UpdateEmployeeActivity.this, ListEmployeeActivity.class);
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