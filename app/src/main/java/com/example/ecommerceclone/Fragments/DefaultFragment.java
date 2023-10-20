package com.example.ecommerceclone.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.ecommerceclone.Model.Users;
import com.example.ecommerceclone.Prevalent.Prevalent;
import com.example.ecommerceclone.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import io.paperdb.Paper;

public class DefaultFragment extends Fragment implements View.OnClickListener {

    private TextView changeProfile,userInfo,settingsClose,userTxtView;
    private ImageView profileImage;

    @Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable  ViewGroup container, @Nullable Bundle savedInstanceState) {

        settingsClose=(getActivity()).findViewById(R.id.settings_close_button);
        return inflater.inflate(R.layout.fragment_setings_default,container,false);

    }

    @Override
    public void onStart() {
        super.onStart();
        settingsClose=(getActivity()).findViewById(R.id.settings_close_button);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        changeProfile=view.findViewById(R.id.change_profile_picture);
        profileImage=view.findViewById(R.id.default_settings_image);
        userTxtView=view.findViewById(R.id.default_settings_username);

        profileSet(profileImage);

        changeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.fragment_settings_layout,new ChangeProfileFragment()).commit();

            }
        });

        userInfo=view.findViewById(R.id.userinfo);
        userInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.fragment_settings_layout, new UserInfoFragment()).commit();
            }
        });

        settingsClose.setOnClickListener(this);
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.settings_close_button:
                getActivity().onBackPressed();
                break;

            case R.id.settings_Update_button:
                Toast.makeText(requireContext(), "update pressed", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void profileSet(ImageView profileImage){


        FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String profileUrl = documentSnapshot.getString("profilepic");
                Picasso.get().load(profileUrl).placeholder(R.color.light_grey).fit().centerCrop().into(profileImage);
                String username= documentSnapshot.getString("username");
                userTxtView.setText(username);
            }
        });


        }





}
