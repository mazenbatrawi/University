package com.example.finalproject;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Instructor_Activity extends AppCompatActivity {

    String user_email="";
    DataBaseHelper dataBaseHelper = new DataBaseHelper(
            Instructor_Activity.this,"TRAINING_CENTER",null,1);

    TextView nameTextView,emailTextView,addressTextView,degreeTextView,mobileTextView,specializationTextView,canteachTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor);
        emailTextView  = findViewById(R.id.emailTextView);
        addressTextView = findViewById(R.id.addressTextView);
        specializationTextView = findViewById(R.id.specializationTextView);
        mobileTextView = findViewById(R.id.mobileTextView);
        degreeTextView = findViewById(R.id.degreeTextView);
        nameTextView = findViewById(R.id.nameTextView);
        canteachTextView = findViewById(R.id.canteachTextView);
        ImageView profilePhoto = findViewById(R.id.uploaded_image);

        user_email = getIntent().getStringExtra("EMAIL");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation2);
        bottomNavigationView.setSelectedItemId(R.id.home_navbar);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home_navbar:
                    return true;
                case R.id.course_navbar:
                    Intent intent = new Intent(Instructor_Activity.this, InstructorCoursesActivity.class);
                    intent.putExtra("EMAIL", user_email);
                    startActivity(intent);
                    finish();
                    return true;
                case R.id.student_navbar:
                    Intent intent2 = new Intent(Instructor_Activity.this, InstructorStudentsActivity.class);
                    intent2.putExtra("EMAIL", user_email);
                    startActivity(intent2);
                    finish();
                    return true;
                case R.id.schedule_navbar:
                    Intent intent3 = new Intent(Instructor_Activity.this, InstructorScheduleActivity.class);
                    intent3.putExtra("EMAIL", user_email);
                    startActivity(intent3);
                    finish();
                    return true;
            }
            return false;
        });

        Cursor user_data = dataBaseHelper.getInstructorData(user_email);

        String user_first_name = "FIRST_NAME";
        String user_last_name = "LAST_NAME";
        String user_mobile_no ="MOBILE_NUMBER";
        String user_address = "ADDRESS";
        String user_specialization = "SPECIALIZATION";
        String user_degree = "DEGREE";
        String user_canTeach = "canTeach";
        byte[]user_personal_photo = null;

        while (user_data.moveToNext()){

            if(user_data.getString(0).equals(user_email) ){
                user_personal_photo = user_data.getBlob(9);
                user_first_name = user_data.getString(2);
                user_last_name = user_data.getString(3);
                user_mobile_no = user_data.getString(4);
                user_address = user_data.getString(5);
                user_specialization = user_data.getString(6);
                user_degree = user_data.getString(7);
                user_canTeach = user_data.getString(8);
            }
        }



        Bitmap bitmap = BitmapFactory.decodeByteArray(user_personal_photo, 0, user_personal_photo.length);
        profilePhoto.setImageBitmap(bitmap);

        emailTextView.setText(user_email);
        emailTextView.setPaintFlags(emailTextView.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

        addressTextView.setText(user_address);
        addressTextView.setPaintFlags(addressTextView.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

        specializationTextView.setText(user_specialization);
        specializationTextView.setPaintFlags(specializationTextView.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

        mobileTextView.setText(user_mobile_no);
        mobileTextView.setPaintFlags(mobileTextView.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

        degreeTextView.setText(user_degree);
        degreeTextView.setPaintFlags(degreeTextView.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

        String name = user_first_name + " " + user_last_name;
        nameTextView.setText(name);
        nameTextView.setPaintFlags(nameTextView.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);



        String canTeachString = user_canTeach.substring(2, user_canTeach.length());
        canteachTextView.setText(canTeachString);
        canteachTextView.setPaintFlags(canteachTextView.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

        Button bt = (Button) findViewById(R.id.button3);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Instructor_Activity.this, InstructorEditProfile.class);
                intent.putExtra("EMAIL", user_email);
                startActivity(intent);
                finish();
            }
        });


        Button logout_button = findViewById(R.id.logout);
        Drawable icon2 = getResources().getDrawable(R.drawable.ic_baseline_exit_to_app_24);
        logout_button.setCompoundDrawablesRelativeWithIntrinsicBounds(icon2, null, null, null);
        logout_button.setPaddingRelative(28,0,0,0);


        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Instructor_Activity.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
    }

}





