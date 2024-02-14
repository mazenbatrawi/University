package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


public class Login extends AppCompatActivity {

    private EditText email,password;
    CheckBox rememberMe;
    boolean valid_email,valid_password;
    SharedPreferences sharedPreferences ;
//    String email_Shared_value = sharedPreferences.getString("email", "");
//    String password_Shared_value = sharedPreferences.getString("password", "");
//        email.setText(email_Shared_value);
//        password.setText(password_Shared_value);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Button back_button = findViewById(R.id.back_button);
        Drawable icon = getResources().getDrawable(R.drawable.ic_baseline_arrow_back_24);
        back_button.setCompoundDrawablesRelativeWithIntrinsicBounds(icon, null, null, null);
        back_button.setPaddingRelative(20,0,0,0);
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        String email_Shared_value = sharedPreferences.getString("email", "");
        String password_Shared_value = sharedPreferences.getString("password", "");


        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        email = findViewById(R.id.edit_first_name_text);
        password = findViewById(R.id.login_password);
        rememberMe = findViewById(R.id.remember_user);
        Button login = findViewById(R.id.login_page_button);
        email.setShowSoftInputOnFocus(true);
        password.setShowSoftInputOnFocus(true);
        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        email.setText(email_Shared_value);
        password.setText(password_Shared_value);


        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No action needed
            }

            @Override
            public void afterTextChanged(Editable s) {
                String email_string = s.toString();
                if (!Patterns.EMAIL_ADDRESS.matcher(email_string).matches()) {
                    email.setError("Please enter a valid email address");
                    valid_email = false;
                }
                else {
                    valid_email = true;
                    email.setError(null);
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String entered_email = email.getText().toString();
                String entered_password = password.getText().toString();
                boolean remember_me_check_box = rememberMe.isChecked();

                DataBaseHelper dataBaseHelper = new DataBaseHelper(
                        Login.this,"TRAINING_CENTER",null,1);

                String isValid = dataBaseHelper.getLoginPassword(entered_email,entered_password);
                if(isValid.equals("ADMIN")){
                    if(remember_me_check_box){

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("email", entered_email);
                        editor.putString("password", entered_password);
                        editor.apply();

                    }
                    Intent intent = new Intent(Login.this, AdminPage.class);
                    intent.putExtra("EMAIL", entered_email);
                    startActivity(intent);
                    finish();

                }
                else if(isValid.equals("INSTRUCTOR")){
                    Intent intent = new Intent(Login.this, Instructor_Activity.class);

                    Cursor user_data = dataBaseHelper.getInstructorData(entered_email);
                    String user_email ="";
                    String user_password="" ;
                    String user_first_name="";
                    String user_last_name="";
                    byte[] user_personal_photo = null;
                    String user_mobile="" ;
                    String user_address="";
                    String user_specialization="";
                    String user_degree="";
                    String user_canTeach="";


                    if (user_data.moveToFirst()) {
                        do {
                            user_email = user_data.getString(0);
                            user_password = user_data.getString(1);
                            user_first_name = user_data.getString(2);
                            user_last_name = user_data.getString(3);
                            user_mobile = user_data.getString(4);
                            user_address = user_data.getString(5);
                            user_specialization = user_data.getString(6);
                            user_degree = user_data.getString(7);
                            user_canTeach = user_data.getString(8);
                            user_personal_photo = user_data.getBlob(9);

                        } while (user_data.moveToNext());
                    }
                    intent.putExtra("EMAIL", user_email);

                    if(remember_me_check_box){

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("email", entered_email);
                        editor.putString("password", entered_password);
                        editor.apply();

                    }
                    startActivity(intent);
                    finish();
                }
                else if(isValid.equals("TRAINEE")){
                    if(remember_me_check_box){

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("email", entered_email);
                        editor.putString("password", entered_password);
                        editor.apply();

                    }
                    Intent intent = new Intent(Login.this, TraineeActivites.class);
                    intent.putExtra("email", entered_email);
                    startActivity(intent);
                    finish();
                }
                else{
                    System.out.println("Wrong email or password");
                    Toast.makeText(Login.this,"Wrong email or password" , Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
