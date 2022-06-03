package com.example.quanlynhapxuat.activity.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyOTPActivity extends AppCompatActivity implements View.OnClickListener {
    TextInputEditText etOTPCode;
    Button btnVerify;
    TextView tvResend,tvPhoneNumVerify;

    private String mPhoneNumber;
    private String mVerificationId;
    Employee employee;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.ForceResendingToken mForceResendingToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        mAuth=FirebaseAuth.getInstance();
        initViews();
        btnVerify.setOnClickListener(this);
        tvResend.setOnClickListener(this);
        getDataIntent();
        Toast.makeText(VerifyOTPActivity.this,employee.getFullName(),Toast.LENGTH_SHORT).show();

    }
    private void initViews(){
        etOTPCode=findViewById(R.id.et_otpcode);
        btnVerify=findViewById(R.id.btn_verify);
        tvResend=findViewById(R.id.tv_resendotp);
        tvPhoneNumVerify=findViewById(R.id.tv_phoneverify);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_verify:
                String otp=etOTPCode.getText().toString().trim();
                verifyOTPCode(otp);
                break;
            case R.id.tv_resendotp:
                resendOTPCode();

        }
    }
    private void getDataIntent(){
        mPhoneNumber=getIntent().getStringExtra("phone_num");
        mVerificationId=getIntent().getStringExtra("verification_id");
        employee= (Employee) getIntent().getExtras().get("employeeSignin");
    }
    private void verifyOTPCode(String otpCode){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otpCode);
        signInWithPhoneAuthCredential(credential);
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = task.getResult().getUser();
                            // Update UI
                            //gotoMainActivity(user.getPhoneNumber());
                            EmployeeService.employeeService.registrationEmployee(employee)
                                    .enqueue(new Callback<Employee>() {
                                        @Override
                                        public void onResponse(Call<Employee> call, Response<Employee> response) {

                                            if (response.isSuccessful()){
                                                Toast.makeText(VerifyOTPActivity.this,"Tạo tài khoản thành công!" ,Toast.LENGTH_SHORT).show();
                                                Intent intent=new Intent(VerifyOTPActivity.this,LoginActivity.class);
                                                startActivity(intent);
                                            }else{
                                                try {
                                                    Gson g = new Gson();
                                                    RestErrorResponse errorResponse = g.fromJson(response.errorBody().string(), RestErrorResponse.class);
                                                    Toast.makeText(VerifyOTPActivity.this,errorResponse.getMessage(),
                                                            Toast.LENGTH_SHORT).show();
                                                } catch (Exception e) {
                                                    Log.e("opt", e.getMessage() );
                                                }

                                            }
                                        }
                                        @Override
                                        public void onFailure(Call<Employee> call, Throwable t) {
                                            Toast.makeText(VerifyOTPActivity.this,"Lỗi! Không thể tạo tài khoản. Hãy" +
                                                            " thử lại.",
                                                    Toast.LENGTH_SHORT).show();
                                            Log.e("opt", t.getMessage() );
                                        }
                                    });
                        } else {
                            // Sign in failed, display a message and update the UI
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(VerifyOTPActivity.this,"The verification" +
                                        "code entered was invalid",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
    private void resendOTPCode(){
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(mPhoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setForceResendingToken(mForceResendingToken)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signInWithPhoneAuthCredential(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(VerifyOTPActivity.this,"Xác thực không thành công",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                mVerificationId=s;
                                mForceResendingToken=forceResendingToken;
                            }
                        })          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void gotoMainActivity(String phoneNumber) {
        Intent intent=new Intent(this,LoginActivity.class);
        intent.putExtra("phone_num",phoneNumber);
        startActivity(intent);
    }
}