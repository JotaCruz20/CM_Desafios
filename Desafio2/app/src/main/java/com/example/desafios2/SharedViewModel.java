package com.example.desafios2;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;


public class SharedViewModel extends ViewModel{
    private final SavedStateHandle state;
    private MutableLiveData<String> noteId = new MutableLiveData<>();
    private MutableLiveData<DB> db = new MutableLiveData<>();
    private AsyncTask asyncTask = new AsyncTask();

    public SharedViewModel(SavedStateHandle state) {
        this.state = state;
    }


    public void setDB(DB db){
        this.db.setValue(db);
    }

    public void updateNote(String id,String title, String body, AsyncTask.Callback callback){
        asyncTask.executeAsyncUpdate(id,title,body,this.db.getValue(),callback);
    }

    public void createNote(String title, String body, AsyncTask.Callback callback){
        asyncTask.executeAsyncCreate(this.db.getValue(), title, body,callback);
    }

    public String getNoteId() {
        return this.noteId.getValue();
    }

    public void setNoteId(String noteId) {
        this.noteId.setValue(noteId);
    }

    public void updateTitleNote(String id, String title, AsyncTask.Callback callback){
        asyncTask.executeAsyncUpdateTitle(id,title,this.db.getValue(),callback);
    }

    public void deleteNote(String id, AsyncTask.Callback callback){
        asyncTask.executeAsyncDelete(id,this.db.getValue(),callback);
    }

    public void getNotes(AsyncTask.Callback callback, boolean isFilter){
        asyncTask.executeAsyncSelectNotes(this.db.getValue(),callback, isFilter);
    }

    public void getNote(String id,AsyncTask.Callback callback){
        asyncTask.executeAsyncSelectNote(id,this.db.getValue(),callback);
    }
}
