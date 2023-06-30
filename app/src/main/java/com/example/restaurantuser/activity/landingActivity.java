package com.example.restaurantuser.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.restaurantuser.R;

public class landingActivity extends AppCompatActivity {
private Button signIn;
private TextView signUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        initView();
        navigationButton();
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