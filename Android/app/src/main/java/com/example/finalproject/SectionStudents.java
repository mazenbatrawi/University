package com.example.finalproject;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SectionStudents extends AppCompatActivity implements SelectListenerForStudents {
    String user_email="";
    String sectionId="";
    DataBaseHelper dataBaseHelper = new DataBaseHelper(SectionStudents.this, "TRAINING_CENTER", null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section_students);

        Button logout_button = findViewById(R.id.back_to_instructorStudent);
        Drawable icon2 = getResources().getDrawable(R.drawable.ic_baseline_arrow_back_24_2);
        logout_button.setCompoundDrawablesRelativeWithIntrinsicBounds(icon2, null, null, null);
        logout_button.setPaddingRelative(28,0,0,0);
        user_email = getIntent().getStringExtra("EMAIL");
        sectionId = getIntent().getStringExtra("SectionId");

        System.out.println(sectionId);




        RecyclerView recyclerView = findViewById(R.id.recyclerview2);

        List<TraineeInSection> items = new ArrayList<TraineeInSection>();

        Cursor cursor = dataBaseHelper.getTraineesInSection2(sectionId);

        System.out.println(cursor);
        if (cursor.moveToFirst()) {
            do {

                String email = cursor.getString(2);
                Cursor cursor2 = dataBaseHelper.getTraineeAll(email);

                cursor2.moveToFirst();
                String name = cursor2.getString(2) + " " +  cursor2.getString(3);
                String mobileNumber = cursor2.getString(4);
                String address = cursor2.getString(5);
                byte [] image = cursor2.getBlob(6);

                items.add( new TraineeInSection(email,name,mobileNumber,address,image));

            } while (cursor.moveToNext());

        }


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyStudentsAdapter(getApplicationContext(),items,this));
















        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SectionStudents.this, InstructorStudentsActivity.class);
                intent.putExtra("EMAIL", user_email);

                startActivity(intent);
                finish();
            }
        });


    }

    @Override
    public void onItemClicked(TraineeInSection trainee) {
        Toast.makeText(this, trainee.getEmail(), Toast.LENGTH_LONG ).show();

    }
}