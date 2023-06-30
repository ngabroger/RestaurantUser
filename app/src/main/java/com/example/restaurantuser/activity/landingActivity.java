package com.example.restaurantuser.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.restaurantuser.R;

public class landingActivity extends AppCompatActivity {
private Button singIn;
private TextView signUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        initView();
    }

    private void initView() {
        singIn= findViewById(R.id.signInBtn);
        signUp =findViewById(R.id.signUpBtn);
    }

}