package com.example.finalproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {


    Context context;
    List<Section> items;
    private SelectListener listener;

    public MyAdapter(Context context, List<Section> items,SelectListener listener) {
        this.context = context;
        this.items = items;
        this.listener=listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.course_item_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull  MyViewHolder holder, int position) {

        holder.startDate.setText(items.get(position).getStartDate());
        holder.startTime.setText(items.get(position).getStartTime());
        holder.endTime.setText(items.get(position).getEndTime());
        holder.room.setText(items.get(position).getRoom());
        holder.course_id.setText(String.valueOf(items.get(position).getCourseID()));

        DataBaseHelper dataBaseHelper = new DataBaseHelper(context.getApplicationContext(), "TRAINING_CENTER", null, 1);

        String CourseName = dataBaseHelper.getCourseName(items.get(position).getCourseID());
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

        byte[] image = dataBaseHelper.getCourseImage(items.get(position).getCourseID());

        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        holder.imageView.setImageBitmap(bitmap);
        holder.courseName.setText(CourseName);
        holder.maxTrainees.setText(String.valueOf(items.get(position).getMaxTrainees()));
        holder.days.setText(items.get(position).getDays());
        holder.endDate.setText(items.get(position).getEndDate());

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
