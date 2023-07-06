package com.example.restaurantuser.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.restaurantuser.Domain.FoodDomain;
import com.example.restaurantuser.Domain.UserDomain;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import android.view.View;
import android.widget.ProgressBar;
import com.example.restaurantuser.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.ByteArrayOutputStream;

public class registerActivity extends AppCompatActivity {
    private ImageView backViewRegister;
    private EditText usernameTxt,emailRegisterTxt,passwordRegisterTxt,passwordRetRegisterTxt;
    private Button registerBtn;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        buttonInteraction();
    }

    private void buttonInteraction() {
        backViewRegister.setOnClickListener(view -> finish());

        registerBtn.setOnClickListener(view -> {
            if (usernameTxt.getText().length()>0 && emailRegisterTxt.getText().length()>0 && passwordRegisterTxt.getText().length()>0 && passwordRetRegisterTxt.getText().length()>0){
                    if(passwordRegisterTxt.getText().toString().equals(passwordRetRegisterTxt.getText().toString())){
                        register(usernameTxt.getText().toString(),emailRegisterTxt.getText().toString(),passwordRegisterTxt.getText().toString());
                    }else{
                        Toast.makeText(getApplicationContext(),"Silahkan masukkan password yang sama mase",Toast.LENGTH_SHORT).show();
                    }
            }else {
                Toast.makeText(getApplicationContext(),"Isi dulu semua baru register bang" ,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void register(String username, String emailRegister, String passwordRegister) {
        Drawable defaultIcon = getResources().getDrawable(R.drawable.personimg);
        Bitmap bitmap = ((BitmapDrawable) defaultIcon).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();


        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(emailRegister, passwordRegister)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            FirebaseUser firebaseUser = task.getResult().getUser();

                            if (firebaseUser != null) {
                                StorageReference imageRef = storageRef.child("profile_images/"+firebaseUser.getUid()+ ".jpg");
                                FoodDomain food = new FoodDomain();
                                DatabaseReference userRef = firebaseDatabase.getReference().child("users").child(firebaseUser.getUid());
                                UserDomain user = new UserDomain();
                                user.setUserId(firebaseUser.getUid());
                                user.setAlamat("");
                                user.setEmail(firebaseUser.getEmail());
                                user.setCategory("user");
                                user.setTanggalLahir("Tanggal lahir belum diatur");
                                UploadTask uploadTask = imageRef.putBytes(data);
                                // Mengatur URL gambar default sebagai foto profil pengguna baru
                                UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(username)
                                        .setPhotoUri(Uri.parse(imageRef.getPath()))
                                        .build();

                                firebaseUser.updateProfile(request).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        userRef.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    reload();
                                                }else {
                                                    Toast.makeText(getApplicationContext(), "Gagal Menyimpan ke database", Toast.LENGTH_SHORT).show();
                                                    progressDialog.dismiss();
                                                }

                                            }
                                        });


                                    }
                                });
                            } else {
                                Toast.makeText(getApplicationContext(), "Register Gagal", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    private void initView() {
        //        Aktivasi Di firebaseDatabase
        firebaseDatabase = FirebaseDatabase.getInstance();
        backViewRegister = findViewById(R.id.backViewRegister);
        usernameTxt = findViewById(R.id.usernameTxt);
        emailRegisterTxt = findViewById(R.id.emailRegisterTxt);
        passwordRegisterTxt = findViewById(R.id.passwordRegisterTxt);
        passwordRetRegisterTxt = findViewById(R.id.passwordRetRegisterTxt);
        registerBtn= findViewById(R.id.registerBtn);


//        mAuth untuk authentication user untuk register
        mAuth = FirebaseAuth.getInstance();


//        progress dialog seperti loading segala macem ngab
        progressDialog = new ProgressDialog(registerActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("silahkan tunggu");
        progressDialog.setCancelable(false);

    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }

    private void reload() {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }
}