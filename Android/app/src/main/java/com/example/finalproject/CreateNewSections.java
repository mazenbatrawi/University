package com.example.finalproject;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.TreeSet;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateNewSections#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateNewSections extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    boolean last = false;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button start_time, end_time, add;
    private TextView instructortv, maxtv, starttimetv, endtimetv, daystv, roomtv;
    private TextView max;
    private Button email;
    private Button days, room;
    int hour1, minute1;
    int hour2, minute2;

    boolean selectDay[];
    ArrayList<Integer> day_index = new ArrayList<>();

    String[] weekDays = {"Saturday", "Monday", "Tuesday", "Wednesday", "Thursday"};

    public CreateNewSections() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateNewSections.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateNewSections newInstance(String param1, String param2) {
        CreateNewSections fragment = new CreateNewSections();
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
        View view = inflater.inflate(R.layout.fragment_create_new_sections, container, false);


        start_time = view.findViewById(R.id.start_time);
        end_time = view.findViewById(R.id.end_time);

        start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener()
                {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
                    {
                        hour1 = selectedHour;
                        minute1 = selectedMinute;
                        if (minute1 < 10 && hour1 < 10) {
                            start_time.setText("0" + String.valueOf(hour1) + ":" + "0" + String.valueOf(minute1));
                        }
                        else if (minute1 < 10) {
                            start_time.setText(String.valueOf(hour1) + ":" + "0" + String.valueOf(minute1));
                        }
                        else if (hour1 < 10) {
                            start_time.setText("0" + String.valueOf(hour1) + ":" + String.valueOf(minute1));
                        }
                        else {
                            start_time.setText(String.valueOf(hour1) + ":" + String.valueOf(minute1));
                        }
                        last = false;
                    }
                };

                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), onTimeSetListener, hour1, minute1, true);
                timePickerDialog.setTitle("Select Time");
                timePickerDialog.show();
            }
        });

        end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener()
                {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
                    {
                        hour2 = selectedHour;
                        minute2 = selectedMinute;
                        if (minute2 < 10 && hour2 < 10) {
                            end_time.setText("0" + String.valueOf(hour2) + ":" + "0" + String.valueOf(minute2));
                        }
                        else if (minute2 < 10) {
                            end_time.setText(String.valueOf(hour2) + ":" + "0" + String.valueOf(minute2));
                        }
                        else if (hour2 < 10) {
                            end_time.setText("0" + String.valueOf(hour2) + ":" + String.valueOf(minute2));
                        }
                        else {
                            end_time.setText(String.valueOf(hour2) + ":" + String.valueOf(minute2));
                        }
                        last = false;
                    }
                };

                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), onTimeSetListener, hour2, minute2, true);
                timePickerDialog.setTitle("Select Time");
                timePickerDialog.show();
            }
        });

        String COURSE_ID = "-1";
        String START_DATE = "-1";
        String END_DATE = "-1";
        Bundle extras = getArguments();
        if (extras != null) {
            COURSE_ID = extras.getString("COURSE_ID");
            START_DATE = extras.getString("START_DATE");
            END_DATE = extras.getString("END_DATE");
        }

        instructortv = view.findViewById(R.id.instructortv);
        maxtv = view.findViewById(R.id.maxtv);
        starttimetv = view.findViewById(R.id.starttimetv);
        endtimetv = view.findViewById(R.id.eendtimetv);
        daystv = view.findViewById(R.id.daystv);
        roomtv = view.findViewById(R.id.roomtv);

        email = view.findViewById(R.id.instructor_email);
        max = view.findViewById(R.id.max_trainees);
        days = view.findViewById(R.id.days_list);
        room = view.findViewById(R.id.room_list);

        add = view.findViewById(R.id.add_the_section);
        selectDay = new boolean[weekDays.length];

        String finalCOURSE_ID = COURSE_ID;
        String finalSTART_DATE = START_DATE;
        String finalEND_DATE = END_DATE;

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!last){
                    Toast.makeText(getContext(), "Select a room first", Toast.LENGTH_SHORT).show();
                    return;
                }
                boolean error = false;
                String EMAIL = email.getText().toString();
                if(EMAIL.isEmpty()){
                    error = true;
                    instructortv.setTextColor(Color.RED);
                }
                else{
                    instructortv.setTextColor(Color.WHITE);
                }
                String MAX = max.getText().toString();
                if(MAX.isEmpty() || Integer.parseInt(MAX) <= 0){
                    error = true;
                    maxtv.setTextColor(Color.RED);
                }
                else{
                    maxtv.setTextColor(Color.WHITE);
                }
                String START_TIME = start_time.getText().toString();
                if(START_TIME.isEmpty()){
                    error = true;
                    starttimetv.setTextColor(Color.RED);
                }
                else{
                    starttimetv.setTextColor(Color.WHITE);
                }
                String END_TIME = end_time.getText().toString();
                if(END_TIME.isEmpty()){
                    error = true;
                    endtimetv.setTextColor(Color.RED);
                }
                else{
                    endtimetv.setTextColor(Color.WHITE);
                }
                String DAYS = days.getText().toString();
                if(DAYS.isEmpty()){
                    error = true;
                    daystv.setTextColor(Color.RED);
                }
                else{
                    daystv.setTextColor(Color.WHITE);
                }
                String ROOM = room.getText().toString();
                if(ROOM.isEmpty()){
                    error = true;
                    roomtv.setTextColor(Color.RED);
                }
                else{
                    roomtv.setTextColor(Color.WHITE);
                }

                if(error){
                    return;
                }

                DataBaseHelper dataBaseHelper = new DataBaseHelper(
                        getContext(),"TRAINING_CENTER",null,1);

                Cursor cursor = dataBaseHelper.getSections();

                boolean can = true;

                while(cursor.moveToNext()){
                    Section section = new Section(
                            cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getInt(2),
                            cursor.getInt(3),
                            cursor.getString(4),
                            cursor.getString(5),
                            cursor.getString(6),
                            cursor.getString(7), cursor.getString(8), cursor.getString(9)
                    );
                    if(section.getInstructorEmail().equals(EMAIL)){
                        String s1 = section.getStartTime();
                        String s2 = section.getEndTime();

                        if(conflict(s1, s2, section.getDays(), START_TIME, END_TIME, DAYS,
                                finalSTART_DATE, finalEND_DATE, section.getStartDate(), section.getEndDate())){
                            can = false;
                        }
                    }
                }
                if(!can){
                    Toast toast = Toast.makeText(getContext(), "The Instructor Has Another Class In This Time", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

                if(!checkTime(START_TIME, END_TIME)){
                    starttimetv.setTextColor(Color.RED);
                    endtimetv.setTextColor(Color.RED);
                    return;
                }
                starttimetv.setTextColor(Color.WHITE);
                endtimetv.setTextColor(Color.WHITE);

                Section section = new Section(-1, EMAIL, Integer.parseInt(finalCOURSE_ID), Integer.parseInt(MAX), START_TIME,
                        END_TIME, DAYS, ROOM, finalSTART_DATE, finalEND_DATE);

                dataBaseHelper.insertSection(section);

                Cursor cursor2 = dataBaseHelper.getCourse(finalCOURSE_ID);
                cursor2.moveToNext();
                String courseName = "(" + cursor2.getString(1) + ", " + cursor2.getString(2) + ")";

                Cursor cursor1 = dataBaseHelper.getAllTrainees();
                while(cursor1.moveToNext()){
                    String email = cursor1.getString(0);

                    Notification notification = new Notification();
                    notification.setTraineeEmail(email);
                    notification.setNotText("A new section has been added to the course " + courseName);
                    notification.setStatus(0);
                    dataBaseHelper.insertNotifications(notification);
                }

                Toast toast = Toast.makeText(getContext(), "Section Added Successfully", Toast.LENGTH_SHORT);
                toast.show();
                replaceFragment(new AdminHomePage());
            }
        });

        days.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        getContext()
                );
                last = false;

                builder.setTitle("Select Days");
                builder.setCancelable(false);

                builder.setMultiChoiceItems(weekDays, selectDay, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if(b){
                            day_index.add(i);
                            Collections.sort(day_index);
                        }
                        else{
                            for(int j = 0; j < day_index.size(); j++){
                                if(day_index.get(j) == i){
                                    day_index.remove(j);
                                    break;
                                }
                            }
                        }
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for(int j = 0; j < day_index.size(); j++){
                            stringBuilder.append(weekDays[day_index.get(j)]);

                            if(j != day_index.size() - 1){
                                stringBuilder.append(", ");
                            }
                        }
                        days.setText(stringBuilder.toString());
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Arrays.fill(selectDay, false);
                        day_index.clear();
                        days.setText("");
                    }
                });

                builder.show();
            }
        });

        String finalCOURSE_ID1 = COURSE_ID;
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                last = false;
                DataBaseHelper dataBaseHelper = new DataBaseHelper(
                        getContext(),"TRAINING_CENTER",null,1);

                Cursor cursor1 = dataBaseHelper.getAllInstructors();
                int sz = cursor1.getCount();
                if(sz == 0){
                    Toast toast =Toast.makeText(getContext(), "There is no instructor", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                boolean[] can = new boolean[sz];
                String[] emails = new String[sz];
                String[] canTeach = new String[sz];
                int j = 0;
                while(cursor1.moveToNext()){
                    can[j] = true;
                    emails[j] = cursor1.getString(0);
                    canTeach[j] = cursor1.getString(8);
                    if(canTeach[j].isEmpty()){
                        can[j] = false;
                        continue;
                    }
                    canTeach[j] = canTeach[j].substring(2);
                    j++;
                }

                Cursor cursor = dataBaseHelper.getTopics(finalCOURSE_ID1);
                while(cursor.moveToNext()){
                    Topic topic = new Topic(
                            cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getString(2));


                    for(int i = 0; i < sz; i++){
                        if(canTeach[i].isEmpty()){
                            can[i] = false;
                            continue;
                        }
                        String[] topics = canTeach[i].split(", ");
                        TreeSet<String> set = new TreeSet<>();
                        Collections.addAll(set, topics);
                        if (!set.contains(topic.getTopicName())) {
                            can[i] = false;
                        }
                    }
                }
                int sz2 = 0;
                for (boolean b : can) {
                    if (b) sz2++;
                }
                if(sz2 == 0){
                    Toast toast = Toast.makeText(getContext(), "There is no instructor can teach this course", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                String[] items = new String[sz2];
                j = 0;
                for(int i = 0; i < sz; i++){
                    if(can[i]){
                        items[j++] = emails[i];
                    }
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Choose Instructor");
                builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        email.setText(items[i]);
                        dialogInterface.dismiss();
                    }
                });
                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = builder.create();
                builder.show();
            }
        });

        room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(start_time.getText().toString().isEmpty() || end_time.getText().toString().isEmpty() ||
                        days.getText().toString().isEmpty() || email.getText().toString().isEmpty() ||
                        max.getText().toString().isEmpty()){
                    Toast toast = Toast.makeText(getContext(), "Select the previous data first", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

                boolean[] can = new boolean[20];
                for(int i = 0; i < 20; i++) can[i] = true;
                DataBaseHelper dataBaseHelper = new DataBaseHelper(
                        getContext(),"TRAINING_CENTER",null,1);

                Cursor cursor = dataBaseHelper.getSections();
                while(cursor.moveToNext()){
                    Section section = new Section(
                            cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getInt(2),
                            cursor.getInt(3),
                            cursor.getString(4),
                            cursor.getString(5),
                            cursor.getString(6),
                            cursor.getString(7), cursor.getString(8), cursor.getString(9));

                    if(conflict(start_time.getText().toString(), end_time.getText().toString(), days.getText().toString(),
                            section.getStartTime(), section.getEndTime(), section.getDays(),
                            finalSTART_DATE, finalEND_DATE, section.getStartDate(), section.getEndDate())){

                        can[Integer.parseInt(section.getRoom()) - 1] = false;
                    }
                }

                int sz = 0;
                for(int i = 0; i < 20; i++){
                    if(can[i]) sz++;
                }

                if(sz != 0){
                    String[] items = new String[sz];
                    int j = 0;
                    for(int i = 0; i < 20; i++){
                        if(can[i]){
                            items[j] = String.valueOf(i + 1);
                            j++;
                        }
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Choose Room");
                    builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            room.setText(items[i]);
                            last = true;
                            dialogInterface.dismiss();
                        }
                    });
                    builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog dialog = builder.create();
                    builder.show();
                }
                else{
                    Toast toast = Toast.makeText(getContext(), "No available rooms for your selection", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        return view;
    }

    private void replaceFragment(Fragment newFragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView, newFragment);
        fragmentTransaction.commit();
    }

    public boolean time_intersect(int h1, int m1, int h2, int m2, int h3, int m3, int h4, int m4){
        int startTime1 = h1 * 60 + m1;
        int endTime1 = h2 * 60 + m2;
        int startTime2 = h3 * 60 + m3;
        int endTime2 = h4 * 60 + m4;

        return endTime1 >= startTime2 && endTime2 >= startTime1;
    }

    private boolean checkIntersectionOfDates(String l1, String r1, String l2, String r2){
        return !checkDates(r1, l2, false) && !checkDates(r2, l1, false);
    }
    private boolean checkDates(String s1, String s2, boolean equal){
        String[] s1_split = s1.split("\\s+");
        String[] s2_split = s2.split("\\s+");
        int month1 = getInvMonthFormat(s1_split[0]);
        int day1 = Integer.parseInt(s1_split[1]);
        int year1 = Integer.parseInt(s1_split[2]);

        int month2 = getInvMonthFormat(s2_split[0]);
        int day2 = Integer.parseInt(s2_split[1]);
        int year2 = Integer.parseInt(s2_split[2]);

        if(year1 == year2){
            if(month1 == month2){
                return equal ? day1 <= day2 : day1 < day2;
            }
            return month1 < month2;
        }
        return year1 < year2;
    }

    private int getInvMonthFormat(String month){
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

    public boolean conflict(String s1, String s2, String days1, String s3, String s4, String days2,
                            String start_date1, String end_date1, String start_date2, String end_date2){
        if(!checkIntersectionOfDates(start_date1, end_date1, start_date2, end_date2)){
            return false;
        }
        String[] days1_split = days1.split(", ");
        String[] days2_split = days2.split(", ");

        String[] s1_split = s1.split(":");
        String[] s2_split = s2.split(":");
        String[] s3_split = s3.split(":");
        String[] s4_split = s4.split(":");

        int h1 = Integer.parseInt(s1_split[0]);
        int h2 = Integer.parseInt(s2_split[0]);
        int h3 = Integer.parseInt(s3_split[0]);
        int h4 = Integer.parseInt(s4_split[0]);

        int m1 = Integer.parseInt(s1_split[1]);
        int m2 = Integer.parseInt(s2_split[1]);
        int m3 = Integer.parseInt(s3_split[1]);
        int m4 = Integer.parseInt(s4_split[1]);

        for(String day1 : days1_split){
            for(String day2 : days2_split){
                if (day1.equals(day2)) {
                    if(time_intersect(h1, m1, h2, m2, h3, m3, h4, m4)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean checkTime(String s1, String s2){
        String[] s1_split = s1.split(":");
        String[] s2_split = s2.split(":");

        int hour_1 = Integer.parseInt(s1_split[0]);
        int hour_2 = Integer.parseInt(s2_split[0]);
        int min_1 = Integer.parseInt(s1_split[1]);
        int min_2 = Integer.parseInt(s2_split[1]);

        if(hour_1 < 8 || hour_1 > 17){
            return false;
        }
        if(hour_1 > hour_2){
            return false;
        }
        if(hour_2 > 17){
            return false;
        }
        if(hour_1 == hour_2){
            return min_1 < min_2;
        }
        return true;
    }
}