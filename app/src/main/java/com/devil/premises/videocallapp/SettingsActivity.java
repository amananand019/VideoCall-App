package com.devil.premises.videocallapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class SettingsActivity extends AppCompatActivity {

    private Button saveBtn;
    private EditText userNameEt, userBioEt;
    private ImageView profileImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        saveBtn = findViewById(R.id.btn_save_settings);
        userNameEt = findViewById(R.id.username_settings);
        userBioEt = findViewById(R.id.bio_settings);
        profileImageView = findViewById(R.id.settings_profile_image);
    }
}