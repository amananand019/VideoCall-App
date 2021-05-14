package com.devil.premises.videocallapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;


public class SettingsActivity extends AppCompatActivity {

    //widgets
    private Button saveBtn;
    private EditText userNameEt, userBioEt;
    private ImageView profileImageView;
    private ProgressDialog progressDialog;

    //vars
    private static final int galleryCode = 1;
    private Uri imageURI;
    private StorageReference userProfileImgRef;
    private String downloadURL;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //widgets
        saveBtn = findViewById(R.id.btn_save_settings);
        userNameEt = findViewById(R.id.username_settings);
        userBioEt = findViewById(R.id.bio_settings);
        profileImageView = findViewById(R.id.settings_profile_image);
        progressDialog = new ProgressDialog(this);

        //firebase
        userProfileImgRef = FirebaseStorage.getInstance().getReference().child("Profile Images");
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");

        profileImageView.setOnClickListener(view -> {
            Intent galleryIntent = new Intent();
            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
            galleryIntent.setType("image/*");
            startActivityForResult(galleryIntent,galleryCode);
        });
        
        saveBtn.setOnClickListener(view -> saveUserData());

        retrieveUserInfo();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == galleryCode && resultCode == RESULT_OK && data != null){
            imageURI = data.getData();
            profileImageView.setImageURI(imageURI);
        }
    }


    private void saveUserData() {
        final String getUserName = userNameEt.getText().toString();
        final String getUserStatus = userBioEt.getText().toString();

        if(imageURI == null){
            saveInfoWithoutImg();
        }else if(getUserName.equals("")){
            Toast.makeText(this, "UserName is mandatory", Toast.LENGTH_SHORT).show();
        }else if(getUserStatus.equals("")){
            Toast.makeText(this, "User Bio is mandatory", Toast.LENGTH_SHORT).show();
        }else{
            //show progress dialog
            progressDialog.setTitle("Profile Updating");
            progressDialog.setMessage("Your profile is being updated.");
            progressDialog.show();

            final StorageReference filePath = userProfileImgRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            final UploadTask uploadTask = filePath.putFile(imageURI);

            uploadTask.continueWithTask(task -> {
                if(!task.isSuccessful()){
                    throw task.getException();
                }

                downloadURL = filePath.getDownloadUrl().toString();
                return filePath.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    downloadURL = task.getResult().toString();

                    HashMap<String, Object> profileMap = new HashMap<>();
                    profileMap.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    profileMap.put("name", getUserName);
                    profileMap.put("status", getUserStatus);
                    profileMap.put("image", downloadURL);

                    userRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .updateChildren(profileMap)
                            .addOnCompleteListener(task1 -> {
                                if(task1.isSuccessful()){
                                    Intent intent = new Intent(SettingsActivity.this, ContactsActivity.class);
                                    startActivity(intent);
                                    finish();
                                    progressDialog.dismiss();

                                    Toast.makeText(SettingsActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            });
        }
    }

    private void saveInfoWithoutImg() {
        final String getUserName = userNameEt.getText().toString();
        final String getUserStatus = userBioEt.getText().toString();

        if(getUserName.equals("")){
        Toast.makeText(this, "UserName is mandatory", Toast.LENGTH_SHORT).show();
        }else if(getUserStatus.equals("")){
            Toast.makeText(this, "User Bio is mandatory", Toast.LENGTH_SHORT).show();
        }else {
            //show progress dialog
            progressDialog.setTitle("Profile Updating");
            progressDialog.setMessage("Your profile is being updated.");
            progressDialog.show();

            HashMap<String, Object> profileMap = new HashMap<>();
            profileMap.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
            profileMap.put("name", getUserName);
            profileMap.put("status", getUserStatus);

            userRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .updateChildren(profileMap)
                    .addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            Intent intent = new Intent(SettingsActivity.this, ContactsActivity.class);
                            startActivity(intent);
                            finish();
                            progressDialog.dismiss();

                            Toast.makeText(SettingsActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void retrieveUserInfo(){
        userRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            String imageDB = snapshot.child("image").getValue().toString();
                            String nameDB = snapshot.child("name").getValue().toString();
                            String statusDB = snapshot.child("status").getValue().toString();

                            userNameEt.setText(nameDB);
                            userBioEt.setText(statusDB);
                            Glide.with(SettingsActivity.this)
                                    .load(imageDB)
                                    .centerCrop()
                                    .placeholder(R.drawable.ic_test_account)
                                    .into(profileImageView);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}