package com.example.ecommerceclone.Fragments;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ecommerceclone.Model.Users;
import com.example.ecommerceclone.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.HashMap;

public class ChangeProfileFragment extends Fragment implements View.OnClickListener {

    private TextView Male, Female;
    private EditText username_et, email_et, address_et;
    private TextView settingsClose, update, phoneNumber;
    private ImageView cameraOpen;
    public Boolean imageResultFlag, imageUplaodFlag;
    private StorageReference profilestorage;
    private Uri imageUri;
    public String dowloadImageUrl;
    private ProgressDialog loadingbar;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private String emailRegex = "^[a-zA-Z0-9+_.-]{4,}@[a-zA-Z0-9-]+\\.[a-zA-Z0-9.]{2,5}$";

    private int genderFlag = -1, counter = 0;


    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //settings close
        settingsClose = (getActivity()).findViewById(R.id.settings_close_button);
        update = getActivity().findViewById(R.id.settings_Update_button);
        update.setVisibility(View.VISIBLE);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_change_profile, container, false);
    }


    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageResultFlag = false;
        imageUplaodFlag = false;

        cameraOpen = view.findViewById(R.id.tap_to_change);
        cameraOpen.setOnClickListener(this);

        username_et = view.findViewById(R.id.change_username);
        email_et = view.findViewById(R.id.change_Email);
        address_et = view.findViewById(R.id.change_address);
        phoneNumber = view.findViewById(R.id.phone_display);

        Male = view.findViewById(R.id.Male);
        Male.setOnClickListener(this);

        Female = view.findViewById(R.id.Female);
        Female.setOnClickListener(this);


        profilestorage = FirebaseStorage.getInstance().getReference().child("profile pictures");
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        loadingbar = new ProgressDialog(getContext());
        fillingData(view);

    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();

        settingsClose = (getActivity()).findViewById(R.id.settings_close_button);
        settingsClose.setOnClickListener(this);
        update = getActivity().findViewById(R.id.settings_Update_button);
        update.setOnClickListener(this);
        update.setVisibility(View.VISIBLE);

    }

    private void fillingData(View view) {

        firestore.collection("USERS").document(firebaseAuth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {

                    Users users = documentSnapshot.toObject(Users.class);


                    if (users.getProfilepic() != null) {
                        Picasso.get().load(users.getProfilepic()).fit().centerCrop().into(cameraOpen);
                    }
                    if (users.getAddress() != null) {
                        address_et.setText(users.getAddress());
                    }
                    if (users.getEmail() != null) {
                        email_et.setText(users.getEmail());
                    }
                    if (users.getPhone() != null) {
                        phoneNumber.setText(users.getPhone());
                    }
                    username_et.setText(users.getUsername());

                    if (users.getGender() != null) {
                        if (users.getGender().equals("male")) {
                            view.findViewById(R.id.Male).performClick();
                        } else {
                            view.findViewById(R.id.Female).performClick();
                        }
                    }

                }
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.settings_close_button:
                update.setVisibility(View.GONE);
                getActivity().onBackPressed();
                break;


            case R.id.settings_Update_button:

                imageUpload();

                break;

            case R.id.tap_to_change:
                mGetContent.launch("image/*");
                break;


            case R.id.Male:
                Female.setTextColor(getResources().getColor(R.color.black));
                Female.setBackgroundColor(0);
                Female.setBackgroundResource(R.drawable.border_black);

                Male.setTextColor(getResources().getColor(R.color.white));
                Male.setBackgroundResource(0);
                Male.setBackgroundColor(getResources().getColor(R.color.shade_blue));
                genderFlag = 1;
                break;

            case R.id.Female:
                Female.setTextColor(getResources().getColor(R.color.white));
                Female.setBackgroundResource(0);
                Female.setBackgroundColor(getResources().getColor(R.color.shade_blue));

                Male.setTextColor(getResources().getColor(R.color.black));
                Male.setBackgroundColor(0);
                Male.setBackgroundResource(R.drawable.border_black);
                genderFlag = 0;
                break;


        }
    }

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri result) {
            if (result != null) {
                Picasso.get().load(result).fit().centerCrop().into(cameraOpen);
                imageUri = result;
                imageResultFlag = true;
            }

        }
    });

    private void imageUpload() {

        loadingbar.setTitle("Updating");
        loadingbar.setMessage("please wait..");
        loadingbar.setCanceledOnTouchOutside(false);
        loadingbar.show();

        if (imageResultFlag) {

            Calendar calendar = Calendar.getInstance();
            StorageReference filepath = profilestorage.child(calendar.getTimeInMillis() + ".jpg");
            UploadTask uploadTask = filepath.putFile(imageUri);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    loadingbar.dismiss();
                    Toast.makeText(requireContext(), "Failed to change image", Toast.LENGTH_SHORT).show();
                }
            }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<UploadTask.TaskSnapshot> task) {
                    if (uploadTask.isSuccessful()) {

                        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Toast.makeText(requireContext(), "image upload success", Toast.LENGTH_SHORT).show();
                                dowloadImageUrl = uri.toString();
                                gettingData();
                            }
                        });

                    } else {
                        loadingbar.dismiss();
                        Toast.makeText(getContext(), uploadTask.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            gettingData();
        }

    }


    private void gettingData() {
        String emailTemp, usernametemp, addressTemp, phoneTemp;

        usernametemp = username_et.getText().toString();
        emailTemp = email_et.getText().toString();
        addressTemp = address_et.getText().toString();


        HashMap<String, Object> userUpdate = new HashMap<>();

        if (!usernametemp.equals("") && usernametemp.length() >= 5) {
            userUpdate.put("username", usernametemp);
        }

        if (!emailTemp.equals("") && emailTemp.matches(emailRegex)) {
            userUpdate.put("email", emailTemp);
        }

        if (!addressTemp.isEmpty()) userUpdate.put("address", addressTemp);

        if (genderFlag != -1) {
            if (genderFlag == 0) {
                userUpdate.put("gender", "female");
            } else {
                userUpdate.put("gender", "male");
            }
        }
        if (dowloadImageUrl != null) userUpdate.put("profilepic", dowloadImageUrl);


        firestore.collection("USERS").document(firebaseAuth.getUid()).update(userUpdate).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                loadingbar.dismiss();
                Toast.makeText(requireContext(), "updated", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loadingbar.dismiss();
                Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
            }
        });


    }


}


