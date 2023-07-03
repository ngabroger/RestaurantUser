package com.example.restaurantuser.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseUser;
import com.example.restaurantuser.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class landingActivity extends AppCompatActivity {
private Button signIn;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
private TextView signUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        initView();
        navigationButton();
        autoMainActivity();

    }


    private void autoMainActivity(){
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {

        startActivity(new Intent(landingActivity.this, MainActivity.class));
        finish();
    }else {

        }
    }

    private void navigationButton() {
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              startActivity(new Intent(landingActivity.this, loginActivity.class));
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(landingActivity.this, registerActivity.class));
            }
        });

    }

    private void initView() {
        signIn= findViewById(R.id.signInBtn);
        signUp =findViewById(R.id.signUpBtn);

    }

}