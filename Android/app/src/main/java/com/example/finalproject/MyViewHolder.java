package com.example.finalproject;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView course_id,courseName, maxTrainees,startTime, endTime,days, room,startDate, endDate;
    CardView cardView;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageview);
        course_id = itemView.findViewById(R.id.Course_ID2);
        courseName = itemView.findViewById(R.id.Course_Name);
        maxTrainees = itemView.findViewById(R.id.Section_max_trainees);
        startTime = itemView.findViewById(R.id.Section_Start_Time);
        endTime = itemView.findViewById(R.id.Section_End_Time);
        days = itemView.findViewById(R.id.Section_days);
        room = itemView.findViewById(R.id.Section_room);
        startDate = itemView.findViewById(R.id.Section_startDate);
        endDate = itemView.findViewById(R.id.Section_endtDate);
        cardView = itemView.findViewById(R.id.cardView);
    }


}