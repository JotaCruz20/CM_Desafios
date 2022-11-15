package com.example.desafios2;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Fragment1 extends Fragment implements AsyncTask.Callback {

    private SharedViewModel sharedViewModel;
    private View view;
    private Adapter adapter;
    RecyclerView recyclerView;
    private ArrayList<Note> notes;

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
        this.sharedViewModel.getNotes(Fragment1.this,false);
        this.sharedViewModel.connection();

        final Observer<String> topicObserver = newMsg -> {
            String[] arrOfStr = newMsg.split("\\|");
            String Title = arrOfStr[0];
            String Body = arrOfStr[1];



            showPopupAcceptWindow(view, Title, Body);
        };
        sharedViewModel.getNewFromTopic().observe(this, topicObserver);
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
                sharedViewModel.setNoteId("0");
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

        this.sharedViewModel.getNotes(Fragment1.this, true);

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
            this.adapter.notifyDataSetChanged();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.view = inflater.inflate(R.layout.fragment1, container, false);

        Toolbar myToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(myToolbar);

        Button subscribeButton = view.findViewById(R.id.subscribe);
        Button unsubscribeButton = view.findViewById(R.id.unsubscribe);

        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#a81c00"));

        // Set BackgroundDrawable
        myToolbar.setBackground(colorDrawable);

        this.recyclerView = this.view.findViewById(R.id.noteList);
        recyclerView.setHasFixedSize(true);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this.view.getContext()));
        this.adapter = new Adapter(this.notes);

        adapter.setOnItemClickListener(new Adapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                sharedViewModel.setNoteId(notes.get(position).getId());
                FragmentSwitch fc = (FragmentSwitch) getActivity();
                fc.replaceFragment(new Fragment2());
            }
        });

        adapter.setOnItemLongClickListener(new Adapter.LongClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                showPopupWindow(v, notes.get(position).getTitle(), notes.get(position).getId());
            }
        });

        //sharedViewModel.insertTopic(topic_temp);

        subscribeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupTopicWindow(v);
            }

        });

        unsubscribeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupUnsubWindow(v);
            }

        });

        this.recyclerView.setAdapter(adapter);

        return view;
    }

    public void showPopupWindow(final View view, String title, String id) {
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window, null);


        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;


        boolean focusable = true;

        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);


        Button buttonEdit = popupView.findViewById(R.id.delete);
        Button buttonUpdate = popupView.findViewById(R.id.update);
        Button buttonShare = popupView.findViewById(R.id.share);

        EditText editText = popupView.findViewById(R.id.updateTitle);
        editText.setText(title);
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedViewModel.deleteNote(id, Fragment1.this);
                popupWindow.dismiss();
            }

        });

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedViewModel.updateTitleNote(id,editText.getText().toString(),Fragment1.this);
                popupWindow.dismiss();
            }
        });

        buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                showPopupShareWindow(v, id);
            }
        });


        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                sharedViewModel.updateTitleNote(id,editText.getText().toString(),Fragment1.this);
                popupWindow.dismiss();
                return true;
            }
        });
    }

    public void showPopupTopicWindow(final View view) {
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_subscribe, null);


        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;


        boolean focusable = true;

        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);


        Button subscribeEdit = popupView.findViewById(R.id.subscribe);
        EditText editText = popupView.findViewById(R.id.updateTopic);

        TextView errormsg = popupView.findViewById(R.id.error);

        subscribeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(sharedViewModel.insertTopic(editText.getText().toString()) != -1) {
                        sharedViewModel.getHelper().subscribeToTopic(editText.getText().toString());
                        errormsg.setText("Added topic successfully ");
                        editText.setText("");
                    } else {
                        errormsg.setText("You've already subscribed this topic");
                    }
                    //popupWindow.dismiss();
                } catch (Exception e) {
                    errormsg.setText("You've already subscribed this topic");
                }
            }
        });


        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }

    public void showPopupShareWindow(final View view, String id) {
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_publish, null);


        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;


        boolean focusable = true;

        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);


        Button shareTopic = popupView.findViewById(R.id.send);
        EditText topicText = popupView.findViewById(R.id.updateTitle);

        shareTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Note note = sharedViewModel.getNote(id);
                String msg = note.getId()+"|"+note.getTitle()+"|"+note.getBody();

                // dividir as linhas e enviar por cada um
                String [] topics = topicText.getText().toString().split("\n");

                for (String topic: topics) {
                    // 2 - exactly one
                    sharedViewModel.publishNote(sharedViewModel.getHelper(), msg, 2, topic, false);
                    Log.w(TAG, "sent topic " + topic);
                }

                popupWindow.dismiss();
            }

        });


        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }

    public void showPopupUnsubWindow(final View view) {
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_unsubscribe, null);


        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;


        boolean focusable = true;

        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);


        Button shareTopic = popupView.findViewById(R.id.unsubscribe);
        EditText topicText = popupView.findViewById(R.id.updateTopic);

        TextView errormsg = popupView.findViewById(R.id.error);

        shareTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!sharedViewModel.deleteTopic(topicText.getText().toString())) {
                    errormsg.setText("You're not subscribed to topic " + topicText.getText().toString());
                } else {
                    sharedViewModel.getHelper().unsubscribeToTopic(topicText.getText().toString());
                    errormsg.setText("Unsubscribed topic " + topicText.getText().toString() + " successfully");
                    topicText.setText("");
                }
                //popupWindow.dismiss();
            }

        });


        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }

    public void showPopupAcceptWindow(View v, String Title, String Body) {
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_accept, null);


        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;


        boolean focusable = true;

        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);


        Button acceptNote = popupView.findViewById(R.id.accept);
        Button deleteNote = popupView.findViewById(R.id.delete);

        acceptNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedViewModel.createNote(Title, Body, Fragment1.this);
                popupWindow.dismiss();
            }

        });

        deleteNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }

        });


        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }

    @Override
    public void onCompleteNote(Note note) {
    }

    @Override
    public void onCompleteNotes(ArrayList<Note> notes, boolean isFilter) {
        if(!isFilter) {
            this.notes = notes;
            this.adapter.setNotesList(notes);
            this.adapter.notifyDataSetChanged();
        }
    }


}