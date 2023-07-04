package com.example.restaurantuser.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.restaurantuser.R;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class UsernameActivity extends AppCompatActivity {
    private ImageView backButton;
    private EditText usernameInputTxt;
    private FirebaseAuth mAuth;
    private LinearLayout saveButton;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_username);
        initView();
        buttonInteraction();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    private void buttonInteraction() {
        saveButton.setOnClickListener(view -> {
            if(usernameInputTxt.getText().length()>0){
                    changeUsername(usernameInputTxt.getText().toString());
            }else {
                Toast.makeText(this, "Isi Username dulu bang", Toast.LENGTH_SHORT).show();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), profileActivity.class));
                progressDialog.show();
            }
        });
    }

    private void changeUsername(String username) {
        progressDialog.show();
        FirebaseUser user = mAuth.getCurrentUser();
        if (username.equals(user.getDisplayName())) {
            Toast.makeText(this, "Username tidak boleh sama dengan yang sebelumnya", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return;
        }
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Username berhasil diubah", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    } else {
                        Toast.makeText(this, "Gagal mengubah username", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
    }

    private void initView() {
        backButton = findViewById(R.id.backBtnUsername);
        usernameInputTxt = findViewById(R.id.editUsernameTxt);
        saveButton = findViewById(R.id.saveUsernameBtn);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(UsernameActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("silahkan tunggu");
        progressDialog.setCancelable(false);
    }
}