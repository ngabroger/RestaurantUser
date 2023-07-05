package com.example.restaurantuser.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CalendarView;

import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.example.restaurantuser.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
public class tanggalLahirActivity extends AppCompatActivity {
    private TextInputLayout datepickerInputLayout;
    private ImageView backBtn;
    private LinearLayout saveBtn;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private TextInputEditText editTanggalLahir;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tanggal_lahir);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        datepickerInputLayout = findViewById(R.id.datepickerInputLayout);
        editTanggalLahir = findViewById(R.id.editTanggalLahir);
        saveBtn = findViewById(R.id.saveTanggalLahirBtn);
        backBtn = findViewById(R.id.backTanggalLahirBtn);
        progressDialog = new ProgressDialog(tanggalLahirActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("silahkan tunggu");
        progressDialog.setCancelable(false);
        editTanggalLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
        BtnSave();


    }


    private void showDatePicker() {
        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Pilih Tanggal Lahir");
        final MaterialDatePicker<Long> datePicker = builder.build();

        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selectedDate) {
                // Proses tanggal yang dipilih
                String formattedDate = formatDate(selectedDate);
                editTanggalLahir.setText(formattedDate);
                Toast.makeText(tanggalLahirActivity.this, "Selected Date: " + formattedDate, Toast.LENGTH_SHORT).show();
            }
        });

        datePicker.show(getSupportFragmentManager(), "DATE_PICKER");
    }
    private String formatDate(Long date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(new Date(date));
    }

    private void BtnSave(){
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
            public void onClick(View v) {
                String newTanggalLahir = editTanggalLahir.getText().toString();
                if (TextUtils.isEmpty(newTanggalLahir)){
                    Toast.makeText(getApplicationContext(), "Isi data dengan benar", Toast.LENGTH_SHORT).show();
                }else {
                    userRef.child("tanggalLahir").setValue(newTanggalLahir).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                progressDialog.show();
                                // Data berhasil diubah
                                Toast.makeText(tanggalLahirActivity.this, "Data berhasil diubah", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                startActivity(new Intent(getApplicationContext(), profileActivity.class));

                            } else {
                                // Gagal mengubah data
                                Toast.makeText(tanggalLahirActivity.this, "Gagal mengubah data", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });
    }
}