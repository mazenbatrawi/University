package com.example.finalproject;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.graphics.Color;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Trainee_Course_history#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Trainee_Course_history extends Fragment {

    private Spinner spinnerDropdown;
    private ListView listView;

    private List<String> dropdownItems;
    private ArrayList<String> listViewItems;

    private ArrayAdapter<String> dropdownAdapter;
    private ArrayAdapter<String> listAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Trainee_Course_history() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Trainee_Course_history.
     */
    // TODO: Rename and change types and number of parameters
    public static Trainee_Course_history newInstance(String param1, String param2) {
        Trainee_Course_history fragment = new Trainee_Course_history();
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

    public void getHistoryOfcourse(DataBaseHelper dataBaseHelper, ArrayList<String> listViewItems, boolean from){

        CreateNewCourses checkDates = new CreateNewCourses();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        String TodayDate = checkDates.makeDateString(currentDay, currentMonth, currentYear);

        //get the SectionId from T2S
        Cursor cursor = dataBaseHelper.getAllSectionsOfTrainee(TraineeActivites.getEmail());
        System.out.println();
        while (cursor.moveToNext()){
            //search the section for the courses of the Trainee
            Cursor cursor2 = dataBaseHelper.getSection(cursor.getString(1));
            System.out.println(cursor.getString(1) + "the section id");

            while (cursor2.moveToNext()){

                Section section = new Section(cursor2.getInt(0), cursor2.getString(1), cursor2.getInt(2),
                        cursor2.getInt(3), cursor2.getString(4), cursor2.getString(5),
                        cursor2.getString(6), cursor2.getString(7), cursor2.getString(8),
                        cursor2.getString(9));

                //int courseId = cursor2.getInt(2);
                int courseId = section.getCourseID();
                System.out.println(courseId + "the course id");

                Cursor cursor3 = dataBaseHelper.getCourseFromId(courseId);

                while (cursor3.moveToNext()){
                    Course course = new Course(cursor3.getInt(0), cursor3.getString(1), cursor3.getString(2),
                            cursor3.getString(3), cursor3.getString(4), cursor3.getString(5),
                            cursor3.getString(6), cursor3.getString(7), cursor3.getBlob(8));

                    System.out.println(course.getPrerequisites() + "the course pre");
                    //check if the course is finished according to today's date
                        if(from)
                            listViewItems.add(course.toString());
                        else
                            listViewItems.add(course.getPrerequisites());
                    }



                //listViewItems.add(section.toString());
            }

        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_trainee__course_history, container, false);

        spinnerDropdown = rootView.findViewById(R.id.spinnerDropdown);
        listView = rootView.findViewById(R.id.listView);

        // Initialize data for dropdown menu
        dropdownItems = new ArrayList<>();
        dropdownItems.add("My Courses");
        dropdownItems.add("Course Browser");


        // Initialize data for list view
        listViewItems = new ArrayList<>();

        // Initialize adapters
        dropdownAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, dropdownItems);
        listAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, listViewItems);


        // Set adapters
        spinnerDropdown.setAdapter(dropdownAdapter);
        listView.setAdapter(listAdapter);

        // Set click listener for dropdown menu item selection
        spinnerDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = dropdownItems.get(position);
                Toast.makeText(requireContext(), "Selected option: " + selectedOption, Toast.LENGTH_SHORT).show();

                DataBaseHelper dataBaseHelper = new DataBaseHelper(getContext(), "TRAINING_CENTER", null, 1);

                // Update list view items based on selected option
                if (selectedOption.equals("Course Browser")) {
                    listViewItems.clear();
                    Cursor cursor = dataBaseHelper.getAllCourses();

                    while (cursor.moveToNext()) {

                        Course course = new Course(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                                cursor.getString(3), cursor.getString(4), cursor.getString(5),
                                cursor.getString(6), cursor.getString(7), cursor.getBlob(8));
                        listViewItems.add(course.toString());
                    }

                    listAdapter.notifyDataSetChanged();

                } else if (selectedOption.equals("My Courses")) {

                    listViewItems.clear();
                    getHistoryOfcourse(dataBaseHelper, listViewItems, true);
                    listAdapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Initialize and set adapter for ListView
        listAdapter = new ArrayAdapter<>(requireContext(), R.layout.text_for_course_browser_trainee, listViewItems);
        listView.setAdapter(listAdapter);






        return rootView;
    }
}
