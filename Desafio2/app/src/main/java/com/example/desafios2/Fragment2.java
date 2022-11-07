package com.example.desafios2;

import android.icu.text.SimpleDateFormat;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment2 extends Fragment {

    private SharedViewModel sharedViewModel;
    private View view;
    DB db;

    EditText editText_title, editText_notes;
    ImageView imageView_save;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment2.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment2 newInstance(String param1, String param2) {
        Fragment2 fragment = new Fragment2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.sharedViewModel = new ViewModelProvider(getActivity()).get(SharedViewModel.class);

        try {
            this.sharedViewModel.getNoteId();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.view = inflater.inflate(R.layout.fragment2, container, false);
        imageView_save = this.view.findViewById(R.id.imageView_save);
        editText_title = this.view.findViewById(R.id.editText_title);
        editText_notes = this.view.findViewById(R.id.editText_notes);

        //if(sharedViewModel.getNoteId() != 0){
        //    editText_notes.setText();
        //    editText_title.setText();
        //}

        imageView_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editText_title.getText().toString();
                String description = editText_notes.getText().toString();

                if( description.isEmpty()){
                    // Toast.makeText(Fragment2.this, "Please add some text" , Toast.LENGTH_SHORT).show();
                    return;
                }

                if (sharedViewModel.getNoteId() == 0){
                    sharedViewModel.createNote(title, description);
                }

                FragmentSwitch fc = (FragmentSwitch) getActivity();
                fc.replaceFragment(new Fragment1());
            }
        });

        // Inflate the layout for this fragment
        return this.view;
    }

    private void finish() {
    }
}