package com.example.quanlynhapxuat.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.quanlynhapxuat.R;

import java.util.List;

public class CustomAlertDialog extends Dialog  {

    public Button btnNegative,btnPositive;
    public ImageView imageView;
    public TextView tvMessage;


    public CustomAlertDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_alert_dialog);
        btnPositive=findViewById(R.id.btnPositive);
        btnNegative=findViewById(R.id.btnNegative);
        imageView=findViewById(R.id.imageViewAlert);
        tvMessage=findViewById(R.id.tvMessageAlert);
    }
    public void setMessage(String message){
        tvMessage.setText(message);
    }
    public void setBtnNegative(String negative){
        btnNegative.setText(negative);
    }
    public void setBtnPositive(String positive){
        btnPositive.setText(positive);
    }
    public void setImageView(int res){
        imageView.setImageResource(res);
    }
}
