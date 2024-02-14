package com.example.finalproject;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.invoke.ConstantCallSite;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditCourses#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditCourses extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditCourses() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditCourses.
     */
    // TODO: Rename and change types and number of parameters
    public static EditCourses newInstance(String param1, String param2) {
        EditCourses fragment = new EditCourses();
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_courses, container, false);
        LinearLayout layout = view.findViewById(R.id.layout);

        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);

        DataBaseHelper dataBaseHelper = new DataBaseHelper(getContext(), "TRAINING_CENTER", null, 1);

        Cursor cursor = dataBaseHelper.getAllCourses();
        while (cursor.moveToNext()) {
            CardView cardView = new CardView(getContext());
            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            cardParams.setMargins(0, 0, 0, 16);
            cardView.setLayoutParams(cardParams);
            cardView.setCardElevation(8);
            cardView.setRadius(16);

            LinearLayout innerLayout = new LinearLayout(getContext());
            innerLayout.setOrientation(LinearLayout.VERTICAL);
            innerLayout.setPadding(16, 16, 16, 16); // Add padding inside the card
            innerLayout.setGravity(Gravity.CENTER_HORIZONTAL);

            byte[] array = cursor.getBlob(8);
            Bitmap bitmap = BitmapFactory.decodeByteArray(array, 0, array.length);
            ImageView imageView = new ImageView(getContext());
            imageView.setImageBitmap(bitmap);
            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200);
            imageView.setLayoutParams(imageParams);
            innerLayout.addView(imageView);

            TextView textView = new TextView(getContext());
            textView.setText(cursor.getString(2));
            textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            textView.setTextSize(18);
            innerLayout.addView(textView);

            cardView.addView(innerLayout);
            layout.addView(cardView);

            int id = cursor.getInt(0);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putString("ID", String.valueOf(id));
                    bundle.putString("COURSE_NAME", textView.getText().toString());

                    Fragment fragment = new DisplaySection();
                    fragment.setArguments(bundle);
                    replaceFragment(fragment);
                }
            });
        }

        return view;
    }

    private void replaceFragment(Fragment newFragment){
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView, newFragment);
        fragmentTransaction.commit();
    }


}