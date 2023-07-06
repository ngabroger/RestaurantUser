package com.example.restaurantuser.activity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.util.Linkify;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restaurantuser.Domain.UserDomain;
import com.example.restaurantuser.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class loginActivity extends AppCompatActivity {
    private ImageView backViewLogin;
    private EditText emailTxt,passwordTxt;
    private Button loginBtn;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private FirebaseDatabase firebaseDatabase;
    private TextView textView37;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        btnInteraction();
        forgotPassword();

    }

    private void forgotPassword() {
        TextView textView37 = findViewById(R.id.textView37);
        String forgotPasswordText = "Forgot Password? Reset your password here";
        SpannableString spannableString = new SpannableString(forgotPasswordText);
        Linkify.addLinks(spannableString, Linkify.WEB_URLS);

        textView37.setText(spannableString);
        textView37.setMovementMethod(LinkMovementMethod.getInstance());
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                String url = "https://diningrestaurant.appspot.com/forgotpassword.jsp";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        };

        spannableString.setSpan(clickableSpan, 0, forgotPasswordText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView37.setText(spannableString);
    }

    private void btnInteraction() {
        backViewLogin.setOnClickListener(view -> finish());

        loginBtn.setOnClickListener(view -> {
            if(emailTxt.getText().length()>0 && passwordTxt.getText().length()>0){
                login(emailTxt.getText().toString(),passwordTxt.getText().toString() );
            }else{
                Toast.makeText(getApplicationContext(),"Silahkan isi semua data", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

    }

    private void login(String email, String password) {
        progressDialog.show();
        DatabaseReference userRef = firebaseDatabase.getReference().child("users");
        userRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        UserDomain user = userSnapshot.getValue(UserDomain.class);

                        if (user != null && user.getCategory() != null && user.getCategory().equals("user")) {
                            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful() && task.getResult() != null) {
                                        FirebaseUser firebaseUser = task.getResult().getUser();

                                        if (firebaseUser != null) {
                                            reload();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Login Gagal", Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                        }
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Login Gagal", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(getApplicationContext(), "Kamu bukan kaum kami", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Akun tidak ditemukan", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Login Gagal", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }


    private void initView() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        backViewLogin = findViewById(R.id.backViewLogin);
        emailTxt = findViewById(R.id.emailLoginTxt);
        passwordTxt = findViewById(R.id.passwordTxt);
        loginBtn = findViewById(R.id.loginBtn);
        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(loginActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("silahkan tunggu");
        progressDialog.setCancelable(false);
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null){
            reload();
        }
    }

    private void reload() {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }
}