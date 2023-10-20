package com.example.ecommerceclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerceclone.Model.AddedProducts;
import com.example.ecommerceclone.Model.Users;
import com.example.ecommerceclone.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import io.paperdb.Paper;

public class Loginpage extends AppCompatActivity {

    private EditText phoneET;
    private Button loginButton;
    private FirebaseAuth mAuth;
    private ProgressBar progressCircle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpage);

        init();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkDataFields();
            }
        });

    }

    private void init() {

        phoneET = findViewById(R.id.login_phone);
        loginButton = findViewById(R.id.login_button);
        progressCircle = findViewById(R.id.login_progress);

        mAuth = FirebaseAuth.getInstance();

    }

    private void checkDataFields() {
        String phone = phoneET.getText().toString();

        if (phone.equals("")) {
            Toast.makeText(this, "Credentials can't be empty", Toast.LENGTH_SHORT).show();
        }else {

            Intent intent=new Intent(Loginpage.this,VerifyOtp.class);
            intent.putExtra("from","login");
            intent.putExtra("phone",phone);
            startActivity(intent);

        }
    }

    private void loginAuth(String username, String password) {

        progressCircle.setVisibility(View.VISIBLE);
        loginButton.setEnabled(false);
        loginButton.setAlpha(0.5f);


        mAuth.signInWithEmailAndPassword(username, password).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Loginpage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                progressCircle.setVisibility(View.GONE);
                loginButton.setEnabled(true);
                loginButton.setAlpha(1f);
            }
        }).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    if(mAuth.getCurrentUser().isEmailVerified()) {
                        progressCircle.setVisibility(View.GONE);

                        Toast.makeText(Loginpage.this, "Logged in successfully", Toast.LENGTH_SHORT).show();


                        Intent intent=new Intent(Loginpage.this,Homepage.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);


                    }else {
                        Toast.makeText(Loginpage.this, "Verify Email", Toast.LENGTH_SHORT).show();
                        progressCircle.setVisibility(View.GONE);
                        loginButton.setEnabled(true);
                        loginButton.setAlpha(1f);
                    }


                } else {
                    progressCircle.setVisibility(View.GONE);
                    loginButton.setEnabled(true);
                    loginButton.setAlpha(1f);
                }

            }
        });


    }

}


