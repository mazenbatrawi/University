package com.example.finalproject;

import static java.security.AccessController.getContext;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.SQLOutput;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.TreeSet;

//course adapter for course registration
public class Trainee_Reg_withraw extends RecyclerView.Adapter<Trainee_Reg_withraw.ViewHolder>
{
    private ArrayList<Section> SectionList;
    private String Temail;
    private ArrayList<String> preReqList;
    private ArrayList<String> pre;

    public Trainee_Reg_withraw(ArrayList<Section> SecList, String email) {
        this.SectionList = SecList;
        this.Temail = email;
    }

    public Trainee_Reg_withraw() {

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trainee_for_course, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Section section = SectionList.get(position);
        holder.bind(section);

        DataBaseHelper dataBaseHelper = new DataBaseHelper(holder.itemView.getContext(), "TRAINING_CENTER",null,1);
        Cursor cursor = dataBaseHelper.checkExistence(Temail, String.valueOf(section.getSectionID()));
        int count = dataBaseHelper.getTraineeCount(String.valueOf(section.getSectionID()));

        if(cursor.getCount() == 0) {
            holder.RegisterButton.setText("Register");

        } else {
            cursor.moveToFirst();
            holder.RegisterButton.setText("Withdraw");
        }

    }

    @Override
    public int getItemCount() {
        return SectionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView CourseNameTextView = itemView.findViewById(R.id.textViewCourseName);
        private TextView courseDescription = itemView.findViewById(R.id.textViewCourseTime);
        private Button RegisterButton = itemView.findViewById(R.id.buttonRegisterCourse);


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Trainee_Course_history history = new Trainee_Course_history();
            preReqList = new ArrayList<String>();
            pre = new ArrayList<String>();
                //max size of class, already registered, already waitlisted
                RegisterButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Section sec = SectionList.get(getAdapterPosition());

                        DataBaseHelper dataBaseHelper = new DataBaseHelper(v.getContext(),
                                "TRAINING_CENTER",null,1);

                        Cursor cursor = dataBaseHelper.checkExistence(Temail, String.valueOf(sec.getSectionID()));
                        //from section we got the pre of the course in question
                        pre = dataBaseHelper.checkPrerquesite(sec.getSectionID());
                        preReqList = dataBaseHelper.getPreTrainee(TraineeActivites.getEmail());
                       // history.getHistoryOfcourse(dataBaseHelper, preReqList, false);//Check tmw


                        if (pre != null && preReqList != null)  {
                            for (String item : pre) {
                                if (!preReqList.contains(item)) {
                                    Toast.makeText(v.getContext(), "You have not completed the prerequisites", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                        }

                        int count = dataBaseHelper.getTraineeCount(String.valueOf(sec.getSectionID()));
                        if(cursor.getCount() == 0) {
                            if (count < sec.getMaxTrainees()){

                                if(checkConflicts(Temail, String.valueOf(sec.getSectionID()))){
                                    Toast.makeText(v.getContext(), "You have a time conflict", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                TraineeToSection t1 = new TraineeToSection(-1, sec.getSectionID() ,Temail, 0);
                                dataBaseHelper.insertTraineeSection(t1);
                                Toast.makeText(v.getContext(), "You have been registered for the course", Toast.LENGTH_SHORT).show();
                                RegisterButton.setText("Withdraw");
                            }
                            else{
                                Toast.makeText(v.getContext(), "The course is full right now", Toast.LENGTH_SHORT).show();
                                return;
                            }

                        } else {
                            System.out.println("In else");
                            cursor.moveToFirst();
                            dataBaseHelper.deleteTraineeFromSection(Temail, String.valueOf(sec.getSectionID()));
                            Toast.makeText(v.getContext(), "You have been removed from the course", Toast.LENGTH_SHORT).show();
                            RegisterButton.setText("Register");
                        }
                    }
                });


        }

        public void bind(Section sections) {
            CourseNameTextView.setText(sections.toString());
        }

        public boolean checkConflicts(String email, String sectionID){
            DataBaseHelper dataBaseHelper = new DataBaseHelper(itemView.getContext(), "TRAINING_CENTER",null,1);
            Cursor cursor = dataBaseHelper.getSection(sectionID);
            assert cursor.getCount() == 1;
            cursor.moveToFirst();
            Section section = new Section(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3),
                    cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7),
                    cursor.getString(8), cursor.getString(9));

            int courseID = section.getCourseID();
            Cursor c = dataBaseHelper.getCourse(String.valueOf(courseID));
            assert c.getCount() == 1;
            c.moveToFirst();
            Course course = new Course(c.getInt(0), c.getString(1), c.getString(2), c.getString(3),
                    c.getString(4), c.getString(5), c.getString(6), c.getString(7), c.getBlob(8));

            Cursor cursor1 = dataBaseHelper.getAllSectionsOfCourse(String.valueOf(courseID));
            while(cursor1.moveToNext()){
                Section section1 = new Section(cursor1.getInt(0), cursor1.getString(1), cursor1.getInt(2), cursor1.getInt(3),
                        cursor1.getString(4), cursor1.getString(5), cursor1.getString(6), cursor1.getString(7),
                        cursor1.getString(8), cursor1.getString(9));

                // check if the student is registered in this section
                Cursor cursor2 = dataBaseHelper.checkExistence(email, String.valueOf(section1.getSectionID()));
                if(cursor2.getCount() > 0){
                    cursor2.moveToFirst();
                    Toast.makeText(itemView.getContext(),
                            "You are already registered in this course in section " + cursor2.getInt(1), Toast.LENGTH_SHORT).show();
                    return true;
                }
            }

            // now we want to check if the student is registered in any other section that has a conflict with this section
            Cursor cursor3 = dataBaseHelper.getAllSectionsOfTrainee(email);
            while(cursor3.moveToNext()){
                TraineeToSection traineeToSection = new TraineeToSection(cursor3.getInt(0), cursor3.getInt(1),
                        cursor3.getString(2), cursor3.getInt(3));

                Cursor cursor4 = dataBaseHelper.getSection(String.valueOf(traineeToSection.getSectionID()));
                assert cursor4.getCount() == 1;
                cursor4.moveToFirst();
                Section section2 = new Section(cursor4.getInt(0), cursor4.getString(1), cursor4.getInt(2), cursor4.getInt(3),
                        cursor4.getString(4), cursor4.getString(5), cursor4.getString(6), cursor4.getString(7),
                        cursor4.getString(8), cursor4.getString(9));

                if(conflict(section, section2)){
                    Toast.makeText(itemView.getContext(),
                            "You are registered in a course that conflicts with this course", Toast.LENGTH_SHORT).show();
                    return true;
                }
            }

            // now we want to check that this user is registering in the registration period
            String registrationStart = course.getRegistrationStart();
            String registrationEnd = course.getRegistrationEnd();

            // split on " "
            String[] registrationStartArray = registrationStart.split(" ");
            String[] registrationEndArray = registrationEnd.split(" ");

            int startYear = Integer.parseInt(registrationStartArray[2]);
            int startMonth = getInvMonthFormat(registrationStartArray[0]);
            int startDay = Integer.parseInt(registrationStartArray[1]);

            int endYear = Integer.parseInt(registrationEndArray[2]);
            int endMonth = getInvMonthFormat(registrationEndArray[0]);
            int endDay = Integer.parseInt(registrationEndArray[1]);

            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
            int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

            if(!isDateInRange(startYear, startMonth, startDay, endYear, endMonth, endDay, currentYear, currentMonth, currentDay)){
                Toast.makeText(itemView.getContext(),
                        "You are not in the registration period for this course", Toast.LENGTH_SHORT).show();
                return true;
            }
            // check if the number of students in this section is full
            Cursor cursor5 = dataBaseHelper.getTraineesInSection(String.valueOf(section.getSectionID()));
            if(cursor5.getCount() >= section.getMaxTrainees()){
                Toast.makeText(itemView.getContext(),
                        "This section is full", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        }

        public boolean conflict(Section section, Section section2) {
            TreeSet<String> days1 = new TreeSet<>();
            TreeSet<String> days2 = new TreeSet<>();

            String sectionDays = section.getDays();
            String section2Days = section2.getDays();

            // Split by ", "
            String[] sectionDaysArray = sectionDays.split(", ");
            String[] section2DaysArray = section2Days.split(", ");

            // add them to their tree sets
            Collections.addAll(days1, sectionDaysArray);
            Collections.addAll(days2, section2DaysArray);

            // check if they have any common days
            for(String day : days1){
                if(days2.contains(day)){
                    // check if they have any common times
                    if(conflictTimes(section, section2)){
                        return true;
                    }
                }
            }
            return false;
        }

        public boolean conflictTimes(Section section, Section section2){
            String startTime1 = section.getStartTime();
            String startTime2 = section2.getStartTime();
            String endTime1 = section.getEndTime();
            String endTime2 = section2.getEndTime();

            // convert each one to minutes, split them to arrays on ":" and then convert to minutes
            String[] startTime1Array = startTime1.split(":");
            String[] startTime2Array = startTime2.split(":");
            String[] endTime1Array = endTime1.split(":");
            String[] endTime2Array = endTime2.split(":");

            int h1 = Integer.parseInt(startTime1Array[0]);
            int h2 = Integer.parseInt(startTime2Array[0]);
            int h3 = Integer.parseInt(endTime1Array[0]);
            int h4 = Integer.parseInt(endTime2Array[0]);

            int m1 = Integer.parseInt(startTime1Array[1]);
            int m2 = Integer.parseInt(startTime2Array[1]);
            int m3 = Integer.parseInt(endTime1Array[1]);
            int m4 = Integer.parseInt(endTime2Array[1]);

            int time1 = h1 * 60 + m1;
            int time2 = h2 * 60 + m2;
            int time3 = h3 * 60 + m3;
            int time4 = h4 * 60 + m4;

            return intersect_time(time1, time3, time2, time4);

        }

        public boolean intersect_time(int l1, int r1, int l2, int r2){
            return r1 >= l2 && l1 <= r2;
        }

        public int getInvMonthFormat(String month){
            if(month.equals("JAN")) return 1;
            if(month.equals("FEB")) return 2;
            if(month.equals("MAR")) return 3;
            if(month.equals("APR")) return 4;
            if(month.equals("MAY")) return 5;
            if(month.equals("JUN")) return 6;
            if(month.equals("JUL")) return 7;
            if(month.equals("AUG")) return 8;
            if(month.equals("SEP")) return 9;
            if(month.equals("OCT")) return 10;
            if(month.equals("NOV")) return 11;
            if(month.equals("DEC")) return 12;
            return -1;
        }

        public boolean isDateInRange(int startYear, int startMonth, int startDay, int endYear, int endMonth, int endDay, int checkYear, int checkMonth, int checkDay) {
            checkMonth++;
            System.out.println("startYear: " + startYear + " startMonth: " + startMonth + " startDay: " + startDay);
            System.out.println("endYear: " + endYear + " endMonth: " + endMonth + " endDay: " + endDay);
            System.out.println("checkYear: " + checkYear + " checkMonth: " + checkMonth + " checkDay: " + checkDay);
            if (checkYear < startYear || checkYear > endYear) {
                return false;
            } else if (checkYear == startYear && checkMonth < startMonth) {
                return false;
            } else if (checkYear == endYear && checkMonth > endMonth) {
                return false;
            } else if (checkYear == startYear && checkMonth == startMonth && checkDay < startDay) {
                return false;
            } else return !(checkYear == endYear && checkMonth == endMonth && checkDay > endDay);
        }
    }
}
