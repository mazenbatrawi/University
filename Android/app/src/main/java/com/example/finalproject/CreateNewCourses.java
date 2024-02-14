package com.example.finalproject;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.TreeSet;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateNewCourses#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateNewCourses extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private DatePickerDialog datePickerDialog1;
    private DatePickerDialog datePickerDialog2;
    private DatePickerDialog datePickerDialog3;
    private DatePickerDialog datePickerDialog4;
    private Button start_date, end_date, reg_start, reg_end;
    private Button photo, add;

    TextView id, name, idtv, nametv, topicstv, startdatetv, enddatetv, regstarttv, regendtv, phototv;

    private final int GALLERY_REQ_CODE = 1000;

    TextView topic_list, pre_list;
    boolean selectTopic[], select_pre[];
    private ImageView imageView;
    ArrayList<Integer> topic_index = new ArrayList<>(), pre_index = new ArrayList<>();

    String[] topics = {"Technology", "Engineering", "Pharmacy", "Business", "Physics", "Medicine",
                        "Marketing", "Math", "Law", "Literature", "Biology", "Accountant"};

    public CreateNewCourses() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateNewCourses.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateNewCourses newInstance(String param1, String param2) {
        CreateNewCourses fragment = new CreateNewCourses();
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
        View view = inflater.inflate(R.layout.fragment_create_new_courses, container, false);

        start_date = view.findViewById(R.id.start_date);
        end_date = view.findViewById(R.id.end_date);
        reg_start = view.findViewById(R.id.registration_start_date);
        reg_end = view.findViewById(R.id.registration_end_date);

        initDatePicker();
        start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog1.show();
            }
        });

        end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog2.show();
            }
        });

        reg_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog3.show();
            }
        });

        reg_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog4.show();
            }
        });

        idtv = view.findViewById(R.id.idtv);
        nametv = view.findViewById(R.id.nametv);
        topicstv = view.findViewById(R.id.topicstv);
        startdatetv = view.findViewById(R.id.startdatetv);
        enddatetv = view.findViewById(R.id.enddatetv);
        regstarttv = view.findViewById(R.id.regstarttv);
        regendtv = view.findViewById(R.id.regendtv);
        phototv = view.findViewById(R.id.phototv);

        id = view.findViewById(R.id.course_id);
        name = view.findViewById(R.id.course_name);


        start_date.setText(getTodaysDate());
        end_date.setText(getTodaysDate());
        reg_start.setText(getTodaysDate());
        reg_end.setText(getTodaysDate());

        topic_list = view.findViewById(R.id.topic_list);
        pre_list = view.findViewById(R.id.prerequisites_list);
        selectTopic = new boolean[topics.length];

        photo = view.findViewById(R.id.add_photo);
        imageView = view.findViewById(R.id.uploaded_image);



        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, GALLERY_REQ_CODE);
            }
        });

        topic_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        getContext()
                );

                builder.setTitle("Select Topics");
                builder.setCancelable(false);

                builder.setMultiChoiceItems(topics, selectTopic, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if(b){
                            topic_index.add(i);
                            Collections.sort(topic_index);
                        }
                        else{
                            for(int j = 0; j < topic_index.size(); j++){
                                if(topic_index.get(j) == i){
                                    topic_index.remove(j);
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
                        for(int j = 0; j < topic_index.size(); j++){
                            stringBuilder.append(topics[topic_index.get(j)]);

                            if(j != topic_index.size() - 1){
                                stringBuilder.append(", ");
                            }
                        }
                        topic_list.setText(stringBuilder.toString());
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
                        Arrays.fill(selectTopic, false);
                        topic_index.clear();
                        topic_list.setText("");
                    }
                });

                builder.show();
            }
        });

        pre_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(name.getText().toString().isEmpty()){
                    Toast toast = Toast.makeText(getContext(), "Please enter course name first", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

                DataBaseHelper dataBaseHelper = new DataBaseHelper(
                        getContext(),"TRAINING_CENTER",null,1);
                Cursor cursor = dataBaseHelper.getAllCourses();
                int sz = 0;
                while(cursor.moveToNext()) sz++;
                if(sz > 0){
                    TreeSet<String> set = new TreeSet<>();

                    cursor = dataBaseHelper.getAllCourses();

                    while(cursor.moveToNext()){
                        if(cursor.getString(2).equals(name.getText().toString())) continue;
                        set.add(cursor.getString(2));
                    }
                    sz = set.size();
                    if(sz == 0){
                        Toast toast = Toast.makeText(getContext(), "No courses available", Toast.LENGTH_SHORT);
                        toast.show();
                        return;
                    }

                    String[] pre = new String[sz];
                    select_pre = new boolean[sz];
                    int k = 0;
                    for(String s : set){
                        pre[k++] = s;
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            getContext()
                    );

                    builder.setTitle("Select Prerequisites");
                    builder.setCancelable(false);

                    builder.setMultiChoiceItems(pre, select_pre, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                            if(b){
                                pre_index.add(i);
                                Collections.sort(pre_index);
                            }
                            else{
                                for(int j = 0; j < pre_index.size(); j++){
                                    if(pre_index.get(j) == i){
                                        pre_index.remove(j);
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
                            for(int j = 0; j < pre_index.size(); j++){
                                stringBuilder.append(pre[pre_index.get(j)]);

                                if(j != pre_index.size() - 1){
                                    stringBuilder.append(", ");
                                }
                            }
                            pre_list.setText(stringBuilder.toString());
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
                            Arrays.fill(select_pre, false);
                            pre_index.clear();
                            pre_list.setText("");
                        }
                    });

                    builder.show();
                }
                else{
                    Toast toast = Toast.makeText(getContext(), "There are no courses yet", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        add = view.findViewById(R.id.add_the_course);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean error = false;
                String ID = id.getText().toString();
                if(ID.isEmpty()){
                    error = true;
                    idtv.setTextColor(Color.RED);
                }
                else{
                    idtv.setTextColor(Color.WHITE);
                }
                String NAME = name.getText().toString();
                if(NAME.isEmpty()){
                    error = true;
                    nametv.setTextColor(Color.RED);
                }
                else{
                    nametv.setTextColor(Color.WHITE);
                }
                String TOPICS = topic_list.getText().toString();
                if(TOPICS.isEmpty()){
                    error = true;
                    topicstv.setTextColor(Color.RED);
                }
                else{
                    topicstv.setTextColor(Color.WHITE);
                }
                String PRE = pre_list.getText().toString();
                String STARTDATE = start_date.getText().toString();
                if(STARTDATE.isEmpty()){
                    error = true;
                    startdatetv.setTextColor(Color.RED);
                }
                else{
                    startdatetv.setTextColor(Color.WHITE);
                }
                String ENDDATE = end_date.getText().toString();
                if(ENDDATE.isEmpty()){
                    error = true;
                    enddatetv.setTextColor(Color.RED);
                }
                else{
                    enddatetv.setTextColor(Color.WHITE);
                }
                String REGSTART = reg_start.getText().toString();
                if(REGSTART.isEmpty()){
                    error = true;
                    regstarttv.setTextColor(Color.RED);
                }
                else{
                    regstarttv.setTextColor(Color.WHITE);
                }
                String REGEND = reg_end.getText().toString();
                if(REGEND.isEmpty()){
                    error = true;
                    regendtv.setTextColor(Color.RED);
                }
                else{
                    regendtv.setTextColor(Color.WHITE);
                }
                if(!STARTDATE.isEmpty() && !ENDDATE.isEmpty() && !checkDates(STARTDATE, ENDDATE, true)){
                    error = true;
                    startdatetv.setTextColor(Color.RED);
                    enddatetv.setTextColor(Color.RED);
                }
                else{
                    startdatetv.setTextColor(Color.WHITE);
                    enddatetv.setTextColor(Color.WHITE);
                }
                if(!REGSTART.isEmpty() && !REGEND.isEmpty() && !checkDates(REGSTART, REGEND, true)){
                    error = true;
                    regstarttv.setTextColor(Color.RED);
                    regendtv.setTextColor(Color.RED);
                }
                else{
                    regstarttv.setTextColor(Color.WHITE);
                    regendtv.setTextColor(Color.WHITE);
                }
                if(!STARTDATE.isEmpty() && !ENDDATE.isEmpty() && !REGSTART.isEmpty() && !REGEND.isEmpty() &&
                        !checkDates(REGEND, STARTDATE, false)){
                    error = true;
                    startdatetv.setTextColor(Color.RED);
                    enddatetv.setTextColor(Color.RED);
                    regstarttv.setTextColor(Color.RED);
                    regendtv.setTextColor(Color.RED);
                }
                else{
                    startdatetv.setTextColor(Color.WHITE);
                    enddatetv.setTextColor(Color.WHITE);
                    regstarttv.setTextColor(Color.WHITE);
                    regendtv.setTextColor(Color.WHITE);
                }

                if(imageView.getDrawable() == null){
                    error = true;
                    phototv.setTextColor(Color.RED);
                }
                else{
                    phototv.setTextColor(Color.WHITE);
                }

                if(error){
                    return;
                }

                DataBaseHelper dataBaseHelper = new DataBaseHelper(
                        getContext(),"TRAINING_CENTER",null,1);

                Cursor cursor = dataBaseHelper.getAllCourses();
                while(cursor.moveToNext()){
                    String courseID = cursor.getString(1);
                    String startDate = cursor.getString(4);
                    String endDate = cursor.getString(5);
                    if(courseID.equals(ID)){
                        if(checkIntersectionOfDates(STARTDATE, ENDDATE, startDate, endDate)){
                            Toast toast = Toast.makeText(getContext(), "The course is offered in the period provided", Toast.LENGTH_SHORT);
                            toast.show();
                            return;
                        }
                    }

                    if(!PRE.isEmpty()){
                        String[] prerequisitesArray = PRE.split(", ");
                        for(String prerequisite : prerequisitesArray){
                            if(prerequisite.equals(cursor.getString(2))){
                                if(checkIntersectionOfDates(STARTDATE, ENDDATE, startDate, endDate) ||
                                        checkDates(STARTDATE, startDate, true)){
                                    Toast toast = Toast.makeText(getContext(), "The prerequisites " + prerequisite + " is offered after the " +
                                            "beginning of the course", Toast.LENGTH_SHORT);
                                    toast.show();
                                    return;
                                }
                            }
                        }
                    }
                }

                Course course = new Course();
                course.setCourseID(ID);
                course.setCourseName(NAME);
                course.setPrerequisites(PRE);
                course.setStartDate(STARTDATE);
                course.setEndDate(ENDDATE);
                course.setRegistrationStart(REGSTART);
                course.setRegistrationEnd(REGEND);
                course.setImage(new ImageHandler().getByteArray(imageView));
                int id = dataBaseHelper.insertCourses(course);

                if(id == -1){
                    Toast toast = Toast.makeText(getContext(), "Error inserting", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

                String[] topics = TOPICS.split(", ");
                for(String topic : topics){
                    Topic topic1 = new Topic(1, String.valueOf(id), topic);
                    dataBaseHelper.insertTopics(topic1);
                }

                Cursor cursor1 = dataBaseHelper.getAllTrainees();
                while(cursor1.moveToNext()){
                    String email = cursor1.getString(0);

                    Notification notification = new Notification();
                    notification.setTraineeEmail(email);
                    notification.setNotText("A new course (" + ID + ", " + NAME + ") has been added");
                    notification.setStatus(0);
                    dataBaseHelper.insertNotifications(notification);
                }

                Toast toast = Toast.makeText(getContext(), "Course added successfully", Toast.LENGTH_SHORT);
                toast.show();
                replaceFragment(new AdminHomePage());
            }
        });


        return view;
    }

    private void replaceFragment(Fragment newFragment){
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView, newFragment);
        fragmentTransaction.commit();
    }

    private boolean checkIntersectionOfDates(String l1, String r1, String l2, String r2){
        return !checkDates(r1, l2, false) && !checkDates(r2, l1, false);
    }

    public boolean checkDates(String s1, String s2, boolean equal){
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

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    public void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String date = makeDateString(day, month, year);
                start_date.setText(date);
            }
        };

        DatePickerDialog.OnDateSetListener dateSetListener2 = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String date = makeDateString(day, month, year);
                end_date.setText(date);
            }
        };

        DatePickerDialog.OnDateSetListener dateSetListener3 = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String date = makeDateString(day, month, year);
                reg_start.setText(date);
            }
        };

        DatePickerDialog.OnDateSetListener dateSetListener4 = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String date = makeDateString(day, month, year);
                reg_end.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog1 = new DatePickerDialog(getContext(), style, dateSetListener, year, month, day);
        datePickerDialog2 = new DatePickerDialog(getContext(), style, dateSetListener2, year, month, day);
        datePickerDialog3 = new DatePickerDialog(getContext(), style, dateSetListener3, year, month, day);
        datePickerDialog4 = new DatePickerDialog(getContext(), style, dateSetListener4, year, month, day);
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

        return "JAN";
    }

    @Override
     public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode == GALLERY_REQ_CODE){
                imageView.setImageURI(data.getData());
            }
        }
    }

}