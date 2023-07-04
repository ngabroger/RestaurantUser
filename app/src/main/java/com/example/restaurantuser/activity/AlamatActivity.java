package com.example.restaurantuser.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.restaurantuser.Domain.UserDomain;
import com.example.restaurantuser.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.nullness.qual.NonNull;


public class AlamatActivity extends AppCompatActivity {
    private EditText alamatInputTxt;
    private LinearLayout saveBtn;
    private ImageView backBtn;
    private TextView alamatTxt;
    private ProgressDialog progressDialog;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alamat);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
            // Pengguna sudah login, lanjutkan dengan logika Anda
            initView();
            clickBtn();
            displayAlamat();
    }

    private void displayAlamat() {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(firebaseUser.getUid());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserDomain user = snapshot.getValue(UserDomain.class);
                if (user != null) {
                    String alamat = snapshot.child("alamat").getValue(String.class);
                    if (TextUtils.isEmpty(alamat)) {
                        alamatTxt.setText("Alamat belum diatur");
                    } else {
                        alamatTxt.setText(alamat);
                    }

                }else{
                    alamatTxt.setText("alamatnya tidak ada tolong logout bang");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any errors that occur during data retrieval
            }
        });
    }

    private void clickBtn() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), profileActivity.class));
            }
        });
        DatabaseReference userRef = firebaseDatabase.getReference().child("users").child(firebaseAuth.getUid());
        String userId = firebaseAuth.getUid();
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newAlamatInput = alamatInputTxt.getText().toString();
                if (TextUtils.isEmpty(newAlamatInput)){
                    Toast.makeText(getApplicationContext(), "Isi data dengan benar", Toast.LENGTH_SHORT).show();
                }else {
                    userRef.child("alamat").setValue(newAlamatInput).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                progressDialog.show();
                                // Data berhasil diubah
                                Toast.makeText(getApplicationContext(), "Data berhasil diubah", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                startActivity(new Intent(getApplicationContext(), profileActivity.class));

                            } else {
                                // Gagal mengubah data
                                Toast.makeText(getApplicationContext(), "Gagal mengubah data", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });
    }


    private void initView() {
        alamatInputTxt = findViewById(R.id.alamatInputTxt);
        saveBtn = findViewById(R.id.saveAlamatBtn);
        backBtn = findViewById(R.id.backBtnAlamat);
        alamatTxt = findViewById(R.id.alamatTxt);
        progressDialog = new ProgressDialog(AlamatActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("silahkan tunggu");
        progressDialog.setCancelable(false);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

}
