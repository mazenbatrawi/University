package com.example.finalproject;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Trainee_Search_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Trainee_Search_Fragment extends Fragment {


    private EditText searchEditText;
    private Button searchButton;
    private RecyclerView coursesRecyclerView;
    private CourseAdapter courseAdapter;
    private List<Course> courseList;
    private List<Course> filteredCourseList;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Trainee_Search_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Trainee_Search_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Trainee_Search_Fragment newInstance(String param1, String param2) {
        Trainee_Search_Fragment fragment = new Trainee_Search_Fragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_trainee__search_, container, false);

        TraineeActivites activity = (TraineeActivites) getActivity();
        String TEmail = activity.getEmail();
        //System.out.println(TEmail);

        searchEditText = rootView.findViewById(R.id.editTextSearch);
        searchButton = rootView.findViewById(R.id.buttonSearch);
        coursesRecyclerView = rootView.findViewById(R.id.recyclerViewCourses);

        // Initialize the course list and adapter
        courseList = new ArrayList<>();
        courseAdapter = new CourseAdapter(courseList, TraineeActivites.getEmail());


        // Set the layout manager and adapter for the RecyclerView
        coursesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        coursesRecyclerView.setAdapter(courseAdapter);

        DataBaseHelper dataBaseHelper = new DataBaseHelper (getContext(),"TRAINING_CENTER",null,1);;
        Cursor cursor = dataBaseHelper.getAllCourses();

        while (cursor.moveToNext()) {
            int ID = cursor.getInt(0);
            String courseID = cursor.getString(1);
            String courseName = cursor.getString(2);
            String prerequisites = cursor.getString(3);
            String startDate = cursor.getString(4);
            String endDate = cursor.getString(5);
            String registrationStart = cursor.getString(6);
            String registrationEnd = cursor.getString(7);
            byte[] image = cursor.getBlob(8);

            courseList.add(new Course(ID, courseID, courseName, prerequisites, startDate, endDate,
                    registrationStart, registrationEnd, image));
        }

        //System.out.println(courseList.size());

        // Set a click listener for the search button
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SearchFilter();

            }
        });

        return rootView;
    }

    public void SearchFilter(){
        String search = searchEditText.getText().toString();
        filteredCourseList = new ArrayList<>();
        for (Course course : courseList) {
            if (course.getCourseName().toLowerCase().contains(search.toLowerCase())) {
                filteredCourseList.add(course);
            }
        }
        courseAdapter.filterList(filteredCourseList);
    }
}