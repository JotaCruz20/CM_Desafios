package com.example.desafios2;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


public class PopupWindowFrag {

    public void showPopupWindow(final View view, String title, String id, DB db, Adapter adapter) {
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
                db.deleteNote(id);
                adapter.setNotesList(db.selectRecords());
                adapter.notifyDataSetChanged();
                popupWindow.dismiss();
            }
        });

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.updateTitle(id, editText.getText().toString());
                adapter.setNotesList(db.selectRecords());
                adapter.notifyDataSetChanged();
                popupWindow.dismiss();
            }
        });


        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                db.updateTitle(id, editText.getText().toString());
                adapter.setNotesList(db.selectRecords());
                adapter.notifyDataSetChanged();
                popupWindow.dismiss();
                return true;
            }
        });
    }

}