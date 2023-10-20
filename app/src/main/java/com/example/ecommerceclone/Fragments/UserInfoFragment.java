package com.example.ecommerceclone.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ecommerceclone.Model.Users;
import com.example.ecommerceclone.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserInfoFragment extends Fragment {

    private LinearLayout verifyPhone;
    private Button sendOTP, verify, proceed;
    private FirebaseAuth firebaseAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseFirestore firestore;
    private TextView username, email, address, phone;
    private Users users;

    public UserInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);
        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        firestore.collection("USERS").document(firebaseAuth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                users = documentSnapshot.toObject(Users.class);
                username.setText(users.getUsername());
                phone.setText(users.getPhone());
                email.setText(users.getEmail());
                address.setText(users.getAddress());


            }
        });
    }

    private void init(View view) {

        verifyPhone = view.findViewById(R.id.verify_phone);
        username = view.findViewById(R.id.userinfo_username);
        email = view.findViewById(R.id.userinfo_email);
        phone = view.findViewById(R.id.userinfo_phone);
        address = view.findViewById(R.id.userinfo_address);

    }
}