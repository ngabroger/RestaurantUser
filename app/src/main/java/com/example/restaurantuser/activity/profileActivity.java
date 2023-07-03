package com.example.restaurantuser.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.restaurantuser.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.File;

public class profileActivity extends AppCompatActivity {
    private TextView userIdTxt,usernameProfileTxt,emailProfileTxt,alamatProfileTxt,tanggalLahirProfileTxt,changePhotoProfileBtn;
    private ConstraintLayout usernameEditBtn,emailEditBtn,alamatEditBtn,tanggalLahirBtn;
    private ImageView backProfileBtn,picUserView;
    private static final int PICK_IMAGE_REQUEST = 1;
    private ProgressDialog progressDialog;
    private static final long MAX_IMAGE_SIZE_BYTES = 5 * 1024 * 1024;

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
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference profileImageRef = storageRef.child("profile_images/"+firebaseUser.getUid()+ ".jpg");

        profileImageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            // Menggunakan Glide untuk memuat foto profil dari URL
            Glide.with(this)
                    .load(uri)
                    .circleCrop()
                    .into(picUserView);
        }).addOnFailureListener(exception -> {
            // Jika gagal mendapatkan URL foto profil, tampilkan placeholder atau gambar default
            picUserView.setImageResource(R.drawable.logolph);
        });

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(firebaseUser.getUid());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String alamat = snapshot.child("alamat").getValue(String.class);
                    String tanggallahir = snapshot.child("tanggalLahir").getValue(String.class);
                    // Update your UI or perform actions with the address data
                    alamatProfileTxt.setText(alamat);
                    tanggalLahirProfileTxt.setText(tanggallahir);

                }else{
                    alamatProfileTxt.setText("alamatnya tidak ada tolong logout bang");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any errors that occur during data retrieval
            }
        });


    }

    private void btnWorking() {
        backProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                finish();
            }
        });

        changePhotoProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Membuka galeri untuk memilih foto baru
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        usernameEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(profileActivity.this, UsernameActivity.class));
                progressDialog.show();
                progressDialog.dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri photoUri = data.getData();

            // Lakukan pengunggahan foto ke Firebase Storage dan pembaruan URL foto profil
            uploadPhotoAndUpdateProfile(photoUri);
        }
    }

    private void uploadPhotoAndUpdateProfile(Uri photoUri) {
        progressDialog.show();
        try {
            // Periksa ukuran gambar sebelum mengunggahnya
            File imageFile = new File(photoUri.getPath());
            long imageSize = imageFile.length();
            if (imageSize > MAX_IMAGE_SIZE_BYTES) {
                // Gambar terlalu besar, berikan pesan kesalahan atau tindakan yang sesuai
                Toast.makeText(profileActivity.this, "Ukuran gambar terlalu besar", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                return;

            }

            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference imageRef = storageRef.child("profile_images/" + firebaseUser.getUid() + ".jpg");
            UploadTask uploadTask = imageRef.putFile(photoUri);

            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        // Foto berhasil diunggah ke Firebase Storage
                        // Dapatkan URL gambar baru
                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // Update URL foto profil pengguna di Firebase Authentication
                                UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                                        .setPhotoUri(uri)
                                        .build();

                                firebaseUser.updateProfile(request).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            reload();
                                            // Foto profil berhasil diperbarui
                                            // Tampilkan foto baru menggunakan Glide atau cara yang sama seperti sebelumnya
                                            Glide.with(profileActivity.this)
                                                    .load(uri)
                                                    .circleCrop()
                                                    .into(picUserView);

                                        } else {
                                            Toast.makeText(profileActivity.this, "Gambar Tidak tersimpan", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });
                    } else {
                        // Gagal mengunggah foto ke Firebase Storage
                        // Tampilkan pesan error atau lakukan penanganan kesalahan lainnya
                        Toast.makeText(profileActivity.this, "Gambar tida tersimpan", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void reload() {
        Toast.makeText(this, "Berhasil Mengubah Photo Profile", Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
    }

    private void initView() {
        userIdTxt = findViewById(R.id.userIdTxt);
             usernameProfileTxt   = findViewById(R.id.nameProfileTxt);
             emailProfileTxt   = findViewById(R.id.emailProfileTxt);
             alamatProfileTxt= findViewById(R.id.alamatProfileTxt);
             tanggalLahirProfileTxt  = findViewById(R.id.tanggalLahirProfileTxt);
             changePhotoProfileBtn = findViewById(R.id.changePhotoProfileBtn);
             usernameEditBtn = findViewById(R.id.usernameEditBtn);
             emailEditBtn = findViewById(R.id.emailEditBtn);
             alamatEditBtn = findViewById(R.id.alamatEditBtn);
             tanggalLahirBtn = findViewById(R.id.tanggalLahirEditBtn);
             backProfileBtn = findViewById(R.id.backProfileBtn);
             picUserView = findViewById(R.id.picUserView);
             firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
             progressDialog = new ProgressDialog(profileActivity.this);
             progressDialog.setTitle("Loading");
             progressDialog.setMessage("silahkan tunggu");
             progressDialog.setCancelable(false);
    }
}