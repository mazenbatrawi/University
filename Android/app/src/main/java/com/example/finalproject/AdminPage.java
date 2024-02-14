package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class AdminPage extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.admin_page);

            Bundle bundle = getIntent().getExtras();
            String email = bundle.getString("EMAIL", "ERROR");

            Button home = (Button) findViewById(R.id.home);
            Button profile = (Button) findViewById(R.id.profile);
            Button logout = (Button) findViewById(R.id.logout);

            home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Fragment admin_home_page = new AdminHomePage();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, admin_home_page).commit();
                }
            });

            profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Fragment admin_profile_page = new AdminProfilePage();
                    Bundle bundle = new Bundle();
                    bundle.putString("EMAIL", email);
                    admin_profile_page.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, admin_profile_page).commit();
                }
            });

            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(AdminPage.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
}
