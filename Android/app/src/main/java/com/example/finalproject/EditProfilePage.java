package com.example.finalproject;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditProfilePage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditProfilePage extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private final int GALLERY_REQ_CODE = 1000;

    private ImageView profilePhoto;

    public EditProfilePage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditProfilePage.
     */
    // TODO: Rename and change types and number of parameters
    public static EditProfilePage newInstance(String param1, String param2) {
        EditProfilePage fragment = new EditProfilePage();
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
        View view = inflater.inflate(R.layout.fragment_edit_profile_page, container, false);

        profilePhoto = view.findViewById(R.id.profile_photo);
        EditText firstName = view.findViewById(R.id.first_name_edittext);
        EditText lastName = view.findViewById(R.id.last_name_edittext);
        EditText email = view.findViewById(R.id.email_edittext);
        EditText password = view.findViewById(R.id.password_edittext);
        EditText confirmPassword = view.findViewById(R.id.confirm_password_edittext);
        Button changePhoto = view.findViewById(R.id.change_photo_button);
        Button save = view.findViewById(R.id.save_button);

        Bundle bundle = getArguments();
        assert bundle != null;
        String origin = bundle.getString("EMAIL", "ERROR");

        DataBaseHelper dataBaseHelper = new DataBaseHelper(
                getContext(), "TRAINING_CENTER", null, 1);
        Cursor cursor = dataBaseHelper.getAdmin(origin);
        assert cursor.getCount() == 1;
        cursor.moveToFirst();
        email.setText(cursor.getString(0));
        password.setText(cursor.getString(1));
        firstName.setText(cursor.getString(2));
        lastName.setText(cursor.getString(3));
        confirmPassword.setText(cursor.getString(1));

        byte[] array = cursor.getBlob(4);
        Bitmap bitmap = BitmapFactory.decodeByteArray(array, 0, array.length);
        profilePhoto.setImageBitmap(bitmap);

        changePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, GALLERY_REQ_CODE);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Email cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Password cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(firstName.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "First name cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(firstName.getText().toString().length() < 3 || firstName.getText().toString().length() > 20) {
                    Toast.makeText(getContext(), "First name must be between 3 and 20 characters", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(lastName.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Last name cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(lastName.getText().toString().length() < 3 || lastName.getText().toString().length() > 20) {
                    Toast.makeText(getContext(), "Last name must be between 3 and 20 characters", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(confirmPassword.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Confirm password cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!password.getText().toString().equals(confirmPassword.getText().toString())) {
                    Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!password.getText().toString().matches(".*[A-Z].*")) {
                    Toast.makeText(getContext(), "Password must contain at least one upper case letter", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!password.getText().toString().matches(".*[a-z].*")) {
                    Toast.makeText(getContext(), "Password must contain at least one lower case letter", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!password.getText().toString().matches(".*[0-9].*")) {
                    Toast.makeText(getContext(), "Password must contain at least one number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password.getText().toString().length() < 8 || password.getText().toString().length() > 15) {
                    Toast.makeText(getContext(), "Password must be between 8 and 15 characters", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!email.getText().toString().equals(origin)) {
                    boolean ok = dataBaseHelper.getUsers(email.getText().toString());
                    if(!ok) {
                        Toast.makeText(getContext(), "Email already exists", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                dataBaseHelper.updateAdmin(origin, email.getText().toString(), password.getText().toString(),
                        firstName.getText().toString(), lastName.getText().toString(), new ImageHandler().getByteArray(profilePhoto));
                Toast.makeText(getContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getContext(), AdminPage.class);
                intent.putExtra("EMAIL", email.getText().toString());
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode == GALLERY_REQ_CODE){
                profilePhoto.setImageURI(data.getData());
            }
        }
    }
}