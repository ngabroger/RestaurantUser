package com.example.restaurantuser.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.example.restaurantuser.Domain.FoodDomain;
import com.example.restaurantuser.Helper.ManagementCart;
import com.example.restaurantuser.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DetailActivity extends AppCompatActivity {
    private Button AddToCartBtn;
    private TextView plustBtn,minusBtn,titleTxt,feeTxt,DescriptionTxt,numberOrderTxt,startTxt,categoryTxt,timeTxt;
    private ImageView picFood,backBtn;
    private FoodDomain object;
    private int numberOrder = 1;
    private ManagementCart managementCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
    managementCart = new ManagementCart(DetailActivity.this);
    initView();
    getBundle();
    setVariable();
    }

    private void setVariable() {
        backBtn.setOnClickListener(view -> finish());
    }

    private void getBundle() {
        object= (FoodDomain) getIntent().getSerializableExtra("object");

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference ImageRef = storageRef.child("images/"+object.getFoto());
        ImageRef.getDownloadUrl().addOnSuccessListener(url -> {
            Glide.with(this)
                    .load(url)
                    .centerCrop()
                    .into(picFood);
        }).addOnFailureListener(exception -> {
        });


        titleTxt.setText((object.getNama()));
        feeTxt.setText(("Rp"+object.getHarga()));
        DescriptionTxt.setText(object.getDeskripsi());
        numberOrderTxt.setText(""+numberOrder);
        categoryTxt.setText(object.getKategori());
        startTxt.setText(object.getRating()+"");
        timeTxt.setText(object.getWaktu()+" min");
        AddToCartBtn.setText("Add To Cart " + "Rp." + (int) Math.round(numberOrder * Double.parseDouble(object.getHarga())));

        plustBtn.setOnClickListener(v -> {
            numberOrder = numberOrder + 1 ;
            numberOrderTxt.setText(""+numberOrder);
            AddToCartBtn.setText("Add To Cart " + "Rp." + (int) Math.round(numberOrder * Double.parseDouble(object.getHarga())));
        });
        minusBtn.setOnClickListener(v -> {
            if (numberOrder > 1) {
                numberOrder = numberOrder - 1;
                numberOrderTxt.setText(""+numberOrder);
                AddToCartBtn.setText("Add To Cart " + "Rp." + (int) Math.round(numberOrder * Double.parseDouble(object.getHarga())));
            }
        });
        AddToCartBtn.setOnClickListener(v -> {
            object.setNumericCart(numberOrder);
            managementCart.insertFood(object);
        });
    }

    private void initView(){
       AddToCartBtn =findViewById(R.id.addToCartbtn);
       timeTxt =findViewById(R.id.timeTxt);
       feeTxt =findViewById(R.id.priceTxt);
       titleTxt =findViewById(R.id.itemTxt);
        DescriptionTxt= findViewById(R.id.descriptionTxt);
       numberOrderTxt =findViewById(R.id.numberItemTxt);
        plustBtn=findViewById(R.id.plusCardBtn);
       minusBtn =findViewById(R.id.minCardBtn);
        picFood=findViewById(R.id.picFood);
        startTxt= findViewById(R.id.ratingTxt);
        categoryTxt = findViewById(R.id.categoryTxt);
        timeTxt = findViewById(R.id.timeTxt);
        backBtn=findViewById(R.id.backBtnDetail);
    }
}