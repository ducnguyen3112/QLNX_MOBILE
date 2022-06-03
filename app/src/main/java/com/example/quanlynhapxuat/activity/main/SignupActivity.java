package com.example.quanlynhapxuat.activity.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanlynhapxuat.R;
import com.example.quanlynhapxuat.model.Employee;
import com.example.quanlynhapxuat.model.RestErrorResponse;
import com.example.quanlynhapxuat.service.EmployeeService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener{
    ImageView ivBack;
    Button btnSignup;
    TextView tvResendOTP;
    TextInputEditText etName,etPhoneSignup;
    ProgressBar progressBar;
EditText etPassword,etPassword2;
Employee employee;

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initViews();
        mAuth=FirebaseAuth.getInstance();
        ivBack.setOnClickListener(this);
        btnSignup.setOnClickListener(this);
    }
    private void initViews(){
        ivBack=findViewById(R.id.iv_backsignin);
        btnSignup=findViewById(R.id.btn_signup);
        tvResendOTP=findViewById(R.id.tv_resendotp);
        etName=findViewById(R.id.et_fullname);
        etPhoneSignup=findViewById(R.id.et_phone_signup);
        etPassword=findViewById(R.id.et_password);
        etPassword2=findViewById(R.id.et_confirm_password);
        progressBar=findViewById(R.id.progessSignup);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_signup:
                String phoneNumber=etPhoneSignup.getText().toString().trim();
                String passwd1=etPassword.getText().toString();
                String passwd2=etPassword2.getText().toString();
                if (etName.getText().toString().isEmpty()){
                    Toast.makeText(SignupActivity.this,"Không được để trống họ và tên!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }else if(!phoneNumber.matches("^[0]{1}[0-9]{9}$")){
                    Toast.makeText(SignupActivity.this,"Định dạng số điện thoại không đúng!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }else if(passwd1.isEmpty()||passwd2.isEmpty()){
                    Toast.makeText(SignupActivity.this,"Không để trống mật khẩu!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }else if(!passwd1.equals(passwd2)){
                    Toast.makeText(SignupActivity.this,"Mật khẩu phải trùng nhau!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                registrationClick(phoneNumber);
                break;
            case R.id.iv_backsignin:
                onBackPressed();
                break;
        }
    }

    private void verifyPhoneNumber(String phoneNumber){
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                progressBar.setVisibility(View.GONE);
                                btnSignup.setVisibility(View.VISIBLE);
                                signInWithPhoneAuthCredential(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                progressBar.setVisibility(View.GONE);
                                btnSignup.setVisibility(View.VISIBLE);
                                Toast.makeText(SignupActivity.this,"Xác thực không thành công"+e.getMessage(),Toast.LENGTH_SHORT).show();
                                Log.e("otp", e.getMessage() );
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                progressBar.setVisibility(View.GONE);
                                btnSignup.setVisibility(View.VISIBLE);
                                super.onCodeSent(s, forceResendingToken);
                                gotoEnterOTPActivity(phoneNumber,s,employee);
                            }
                        })          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }



    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = task.getResult().getUser();
                            // Update UI
                            gotoMainActivity(user.getPhoneNumber());
                        } else {
                            // Sign in failed, display a message and update the UI
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(SignupActivity.this,"The verification" +
                                        "code entered was invalid",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void gotoMainActivity(String phoneNumber) {
        Intent intent=new Intent(SignupActivity.this,LoginActivity.class);
        intent.putExtra("phone_num",phoneNumber);
        startActivity(intent);
    }

    private void gotoEnterOTPActivity(String phoneNumber, String s,Employee employee) {
        Intent intent=new Intent(SignupActivity.this,VerifyOTPActivity.class);
        intent.putExtra("phone_num",phoneNumber);
        intent.putExtra("verification_id",s);
        Bundle bundle=new Bundle();
        bundle.putSerializable("employeeSignin",employee);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    private  void registrationClick(String phoneNumber) {

        employee = new Employee();
        employee.setFullName(etName.getText().toString().trim());
        employee.setRole(1);
        employee.setStatus(1);
        employee.setPhoneNumber(etPhoneSignup.getText().toString());
        employee.setPassword(etPassword.getText().toString());
        verifyPhoneNumber("+84" + etPhoneSignup.getText().toString().substring(1));
    }
}