package com.example.desafio3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{
    ArrayList<Records> recordList;
    Context context;

    public Adapter(ArrayList<Records> notesList, Context context) {
        this.recordList = notesList;
        this.context = context;
    }

    public void setNotesList(ArrayList<Records> notesList) {
        this.recordList = notesList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder view = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false));

        return view;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Records records = recordList.get(position);
        holder.sensorName.setText(records.getSensorName());
        holder.atualizado.setText(records.getAtualizado());
        holder.value.setText(String.valueOf(records.getPercentage()));
        if (records.getPercentage() < 20) {
            holder.value.setTextColor(ContextCompat.getColor(context, R.color.chill));
        }
        else if (records.getPercentage() < 30) {
            holder.value.setTextColor(ContextCompat.getColor(context, R.color.mid));
        }
        else{
            holder.value.setTextColor(ContextCompat.getColor(context, R.color.hot));
        }
    }

    @Override
    public int getItemCount() {
        if(recordList==null){
            return 0;
        }
        return recordList.size();
    }

    @Override
    public int getItemViewType(final int position) {
        return R.layout.recordlayout;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView sensorName;
        TextView atualizado;
        TextView value;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            sensorName = itemView.findViewById(R.id.SensorName);
            atualizado = itemView.findViewById(R.id.Date);
            value = itemView.findViewById(R.id.value);

        }

    }

}
