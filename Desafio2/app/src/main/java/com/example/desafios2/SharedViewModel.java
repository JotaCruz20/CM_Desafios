package com.example.desafios2;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class SharedViewModel extends ViewModel implements  AsyncTask.Callback{
    private final SavedStateHandle state;
    private MutableLiveData<String> noteId = new MutableLiveData<>();
    private MutableLiveData<Adapter> adapter = new MutableLiveData<>();
    private MutableLiveData<DB> db = new MutableLiveData<>();
    private AsyncTask asyncTask = new AsyncTask();

    public SharedViewModel(SavedStateHandle state) {
        this.state = state;
    }

    public void setAdapter(Adapter adapter){
        this.adapter.setValue(adapter);
    }

    public void setDB(DB db){
        this.db.setValue(db);
    }

    public void updateNote(String id,String title, String body){

    }

    public void createNote(String id, String title, String body){

    }

    public void updateTitleNote(String id,String title){
        asyncTask.executeAsyncUpdateTitle(id,title,this.db.getValue(),SharedViewModel.this);
    }

    public void deleteNote(String id){
        asyncTask.executeAsyncDelete(id,this.db.getValue(),SharedViewModel.this);
    }

    @Override
    public void onComplete() {
        this.adapter.getValue().setNotesList(this.db.getValue().selectRecords());
        this.adapter.getValue().notifyDataSetChanged();
    }
}
