package com.example.restaurantuser.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.DecimalFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.example.restaurantuser.Helper.ChangeNumberItemsListener;
import com.example.restaurantuser.Helper.ManagementCart;
import com.example.restaurantuser.R;
import com.example.restaurantuser.adapter.CartListAdapter;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class CartActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerViewList;
    private ManagementCart managementCart;
    private TextView totalFeeTxt,taxTxt,deliveryTxt,totalTxt,emptyTxt;
    private double tax;
    private ScrollView scrollView;
    private ImageView backBtn, picView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        managementCart = new ManagementCart(this);
        initView();
        initList();
        calculateCart();
        setVariable();
    }

    private void setVariable() {
        backBtn.setOnClickListener(view -> finish());
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

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference ImageRef = storageRef.child("images/"+managementCart);
        ImageRef.getDownloadUrl().addOnSuccessListener(url -> {
            Glide.with(this)
                    .load(url)
                    .transform(new GranularRoundedCorners(40, 40, 0, 0))
                    .into(picView);
        }).addOnFailureListener(exception -> {
        });

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
        deliveryTxt = findViewById(R.id.deliveryTxt);
        totalTxt =findViewById(R.id.totalTxt);
        recyclerViewList =findViewById(R.id.view3);
        scrollView=findViewById(R.id.scrollView3);
        backBtn=findViewById(R.id.backBtn);
        emptyTxt=findViewById(R.id.emptyTxt);
        picView=findViewById(R.id.pic);
    }
}