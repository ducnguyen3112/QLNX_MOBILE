package com.example.quanlynhapxuat.activity.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quanlynhapxuat.R;
import com.example.quanlynhapxuat.model.Employee;
import com.example.quanlynhapxuat.model.RestErrorResponse;
import com.example.quanlynhapxuat.service.EmployeeService;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnForgot,btnSignin,btnGoSignup;
    TextInputEditText etPhoneLogin;
    EditText etPasswordLogin;
    public static int idLogin;
    public static String nameLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        btnGoSignup.setOnClickListener(this);
        btnSignin.setOnClickListener(this);

    }
    private void initViews(){
        btnForgot=findViewById(R.id.btn_forgot_password);
        btnSignin=findViewById(R.id.btn_signin);
        btnGoSignup=findViewById(R.id.btn_go_signup);
        etPhoneLogin=findViewById(R.id.et_phone_login);
        etPasswordLogin=findViewById(R.id.et_password_login);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_forgot_password:
                Intent intent=new Intent(LoginActivity.this,ForgotPasswdActivity.class);
                startActivity(intent);
            case R.id.btn_signin:
                signinClick();
//                Intent intent1=new Intent(LoginActivity.this,MainActivity.class);
//                startActivity(intent1);
                break;
            case R.id.btn_go_signup:
                intent=new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(intent);
                break;

        }
    }
    public void signinClick(){
        String phone=etPhoneLogin.getText().toString().trim();
        String password=etPasswordLogin.getText().toString();
        EmployeeService.employeeService.authenticationEmployee(phone,password).enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(Call<Employee> call, Response<Employee> response) {
                if (response.isSuccessful()){
                    Employee employee=response.body();
                    idLogin=employee.getId();
                    nameLogin=employee.getFullName();
                    Toast.makeText(LoginActivity.this,"Đăng nhập thành công!",
                            Toast.LENGTH_SHORT).show();
                    Intent intent1=new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent1);
                }
                else {
                    try {
                        Gson g = new Gson();
                        RestErrorResponse errorResponse = g.fromJson(response.errorBody().string(), RestErrorResponse.class);
                        Toast.makeText(LoginActivity.this, errorResponse.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(LoginActivity.this, e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Employee> call, Throwable t) {
                Toast.makeText(LoginActivity.this,"Sai số điện thoại hoặc mật khẩu!",
                        Toast.LENGTH_SHORT).show();
                Log.e("login", t.getMessage() );
            }
        });
    }

}