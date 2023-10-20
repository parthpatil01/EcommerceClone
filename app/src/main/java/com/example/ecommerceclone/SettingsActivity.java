package com.example.ecommerceclone;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.ecommerceclone.Fragments.ChangeProfileFragment;
import com.example.ecommerceclone.Fragments.DefaultFragment;
import com.example.ecommerceclone.Fragments.UserInfoFragment;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    TextView settingsClose, update;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = findViewById(R.id.settings_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_settings_layout, new DefaultFragment()).commit();


    }

    @Override
    protected void onStart() {
        super.onStart();
        settingsClose = findViewById(R.id.settings_close_button);
        settingsClose.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.settings_close_button:
                finish();
                break;

            case R.id.settings_Update_button:
                break;


            case R.id.change_profile_picture:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_settings_layout, new ChangeProfileFragment()).commit();
                break;

            case R.id.userinfo:
                break;


        }


    }


}
