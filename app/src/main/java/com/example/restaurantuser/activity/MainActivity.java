package com.example.restaurantuser.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restaurantuser.Domain.FoodDomain;
import com.example.restaurantuser.R;
import com.example.restaurantuser.adapter.foodListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.bumptech.glide.Glide;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerViewFood;
    private foodListAdapter adapterFoodList;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private ImageView imageProfile;
    private TextView usernameTxt, alamatHomeTxt;
    private ArrayList<FoodDomain> items;
    private LinearLayout orderBtn;
    private ConstraintLayout clfood, cldrink, clmore;
    private List<FoodDomain> originalFoodList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the database reference
        databaseReference = FirebaseDatabase.getInstance().getReference().child("products");
        orderBtn = findViewById(R.id.orderLinear);
        recyclerViewFood = findViewById(R.id.viewBest);
        recyclerViewFood.setLayoutManager(new GridLayoutManager(this, 2));
        adapterFoodList = new foodListAdapter((Context) MainActivity.this, items);
        recyclerViewFood.setAdapter(adapterFoodList);
        EditText searchEditText = findViewById(R.id.editTextText4);
        // Add a TextWatcher to the search EditTex
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // No implementation needed here
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Call the filter method with the current text in the search EditText
                filterFoodList(charSequence.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // No implementation needed here
            }
        });
        bottoNavigation();

        items = new ArrayList<>();
        adapterFoodList.setItems(items);
        displayUser();
        readData();
        orderZone();
    }

    private void orderZone() {
        // TODO: Implement your order functionality here
    }

    @SuppressLint("SetTextI18n")
    private void displayUser() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser.getDisplayName() != null && firebaseUser.getPhotoUrl() != null) {
            usernameTxt.setText(firebaseUser.getDisplayName());
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(firebaseUser.getUid());
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String alamat = snapshot.child("alamat").getValue(String.class);
                        // Update your UI or perform actions with the address data
                        if (TextUtils.isEmpty(alamat)) {
                            alamatHomeTxt.setText("Alamat belum diatur");
                        } else {
                            alamatHomeTxt.setText(alamat);
                        }
                    } else {
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
            StorageReference profileImageRef = storageRef.child("profile_images/" + firebaseUser.getUid() + ".jpg");
            profileImageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                // Menggunakan Glide untuk memuat foto profil dari URL
                Glide.with(MainActivity.this)
                        .load(uri)
                        .circleCrop()
                        .into(imageProfile);
            }).addOnFailureListener(exception -> {
                // Jika gagal mendapatkan URL foto profil, tampilkan placeholder atau gambar default
                imageProfile.setImageResource(R.drawable.logolph);
            });
        } else {
            usernameTxt.setText("Login Gagal!");
        }
    }

    private void bottoNavigation() {
        LinearLayout cartBtn = findViewById(R.id.cartBtn);
        alamatHomeTxt = findViewById(R.id.alamatHomeTxt);
        LinearLayout settingBtn = findViewById(R.id.settingBtn);
        imageProfile = findViewById(R.id.imageView);
        cartBtn.setOnClickListener(view -> {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(firebaseUser.getUid());
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String alamat = snapshot.child("alamat").getValue(String.class);
                        if (TextUtils.isEmpty(alamat)) {
                            Intent intent = new Intent(MainActivity.this, AlamatActivity.class);
                            startActivity(intent);
                            Toast.makeText(MainActivity.this, "Tolong Isi Alamat terlebih dahulu", Toast.LENGTH_SHORT).show();
                        } else {
                            startActivity(new Intent(MainActivity.this, CartActivity.class));
                        }
                    } else {
                        // Data pengguna tidak ada
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Tangani kesalahan yang terjadi saat mengambil data
                }
            });
        });

        settingBtn.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, SettingActivity.class)));
        usernameTxt = findViewById(R.id.displayUsernameTxt);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        // Add click listener to the "Food" category
        clfood = findViewById(R.id.foodCategory);
        clfood.setOnClickListener(view -> filterItems("Food"));

        // Add click listener to the "Drink" category
        cldrink = findViewById(R.id.drinkCategory);
        cldrink.setOnClickListener(view -> filterItems("Drink"));

        // Add click listener to the "More" category
        clmore = findViewById(R.id.moreCategory);
        clmore.setOnClickListener(view -> filterItems("More"));
    }

    private void filterItems(String category) {
        ArrayList<FoodDomain> filteredItems = new ArrayList<>();

        if (category.equalsIgnoreCase("More")) {
            // If the selected category is "More," show all items without filtering
            filteredItems.addAll(originalFoodList);
        } else {
            // Loop through all items and add the ones with the selected category to the filteredItems list
            for (FoodDomain item : originalFoodList) {
                if (item.getKategori().equalsIgnoreCase(category)) {
                    filteredItems.add(item);
                }
            }
        }

        items.clear();
        items.addAll(filteredItems);
        adapterFoodList.notifyDataSetChanged();
    }

    private void readData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                originalFoodList.clear();
                items.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    FoodDomain foodDomain = dataSnapshot.getValue(FoodDomain.class);
                    originalFoodList.add(foodDomain);
                    items.add(foodDomain);
                }
                adapterFoodList.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error if needed
            }
        });
    }

    private void filterFoodList(String query) {
        List<FoodDomain> filteredList = new ArrayList<>();

        for (FoodDomain food : originalFoodList) {
            if (food.getNama().toLowerCase().contains(query)) {
                filteredList.add(food);
            }
        }

        items.clear();
        items.addAll(filteredList);
        adapterFoodList.notifyDataSetChanged();
    }

    private void initializeFoodList() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                originalFoodList.clear(); // Clear the list before adding new items

                for (DataSnapshot foodSnapshot : dataSnapshot.getChildren()) {
                    // Assuming you have a FoodDomain constructor that takes the necessary parameters
                    FoodDomain food = foodSnapshot.getValue(FoodDomain.class);
                    originalFoodList.add(food);
                }

                items.addAll(originalFoodList);
                adapterFoodList.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error if any
                Toast.makeText(MainActivity.this, "Error retrieving data from Firebase", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
