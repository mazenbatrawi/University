package com.example.finalproject;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class InstructorStudentsActivity extends AppCompatActivity implements SelectListener {
    String user_email="";
    boolean flag = false;
    int cnt=0;
    DataBaseHelper dataBaseHelper = new DataBaseHelper(InstructorStudentsActivity.this, "TRAINING_CENTER", null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_students);
        user_email = getIntent().getStringExtra("EMAIL");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation2);
        bottomNavigationView.setSelectedItemId(R.id.student_navbar);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home_navbar:
                    Intent intent = new Intent(InstructorStudentsActivity.this, Instructor_Activity.class);
                    intent.putExtra("EMAIL", user_email);

                    startActivity(intent);
                    finish();
                    System.out.println("1");
                    return true;
                case R.id.course_navbar:
                    Intent intent3 = new Intent(InstructorStudentsActivity.this, InstructorCoursesActivity.class);
                    intent3.putExtra("EMAIL", user_email);

                    startActivity(intent3);
                    finish();
                    System.out.println("2");
                    return true;
                case R.id.student_navbar:

                    return true;
                case R.id.schedule_navbar:
                    Intent intent2 = new Intent(InstructorStudentsActivity.this, InstructorScheduleActivity.class);
                    intent2.putExtra("EMAIL", user_email);

                    startActivity(intent2);
                    finish();
                    System.out.println("3");
                    System.out.println("4");
                    return true;
            }
            return false;
        });



        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        List<Section> items = new ArrayList<Section>();

//        items.add( new Section(20,"laith@gmail.com",11,45,"8:30","9:30","M,W","Masri 332","22/7","23/7"));
//        items.add( new Section(21,"laith@gmail.com",11,45,"8:30","9:30","M,W","Masri 332","22/7","23/7"));
//        items.add( new Section(22,"laith@gmail.com",11,45,"8:30","9:30","M,W","Masri 332","22/7","23/7"));
//        items.add( new Section(23,"laith@gmail.com",11,45,"8:30","9:30","M,W","Masri 332","22/7","23/7"));
//        items.add( new Section(24,"laith@gmail.com",11,45,"8:30","9:30","M,W","Masri 332","22/7","23/7"));
//        items.add( new Section(25,"laith@gmail.com",11,45,"8:30","9:30","M,W","Masri 332","22/7","23/7"));

        System.out.println("wds" +user_email);


        Cursor cursor = dataBaseHelper.getAllSectionsBasedOnInstructor(user_email);
        System.out.println(cursor);
        if (cursor.moveToFirst()) {
            do {
                int sectionID = cursor.getInt(0);
                String instructorEmail = cursor.getString(1);
                int courseID = cursor.getInt(2);
                int maxTrainees = cursor.getInt(3);
                String startTime = cursor.getString(4);
                String endTime = cursor.getString(5);
                String days = cursor.getString(6);
                String room = cursor.getString(7);
                String startDate = cursor.getString(8);
                String endDate = cursor.getString(9);


                items.add( new Section(sectionID,instructorEmail,courseID,maxTrainees,startTime,endTime,days,room,startDate,endDate));

            } while (cursor.moveToNext());

        }


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapter(getApplicationContext(),items,this));




    }

    @Override
    public void onItemClicked(Section section) {
        Toast.makeText(this, section.getStartDate(), Toast.LENGTH_LONG ).show();
        Intent intent = new Intent(InstructorStudentsActivity.this, SectionStudents.class);
        intent.putExtra("EMAIL", user_email);
        intent.putExtra("SectionId", String.valueOf(section.getSectionID()));
        startActivity(intent);
        finish();

    }
}

