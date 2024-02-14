package com.example.finalproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MyStudentsAdapter extends RecyclerView.Adapter<MyViewHolderStudents> {


    Context context;
    List<TraineeInSection> items;
    private SelectListenerForStudents listener;

    public MyStudentsAdapter(Context context, List<TraineeInSection> items, SelectListenerForStudents listener) {
        this.context = context;
        this.items = items;
        this.listener=listener;
    }

    @NonNull
    @Override
    public MyViewHolderStudents onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        return new MyViewHolderStudents(LayoutInflater.from(context).inflate(R.layout.student_item_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolderStudents holder, int position) {
        holder.email.setText(items.get(position).getEmail());
        holder.name.setText(items.get(position).getName());
        holder.mobileNumber.setText(items.get(position).getMobileNumber());
        holder.address.setText(items.get(position).getAddress());
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context.getApplicationContext(), "TRAINING_CENTER", null, 1);
//        byte[] image2 = dataBaseHelper.getStudentImage(items.get(position).getEmail());
        byte[] image = dataBaseHelper.getStudentImage(items.get(position).getEmail());

        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        holder.imageView.setImageBitmap(bitmap);
//
//        String CourseName = dataBaseHelper.getCourseName(items.get(position).getCourseID());
////        String CourseName = Course.getString(2);
////        System.out.println("here iiss the returned naem:   " + Course);
////        System.out.println("here iiss the returned naem:   " + CourseName);
//        if (Course.moveToFirst()) {
//            do {
//                String name = Course.getString(2);
//                System.out.println("here iiss the returned naem:   " + name);
//
//
//            } while (Course.moveToNext());
//        }


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 listener.onItemClicked(items.get(position));

            }
        });
//        holder.imageView.setImageResource(items.get(position).getImage());



    }




    @Override
    public int getItemCount() {
        return items.size();
    }
}
