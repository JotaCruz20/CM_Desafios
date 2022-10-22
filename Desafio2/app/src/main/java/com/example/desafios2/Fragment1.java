package com.example.desafios2;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class Fragment1 extends Fragment {

    private ViewModel model;
    private View view;
    RecyclerView recyclerView;

    public Fragment1() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static Fragment1 newInstance() {
        Fragment1 fragment = new Fragment1();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        this.model = new ViewModelProvider(getActivity()).get(SharedViewModel.class);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:  {
                System.out.println("TESTE 1");
                return true;
            }
            case R.id.add: {
                System.out.println("TESTE 2");
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.view = inflater.inflate(R.layout.fragment1, container, false);

        Toolbar myToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(myToolbar);

        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#a81c00"));

        // Set BackgroundDrawable
        myToolbar.setBackground(colorDrawable);

        DB db = new DB(getActivity());
        ArrayList<Note> notes = db.selectRecords();

        this.recyclerView = this.view.findViewById(R.id.noteList);
        recyclerView.setHasFixedSize(true);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this.view.getContext()));
        Adapter adapter = new Adapter(notes);

        adapter.setOnItemClickListener(new Adapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                //Toast.makeText(MainActivity.this, "Click on " + position, Toast.LENGTH_SHORT).show();
                //Toast.makeText(MainActivity.this, "Click on " + position + " data = " + adapter.getItem(position), Toast.LENGTH_SHORT).show();
            }
        });

        adapter.setOnItemLongClickListener(new Adapter.LongClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                //Toast.makeText(MainActivity.this, "Click on " + position, Toast.LENGTH_SHORT).show();
                //Toast.makeText(MainActivity.this, "Click on " + position + " data = " + adapter.getItem(position), Toast.LENGTH_SHORT).show();
            }
        });
        this.recyclerView.setAdapter(adapter);

        return view;
    }
}