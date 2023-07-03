package com.example.restaurantuser.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.bumptech.glide.Glide;
import com.example.restaurantuser.Domain.FoodDomain;
import com.example.restaurantuser.R;
import com.example.restaurantuser.adapter.foodListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapterFoodList;
    private DatabaseReference databaseReference;
            private RecyclerView recyclerViewFood;
            private FirebaseUser firebaseUser;
            private ImageView  imageProfile;
            private TextView usernameTxt,alamatHomeTxt;
            private ArrayList<FoodDomain> items;
            private foodListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottoNavigation();

        recyclerViewFood = findViewById(R.id.viewBest);
        recyclerViewFood.setLayoutManager(new GridLayoutManager(this, 2));

        items = new ArrayList<>();
        adapter = new foodListAdapter(MainActivity.this, items);
        recyclerViewFood.setAdapter(adapter);


        displayUser();

        readData();
    }

    @SuppressLint("SetTextI18n")
    private void displayUser() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser.getDisplayName()!=null && firebaseUser.getPhotoUrl()!=null) {
            usernameTxt.setText(firebaseUser.getDisplayName());
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(firebaseUser.getUid());
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String alamat = snapshot.child("alamat").getValue(String.class);
                        // Update your UI or perform actions with the address data
                        alamatHomeTxt.setText(alamat);

                    }else{
                        alamatHomeTxt.setText("alamatnya tidak ada tolong logout bang");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle any errors that occur during data retrieval
                }
            });
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            StorageReference profileImageRef = storageRef.child("profile_images/"+firebaseUser.getUid()+ ".jpg");
            profileImageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                // Menggunakan Glide untuk memuat foto profil dari URL
                Glide.with(this)
                        .load(uri)
                        .circleCrop()
                        .into(imageProfile);
            }).addOnFailureListener(exception -> {
                // Jika gagal mendapatkan URL foto profil, tampilkan placeholder atau gambar default
                imageProfile.setImageResource(R.drawable.logolph);
            });
        }else {
            usernameTxt.setText("Login Gagal!");
        }
    }

    private void bottoNavigation() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("products");
        LinearLayout homeBtn = findViewById(R.id.homeBtn);
        LinearLayout cartBtn = findViewById(R.id.cartBtn);
        alamatHomeTxt = findViewById(R.id.alamatHomeTxt);
        LinearLayout settingBtn = findViewById(R.id.settingBtn);
        imageProfile = findViewById(R.id.imageView);

        cartBtn.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, CartActivity.class)));
        settingBtn.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, SettingActivity.class)));
        usernameTxt =findViewById(R.id.displayUsernameTxt);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        
    }




    private void readData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                items.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    FoodDomain foodDomain = dataSnapshot.getValue(FoodDomain.class);
                    items.add(foodDomain);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Tangani kesalahan jika diperlukan
            }
        });
    }
}