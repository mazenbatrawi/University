package com.example.finalproject;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Ccourse_desc_Frag_Trainee#newInstance} factory method to
 * create an instance of this fragment.
 */
//inside the view course after search
public class Ccourse_desc_Frag_Trainee extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Ccourse_desc_Frag_Trainee() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Ccourse_desc_Frag_Trainee.
     */
    // TODO: Rename and change types and number of parameters
    public static Ccourse_desc_Frag_Trainee newInstance(String param1, String param2) {
        Ccourse_desc_Frag_Trainee fragment = new Ccourse_desc_Frag_Trainee();
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
        View rootView =  inflater.inflate(R.layout.fragment_ccourse_desc__frag__trainee, container, false);

        TextView textViewCourseName = rootView.findViewById(R.id.textViewCourseName);
        TextView textViewCourseDescription = rootView.findViewById(R.id.textViewCourseDescription);
        Button buttonBack = rootView.findViewById(R.id.buttonBack);
        int courseId = 0;

        Bundle args = getArguments();
        if (args != null) {
            courseId = args.getInt("courseId");
            DataBaseHelper dataBaseHelper = new DataBaseHelper(getContext(),
                    "TRAINING_CENTER",null,1);
            Cursor cursor = dataBaseHelper.getCourseId(courseId);
            if(cursor.moveToNext()){
                Course course = new Course(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6),
                cursor.getString(7),
                cursor.getBlob(8));
                textViewCourseName.setText(course.getCourseName());
                textViewCourseDescription.setText(course.toString());
            }
        }

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });

        return rootView;
    }
}