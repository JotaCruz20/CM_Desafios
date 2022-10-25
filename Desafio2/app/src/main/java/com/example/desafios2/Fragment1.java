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
import androidx.appcompat.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

public class Fragment1 extends Fragment {

    private ViewModel model;
    private View view;
    private Adapter adapter;
    RecyclerView recyclerView;

    public Fragment1() {
        // Required empty public constructor
    }

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

        MenuItem searchItem = menu.findItem(R.id.search);

        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filter(query, true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText, false);
                return false;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:  {
                return true;
            }
            case R.id.add: {
                FragmentSwitch fc= (FragmentSwitch) getActivity();
                fc.replaceFragment(new Fragment2());
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void filter(String text, boolean mode) {
        ArrayList<Note> filteredlist = new ArrayList<Note>();

        DB db = new DB(getActivity());
        ArrayList<Note> notes = db.selectRecords();

        for (Note item : notes) {
            if (item.getTitle().toLowerCase().startsWith(text.toLowerCase())) {
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            if(mode) {
                Toast.makeText(this.view.getContext(), "No Data Found..", Toast.LENGTH_SHORT).show();
            }
        } else {
            this.adapter.filterList(filteredlist);
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

        this.recyclerView = this.view.findViewById(R.id.noteList);
        recyclerView.setHasFixedSize(true);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this.view.getContext()));
        this.adapter = new Adapter(db.selectRecords());

        adapter.setOnItemClickListener(new Adapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                /** ABRIR PARA O FRAG2 **/
            }
        });

        adapter.setOnItemLongClickListener(new Adapter.LongClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                PopupWindowFrag popUpClass = new PopupWindowFrag();
                popUpClass.showPopupWindow(v, db.selectRecords().get(position).getTitle(), db.selectRecords().get(position).getId(), db, adapter);
            }
        });
        this.recyclerView.setAdapter(adapter);

        return view;
    }

}