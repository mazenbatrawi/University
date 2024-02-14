package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        Button back_button = findViewById(R.id.back_button);

        Drawable icon = getResources().getDrawable(R.drawable.ic_baseline_arrow_back_24);
        back_button.setCompoundDrawablesRelativeWithIntrinsicBounds(icon, null, null, null);
        back_button.setPaddingRelative(20,0,0,0);

        Button admin_button = (Button) findViewById(R.id.login_button);
        Button trainee_button = (Button) findViewById(R.id.trainee);
        Button instructor_button = (Button) findViewById(R.id.instructor);
        trainee_button.setTextColor(getApplication().getResources().getColor(R.color.mainColor));;
        trainee_button.setBackgroundColor(getApplication().getResources().getColor(R.color.whiteColor));
        instructor_button.setTextColor(getApplication().getResources().getColor(R.color.mainColor));;
        instructor_button.setBackgroundColor(getApplication().getResources().getColor(R.color.whiteColor));
        admin_button.setTextColor(getApplication().getResources().getColor(R.color.whiteColor));;
        admin_button.setBackgroundColor(getApplication().getResources().getColor(R.color.mainColor));
        admin_button.setBackground(Drawable.createFromPath("@drawable/round_buttons_dashboard2"));


        admin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trainee_button.setTextColor(getApplication().getResources().getColor(R.color.mainColor));;
                trainee_button.setBackgroundColor(getApplication().getResources().getColor(R.color.whiteColor));
                instructor_button.setTextColor(getApplication().getResources().getColor(R.color.mainColor));;
                instructor_button.setBackgroundColor(getApplication().getResources().getColor(R.color.whiteColor));
                admin_button.setTextColor(getApplication().getResources().getColor(R.color.whiteColor));;
                admin_button.setBackgroundColor(getApplication().getResources().getColor(R.color.mainColor));
                admin_button.setBackground(Drawable.createFromPath("@drawable/round_buttons_dashboard2"));
                replaceFragment(new admin_sign_up_fragment());
            }
        });

        trainee_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                admin_button.setTextColor(getApplication().getResources().getColor(R.color.mainColor));;
                admin_button.setBackgroundColor(getApplication().getResources().getColor(R.color.whiteColor));
                instructor_button.setTextColor(getApplication().getResources().getColor(R.color.mainColor));;
                instructor_button.setBackgroundColor(getApplication().getResources().getColor(R.color.whiteColor));
                trainee_button.setTextColor(getApplication().getResources().getColor(R.color.whiteColor));;
                trainee_button.setBackgroundColor(getApplication().getResources().getColor(R.color.mainColor));
                trainee_button.setBackground(Drawable.createFromPath("@drawable/round_buttons_dashboard2"));
                replaceFragment(new trainee_sign_up_fragment());
            }
        });
        instructor_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                admin_button.setTextColor(getApplication().getResources().getColor(R.color.mainColor));;
                admin_button.setBackgroundColor(getApplication().getResources().getColor(R.color.whiteColor));
                trainee_button.setTextColor(getApplication().getResources().getColor(R.color.mainColor));;
                trainee_button.setBackgroundColor(getApplication().getResources().getColor(R.color.whiteColor));
                instructor_button.setTextColor(getApplication().getResources().getColor(R.color.whiteColor));;
                instructor_button.setBackgroundColor(getApplication().getResources().getColor(R.color.mainColor));
                instructor_button.setBackground(Drawable.createFromPath("@drawable/round_buttons_dashboard2"));
                replaceFragment(new instructor_sign_up_fragment());
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView3,fragment);
        fragmentTransaction.commit();
    }
}