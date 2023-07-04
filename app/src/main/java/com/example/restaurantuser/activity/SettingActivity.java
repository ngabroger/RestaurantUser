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
import com.example.restaurantuser.Domain.UserDomain;
import com.example.restaurantuser.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.checkerframework.checker.nullness.qual.NonNull;

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

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference profileImageRef = storageRef.child("profile_images/"+firebaseUser.getUid()+ ".jpg");
        profileImageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            // Menggunakan Glide untuk memuat foto profil dari URL
            Glide.with(this)
                    .load(uri)
                    .circleCrop()
                    .into(imageSetting);
        }).addOnFailureListener(exception -> {
            // Jika gagal mendapatkan URL foto profil, tampilkan placeholder atau gambar default
            imageSetting.setImageResource(R.drawable.logolph);
        });

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(firebaseUser.getUid());

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserDomain user = snapshot.getValue(UserDomain.class);
                if (user != null) {
                    usernameSettingTxt.setText(firebaseUser.getDisplayName());
                    emailSettingTxt.setText(firebaseUser.getEmail());

                }else{

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any errors that occur during data retrieval
            }
        });
    }
    private void setVariable() {
        backButton.setOnClickListener(view -> {
            progressDialog.show();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            progressDialog.dismiss();

                });
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