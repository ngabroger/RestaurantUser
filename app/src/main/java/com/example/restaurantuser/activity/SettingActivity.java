package com.example.restaurantuser.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.example.restaurantuser.R;

public class SettingActivity extends AppCompatActivity {
private ImageView backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
        setVariable();

    }

    private void setVariable() {
        backButton.setOnClickListener(view -> finish());
    }

    private void initView(){
        backButton = findViewById(R.id.backButtonSetting);
    }
}