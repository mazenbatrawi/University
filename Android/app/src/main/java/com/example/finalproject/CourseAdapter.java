package com.example.finalproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {
    private List<Course> courseList;
    private String email;

    public CourseAdapter(List<Course> courseList, String Temail) {
        this.courseList = courseList;
        this.email = Temail;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_in_trainee, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Course course = courseList.get(position);
        holder.bind(course);
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public void filterList(List<Course> filteredCourseList) {
        courseList = filteredCourseList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView courseNameTextView;
        private TextView courseDescriptionTextView;
        private Button buttonView;
        private Button buttonRegister;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            courseNameTextView = itemView.findViewById(R.id.textViewCourseName);
            courseDescriptionTextView = itemView.findViewById(R.id.textViewCourseDescription);
            buttonView = itemView.findViewById(R.id.buttonView);
            buttonRegister = itemView.findViewById(R.id.buttonRegister);

            buttonView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Course course = courseList.get(position);
                        Ccourse_desc_Frag_Trainee fragment = new Ccourse_desc_Frag_Trainee();

                        Bundle args = new Bundle();
                        args.putInt("courseId", course.getID());
                        args.putString("email", email);
                        fragment.setArguments(args);

                        FragmentManager fragmentManager = ((FragmentActivity) itemView.getContext()).getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                        Fragment currentFragment = fragmentManager.findFragmentById(R.id.FirstFragment);
                        if(currentFragment != null){
                            fragmentTransaction.replace(R.id.content_frame, fragment);
                        }
                        else{
                            fragmentTransaction.add(R.id.content_frame, fragment);
                        }
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                }
            });

            buttonRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Course course = courseList.get(position);//fix to get id

                        Trainee_Course_Registration fragment = new Trainee_Course_Registration();

                        FragmentManager fragmentManager = ((FragmentActivity) itemView.getContext()).getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                        Bundle args = new Bundle();
                        args.putInt("courseId", course.getID());
                        args.putString("email", email);
                        fragment.setArguments(args);

                        Fragment currentFragment = fragmentManager.findFragmentById(R.id.FirstFragment);
                        if (currentFragment != null) {
                            fragmentTransaction.replace(R.id.content_frame, fragment);
                        } else {
                            fragmentTransaction.add(R.id.content_frame, fragment);
                        }

                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();

                    }
                }
            });
        }

        public void bind(Course course) {
            courseNameTextView.setText(course.getCourseName());
        }
    }
}
