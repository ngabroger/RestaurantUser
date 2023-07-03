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

import com.bumptech.glide.Glide;
import com.example.restaurantuser.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class SettingActivity extends AppCompatActivity {
private ImageView backButton,imageSetting;
private TextView usernameSettingTxt, emailSettingTxt;

    private FirebaseUser firebaseUser;
private ConstraintLayout logOutBtn,changePasswordBtn,profileDataBtn;
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

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            StorageReference profileImageRef = storageRef.child("profile_images/"+firebaseUser.getUid()+ ".jpg");
            profileImageRef.getDownloadUrl().addOnSuccessListener(uri -> {

                Glide.with(this)
                        .load(uri)
                        .circleCrop()
                        .into(imageSetting);
            }).addOnFailureListener(exception -> {

                imageSetting.setImageResource(R.drawable.logolph);
            });
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
                Intent intent = new Intent(getApplicationContext(), landingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                finish();


            }
        });

        changePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.dismiss();
                startActivity(new Intent(getApplicationContext(), changePassActivity.class));
            }
        });

        profileDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.dismiss();
                startActivity(new Intent(getApplicationContext(), profileActivity.class));
            }
        });

    }

    private void initView(){
        backButton = findViewById(R.id.backButtonSetting);
        logOutBtn = findViewById(R.id.logOutBtn);
        usernameSettingTxt  = findViewById(R.id.usernameSettingTxt);
        emailSettingTxt = findViewById(R.id.emailSettingTxt);
        changePasswordBtn = findViewById(R.id.changePasswordBtn);
        profileDataBtn = findViewById(R.id.profileDataBtn);
        imageSetting = findViewById(R.id.imageSetting);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        progressDialog = new ProgressDialog(SettingActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("silahkan tunggu");
        progressDialog.setCancelable(false);

    }



}