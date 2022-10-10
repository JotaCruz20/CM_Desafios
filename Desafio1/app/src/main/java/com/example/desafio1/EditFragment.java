package com.example.desafio1;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditFragment extends Fragment {
    private static final String ARG_PARAM1 = "selected";
    private int selected;
    private SharedViewModel model;
    private View view;
    private Button button;
    private EditText editName, editAge;

    public EditFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static EditFragment newInstance(String param1) {
        EditFragment fragment = new EditFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.model = new ViewModelProvider(getActivity()).get(SharedViewModel.class);

        if (getArguments() != null) {
            selected = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.view = inflater.inflate(R.layout.fragment_edit, container, false);
        this.editName = (EditText) view.findViewById(R.id.editName);
        this.editAge = (EditText) view.findViewById(R.id.editAge);
        this.button = (Button) view.findViewById(R.id.button2);

        Animal animalSelected = model.getSelectedAnimal(this.selected);

        this.editAge.setText(String.valueOf(animalSelected.getAge()));
        this.editName.setText(animalSelected.getName());

        this.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.changeAnimal(editName.getText().toString(), Integer.parseInt(editAge.getText().toString()), selected);

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainerView2, new FragmentSelection())
                        .addToBackStack(null)
                        .commit();

            }
        });

        return view;
    }
}