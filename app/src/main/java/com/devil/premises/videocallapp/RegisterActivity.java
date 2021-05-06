package com.devil.premises.videocallapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.hbb20.CountryCodePicker;

public class RegisterActivity extends AppCompatActivity {

    //vars
    private CountryCodePicker ccp;
    private EditText phoneText;
    private EditText codeText;
    private Button continueAndNextBtn;
    private String checker = "", phoneNumber = "";
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        phoneText = findViewById(R.id.phoneTxt);
        codeText = findViewById(R.id.codeText);
        continueAndNextBtn = findViewById(R.id.continueNextButton);
        relativeLayout = findViewById(R.id.phoneAuth);
        ccp = findViewById(R.id.ccp);
    }
}