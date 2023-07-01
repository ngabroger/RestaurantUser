package com.example.restaurantuser.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.restaurantuser.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class profileActivity extends AppCompatActivity {
    private TextView userIdTxt,usernameProfileTxt,emailProfileTxt,alamatProfileTxt,tanggalLahirProfileTxt,changePhotoProfileBtn;
    private ConstraintLayout usernameEditBtn,emailEditBtn,alamatEditBtn,tanggalLahirBtn;
    private ImageView backProfileBtn,picUserView;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initView();
        btnWorking();
        displayProfile();
    }

    private void displayProfile() {
        emailProfileTxt.setText(firebaseUser.getEmail());
        usernameProfileTxt.setText(firebaseUser.getDisplayName());
        userIdTxt.setText(firebaseUser.getUid());
    }

    private void btnWorking() {
        backProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initView() {
        userIdTxt = findViewById(R.id.userIdTxt);
             usernameProfileTxt   = findViewById(R.id.nameProfileTxt);
             emailProfileTxt   = findViewById(R.id.emailProfileTxt);
             tanggalLahirProfileTxt  = findViewById(R.id.tanggalLahirProfileTxt);
             changePhotoProfileBtn = findViewById(R.id.changePhotoProfileBtn);
             usernameEditBtn = findViewById(R.id.usernameEditBtn);
             emailEditBtn = findViewById(R.id.emailEditBtn);
             alamatEditBtn = findViewById(R.id.alamatEditBtn);
             tanggalLahirBtn = findViewById(R.id.tanggalLahirEditBtn);
             backProfileBtn = findViewById(R.id.backProfileBtn);
             picUserView = findViewById(R.id.picUserView);
             firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }
}