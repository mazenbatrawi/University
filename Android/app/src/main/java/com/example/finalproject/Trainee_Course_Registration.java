package com.example.finalproject;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.sql.SQLOutput;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Trainee_Course_Registration#newInstance} factory method to
 * create an instance of this fragment.
 */
//fragment for trainee course registration
public class Trainee_Course_Registration extends Fragment {

    private ArrayList<Course> filteredCourseList;
    Trainee_Reg_withraw reg_withdraw;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Trainee_Course_Registration() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Trainee_Course_Registration.
     */
    // TODO: Rename and change types and number of parameters
    public static Trainee_Course_Registration newInstance(String param1, String param2) {
        Trainee_Course_Registration fragment = new Trainee_Course_Registration();
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
        View rootView =  inflater.inflate(R.layout.fragment_trainee__course__registration, container, false);

        RecyclerView recyclerViewCourses = rootView.findViewById(R.id.recyclerViewCourses);
        ArrayList<Section> courseList = new ArrayList<>();
        Bundle args = getArguments();
        int courseId = 0;
        String Temail = "";

        if (args != null) {
            // Get the course name from the arguments
            courseId = args.getInt("courseId"); //sent from course adapter
            //Temail = args.getString("email"); //sent from course adapter
        }

        DataBaseHelper dataBaseHelper = new DataBaseHelper(getContext(),"TRAINING_CENTER",null,1);

        //query about the (from the arg) section and the trainee and send to trainee_Reg_withraw
        Cursor cursor = dataBaseHelper.getSections();
       // System.out.println(courseId);


        while(cursor.moveToNext()){
            if(courseId == cursor.getInt(2)) {
                Section instructor1 = new Section(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getInt(2),
                        cursor.getInt(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8),
                        cursor.getString(9)
                );
                System.out.println(instructor1.toString() + "here the sections");
                courseList.add(instructor1);
            }
        }

        for(Section sec : courseList)
            System.out.println(sec.toString() + "here the sections");

        //go to the courseAdapter Trainee_Reg_withraw
        reg_withdraw = new Trainee_Reg_withraw(courseList, TraineeActivites.getEmail());

        // Set the layout manager and adapter for the RecyclerView
        recyclerViewCourses.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewCourses.setAdapter(reg_withdraw);


    return rootView;
    }
}