package com.devil.premises.videocallapp;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ContactsActivity extends AppCompatActivity {

    BottomNavigationView navView;
    RecyclerView myContactList;
    ImageView findPeopleBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        findPeopleBtn = findViewById(R.id.find_people_btn);
        myContactList = findViewById(R.id.contact_list);
        myContactList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        findPeopleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent findPeopleIntent = new Intent(ContactsActivity.this, FindPeopleActivity.class);
                startActivity(findPeopleIntent);
            }
        });

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.navigation_home:
                    Intent mainIntent = new Intent(ContactsActivity.this, ContactsActivity.class);
                    startActivity(mainIntent);
                    finish();
                    break;

                case R.id.navigation_settings:
                    Intent settingsIntent = new Intent(ContactsActivity.this, SettingsActivity.class);
                    startActivity(settingsIntent);
                    break;

                case R.id.navigation_notifications:
                    Intent notificationIntent = new Intent(ContactsActivity.this, NotificationActivity.class);
                    startActivity(notificationIntent);
                    break;

                case R.id.navigation_logout:
                    FirebaseAuth.getInstance().signOut();
                    Intent logoutIntent = new Intent(ContactsActivity.this, RegisterActivity.class);
                    startActivity(logoutIntent);
                    finish();
                    break;
            }
            return true;
        }
    };

}