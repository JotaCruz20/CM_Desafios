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

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.SearchView;

import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.util.ArrayList;

public class Fragment1 extends Fragment{

    private SharedViewModel sharedViewModel;
    private View view;
    private Adapter adapter;
    RecyclerView recyclerView;
    DB db;

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

        this.sharedViewModel = new ViewModelProvider(getActivity()).get(SharedViewModel.class);
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
            case R.id.search: {
                return true;
            }
            case R.id.add: {
                sharedViewModel.setNoteId(0);
                FragmentSwitch fc = (FragmentSwitch) getActivity();
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
            if (mode) {
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

        this.db = new DB(getActivity());

        this.recyclerView = this.view.findViewById(R.id.noteList);
        recyclerView.setHasFixedSize(true);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this.view.getContext()));
        this.adapter = new Adapter(db.selectRecords());
        sharedViewModel.setAdapter(this.adapter);
        sharedViewModel.setDB(this.db);

        adapter.setOnItemClickListener(new Adapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                /** ABRIR PARA O FRAG2 **/
                sharedViewModel.setNoteId(db.selectRecords().get(position).getId());
                FragmentSwitch fc = (FragmentSwitch) getActivity();
                fc.replaceFragment(new Fragment2());
            }
        });

        adapter.setOnItemLongClickListener(new Adapter.LongClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                showPopupWindow(v, db.selectRecords().get(position).getTitle(), db.selectRecords().get(position).getId(), db, adapter);
            }
        });
        this.recyclerView.setAdapter(adapter);

        return view;
    }

    public void showPopupWindow(final View view, String title, int id, DB db, Adapter adapter) {
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window, null);


        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;


        boolean focusable = true;

        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);


        Button buttonEdit = popupView.findViewById(R.id.delete);
        Button buttonUpdate = popupView.findViewById(R.id.update);
        EditText editText = popupView.findViewById(R.id.updateTitle);
        editText.setText(title);
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedViewModel.deleteNote(id);
                popupWindow.dismiss();
            }

        });

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedViewModel.updateTitleNote(id,editText.getText().toString());
                popupWindow.dismiss();
            }
        });


        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                sharedViewModel.updateTitleNote(id,editText.getText().toString());
                popupWindow.dismiss();
                return true;
            }
        });
    }
}