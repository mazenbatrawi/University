package com.example.finalproject;

import static com.example.finalproject.TraineeActivites.fragmentManagerTrainee;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Trainee_Profile_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Trainee_Profile_Fragment extends Fragment {

    private TextView textViewFirstName;
    private TextView textViewLastName;
    private TextView textViewEmail;
    private TextView textViewMobileNumber;
    private TextView textViewAddress;
    private Button buttonEdit;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Trainee_Profile_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Trainee_Profile_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Trainee_Profile_Fragment newInstance(String param1, String param2) {
        Trainee_Profile_Fragment fragment = new Trainee_Profile_Fragment();
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
        // Inflate the layout for this fragment
        DataBaseHelper dataBaseHelper = new DataBaseHelper(getContext(),"TRAINING_CENTER",null,1);
        View rootView =  inflater.inflate(R.layout.fragment_trainee__profile_, container, false);
        textViewFirstName = rootView.findViewById(R.id.textViewFirstName);
        textViewLastName = rootView.findViewById(R.id.textViewLastName);
        textViewEmail = rootView.findViewById(R.id.textViewEmail);
        textViewMobileNumber = rootView.findViewById(R.id.textViewMobileNumber);
        textViewAddress =  rootView.findViewById(R.id.textViewAddress);
        buttonEdit = rootView.findViewById(R.id.buttonEdit);
        String email = null, firstName = null, lastName = null, mobileNumber = null, address = null;
        byte[] image = new byte[0];
        System.out.println(TraineeActivites.getEmail() + "email in trainee profile");
        Cursor cursor = dataBaseHelper.getOneTrainee(TraineeActivites.getEmail());

        while (cursor.moveToNext()){
                    email = cursor.getString(0);
                    firstName = cursor.getString(2);
                    lastName = cursor.getString(3);
                    mobileNumber = cursor.getString(4);
                    address = cursor.getString(5);
                    image = cursor.getBlob(6);

        }

        byte[] array = image;
        Bitmap bitmap = BitmapFactory.decodeByteArray(array, 0, array.length);
        ImageView imageView = (ImageView) rootView.findViewById(R.id.imageViewTraineeProfile);
        imageView.setImageBitmap(bitmap);

        textViewEmail.setText(email);
        textViewFirstName.setText(firstName);
        textViewLastName.setText(lastName);
        textViewMobileNumber.setText(mobileNumber);
        textViewAddress.setText(address);


        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Button logout = rootView.findViewById(R.id.buttonExit);

        //when click on logout button go back to login page
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inside your fragment's code
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);

            }
        });
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment Editfragment = new Trainee_editProfile_Fragment();
                FragmentTransaction fragmentTransaction = fragmentManagerTrainee.beginTransaction();

                // Hide all other fragments
               /* for (Fragment fragment : fragmentManagerTrainee.getFragments()) {
                    if (fragment != Editfragment) {
                        fragmentTransaction.hide(fragment);
                    }
                }*/

                // Show the search fragment if it is not already added
                if (!Editfragment.isAdded()) {
                    fragmentTransaction.replace(R.id.content_frame, Editfragment);
                }

                // Show the search fragment
                fragmentTransaction.show(Editfragment);

                // Add the transaction to the back stack
                fragmentTransaction.addToBackStack(null);

                // Commit the transaction
                fragmentTransaction.commit();

                // Update the current fragment
                //currentFragment = searchFragment;

           }
        });


       return rootView;
    }
}