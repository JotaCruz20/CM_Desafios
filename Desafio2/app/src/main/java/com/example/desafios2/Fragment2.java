package com.example.desafios2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;


public class Fragment2 extends Fragment implements AsyncTask.Callback {

    private SharedViewModel sharedViewModel;
    private View view;
    private Note note;

    EditText editText_title, editText_notes;
    ImageView imageView_save, imageView_back;

    public Fragment2() {
        // Required empty public constructor
    }

    public static Fragment2 newInstance(String param1, String param2) {
        Fragment2 fragment = new Fragment2();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.sharedViewModel = new ViewModelProvider(getActivity()).get(SharedViewModel.class);

        if (!this.sharedViewModel.getNoteId().equals("0")){
            this.sharedViewModel.getNote(this.sharedViewModel.getNoteId(),Fragment2.this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.view = inflater.inflate(R.layout.fragment2, container, false);
        imageView_save = this.view.findViewById(R.id.imageView_save);
        imageView_back = this.view.findViewById(R.id.imageView_back);
        editText_title = this.view.findViewById(R.id.editText_title);
        editText_notes = this.view.findViewById(R.id.editText_notes);

        imageView_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editText_title.getText().toString();
                String description = editText_notes.getText().toString();

                if( description.isEmpty()){
                    // Toast.makeText(Fragment2.this, "Please add some text" , Toast.LENGTH_SHORT).show();
                    return;
                }

                if (note == null){
                    sharedViewModel.createNote(title, description, Fragment2.this);
                }
                else{
                    sharedViewModel.updateNote(note.getId(),title,description,Fragment2.this);
                }

                FragmentSwitch fc = (FragmentSwitch) getActivity();
                fc.replaceFragment(new Fragment1());
            }
        });

        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentSwitch fc = (FragmentSwitch) getActivity();
                fc.replaceFragment(new Fragment1());
            }
        });

        // Inflate the layout for this fragment
        return this.view;
    }


    @Override
    public void onCompleteNote(Note note) {
        this.note = note;
        editText_notes.setText(note.getBody());
        editText_title.setText(note.getTitle());
    }

    @Override
    public void onCompleteNotes(ArrayList<Note> notes, boolean isFilter) {
    }
}