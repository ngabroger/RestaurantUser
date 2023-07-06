package com.example.restaurantuser.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.example.restaurantuser.Domain.FoodDomain;
import com.example.restaurantuser.Domain.OrderData;
import com.example.restaurantuser.Domain.UserDomain;
import com.example.restaurantuser.Helper.ChangeNumberItemsListener;
import com.example.restaurantuser.Helper.ManagementCart;
import com.example.restaurantuser.Helper.TinyDB;
import com.example.restaurantuser.R;
import com.example.restaurantuser.adapter.CartListAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.checkerframework.checker.nullness.qual.NonNull;

public class CartActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerViewList;
    private ManagementCart managementCart;
    private TextView totalFeeTxt,taxTxt,deliveryTxt,totalTxt,emptyTxt,alamatCartTxt;
    private double tax;
    private Button orderBtn;
    private ScrollView scrollView;
    private ImageView backBtn,alamatIntentBtn;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser == null) {
            finish();
        } else {
            managementCart = new ManagementCart(this);
            initView();
            initList();
            calculateCart();
            setVariable();
            setDisplayAlamat();
        }
    }

    private void setDisplayAlamat() {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(firebaseUser.getUid());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserDomain user = snapshot.getValue(UserDomain.class);
                if (user != null) {
                    String alamat = user.getAlamat();
                    // Update your UI or perform actions with the address data
                    alamatCartTxt.setText(alamat);

                }else{
                    alamatCartTxt.setText("alamatnya tidak ada tolong logout bang");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any errors that occur during data retrieval
            }
        });
    }

    private void setVariable() {
        backBtn.setOnClickListener(view -> finish());

        alamatIntentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AlamatActivity.class));
            }
        });

        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendOrderToFirebase();
            }
        });
    }

    private void initList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
            recyclerViewList.setLayoutManager(linearLayoutManager);
        adapter= new CartListAdapter(managementCart.getListCart(), this, new ChangeNumberItemsListener() {
            @Override
            public void changed() {

                calculateCart();

            }
        });
        recyclerViewList.setAdapter(adapter);



        if(managementCart.getListCart().isEmpty()){
            emptyTxt.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
        }else {
            emptyTxt.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
        }

    }

    private void calculateCart(){
        double  percentTxt = 0.11;
        double delivery= 35000;
        tax = Math.round((managementCart.getTotalFee()*percentTxt*100.0))/100;
        double total =Math.round((managementCart.getTotalFee()+tax+delivery)*100)/100;
        double itemTotal=Math.round(managementCart.getTotalFee()*100)/100;
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.##"); // Format untuk menghilangkan .0
        String formattedItemTotal = decimalFormat.format(itemTotal);
        String formattedTax = decimalFormat.format(tax);
        String formattedDelivery = decimalFormat.format(delivery);
        String formattedTotal = decimalFormat.format(total);

        totalFeeTxt.setText("Rp." + formattedItemTotal);
        taxTxt.setText("Rp." + formattedTax);
        deliveryTxt.setText("Rp." + formattedDelivery);
        totalTxt.setText("Rp." + formattedTotal);

        if (managementCart.getListCart().isEmpty()) {
            emptyTxt.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
        } else {
            emptyTxt.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
        }
    }
    private void initView() {
        totalFeeTxt=findViewById(R.id.totalFeeTxt);
        taxTxt=findViewById(R.id.taxTxt);
        alamatCartTxt = findViewById(R.id.alamatCartTxt);
        deliveryTxt = findViewById(R.id.deliveryTxt);
        totalTxt =findViewById(R.id.totalTxt);
        recyclerViewList =findViewById(R.id.view3);
        scrollView=findViewById(R.id.scrollView3);
        backBtn=findViewById(R.id.backBtn);
        emptyTxt=findViewById(R.id.emptyTxt);
        alamatIntentBtn = findViewById(R.id.alamatIntentBtn);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        orderBtn = findViewById(R.id.orderBtn);
    }

    private void sendOrderToFirebase() {
        // Mendapatkan referensi ke Firebase Realtime Database
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();

        // Mendapatkan ID pengguna saat ini
        String userId = firebaseAuth.getCurrentUser().getUid();

        // Mendapatkan username dari pengguna saat ini
        String username = firebaseUser.getDisplayName();

        // Mendapatkan alamat dari pengguna saat ini
        String address = alamatCartTxt.getText().toString();

        // Mendapatkan pesanan dari keranjang
        ArrayList<FoodDomain> cartItems = managementCart.getListCart();

        // Menghitung total harga untuk setiap item di keranjang dan total pemesanan
        double totalPemesanan = 0.0;
        for (FoodDomain item : cartItems) {
            int harga = Integer.parseInt(item.getHarga()); // Convert the harga from String to int
            double totalHarga = harga * item.getNumericCart();
            item.setTotalHarga(totalHarga);
            totalPemesanan += totalHarga;
        }

        // Mengubah tanggal dan waktu menjadi format yang diinginkan
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String orderDateTime = dateFormat.format(new Date());

// Membuat objek untuk menyimpan data pesanan
        OrderData orderData = new OrderData(username, address, cartItems);
        orderData.setTotalPemesanan(totalPemesanan);
        orderData.setOrderDateTime(orderDateTime);


        // Menambahkan pesanan ke Firebase Realtime Database
        databaseRef.child("onlineorder").child(userId).setValue(orderData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Pesanan berhasil ditambahkan ke Firebase Realtime Database
                        Toast.makeText(CartActivity.this, "Pesanan berhasil dikirim", Toast.LENGTH_SHORT).show();

                        // Menghapus semua item dari keranjang setelah pesanan terkirim
                        managementCart.clearCart();

                        // Kembali ke halaman sebelumnya atau halaman utama
                        finish(); // Atau ganti dengan intent ke halaman lain
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Gagal mengirim pesanan ke Firebase Realtime Database
                        Toast.makeText(CartActivity.this, "Gagal mengirim pesanan", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}