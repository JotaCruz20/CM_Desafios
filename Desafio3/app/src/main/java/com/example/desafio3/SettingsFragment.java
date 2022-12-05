package com.example.desafio3;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

public class SettingsFragment extends Fragment implements AsyncTask.Callback{
    private SharedViewModel sharedViewModel;
    private View view;
    private NotificationBroadcast notificationBroadcast;
    private EditText temp, hum;

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.sharedViewModel = new ViewModelProvider(getActivity()).get(SharedViewModel.class);

        this.notificationBroadcast = new NotificationBroadcast();
        this.notificationBroadcast.createNotificationChannel(getContext());

        final Observer<String> topicObserverTemp = newMsg -> {
            String[] arrOfStr = newMsg.toString().split("\\|", 2);
            String epoch = arrOfStr[0];
            String value = arrOfStr[1];

            if(Double.parseDouble(value) > this.sharedViewModel.getTreshTemp()){
                this.notificationBroadcast.sendNotification(getContext(),"NIVEL DE HUMIDADE ACIMA DO LIMITE");
                Toast.makeText(getContext(),"NIVEL DE TEMPERATURA ACIMA DO LIMITE", Toast.LENGTH_SHORT).show();
            }
            this.sharedViewModel.createTempRecord(Long.parseLong(epoch), Double.parseDouble(value), SettingsFragment.this);
        };

        sharedViewModel.getNewFromTopicTemp().observe(this, topicObserverTemp);

        final Observer<String> topicObserverHum = newMsg -> {
            String[] arrOfStr = newMsg.toString().split("\\|", 2);
            String epoch = arrOfStr[0];
            String value = arrOfStr[1];

            if(Double.parseDouble(value) > this.sharedViewModel.getTreshTemp()){
                this.notificationBroadcast.sendNotification(getContext(),"NIVEL DE HUMIDADE ACIMA DO LIMITE");
                Toast.makeText(getContext(),"NIVEL DE HUMIDADE ACIMA DO LIMITE", Toast.LENGTH_SHORT).show();
            }
            this.sharedViewModel.createHumRecord(Long.parseLong(epoch), Double.parseDouble(value), SettingsFragment.this);
        };

        sharedViewModel.getNewFromTopicHum().observe(this, topicObserverHum);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.view = inflater.inflate(R.layout.fragment_settings, container, false);

        this.temp = this.view.findViewById(R.id.temp);
        this.hum = this.view.findViewById(R.id.hum);

        this.sharedViewModel.getTreshValue("temp", SettingsFragment.this);
        this.sharedViewModel.getTreshValue("hum", SettingsFragment.this);

        Button done = this.view.findViewById(R.id.done);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hum.getText().toString().equals("") || temp.getText().toString().equals("")){
                    Toast.makeText(view.getContext(),"NAO DEIXES NADA SEM VALOR", Toast.LENGTH_SHORT).show();
                }
                else {

                    sharedViewModel.updateTreshValue("temp", Double.parseDouble(temp.getText().toString()), SettingsFragment.this);
                    sharedViewModel.updateTreshValue("hum", Double.parseDouble(hum.getText().toString()), SettingsFragment.this);
                    FragmentSwitch fc = (FragmentSwitch) getActivity();
                    fc.replaceFragment(new MainFragment());
                }

            }
        });

        //FOOTER
        ImageButton dashboard = this.view.findViewById(R.id.imageButton);
        ImageButton records = this.view.findViewById(R.id.imageButton2);
        ImageButton settings = this.view.findViewById(R.id.imageButton3);

        records.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentSwitch fc = (FragmentSwitch) getActivity();
                fc.replaceFragment(new ValuesFragment());
            }
        });

        dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentSwitch fc = (FragmentSwitch) getActivity();
                fc.replaceFragment(new MainFragment());
            }
        });

        return this.view;
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onCompleteH(ArrayList<Humidity> response) {

    }

    @Override
    public void onCompleteT(ArrayList<Temperature> response) {

    }

    @Override
    public void onCompleteV(double value, String type) {
        if(type.equals("temp")){
            temp.setText(String.valueOf(value));
        }
        else{
            hum.setText(String.valueOf(value));
        }
    }
}