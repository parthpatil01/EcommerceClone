package com.example.ecommerceclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {


    private String emailRegex = "^[a-zA-Z0-9+_.-]{4,}@[a-zA-Z0-9-]+\\.[a-zA-Z0-9.]{2,5}$";

    private String phoneRegex = "^[0-9]{10}$";


    private ProgressBar progressCircle;
    private TextView usernameError, emailError, phoneError;
    private EditText usernameBox, emailBox, phoneBox;

    private ProgressDialog loadingbar;

    private Button signUpButton;
    private HashMap<String, Object> userInfo;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_register_activity);

        init();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        progressCircle.setVisibility(View.GONE);
        signUpButton.setEnabled(true);
        signUpButton.setAlpha(1f);
    }

    void init() {

        progressCircle = findViewById(R.id.register_progress);
        signUpButton = findViewById(R.id.reg_signup_Button);
        usernameBox = findViewById(R.id.reg_username);
        usernameError = findViewById(R.id.username_error);
        emailBox = findViewById(R.id.email);
        emailError = findViewById(R.id.email_error);
        phoneBox = findViewById(R.id.phone);
        phoneError = findViewById(R.id.phone_error);


        loadingbar = new ProgressDialog(this);

    }

    private void validate() {

        String username = usernameBox.getText().toString();
        String email = emailBox.getText().toString();
        String phone = phoneBox.getText().toString();

        userInfo = new HashMap<>();

        if (!(username.equals("") && email.equals("") && phone.equals(""))) {


            if (!username.equals("")) {
                if (username.length() >= 5) {
                    usernameError.setVisibility(View.GONE);
                    userInfo.put("username", username);

                } else {

                    usernameError.setVisibility(View.VISIBLE);

                }
            }

            if (!email.equals("")) {
                if (email.matches(emailRegex)) {

                    emailError.setVisibility(View.GONE);

                    userInfo.put("email", email);

                } else {
                    Toast.makeText(getApplicationContext(), "invalid email", Toast.LENGTH_SHORT).show();

                    emailError.setVisibility(View.VISIBLE);

                }
            }

            if (!phone.equals("")) {
                if (phone.matches(phoneRegex)) {


                    Pattern pattern = Pattern.compile("[0-9]+");
                    Matcher m = pattern.matcher(phone);

                    if (m.find()) {
                        phoneError.setVisibility(View.GONE);
                        userInfo.put("phone", phone);

                    } else {
                        Toast.makeText(getApplicationContext(), "phone find else", Toast.LENGTH_SHORT).show();
                        phoneError.setVisibility(View.VISIBLE);
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "phone else", Toast.LENGTH_SHORT).show();
                    phoneError.setVisibility(View.VISIBLE);

                }
            }


        }

        if (userInfo.get("username") != null && userInfo.get("email") != null && userInfo.get("phone") != null) {

            creatAccount();

        }


    }

    private void creatAccount() {

        Toast.makeText(this, "create account", Toast.LENGTH_SHORT).show();
        signUpButton.setEnabled(false);
        signUpButton.setAlpha(0.4f);

        progressCircle.setVisibility(View.VISIBLE);

        Intent intent = new Intent(getApplicationContext(), VerifyOtp.class);
        intent.putExtra("from","signup");
        intent.putExtra("username", userInfo.get("username").toString());
        intent.putExtra("phone", userInfo.get("phone").toString());
        intent.putExtra("email", userInfo.get("email").toString());


        startActivity(intent);

//        AsyncTaskParseJson obj = new AsyncTaskParseJson(userInfo.get("email").toString());
//        obj.execute();


    }

    public class AsyncTaskParseJson extends AsyncTask<String, String, String> {

        String key = "92e00b1ec0674f84a558eeadc1cd7ea3";
        String ip = "";
        String url = "";

        AsyncTaskParseJson(String email) {

            url = "https://api.zerobounce.in/v2/validate?api_key=" + key + "&email=" + email + "&ip_address=" + ip;
        }
//        email@example.com

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... arg0) {

            JsonParser jParser = new JsonParser();
            JSONObject json = jParser.getJSONFromUrl(url);
            Iterator keys = json.keys();
            String status = "invalid";

            while (keys.hasNext()) {
                String key = (String) keys.next();
                try {
                    System.out.println("ZeroBounce: " + key + " = " + json.get(key).toString());
                } catch (Exception e) {
                }
            }

            return status;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg) {


            if (strFromDoInBg.equals("valid")) {


                Intent intent = new Intent(getApplicationContext(), VerifyOtp.class);
                intent.putExtra("username", userInfo.get("username").toString());
                intent.putExtra("phone", userInfo.get("phone").toString());
                intent.putExtra("email", userInfo.get("email").toString());


                startActivity(intent);

            } else {
                emailError.setVisibility(View.VISIBLE);
                progressCircle.setVisibility(View.GONE);
                signUpButton.setEnabled(true);
                signUpButton.setAlpha(1f);
            }

            System.out.println(strFromDoInBg);
        }
    }


}


