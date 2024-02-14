package com.example.finalproject;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Trainee_Schedule_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Trainee_Schedule_Fragment extends Fragment {

    private TableLayout tableLayout;
    private TextView txtCourse1;
    private TextView txtCourse2;
    private TextView txtCourse3;
    private TextView txtCourse4;
    private TextView txtCourse5;
    private TextView txtCourse6;
    private TextView txtCourse7;
    private TextView txtCourse8;
    private TextView txtCourse9;

    private TextView txtTime1;
    private TextView txtTime2;
    private TextView txtTime3;
    private TextView txtTime4;
    private TextView txtTime5;
    private TextView txtTime6;
    private TextView txtTime7;
    private TextView txtTime8;
    private TextView txtTime9;

    int sz = 0;

    private Button selectDate;
    private TextView lectureInfoTextView;
    private Calendar calendar;
    private LinearLayout addme;

    private ArrayList<Section> list = new ArrayList<Section>();

    ArrayList<TextView> txtTimeList = new ArrayList<TextView>();
    ArrayList<TextView> txtCourseList = new ArrayList<TextView>();
    ArrayList<String> daysList = new ArrayList<String>();
    private LinearLayout scheduleContainer;
    LinearLayout sectionLayout;



    String Email;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Trainee_Schedule_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Trainee_Schedule_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Trainee_Schedule_Fragment newInstance(String param1, String param2) {
        Trainee_Schedule_Fragment fragment = new Trainee_Schedule_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_trainee__schedule_, container, false);
        DataBaseHelper dataBaseHelper = new DataBaseHelper(getContext(), "TRAINING_CENTER", null, 1);

        scheduleContainer = rootView.findViewById(R.id.das);
        selectDate = rootView.findViewById(R.id.selectDateButton);
        lectureInfoTextView= rootView.findViewById(R.id.lectureInfoTextView);
        calendar = Calendar.getInstance();
        addme = rootView.findViewById(R.id.addme);
        sectionLayout = new LinearLayout(getContext());

        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.clear();
                showDatePicker();
            }
        });


        return rootView;
    }


    void showDatePicker() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        //sectionLayout.removeAllViews();
                            checkLectureOnDate(year, month, dayOfMonth);

                            displayMatchingSections();

                    }
                }, year, month, day);
        datePickerDialog.show();
    }


    void checkLectureOnDate(int year, int month, int dayOfMonth) {
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.set(year, month, dayOfMonth);
        System.out.println("here");
        System.out.println("selected date: " + selectedDate.getTime());
        String dayOfWeekS = String.valueOf(selectedDate.get(Calendar.DAY_OF_MONTH));
        String monthS = String.valueOf(selectedDate.get(Calendar.MONTH) + 1);
        String yearS = String.valueOf(selectedDate.get(Calendar.YEAR));
        lectureInfoTextView.setText(yearS + " " + monthS + " " + dayOfWeekS);
        addme.removeAllViews();

        DataBaseHelper dataBaseHelper = new DataBaseHelper(getContext(),
                "TRAINING_CENTER", null, 1);
        Cursor cursor = dataBaseHelper.getSecId(TraineeActivites.getEmail());

        while (cursor.moveToNext()) {
            int secId = cursor.getInt(1);

            Cursor cursor1 = dataBaseHelper.getSecInfo(secId);

            if (cursor1.moveToNext()) {
                daysList.clear();

                String startDate = cursor1.getString(8);//get the start date of the section
                String[] startDateArray = startDate.split(" ");
                int year1 = Integer.parseInt(startDateArray[2]);
                int month1 = getInvMonthFormat(startDateArray[0]);
                int day1 = Integer.parseInt(startDateArray[1]);

                String endDate = cursor1.getString(9);

                String[] endDateArray = endDate.split(" ");
                int year2 = Integer.parseInt(endDateArray[2]);
                int month2 = getInvMonthFormat(endDateArray[0]);
                int day2 = Integer.parseInt(endDateArray[1]);


                String days = cursor1.getString(6);
                String[] daysArray = days.split(", ");


                for (int i = 0; i < daysArray.length; i++) {
                    System.out.println(daysArray[i]);
                    daysList.add(daysArray[i].toLowerCase().trim());
                }

                Calendar startDateCalendar = Calendar.getInstance();
                startDateCalendar.set(year1, month1 - 1, day1);

                Calendar endDateCalendar = Calendar.getInstance();
                endDateCalendar.set(year2, month2 - 1, day2);

                if (selectedDate.compareTo(startDateCalendar) >= 0 && selectedDate.compareTo(endDateCalendar) <= 0) {

                    //get the day of the selected date and convert it to a string
                    int dayOfWeek = selectedDate.get(Calendar.DAY_OF_WEEK);
                    String dayOfWeekString = conDay(dayOfWeek);
                    System.out.println(dayOfWeekString + " " + dayOfWeek);

                    if (daysList.contains(dayOfWeekString.toLowerCase().trim())) {

                        list.add(new Section(cursor1.getInt(0), cursor1.getString(1), cursor1.getInt(2), cursor1.getInt(3),
                                cursor1.getString(4), cursor1.getString(5), cursor1.getString(6), cursor1.getString(7),
                                cursor1.getString(8), cursor1.getString(9)));

                        System.out.println(list.size() + "size of list");


                    } else {
                        System.out.println("Did not enter the if statement");
                    }
                }
            }
        }

        for (Section section : list) {
            String courseNamen = dataBaseHelper.getCourseName(section.getCourseID());
            String startTime = section.getStartTime();
            String endTime = section.getEndTime();
            String room = section.getRoom();

            // Create a LinearLayout to hold the TextViews for each section
            LinearLayout sectionLayout = new LinearLayout(getContext());
            sectionLayout.setOrientation(LinearLayout.HORIZONTAL);

            // Create TextViews for each attribute and add them to the sectionLayout
            TextView courseNameTextView = new TextView(getContext());
            courseNameTextView.setText(courseNamen);
            courseNameTextView.setTextColor(Color.BLACK);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    220,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 0, 50, 0); // Add margin between TextViews (adjust as needed)
            sectionLayout.addView(courseNameTextView, layoutParams);

            TextView startTimeTextView = new TextView(getContext());
            startTimeTextView.setTextColor(Color.BLACK);
            startTimeTextView.setText(startTime);
            layoutParams = new LinearLayout.LayoutParams(
                    220,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 0, 50, 0); // Add margin between TextViews (adjust as needed)
            sectionLayout.addView(startTimeTextView, layoutParams);

            TextView endTimeTextView = new TextView(getContext());
            endTimeTextView.setTextColor(Color.BLACK);
            endTimeTextView.setText(endTime);
            layoutParams = new LinearLayout.LayoutParams(
                    220,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 0, 50, 0); // Add margin between TextViews (adjust as needed)
            sectionLayout.addView(endTimeTextView, layoutParams);

            TextView roomTextView = new TextView(getContext());
            roomTextView.setTextColor(Color.BLACK);
            roomTextView.setText(room);
            layoutParams = new LinearLayout.LayoutParams(
                   200,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 0, 50, 0); // Add margin between TextViews (adjust as needed)
            sectionLayout.addView(roomTextView, layoutParams);

            // Set layout parameters for the sectionLayout
            LinearLayout.LayoutParams sectionLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            sectionLayoutParams.setMargins(0, 25, 0, 0); // Add margin top of 16dp (adjust as needed)

            // Add the sectionLayout to the main LinearLayout container
            addme.addView(sectionLayout, sectionLayoutParams);

        }


    }

    void displayMatchingSections() {
        // You can display or utilize the list of matching sections here
//        System.out.println("List size: " + list.size());
//        for (Section section : list) {
//            System.out.println("Section: " + section.toString());
//        }
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

    public String conDay(int dayOfWeek) {
        String dayOfWeekString = "";
        switch (dayOfWeek) {
            case 1:
                dayOfWeekString = "Sunday";
                break;
            case 2:
                dayOfWeekString = "Monday";
                break;
            case 3:
                dayOfWeekString = "Tuesday";
                break;
            case 4:
                dayOfWeekString = "Wednesday";
                break;
            case 5:
                dayOfWeekString = "Thursday";
                break;
            case 6:
                dayOfWeekString = "Friday";
                break;
            case 7:
                dayOfWeekString = "Saturday";
                break;
        }
        return dayOfWeekString;
    }
}
