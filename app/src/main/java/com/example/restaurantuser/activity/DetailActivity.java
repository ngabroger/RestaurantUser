package com.example.restaurantuser.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.restaurantuser.Domain.FoodDomain;
import com.example.restaurantuser.Helper.ManagementCart;
import com.example.restaurantuser.R;

public class DetailActivity extends AppCompatActivity {
    private Button AddToCartBtn;
    private TextView plustBtn,minusBtn,titleTxt,feeTxt,DescriptionTxt,numberOrderTxt,startTxt,categoryTxt,timeTxt;
    private ImageView picFood;
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
    }

    private void getBundle() {
        object= (FoodDomain) getIntent().getSerializableExtra("object");
        int drawableResourceId= this.getResources().getIdentifier(object.getPicUrl(),"drawable",this.getPackageName());
        Glide.with(this)
                .load(drawableResourceId)
                .into(picFood);


        titleTxt.setText((object.getTitle()));
        feeTxt.setText(("Rp"+object.getPrice()));
        DescriptionTxt.setText(object.getDescription());
        numberOrderTxt.setText(""+numberOrder);
        categoryTxt.setText(object.getCategory());
        startTxt.setText(object.getScore()+"");
        timeTxt.setText(object.getTimes()+" min");
        AddToCartBtn.setText("Add To Cart " + "Rp." +Math.round(numberOrder * object.getPrice()));

        plustBtn.setOnClickListener(v -> {
            numberOrder = numberOrder + 1 ;
            numberOrderTxt.setText(""+numberOrder);
            AddToCartBtn.setText("Add To Cart " + "Rp." +Math.round(numberOrder * object.getPrice()));
        });
        minusBtn.setOnClickListener(v -> {
            if (numberOrder > 1) {
                numberOrder = numberOrder - 1;
                numberOrderTxt.setText(""+numberOrder);
                AddToCartBtn.setText("Add To Cart " + "Rp." + Math.round(numberOrder * object.getPrice()));
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
    }
}