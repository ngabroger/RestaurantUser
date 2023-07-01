package com.example.restaurantuser.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.restaurantuser.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SettingActivity extends AppCompatActivity {
private ImageView backButton;
private TextView usernameSettingTxt, emailSettingTxt;
    private FirebaseUser firebaseUser;
private ConstraintLayout logOutBtn,changePasswordBtn;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
        displayUser();
        setVariable();
    }
    @SuppressLint("SetTextI18n")
    private void displayUser() {
        if (firebaseUser.getDisplayName()!=null && firebaseUser.getEmail()!=null) {
            usernameSettingTxt.setText(firebaseUser.getDisplayName());
            emailSettingTxt.setText(firebaseUser.getEmail());
        }else {
            usernameSettingTxt.setText("Login Gagal!");
            emailSettingTxt.setText("email nya mana bang");
        }
    }
    private void setVariable() {
        backButton.setOnClickListener(view -> finish());
        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), landingActivity.class));
            }
        });

        changePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), changePassActivity.class));
            }
        });
    }

    private void initView(){
        backButton = findViewById(R.id.backButtonSetting);
        logOutBtn = findViewById(R.id.logOutBtn);
        usernameSettingTxt  = findViewById(R.id.usernameSettingTxt);
        emailSettingTxt = findViewById(R.id.emailSettingTxt);
        changePasswordBtn = findViewById(R.id.changePasswordBtn);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        progressDialog = new ProgressDialog(SettingActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("silahkan tunggu");
        progressDialog.setCancelable(false);

    }
}