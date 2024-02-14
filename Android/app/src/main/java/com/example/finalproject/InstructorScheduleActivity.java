package com.example.finalproject;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class InstructorScheduleActivity extends AppCompatActivity implements SelectListener {
    String user_email="";
    Button DatePicker;
    DatePickerDialog datePickerDialog1;
    TextView DateTextView;
    String pickedDay="7", pickedMonth="JUL", pickedYear="2023",fullPickedDate="";
    DataBaseHelper dataBaseHelper = new DataBaseHelper(InstructorScheduleActivity.this, "TRAINING_CENTER", null, 1);
    List<Section> schedule = new ArrayList<Section>();


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_schedule);
        user_email = getIntent().getStringExtra("EMAIL");
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation2);
        bottomNavigationView.setSelectedItemId(R.id.schedule_navbar);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home_navbar:
                    Intent intent = new Intent(InstructorScheduleActivity.this, Instructor_Activity.class);
                    intent.putExtra("EMAIL", user_email);
                    startActivity(intent);
                    finish();
                    return true;
                case R.id.course_navbar:
                    Intent intent3 = new Intent(InstructorScheduleActivity.this, InstructorCoursesActivity.class);
                    intent3.putExtra("EMAIL", user_email);
                    startActivity(intent3);
                    finish();
                    return true;
                case R.id.student_navbar:
                    Intent intent2 = new Intent(InstructorScheduleActivity.this, InstructorStudentsActivity.class);
                    intent2.putExtra("EMAIL", user_email);

                    startActivity(intent2);
                    finish();
                    return true;
                case R.id.schedule_navbar:

                    return true;
            }
            return false;
        });

        DatePicker = findViewById(R.id.DatePicker);
        DateTextView = findViewById(R.id.textView8);

        initDatePicker();
        DatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog1.show();
                schedule.clear();
            }
        });
        DatePicker.setText(getTodaysDate());
        fullPickedDate =getTodaysDate();
        String[] dateParts = fullPickedDate.split(" ");
        pickedMonth  = dateParts[0];
        pickedDay  = dateParts[1];
        pickedYear  = dateParts[2];

        String PickedDate = DatePicker.getText().toString();
        if(PickedDate.isEmpty()){
            DateTextView.setTextColor(Color.RED);
        }
        else{
            DateTextView.setTextColor(Color.WHITE);
        }
    }


    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(android.widget.DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String date = makeDateString(day, month, year);
                DateTextView.setText(date);
                fullPickedDate = DateTextView.getText().toString();
                String[] dateParts = fullPickedDate.split(" ");
                pickedMonth  = dateParts[0];
                pickedDay  = dateParts[1];
                pickedYear  = dateParts[2];
                DatePicker.setText(fullPickedDate);
                RecycleStart(pickedDay,pickedMonth,pickedYear);

            }
        };
        RecyclerView recyclerView = findViewById(R.id.recyclerview3);

        Cursor cursor = dataBaseHelper.getAllSectionsBasedOnInstructor(user_email);

        //        JUL 7 2023
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


                String[] startDateParts = startDate.split(" ");
                String startMonth  = startDateParts[0];
                String startDay  = startDateParts[1];
                String startYear  = startDateParts[2];

                String[] endDateParts = endDate.split(" ");
                String endMonth  = endDateParts[0];
                String endDay  = endDateParts[1];
                String endYear  = endDateParts[2];

                LocalDate LocalstartDate = LocalDate.of(Integer.parseInt(startYear), getInvMonthFormat(startMonth) ,Integer.parseInt(startDay) );
                LocalDate LocalendDate = LocalDate.of(Integer.parseInt(endYear),getInvMonthFormat(endMonth) ,Integer.parseInt(endDay) );
                LocalDate LocalinputDate = LocalDate.of(Integer.parseInt(pickedYear), getInvMonthFormat(pickedMonth), Integer.parseInt(pickedDay));

                if ((LocalinputDate.isAfter(LocalstartDate) && LocalinputDate.isBefore(LocalendDate))   || (LocalinputDate.isEqual(LocalendDate))|| (LocalinputDate.isEqual(LocalstartDate))) {
                    System.out.println("Input date is within the specified range.");
                    schedule.add( new Section(sectionID,instructorEmail,courseID,maxTrainees,startTime,endTime,days,room,startDate,endDate));

                } else {
                    System.out.println("Input date is outside the specified range.");
                }



            } while (cursor.moveToNext());

        }


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapter(getApplicationContext(),schedule,this));

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog1 = new DatePickerDialog(this, style, dateSetListener, year, month, day);




        DatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                schedule.clear();
                initDatePicker();
                System.out.println("cmooooooooooooooooon");
                System.out.println("cmoooooooooooooooo1on");
                System.out.println("cmooooooooooooooo11oon");
                System.out.println("cmoooooooooooooooo121233on");
                System.out.println("cmoooooooooooooooo123on");
                System.out.println("cmoooooooooooooooo132on");
                System.out.println("cmooooooooooooooooon321");
                MyAdapter adapter = new MyAdapter(InstructorScheduleActivity.this, schedule, new SelectListener() {
                    @Override
                    public void onItemClicked(Section section) {

                    }
                });
                recyclerView.setAdapter(adapter);

// To reload the RecyclerView
                adapter.notifyDataSetChanged();

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void RecycleStart (String day, String month, String year){


        RecyclerView recyclerView = findViewById(R.id.recyclerview3);

        Cursor cursor = dataBaseHelper.getAllSectionsBasedOnInstructor(user_email);

        //        JUL 7 2023
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


                String[] startDateParts = startDate.split(" ");
                String startMonth  = startDateParts[0];
                String startDay  = startDateParts[1];
                String startYear  = startDateParts[2];

                String[] endDateParts = endDate.split(" ");
                String endMonth  = endDateParts[0];
                String endDay  = endDateParts[1];
                String endYear  = endDateParts[2];

                LocalDate LocalstartDate = LocalDate.of(Integer.parseInt(startYear), getInvMonthFormat(startMonth) ,Integer.parseInt(startDay) );
                LocalDate LocalendDate = LocalDate.of(Integer.parseInt(endYear),getInvMonthFormat(endMonth) ,Integer.parseInt(endDay) );
                LocalDate LocalinputDate = LocalDate.of(Integer.parseInt(pickedYear), getInvMonthFormat(pickedMonth), Integer.parseInt(pickedDay));

                if ((LocalinputDate.isAfter(LocalstartDate) && LocalinputDate.isBefore(LocalendDate))   || (LocalinputDate.isEqual(LocalendDate))|| (LocalinputDate.isEqual(LocalstartDate))) {
                    System.out.println("Input date is within the specified range.");
                    schedule.add( new Section(sectionID,instructorEmail,courseID,maxTrainees,startTime,endTime,days,room,startDate,endDate));

                } else {
                    System.out.println("Input date is outside the specified range.");
                }



            } while (cursor.moveToNext());

        }


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapter(getApplicationContext(),schedule,this));

        System.out.println(day + "asd " + month + " " + year);


    }


    public String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    public int getInvMonthFormat(String month){
        if(month.equals("JAN")) return 1;
        if(month.equals("FEB")) return 2;
        if(month.equals("MAR")) return 3;
        if(month.equals("APR")) return 4;
        if(month.equals("MAY")) return 5;
        if(month.equals("JUN")) return 6;
        if(month.equals("JUL")) return 7;
        if(month.equals("AUG")) return 8;
        if(month.equals("SEP")) return 9;
        if(month.equals("OCT")) return 10;
        if(month.equals("NOV")) return 11;
        if(month.equals("DEC")) return 12;
        return -1;
    }

    private String getMonthFormat(int month) {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";

        //default should never happen
        return "JAN";
    }


    @Override
    public void onItemClicked(Section section) {
        Toast.makeText(this, section.getDays(), Toast.LENGTH_SHORT ).show();
    }
}