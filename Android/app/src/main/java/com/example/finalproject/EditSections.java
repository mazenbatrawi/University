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
import android.widget.EditText;
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
 * Use the {@link EditSections#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditSections extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    int hour1, minute1;
    int hour2, minute2;

    boolean selectDay[];
    ArrayList<Integer> day_index = new ArrayList<>();

    String[] weekDays = {"Saturday", "Monday", "Tuesday", "Wednesday", "Thursday"};

    Button start_time;
    Button end_time;
    Button days, room, email;
    final String[] finalSTART_DATE = {"-1"};
    final String[] finalEND_DATE = { "-1" };
    final String[] finalCourse_ID = {"-1"};
    boolean last = false;

    public EditSections() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditSections.
     */
    // TODO: Rename and change types and number of parameters
    public static EditSections newInstance(String param1, String param2) {
        EditSections fragment = new EditSections();
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
        View view = inflater.inflate(R.layout.fragment_edit_sections, container, false);

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

        Bundle extras = getArguments();
        String SECTION_ID = "-1";
        if (extras != null) {
            SECTION_ID = extras.getString("SECTION_ID");
            String [] split = SECTION_ID.split(" ");
            SECTION_ID = split[2];
        }
        selectDay = new boolean[weekDays.length];
        int ID = Integer.parseInt(SECTION_ID);
        EditText max_students = view.findViewById(R.id.max_trainees);

        days = view.findViewById(R.id.days_list);
        room = view.findViewById(R.id.room_list);
        email = view.findViewById(R.id.instructor_email);

        String finalSECTION_ID = SECTION_ID;
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

                int courseID = dataBaseHelper.getCourseIDForSection(Integer.parseInt(finalSECTION_ID));

                Cursor cursor = dataBaseHelper.getTopics(String.valueOf(courseID));
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
                    Toast toast =Toast.makeText(getContext(), "There is no instructor can teach this course", Toast.LENGTH_SHORT);
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


        TextView instructor_text = view.findViewById(R.id.instructortv);
        TextView max_students_text = view.findViewById(R.id.maxtv);
        TextView start_time_text = view.findViewById(R.id.starttimetv);
        TextView end_time_text = view.findViewById(R.id.eendtimetv);
        TextView days_text = view.findViewById(R.id.daystv);
        TextView room_text = view.findViewById(R.id.roomtv);

        Button edit = view.findViewById(R.id.edit);


        DataBaseHelper dataBaseHelper = new DataBaseHelper(
                getContext(),"TRAINING_CENTER",null,1);

        Cursor cursor = dataBaseHelper.getSections();
        int course = 0;
        String initialStartTime = "";
        String initialEndTime = "";
        while(cursor.moveToNext()){
            if(cursor.getInt(0) == ID){
                Section section = new Section(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3),
                        cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7),
                        cursor.getString(8), cursor.getString(9));

                email.setText(section.getInstructorEmail());
                max_students.setText(String.valueOf(section.getMaxTrainees()));
                start_time.setText(section.getStartTime());
                end_time.setText(section.getEndTime());
                days.setText(section.getDays());
                room.setText(section.getRoom());
                course = section.getCourseID();
                initialStartTime = section.getStartTime();
                initialEndTime = section.getEndTime();
                break;
            }
        }

        Cursor cursor1 = dataBaseHelper.getAllCourses();
        while(cursor1.moveToNext()){
            if(course == cursor1.getInt(0)){
                finalSTART_DATE[0] = cursor1.getString(4);
                finalEND_DATE[0] = cursor1.getString(5);
                finalCourse_ID[0] = String.valueOf(cursor1.getInt(0));
                break;
            }
        }
        assert !finalSTART_DATE[0].equals("-1");
        assert !finalEND_DATE[0].equals("-1");
        assert !finalCourse_ID[0].equals("-1");
        String finalInitialStartTime = initialStartTime;
        String finalInitialEndTime = initialEndTime;
        edit.setOnClickListener(new View.OnClickListener() {
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
                    instructor_text.setTextColor(Color.RED);
                }
                else{
                    instructor_text.setTextColor(Color.WHITE);
                }
                String MAX = max_students.getText().toString();
                if(MAX.isEmpty() || Integer.parseInt(MAX) <= 0){
                    error = true;
                    max_students_text.setTextColor(Color.RED);
                }
                else{
                    max_students_text.setTextColor(Color.WHITE);
                }
                String START = start_time.getText().toString();
                if(START.isEmpty()){
                    error = true;
                    start_time_text.setTextColor(Color.RED);
                }
                else{
                    start_time_text.setTextColor(Color.WHITE);
                }
                String END = end_time.getText().toString();
                if(END.isEmpty()){
                    error = true;
                    end_time_text.setTextColor(Color.RED);
                }
                else{
                    end_time_text.setTextColor(Color.WHITE);
                }
                String DAYS = days.getText().toString();
                if(DAYS.isEmpty()){
                    error = true;
                    days_text.setTextColor(Color.RED);
                }
                else{
                    days_text.setTextColor(Color.WHITE);
                }
                String ROOM = room.getText().toString();
                if(ROOM.isEmpty()){
                    error = true;
                    room_text.setTextColor(Color.RED);
                }
                else{
                    room_text.setTextColor(Color.WHITE);
                }
                if(error){
                    return;
                }

                DataBaseHelper dataBaseHelper = new DataBaseHelper(
                        getContext(),"TRAINING_CENTER",null,1);

                if(!dataBaseHelper.checkInstructor(EMAIL)){
                    Toast toast =Toast.makeText(getContext(), "The Instructor Doesn't Exist", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

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
                    if(section.getSectionID() == ID){
                        continue;
                    }
                    if(section.getInstructorEmail().equals(EMAIL)){
                        String s1 = section.getStartTime();
                        String s2 = section.getEndTime();

                        assert !finalSTART_DATE[0].equals("-1");
                        if(conflict(s1, s2, section.getDays(), START, END, DAYS,
                                finalSTART_DATE[0], finalEND_DATE[0], section.getStartDate(), section.getEndDate())){
                            can = false;
                        }
                    }
                }
                if(!can){
                    Toast toast = Toast.makeText(getContext(), "The Instructor Has Another Class In This Time", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

                if(!checkTime(START, END)){
                    start_time_text.setTextColor(Color.RED);
                    end_time_text.setTextColor(Color.RED);
                    return;
                }
                start_time_text.setTextColor(Color.WHITE);
                end_time_text.setTextColor(Color.WHITE);

                if(checkConflictForTrainee(finalInitialStartTime, finalInitialEndTime, START, END, ID)){
                    Toast toast = Toast.makeText(getContext(), "There Is A Conflict For Trainees", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

                Section section = new Section(ID, EMAIL, Integer.parseInt(finalCourse_ID[0]), Integer.parseInt(MAX), START,
                        END, DAYS, ROOM, finalSTART_DATE[0], finalEND_DATE[0]);

                dataBaseHelper.updateSection(section);

                Toast.makeText(getContext(), "Section Updated Successfully", Toast.LENGTH_SHORT).show();
                replaceFragment(new AdminHomePage());
            }
        });

        days.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                last = false;
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        getContext()
                );

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

        room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(start_time.getText().toString().isEmpty() || end_time.getText().toString().isEmpty() ||
                        days.getText().toString().isEmpty() || email.getText().toString().isEmpty() ||
                        max_students.getText().toString().isEmpty()){
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
                    if(section.getSectionID() == ID){
                        continue;
                    }

                    if(conflict(start_time.getText().toString(), end_time.getText().toString(), days.getText().toString(),
                            section.getStartTime(), section.getEndTime(), section.getDays(),
                            finalSTART_DATE[0], finalEND_DATE[0], section.getStartDate(), section.getEndDate())){

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
                    Toast toast =Toast.makeText(getContext(), "No available rooms for your selection", Toast.LENGTH_SHORT);
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

    public boolean checkConflictForTrainee(String finalInitialStartTime, String finalInitialEndTime,
                                           String START, String END, int ID){
        DataBaseHelper dataBaseHelper = new DataBaseHelper(
                getContext(),"TRAINING_CENTER",null,1);

        if(!(finalInitialStartTime.equals(START) && finalInitialEndTime.equals(END))){

            // Get all students in this section from Trainee_Section table
            Cursor cursor2 = dataBaseHelper.getSectionsForTrainee(ID);
            while(cursor2.moveToNext()){
                TraineeToSection traineeToSection = new TraineeToSection(
                        cursor2.getInt(0),
                        cursor2.getInt(1),
                        cursor2.getString(2),
                        cursor2.getInt(3)
                );


                // get the course ID for this section
                int courseID = dataBaseHelper.getCourseIDForSection(ID);
                // get all data from traineeToSection table
                Cursor cursor3 = dataBaseHelper.getTraineeToSection();
                while(cursor3.moveToNext()){
                    String email = cursor3.getString(2);
                    if(!email.equals(traineeToSection.getTraineeEmail())){
                        continue;
                    }
                    int otherSectionID = cursor3.getInt(1);
                    if(otherSectionID == ID){
                        continue;
                    }
                    // get the course ID for this section
                    int otherCourseID = dataBaseHelper.getCourseIDForSection(otherSectionID);
                    // get the start date  of the 2 courses
                    String startDate1 = dataBaseHelper.getStartDate(courseID);
                    String startDate2 = dataBaseHelper.getStartDate(otherCourseID);
                    // get the end date  of the 2 courses
                    String endDate1 = dataBaseHelper.getEndDate(courseID);
                    String endDate2 = dataBaseHelper.getEndDate(otherCourseID);

                    if(date_intersect(startDate1, endDate1, startDate2, endDate2)){
                        // get the start time of the 2 sections
                        String startTime1 = start_time.getText().toString();
                        String startTime2 = dataBaseHelper.getStartTime(otherSectionID);
                        // get the end time of the 2 sections
                        String endTime1 = end_time.getText().toString();
                        String endTime2 = dataBaseHelper.getEndTime(otherSectionID);
                        // get the days of the 2 sections
                        String days1 = days.getText().toString();
                        String days2 = dataBaseHelper.getDays(otherSectionID);
                        // check if there is a conflict
                        if(conflict(startTime1, endTime1, days1, startTime2, endTime2, days2,
                                finalSTART_DATE[0], finalEND_DATE[0], startDate1, endDate1)){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean conflict(String s1, String s2, String days1, String s3, String s4, String days2,
                            String start_date1, String end_date1, String start_date2, String end_date2){
        if(!date_intersect(start_date1, end_date1, start_date2, end_date2)){
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

    public boolean time_intersect(int h1, int m1, int h2, int m2, int h3, int m3, int h4, int m4){
        int startTime1 = h1 * 60 + m1;
        int endTime1 = h2 * 60 + m2;
        int startTime2 = h3 * 60 + m3;
        int endTime2 = h4 * 60 + m4;

        return endTime1 >= startTime2 && endTime2 >= startTime1;
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

    public boolean date_intersect(String l1, String r1, String l2, String r2) {
        String[] l1_split = l1.split("\\s+");
        String[] l2_split = l2.split("\\s+");
        String[] r1_split = r1.split("\\s+");
        String[] r2_split = r2.split("\\s+");

        int startMonth1 = getInvMonthFormat(l1_split[0]);
        int startDay1 = Integer.parseInt(l1_split[1]);
        int startYear1 = Integer.parseInt(l1_split[2]);

        int endMonth1 = getInvMonthFormat(r1_split[0]);
        int endDay1 = Integer.parseInt(r1_split[1]);
        int endYear1 = Integer.parseInt(r1_split[2]);

        int startMonth2 = getInvMonthFormat(l2_split[0]);
        int startDay2 = Integer.parseInt(l2_split[1]);
        int startYear2 = Integer.parseInt(l2_split[2]);

        int endMonth2 = getInvMonthFormat(r2_split[0]);
        int endDay2 = Integer.parseInt(r2_split[1]);
        int endYear2 = Integer.parseInt(r2_split[2]);

        if (endYear1 < startYear2 || endYear2 < startYear1) {
            return false;
        } else if (endYear1 == startYear2 && endMonth1 < startMonth2) {
            return false;
        } else if (endYear2 == startYear1 && endMonth2 < startMonth1) {
            return false;
        } else if (endYear1 == startYear2 && endMonth1 == startMonth2 && endDay1 < startDay2) {
            return false;
        } else if (endYear2 == startYear1 && endMonth2 == startMonth1 && endDay2 < startDay1) {
            return false;
        }
        return true;
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
}