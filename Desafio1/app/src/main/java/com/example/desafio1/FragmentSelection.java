package com.example.desafio1;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentSelection#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentSelection extends Fragment {
    private SharedViewModel model;
    private View view;
    private Spinner spinner;
    private TextView name,age,owner;
    private ImageView imageView;
    private Button button;
    ArrayList<String> arrayList;

    public FragmentSelection() {
        // Required empty public constructor
    }

    public static FragmentSelection newInstance() {
        FragmentSelection fragment = new FragmentSelection();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.model = new ViewModelProvider(getActivity()).get(SharedViewModel.class);

        this.arrayList = new ArrayList<>();

        for (Animal animal: this.model.getAnimals()) {
            this.arrayList.add(animal.getName());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.view = inflater.inflate(R.layout.fragment_selection, container, false);

        this.spinner =  (Spinner) view.findViewById(R.id.spinner);
        this.name = (TextView) view.findViewById(R.id.textView);
        this.age = (TextView) view.findViewById(R.id.textView2);
        this.owner = (TextView) view.findViewById(R.id.textView4);
        this.imageView = (ImageView) view.findViewById(R.id.image);
        this.button = (Button) view.findViewById(R.id.button);

        this.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Animal animal = model.getSelectedAnimal(position);
                name.setText(animal.getName());
                age.setText(Integer.toString(animal.getAge()));
                owner.setText(animal.getOwner());
                imageView.setImageResource(animal.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_spinner_item, this.arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinner.setAdapter(arrayAdapter);


        this.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("selected",spinner.getSelectedItemPosition());

                EditFragment editFragment = new EditFragment();
                editFragment.setArguments(bundle);

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainerView2, editFragment)
                        .addToBackStack(null)
                        .commit();

            }
        });

        return view;
    }
}
