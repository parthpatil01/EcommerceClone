package com.example.ecommerceclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class VerifyOtp extends AppCompatActivity {

    private Button verifyOtp, resendOtp;
    private EditText OtpEt;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private String username, phone, email;
    private Boolean flag = false;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private String veriId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        init();

        Intent intent = getIntent();

        if (intent.getStringExtra("from").equals("signup")) {
            flag = true;
            username = intent.getStringExtra("username");
            email = intent.getStringExtra("email");
        }

        phone = intent.getStringExtra("phone");

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        sendOtp();

        verifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String code = OtpEt.getText().toString().trim();
                if (!code.isEmpty()) {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(veriId, code);

                    firebaseAuth.signInWithCredential(credential)
                            .addOnCompleteListener(VerifyOtp.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        if (flag) {
                                            HashMap<String, Object> userInfo = new HashMap<>();
                                            userInfo.put("username", username);
                                            userInfo.put("email", email);
                                            userInfo.put("phone", phone);

                                            firestore.collection("USERS").document(firebaseAuth.getUid()).set(userInfo).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {

                                                }
                                            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    goToHomepage();
                                                }
                                            });
                                        } else {
                                            goToHomepage();
                                        }

                                    } else {
                                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                            Toast.makeText(VerifyOtp.this, "Error Occurred", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                }
            }
        });

    }

    private void goToHomepage() {

        verifyOtp.setEnabled(false);
        Toast.makeText(VerifyOtp.this, "Success", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(VerifyOtp.this, Homepage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

    void init() {

        verifyOtp = findViewById(R.id.verify_otp);
        resendOtp = findViewById(R.id.resend_otp);
        OtpEt = findViewById(R.id.otp_et);

    }

    private void sendOtp() {

        if (!phone.isEmpty()) {

            mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                @Override
                public void onVerificationCompleted(PhoneAuthCredential credential) {

                }

                @Override
                public void onVerificationFailed(FirebaseException e) {
                    verifyOtp.setEnabled(false);
                    finish();
                    Toast.makeText(VerifyOtp.this, "Please check your phone number", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCodeSent(@NonNull String verificationId,
                                       @NonNull PhoneAuthProvider.ForceResendingToken token) {
                    veriId = verificationId;
                    resendOtp.setEnabled(false);
                    Toast.makeText(VerifyOtp.this, "OTP sent successfully", Toast.LENGTH_SHORT).show();

                }
            };

            PhoneAuthOptions options =
                    PhoneAuthOptions.newBuilder(firebaseAuth)
                            .setPhoneNumber("+91" + phone)       // Phone number to verify
                            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                            .setActivity(VerifyOtp.this)                 // Activity (for callback binding)
                            .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                            .build();

            PhoneAuthProvider.verifyPhoneNumber(options);
        } else finish();

    }


}