package com.example.finalproject;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DisplaySection#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DisplaySection extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DisplaySection() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DisplaySection.
     */
    // TODO: Rename and change types and number of parameters
    public static DisplaySection newInstance(String param1, String param2) {
        DisplaySection fragment = new DisplaySection();
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
        View view = inflater.inflate(R.layout.fragment_display_section, container, false);
        TextView textView = view.findViewById(R.id.textView2);
        Bundle extras = getArguments();
        String COURSE_ID = "-1";
        if (extras != null) {
            textView.setText(extras.getString("COURSE_NAME"));
            textView.setTextSize(30);
            textView.setTextColor(Color.WHITE);
            COURSE_ID = extras.getString("ID");
        }
        Button add = view.findViewById(R.id.button);
        String finalCOURSE_ID = COURSE_ID;
        System.out.println(finalCOURSE_ID);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataBaseHelper dataBaseHelper = new DataBaseHelper(
                        getContext(),"TRAINING_CENTER",null,1);

                Cursor cursor = dataBaseHelper.getAllCourses();
                while(cursor.moveToNext()){
                    if(cursor.getInt(0) == Integer.parseInt(finalCOURSE_ID)){
                        Course course = new Course(Integer.parseInt(finalCOURSE_ID), cursor.getString(1), cursor.getString(2), cursor.getString(3),
                                cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7),
                                cursor.getBlob(8));

                        String START_DATE = course.getStartDate();
                        String END_DATE = course.getEndDate();

                        Fragment fragment = new CreateNewSections();
                        Bundle bundle = new Bundle();
                        bundle.putString("COURSE_ID", finalCOURSE_ID);
                        bundle.putString("START_DATE", START_DATE);
                        bundle.putString("END_DATE", END_DATE);
                        fragment.setArguments(bundle);

                        replaceFragment(fragment);
                        return;
                    }
                }
            }
        });

        DataBaseHelper dataBaseHelper = new DataBaseHelper(
                getContext(),"TRAINING_CENTER",null,1);

        Cursor cursor = dataBaseHelper.getSections();
        LinearLayout layout = view.findViewById(R.id.layout);

        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.LEFT);

        while(cursor.moveToNext()){
            if(cursor.getString(2).equals(COURSE_ID)){
                Section section = new Section(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3),
                        cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7),
                        cursor.getString(8), cursor.getString(9));


                layout.addView(createVerticalLayout("Section ID:  ", String.valueOf(section.getSectionID())));
                layout.addView(createVerticalLayout("Instructor:  ", section.getInstructorEmail()));
                layout.addView(createVerticalLayout("Course ID:  ", String.valueOf(section.getCourseID())));
                layout.addView(createVerticalLayout("Max Trainees:  ", String.valueOf(section.getMaxTrainees())));
                layout.addView(createVerticalLayout("Start Time:  ", section.getStartTime()));
                layout.addView(createVerticalLayout("End Time:  ", section.getEndTime()));
                layout.addView(createVerticalLayout("Start Date:  ", section.getStartDate()));
                layout.addView(createVerticalLayout("End Date:  ", section.getEndDate()));
                layout.addView(createVerticalLayout("Days:  ", section.getDays()));
                layout.addView(createVerticalLayout("Room:  ", section.getRoom()));
                layout.addView(createVerticalLayout("Start Date:  ", section.getStartDate()));
                layout.addView(createVerticalLayout("End Date:  ", section.getEndDate()));
                String has  = String.valueOf(dataBaseHelper.countStudentsInSection(String.valueOf(section.getSectionID())));
                String mx = String.valueOf(section.getMaxTrainees());
                layout.addView(createVerticalLayout("Students count:  ", has + "/" + mx));

                Button button = new Button(getContext());
                button.setText("Edit Section " + String.valueOf(cursor.getInt(0)));
                button.setTextColor(Color.WHITE);
                button.setTextSize(20);

                Button delete = new Button(getContext());
                delete.setText("Delete Section " + String.valueOf(cursor.getInt(0)));
                delete.setTextColor(Color.WHITE);
                delete.setTextSize(20);

                LinearLayout vertical = new LinearLayout(getContext());
                vertical.setOrientation(LinearLayout.VERTICAL);
                vertical.setGravity(Gravity.CENTER_HORIZONTAL);
                vertical.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));

                vertical.addView(button);
                vertical.addView(delete);
                layout.addView(vertical);

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DataBaseHelper dataBaseHelper = new DataBaseHelper(
                                getContext(),"TRAINING_CENTER",null,1);
                        dataBaseHelper.deleteSection(delete.getText().toString().substring(15));

                        Toast.makeText(getContext(), "Section " + delete.getText().toString().substring(15) +  " Deleted Successfully", Toast.LENGTH_SHORT).show();

                        Fragment fragment = new DisplaySection();
                        Bundle bundle = new Bundle();
                        bundle.putString("ID", finalCOURSE_ID);
                        bundle.putString("COURSE_NAME", textView.getText().toString());
                        fragment.setArguments(bundle);
                        replaceFragment(fragment);
                    }
                });

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Fragment fragment = new EditSections();
                        Bundle bundle = new Bundle();
                        bundle.putString("SECTION_ID", button.getText().toString());
                        fragment.setArguments(bundle);
                        replaceFragment(fragment);
                    }
                });

                TextView textView1 = new TextView(getContext());
                textView1.setText("\n\n");
                layout.addView(textView1);
            }
        }
        return view;
    }

    private void replaceFragment(Fragment newFragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView, newFragment);
        fragmentTransaction.commit();
    }

    LinearLayout createVerticalLayout(String s1, String s2){
        LinearLayout linearLayout1 = new LinearLayout(getContext());
        linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout1.setGravity(Gravity.LEFT);
        linearLayout1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        TextView textView1 = new TextView(getContext());
        textView1.setText(s1);
        textView1.setTextColor(Color.WHITE);
        textView1.setTextSize(20);

        TextView textView2 = new TextView(getContext());
        textView2.setText(s2);
        textView2.setTextColor(Color.WHITE);
        textView2.setTextSize(20);

        linearLayout1.addView(textView1);
        linearLayout1.addView(textView2);
        return linearLayout1;
    }
}