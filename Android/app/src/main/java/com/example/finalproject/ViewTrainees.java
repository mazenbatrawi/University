package com.example.finalproject;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewTrainees#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewTrainees extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ViewTrainees() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewTrainees.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewTrainees newInstance(String param1, String param2) {
        ViewTrainees fragment = new ViewTrainees();
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
        View view = inflater.inflate(R.layout.fragment_view_trainees, container, false);
        LinearLayout layout = view.findViewById(R.id.layout);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);

        DataBaseHelper dataBaseHelper = new DataBaseHelper(getContext(), "TRAINING_CENTER", null,1);
        Cursor cursor = dataBaseHelper.getAllTrainees();

        while (cursor.moveToNext()) {
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

            Trainee trainee = new Trainee(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getBlob(6));

            LinearLayout layout1 = new LinearLayout(getContext());
            layout1.setOrientation(LinearLayout.VERTICAL);
            layout1.setGravity(Gravity.CENTER_HORIZONTAL);
            layout1.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            byte[] array = trainee.getImage();
            Bitmap bitmap = BitmapFactory.decodeByteArray(array, 0, array.length);
            ImageView imageView = new ImageView(getContext());
            imageView.setImageResource(R.drawable.profile_photo_placeholder);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(600, 600));
            imageView.setImageBitmap(bitmap);

            String s = trainee.toString();
            TextView textView1 = new TextView(getContext());
            textView1.setText(s);
            textView1.setTextColor(Color.WHITE);
            textView1.setTextSize(20);
            textView1.setGravity(Gravity.CENTER_HORIZONTAL);

            layout1.addView(imageView);
            layout1.addView(textView1);
            cardView.addView(layout1);
            layout.addView(cardView);
            TextView textView = new TextView(getContext());
            textView.setText("\n\n");
            layout.addView(textView);
        }
        return view;
    }
}