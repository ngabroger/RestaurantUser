package com.example.restaurantuser.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.restaurantuser.Domain.FoodDomain;
import com.example.restaurantuser.R;
import com.example.restaurantuser.adapter.foodListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapterFoodList;
            private RecyclerView recyclerViewFood;
            private FirebaseUser firebaseUser;
            private TextView usernameTxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initRecycleView();
        bottoNavigation();
        displayUser();
    }

    @SuppressLint("SetTextI18n")
    private void displayUser() {
        if (firebaseUser.getDisplayName()!=null) {
            usernameTxt.setText(firebaseUser.getDisplayName());
        }else {
            usernameTxt.setText("Login Gagal!");
        }
    }

    private void bottoNavigation() {
        LinearLayout homeBtn = findViewById(R.id.homeBtn);
        LinearLayout cartBtn = findViewById(R.id.cartBtn);
        LinearLayout settingBtn = findViewById(R.id.settingBtn);
        homeBtn.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, MainActivity.class)));
        cartBtn.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, CartActivity.class)));
        settingBtn.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, SettingActivity.class)));
        usernameTxt =findViewById(R.id.displayUsernameTxt);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        
    }

    private void initRecycleView(){
        ArrayList<FoodDomain> items = new ArrayList<>();
        items.add(new FoodDomain("BIG Burger","The best food On our Menu that ever made in our restaurant , try now before it's gone to our world","fast_1",50000,44,"Food",4));
        items.add(new FoodDomain("Pizza","You know Pizza , if you know you know the delicious this food ever made , from italy to our los polos hermanos","fast_2",75000,42,"Food",3));
        items.add(new FoodDomain("Vegetable Pizza","for vegan we made , this pizza made from love to vegan , believe that shit n**ga","fast_3",80000,44,"Food",4));

        recyclerViewFood=findViewById((R.id.viewBest));
        recyclerViewFood.setLayoutManager(new GridLayoutManager(this,2));
        adapterFoodList = new foodListAdapter(items);
        recyclerViewFood.setAdapter(adapterFoodList);
    }
}