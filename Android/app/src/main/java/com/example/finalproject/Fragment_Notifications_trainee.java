package com.example.finalproject;

import static com.example.finalproject.TraineeActivites.getEmail;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


 class Pair{
    int id;
    String text;

     public Pair(int id, String text) {
         this.id = id;
         this.text = text;
     }

     public int getId() {
         return id;
     }

     public void setId(int id) {
         this.id = id;
     }

     public String getText() {
         return text;
     }

     public void setText(String text) {
         this.text = text;
     }
 }
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Notifications_trainee#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Notifications_trainee extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment_Notifications_trainee() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Notifications_trainee.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Notifications_trainee newInstance(String param1, String param2) {
        Fragment_Notifications_trainee fragment = new Fragment_Notifications_trainee();
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
        View rootView =  inflater.inflate(R.layout.fragment__notifications_trainee, container, false);
        ListView listV = (ListView) rootView.findViewById(R.id.listView);
        ArrayList<Pair> list = new ArrayList<>();

        DataBaseHelper db = new DataBaseHelper(getContext(), "TRAINING_CENTER", null, 1);
        Cursor cursor = db.getNotificationsForTrainee(TraineeActivites.getEmail());


        while(cursor.moveToNext()){
            String not = cursor.getString(1);
            list.add(new Pair(cursor.getInt(0), not));
        }

        ArrayAdapter<Pair> adapter = new ArrayAdapter<Pair>(requireContext(), R.layout.for_fragment_notfication,R.id.textViewTitle ,list) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    TextView textViewTitle = view.findViewById(R.id.textViewTitle);

                    textViewTitle.setText(list.get(position).getText());

                    return view;
                }
            };

             listV.setAdapter(adapter);

            listV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Pair selectedItem = (Pair) parent.getItemAtPosition(position);
                        int idNot = selectedItem.getId();
                        db.updateNotification(idNot);
                        String not = TraineeActivites.notificationsText.getText().toString();
                        int nots = Integer.parseInt(not.substring(not.indexOf("(") + 1, not.indexOf(")")));
                        nots--;

                        if(nots > 0) {

                            TraineeActivites.notificationsText.setText("(" + (nots) + ")");
                        } else{
                                TraineeActivites.notificationsText.setText("");
                        }

                        replaceFragment(new Fragment_Notifications_trainee());

                    }
                });

        return rootView;
    }

    private void replaceFragment(Fragment newFragment){
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, newFragment);
        fragmentTransaction.commit();
    }
}