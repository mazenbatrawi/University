package com.example.finalproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewOfferingsHistory#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewOfferingsHistory extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ViewOfferingsHistory() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewOfferingsHistory.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewOfferingsHistory newInstance(String param1, String param2) {
        ViewOfferingsHistory fragment = new ViewOfferingsHistory();
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
        View view = inflater.inflate(R.layout.fragment_view_offerings_history, container, false);
        Button button = view.findViewById(R.id.button);
        LinearLayout linearLayout = view.findViewById(R.id.layout);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataBaseHelper dataBaseHelper = new DataBaseHelper(
                        getContext(),"TRAINING_CENTER",null,1);

                Cursor cursor = dataBaseHelper.getAllCourses();
                int sz = cursor.getCount();
                String[] items = new String[sz];
                int[] id = new int[sz];
                int i = 0;
                while(cursor.moveToNext()){
                    items[i] = cursor.getString(2);
                    id[i] = cursor.getInt(0);
                    i++;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Select Course");
                builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        button.setText(items[i]);
                        linearLayout.removeAllViews();

                        Cursor cursor1 = dataBaseHelper.getAllCourses();
                        Course course = new Course();
                        while(cursor1.moveToNext()){
                            if(cursor1.getInt(0) == id[i]){
                                course.setID(cursor1.getInt(0));
                                course.setCourseID(cursor1.getString(1));
                                course.setCourseName(cursor1.getString(2));
                                course.setPrerequisites(cursor1.getString(3));
                                course.setStartDate(cursor1.getString(4));
                                course.setEndDate(cursor1.getString(5));
                                course.setRegistrationStart(cursor1.getString(6));
                                course.setRegistrationEnd(cursor1.getString(7));
                                course.setImage(cursor1.getBlob(8));
                                break;
                            }
                        }

                        String s = course.toString();
                        s = "\n\n" + s;
                        TextView textView = new TextView(getContext());
                        textView.setText(s);
                        textView.setTextSize(20);
                        textView.setTextColor(Color.WHITE);
                        textView.setGravity(Gravity.CENTER_HORIZONTAL);

                        Bitmap bitmap = BitmapFactory.decodeByteArray(course.getImage(), 0, course.getImage().length);
                        ImageView imageView = new ImageView(getContext());
                        imageView.setImageResource(R.drawable.profile_photo_placeholder);
                        imageView.setLayoutParams(new LinearLayout.LayoutParams(600, 600));
                        imageView.setImageBitmap(bitmap);
                        linearLayout.addView(imageView);
                        linearLayout.addView(textView);

                        TextView textView1 = new TextView(getContext());
                        textView1.setText("Offering History");
                        textView1.setTextSize(30);
                        textView1.setTypeface(null, Typeface.BOLD);
                        textView1.setTextColor(Color.WHITE);
                        textView1.setGravity(Gravity.CENTER_HORIZONTAL);
                        linearLayout.addView(textView1);

                        Cursor cursor2 = dataBaseHelper.getAllOfferings(String.valueOf(id[i]));
                        while (cursor2.moveToNext()){
                            Section section = new Section();

                            section.setSectionID(cursor2.getInt(0));
                            section.setInstructorEmail(cursor2.getString(1));
                            section.setCourseID(cursor2.getInt(2));
                            section.setMaxTrainees(cursor2.getInt(3));
                            section.setStartTime(cursor2.getString(4));
                            section.setEndTime(cursor2.getString(5));
                            section.setDays(cursor2.getString(6));
                            section.setRoom(cursor2.getString(7));
                            section.setStartDate(cursor2.getString(8));
                            section.setEndDate(cursor2.getString(9));

                            String s1 = section.toString();
                            TextView textView2 = new TextView(getContext());
                            textView2.setText(s1);
                            textView2.setTextSize(20);
                            textView2.setTextColor(Color.WHITE);
                            textView2.setGravity(Gravity.CENTER_HORIZONTAL);

                            CardView cardView = new CardView(getContext());
                            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                            );
                            cardParams.setMargins(0, 0, 0, 16); // Set spacing between card views
                            cardView.setLayoutParams(cardParams);
                            cardView.setRadius(8);
                            cardView.setCardElevation(4);
                            cardView.setUseCompatPadding(true);

                            cardView.addView(textView2);
                            linearLayout.addView(cardView);
                            // add \n\n\n
                            TextView textView3 = new TextView(getContext());
                            textView3.setText("\n\n\n");
                            linearLayout.addView(textView3);

                        }
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
        return view;
    }
}