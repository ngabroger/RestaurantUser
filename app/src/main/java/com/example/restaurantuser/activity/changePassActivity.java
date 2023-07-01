package com.example.restaurantuser.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.example.restaurantuser.R;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class changePassActivity extends AppCompatActivity {
    private EditText oldPasswordTxt,newPasswordTxt,RePasswordTxt;
    private ImageView backBtnPassword;
    private LinearLayout saveBtn;
    private FirebaseAuth mAuth;

    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
        initView();
        buttonInteraction();
        FirebaseUser currentUser = mAuth.getCurrentUser();

    }

    private void buttonInteraction() {
        backBtnPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        saveBtn.setOnClickListener(view -> {
            if (oldPasswordTxt.getText().length()>0 && newPasswordTxt.getText().length()>0 && RePasswordTxt.getText().length()>0){
                if (newPasswordTxt.getText().toString().equals(RePasswordTxt.getText().toString())){
                        changePassword(oldPasswordTxt.getText().toString(),newPasswordTxt.getText().toString());
                }else {
                    Toast.makeText(getApplicationContext(),"Passwordnya Belum sama Euy",Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(getApplicationContext(),"Isi dulu semuanya bang",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void changePassword(String oldPassword, String newPassword) {
        progressDialog.show();
        FirebaseUser user = mAuth.getCurrentUser();
        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), oldPassword);
        
        user.reauthenticate(credential)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        user.updatePassword(newPassword)
                                .addOnCompleteListener(updateTask ->{
                                   if(updateTask.isSuccessful()){
                                       Toast.makeText(getApplicationContext(), "Password Berhasil Diubah , silahkan login Kembali", Toast.LENGTH_SHORT).show();
                                       progressDialog.dismiss();
                                   }else{
                                       progressDialog.dismiss();
                                       Toast.makeText(getApplicationContext(), "Gagal Mengubah Password", Toast.LENGTH_SHORT).show();
                                   }
                                });        
                    }else {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Password Lama Salah", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void initView() {
        oldPasswordTxt = findViewById(R.id.oldPasswordTxt);
        newPasswordTxt = findViewById(R.id.newPasswordTxt);
        RePasswordTxt = findViewById(R.id.rePasswordTxt);
        saveBtn = findViewById(R.id.savePasswordBtn);
        backBtnPassword = findViewById(R.id.backBtnpassword);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(changePassActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("silahkan tunggu");
        progressDialog.setCancelable(false);
    }
}