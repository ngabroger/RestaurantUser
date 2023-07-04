package com.example.restaurantuser.activity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.restaurantuser.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class loginActivity extends AppCompatActivity {
    private ImageView backViewLogin;
    private EditText emailTxt,passwordTxt;
    private Button loginBtn;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        btnInteraction();
    }

    private void btnInteraction() {
        backViewLogin.setOnClickListener(view -> finish());

        loginBtn.setOnClickListener(view -> {
            if(emailTxt.getText().length()>0 && passwordTxt.getText().length()>0){
                login(emailTxt.getText().toString(),passwordTxt.getText().toString() );
            }else{
                Toast.makeText(getApplicationContext(),"Silahkan isi semua data", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

    }

    private void login(String email, String password) {
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful() && task.getResult()!=null){
                            if(task.getResult().getUser()!=null){
                                reload();
                            }else{
                                Toast.makeText(getApplicationContext(),"Login Gagal", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                    }else {
                        Toast.makeText(getApplicationContext(),"Login Gagal", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
            }
        });
    }

    private void initView() {
        backViewLogin = findViewById(R.id.backViewLogin);
        emailTxt = findViewById(R.id.emailLoginTxt);
        passwordTxt = findViewById(R.id.passwordTxt);
        loginBtn = findViewById(R.id.loginBtn);
        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(loginActivity.this);
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