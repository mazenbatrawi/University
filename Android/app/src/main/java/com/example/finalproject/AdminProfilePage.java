package com.example.finalproject;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminProfilePage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminProfilePage extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AdminProfilePage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminProfilePage.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminProfilePage newInstance(String param1, String param2) {
        AdminProfilePage fragment = new AdminProfilePage();
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
        View view = inflater.inflate(R.layout.fragment_admin_profile_page, container, false);
        Bundle bundle = getArguments();
        assert bundle != null;
        String email = bundle.getString("EMAIL", "ERROR");

        ImageView profilePhoto = view.findViewById(R.id.profile_photo);
        TextView full_name_text = view.findViewById(R.id.full_name_text);
        TextView email_text = view.findViewById(R.id.email_text);
        TextView password_text = view.findViewById(R.id.password_text);
        TextView edit_profile_button = view.findViewById(R.id.edit_profile_button);
        ImageButton password_toggle_button = view.findViewById(R.id.password_toggle_button);
        final boolean[] password_visible = {false};

        DataBaseHelper dataBaseHelper = new DataBaseHelper(
                getContext(),"TRAINING_CENTER",null,1);
        Cursor cursor = dataBaseHelper.getAdmin(email);
        assert cursor.getCount() == 1;
        cursor.moveToFirst();
        String full_name = cursor.getString(2) + " " + cursor.getString(3);
        String password = cursor.getString(1);

        byte[] array = cursor.getBlob(4);
        Bitmap bitmap = BitmapFactory.decodeByteArray(array, 0, array.length);
        profilePhoto.setImageBitmap(bitmap);

        full_name_text.setText(full_name);
        email_text.setText(email);
        String current_password = password;
        for(int i = 0; i < password.length(); i++)
            current_password = current_password.replace(password.charAt(i), '*');
        password_text.setText(current_password);

        String finalCurrent_password = current_password;

        password_toggle_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(password_visible[0]) {
                    password_text.setText(finalCurrent_password);
                    password_visible[0] = false;
                }
                else {
                    password_text.setText(password);
                    password_visible[0] = true;
                }
            }
        });

        edit_profile_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("EMAIL", email);
                EditProfilePage editProfilePage = new EditProfilePage();
                editProfilePage.setArguments(bundle);
                replaceFragment(editProfilePage);
            }
        });

        return view;
    }

    private void replaceFragment(Fragment newFragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView, newFragment);
        fragmentTransaction.commit();
    }
}