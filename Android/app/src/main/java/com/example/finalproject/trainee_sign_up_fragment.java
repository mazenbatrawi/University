package com.example.finalproject;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class trainee_sign_up_fragment extends Fragment {
    private EditText mobileNo,address,email,first_name,last_name,password,confirm_password;
    boolean valid_email,valid_last_name,valid_first_name,valid_confirm_password,valid_password,valid_address,valid_mobile_no;
    private static final String TOAST_TEXT = "check the wrong fields";
    Uri selectedImageUri;
    private static final int REQUEST_CODE_IMAGE_PICKER = 100;
    private ImageView imageView;
    private Button chooseImageButton;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.trainee_sign_up_fragment, container, false);

        imageView = view.findViewById(R.id.imageView);
        chooseImageButton = view.findViewById(R.id.chooseImageButton);

        chooseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        email = view.findViewById(R.id.Course_startDate);
        first_name = view.findViewById(R.id.edit_first_name_text);
        last_name = view.findViewById(R.id.edit_last_name_text);
        password = view.findViewById(R.id.edit_password_text);
        confirm_password =view.findViewById(R.id.confirm_password);
        mobileNo = view.findViewById(R.id.edit_mobile_text);
        address = view.findViewById(R.id.edit_address_text);


        email.setShowSoftInputOnFocus(true);
        first_name.setShowSoftInputOnFocus(true);
        last_name.setShowSoftInputOnFocus(true);
        password.setShowSoftInputOnFocus(true);
        confirm_password.setShowSoftInputOnFocus(true);
        address.setShowSoftInputOnFocus(true);

        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        confirm_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        email.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        first_name.setInputType(InputType.TYPE_CLASS_TEXT);
        last_name.setInputType(InputType.TYPE_CLASS_TEXT);
        mobileNo.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_PHONE);
        address.setInputType(InputType.TYPE_CLASS_TEXT);

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
                // Validate character length
                String email_string = s.toString();
                if (!Patterns.EMAIL_ADDRESS.matcher(email_string).matches()) {
                    email.setError("Please enter a valid email address");
                    valid_email = false;
                } else {
                    valid_email = true;
                    email.setError(null);
                }
            }
        });
        first_name.addTextChangedListener(new TextWatcher() {
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
                // Validate character length
                boolean containsNumbers = containsNumbers(s.toString());

                if (containsNumbers) {
                    first_name.setError("First name can't contain numbers");
                    valid_first_name = false;
                }
                else if (s.length() < 3 || s.length() > 20) {
                    first_name.setError("First Name length must be between 3 and 20");
                    valid_first_name = false;
                } else {
                    first_name.setError(null);
                    valid_first_name = true;
                }
            }
        });
        last_name.addTextChangedListener(new TextWatcher() {
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
                // Validate character length
                boolean containsNumbers = containsNumbers(s.toString());

                if (containsNumbers) {
                    last_name.setError("Last name can't contain numbers");
                    valid_last_name = false;
                }
                else if (s.length() < 3 || s.length() > 20) {
                    last_name.setError("Last Name length must be between 3 and 20");
                    valid_last_name = false;
                } else {
                    last_name.setError(null);
                    valid_last_name = true;
                }
            }
        });
        password.addTextChangedListener(new TextWatcher() {
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
                // Validate character length
                boolean containsNumbers = containsNumbers(s.toString());
                boolean contains_lower = containsLowercase(s.toString());
                boolean contains_upper = containsUppercase(s.toString());
                if(!containsNumbers || !contains_lower ||!contains_upper ){
                    password.setError("Password must contain at least one \n" +
                            "number, one lowercase letter, and one uppercase letter");
                    valid_password = false;
                }else if (s.length() < 8 || s.length() > 15) {
                    password.setError("Password length must be between 8 and 15");
                    valid_password = false;
                } else {
                    password.setError(null);
                    valid_password = true;
                }
            }
        });
        confirm_password.addTextChangedListener(new TextWatcher() {
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
                // Validate character length
                if (!(password.getText().toString().equals(confirm_password.getText().toString()))) {
                    confirm_password.setError("the confirm password field is different");
                    valid_confirm_password = false;
                } else {
                    confirm_password.setError(null);
                    valid_confirm_password = true;
                }
            }
        });
        mobileNo.addTextChangedListener(new TextWatcher() {
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
                // Validate character length
                String mobile = s.toString();
                if (!isValidMobileNO(mobile)) {
                    mobileNo.setError("Please enter a valid mobile Number");
                    valid_mobile_no = false;
                } else {
                    valid_mobile_no = true;
                    email.setError(null);
                }
            }
        });

        Button sign_up_button = view.findViewById(R.id.sign_up2);

        sign_up_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


//                                    String databaseName = "TRAINING_CENTER";
//                    getContext().deleteDatabase(databaseName);

                String email_string = email.getText().toString();
                String first_name_string = first_name.getText().toString();
                String last_name_string = last_name.getText().toString();
                String password_string = password.getText().toString();
                String confirm_password_string = confirm_password.getText().toString();
                String mobile_string = mobileNo.getText().toString();
                String address_string = address.getText().toString();
                if(!address_string.equals("")){
                    valid_address=true;
                }

                if(valid_address&&valid_mobile_no && valid_email && valid_confirm_password && valid_first_name && valid_last_name && valid_password){
                    System.out.println(email_string);
                    System.out.println(first_name_string);
                    System.out.println(last_name_string);
                    System.out.println(password_string);
                    System.out.println(confirm_password_string);
                    System.out.println(selectedImageUri);
                    System.out.println(mobile_string);
                    System.out.println(address_string);

                    Trainee trainee = new Trainee();
                    trainee.setEmail(email_string);
                    trainee.setFirstName(first_name_string);
                    trainee.setLastName(last_name_string);
                    trainee.setPassword(password_string);
                    trainee.setImage(new ImageHandler().getByteArray(imageView));

                    trainee.setMobileNumber(mobile_string);
                    trainee.setAddress(address_string);
                    DataBaseHelper dataBaseHelper = new DataBaseHelper(
                            getContext(),"TRAINING_CENTER",null,1);


                    boolean validSignUp = dataBaseHelper.vaildSignUp(email_string);
                    if(validSignUp){
                        dataBaseHelper.insertTrainee(trainee);
                        email.getText().clear();
                        first_name.getText().clear();
                        last_name.getText().clear();
                        password.getText().clear();
                        confirm_password.getText().clear();
                        mobileNo.getText().clear();
                        address.getText().clear();

                        Toast.makeText(getContext(),"The User Inserted Successfully" , Toast.LENGTH_LONG).show();




                    }else{
                        Toast.makeText(getContext(),"the email is used" , Toast.LENGTH_SHORT).show();
                    }
                    Cursor res2 = dataBaseHelper.getAllTrainee();
                    System.out.println("TRAINEES:");
                    while (res2.moveToNext()){

                        System.out.println(
                                "Email= "+res2.getString(0)
                                        +"\nPassword= "+res2.getString(1)
                                        +"\nFirstName= "+res2.getString(2)
                                        +"\nLastName= "+res2.getString(3)
                                        +"\nPersonalPhoto= "+res2.getBlob(4)
                                        +"\n\n"
                        );
                    }






                }else{
                    Toast.makeText(getContext(),TOAST_TEXT , Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;
    }
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_IMAGE_PICKER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_IMAGE_PICKER && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            imageView.setImageURI(selectedImageUri);
        }
    }

    public boolean containsNumbers(String s) {
        for (char curr : s.toCharArray()) {
            if (Character.isDigit(curr)) {
                return true;
            }
        }
        return false;
    }
    public boolean containsUppercase(String s) {
        for (char curr : s.toCharArray()) {
            if (Character.isUpperCase(curr)) {
                return true;
            }
        }
        return false;
    }
    public boolean containsLowercase(String s) {
        for (char curr : s.toCharArray()) {
            if (Character.isLowerCase(curr)) {
                return true;
            }
        }
        return false;
    }
    public boolean isValidMobileNO(String mobile_No) {
        String regex = "^[+]?[0-9]{10,13}$";
        return mobile_No.matches(regex);
    }

}

