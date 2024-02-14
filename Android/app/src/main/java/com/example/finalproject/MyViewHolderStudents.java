package com.example.finalproject;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolderStudents extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView name, email,mobileNumber, address;
    CardView cardView;


    public MyViewHolderStudents(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageView6);
        name = itemView.findViewById(R.id.Student_Name);
        email = itemView.findViewById(R.id.Student_Email);
        mobileNumber = itemView.findViewById(R.id.Student_mobile);
        address= itemView.findViewById(R.id.Student_Address);
        cardView = itemView.findViewById(R.id.cardView);

    }


}