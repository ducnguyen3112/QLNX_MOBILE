package com.example.quanlynhapxuat.activity.Employee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.SwitchCompat;

import com.bumptech.glide.Glide;
import com.example.quanlynhapxuat.R;
import com.example.quanlynhapxuat.api.ApiUtils;
import com.example.quanlynhapxuat.model.Employee;
import com.example.quanlynhapxuat.utils.CustomToast;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileEmployeeActivity extends AppCompatActivity {

    private ImageView ivEAvatar;
    private EditText edtEName, edtEDate, edtEAdDress, edtEPhone;
    private Button btnCancelProfile;
    private Employee dto;
    private Drawable drawable;
    SwitchCompat edtEStatus;
    RadioButton rdbNV, rdbAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_employee);
        setControl();
        getAndSetIntentData();
        setEvent();
    }

    private void setEvent() {
        btnCancelProfile.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileEmployeeActivity.this, ListEmployeeActivity.class);
            startActivity(intent);
        });
    }

    private void setControl() {
        drawable = AppCompatResources.getDrawable(this, R.drawable.ic_baseline_account_circle);
        ivEAvatar = findViewById(R.id.ivEAvatar);
        edtEName = findViewById(R.id.edtEName);
        edtEDate = findViewById(R.id.edtEDate);
        edtEAdDress = findViewById(R.id.edtEAdDress);
        edtEPhone = findViewById(R.id.edtEPhone);
        btnCancelProfile = findViewById(R.id.btnCancelProfile);
        edtEStatus = findViewById(R.id.edtEStatus);
        rdbNV = findViewById(R.id.radio_Euser);
        rdbAd = findViewById(R.id.radio_Eadmin);
    }


    void getAndSetIntentData() {
        Intent intent = getIntent();
        int dtoId = (Integer) intent.getSerializableExtra("E");

        ApiUtils.employeeRetrofit().getEmployee(dtoId).enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(@NonNull Call<Employee> call, @NonNull Response<Employee> response) {
                if (response.isSuccessful()) {
                    dto = response.body();
                    if (dto.getAvatar() != null) {
                        Glide.with(ProfileEmployeeActivity
                                        .this)
                                .load(dto.getAvatar())
                                .circleCrop()
                                .into(ivEAvatar);
                    } else {
                        ivEAvatar.setImageDrawable(drawable);
                    }
                    edtEName.setText(dto.getFullName());
                    String str[] = dto.getDateOfBirth().split(" ");
                    String strB[] = str[0].split("-");
                    edtEDate.setText(strB[2] + "-" + strB[1] + "-" + strB[0]);
                    edtEAdDress.setText(dto.getAddress());
                    edtEPhone.setText(dto.getPhoneNumber());

                    edtEStatus.setChecked(dto.getStatus() == 1);
                    if (dto.getRole() == 1) {
                        rdbNV.isChecked();
                    } else if (dto.getRole() == 1) {
                        rdbAd.isChecked();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Employee> call, @NonNull Throwable t) {
                CustomToast.makeText(ProfileEmployeeActivity.this, "Lấy nhân viên thất bại",
                        CustomToast.LENGTH_LONG, CustomToast.ERROR).show();
                Log.e("Error", t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ProfileEmployeeActivity.this, ListEmployeeActivity.class);
        startActivity(intent);
    }
}