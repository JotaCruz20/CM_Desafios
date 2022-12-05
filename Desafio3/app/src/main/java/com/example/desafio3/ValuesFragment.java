package com.example.desafio3;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ValuesFragment extends Fragment implements AsyncTask.Callback{

    private SharedViewModel sharedViewModel;
    private View view;
    private boolean temp,hum;
    private ArrayList<Records> records;
    private RecyclerView recyclerView;
    private Adapter adapter;
    private NotificationBroadcast notificationBroadcast;
    private List<Humidity> humList;
    private List<Temperature> tempList;


    public ValuesFragment() {
        // Required empty public constructor
    }


    public static ValuesFragment newInstance(String param1, String param2) {
        ValuesFragment fragment = new ValuesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.sharedViewModel = new ViewModelProvider(getActivity()).get(SharedViewModel.class);

        this.hum = true;
        this.temp = true;

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
            this.sharedViewModel.createTempRecord(Long.parseLong(epoch), Double.parseDouble(value), ValuesFragment.this);
            this.sharedViewModel.getTemperature(0, ValuesFragment.this);
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
            this.sharedViewModel.createHumRecord(Long.parseLong(epoch), Double.parseDouble(value), ValuesFragment.this);
            this.sharedViewModel.getHumidity(0, ValuesFragment.this);
        };

        sharedViewModel.getNewFromTopicHum().observe(this, topicObserverHum);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.view = inflater.inflate(R.layout.fragment_values, container, false);

        this.recyclerView = this.view.findViewById(R.id.recyclerView);
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        this.adapter = new Adapter(this.records,view.getContext());
        recyclerView.setAdapter(this.adapter);
        this.filterList();

        Button temperatura = this.view.findViewById(R.id.temperatura);
        Button humidade = this.view.findViewById(R.id.humidade);

        temperatura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mudar cenas
                if(!temp) {
                    temperatura.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_check_box_24, 0, 0, 0);
                }
                else{
                    temperatura.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_check_box_outline_blank_24, 0, 0, 0);
                }
                temp=!temp;
                filterList();
            }
        });

        humidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mudar cenas
                if(!hum) {
                    humidade.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_check_box_24, 0, 0, 0);
                }
                else{
                    humidade.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_check_box_outline_blank_24, 0, 0, 0);
                }
                hum=!hum;
                filterList();
            }
        });

        //FOOTER
        ImageButton dashboard = this.view.findViewById(R.id.imageButton);
        ImageButton records = this.view.findViewById(R.id.imageButton2);
        ImageButton settings = this.view.findViewById(R.id.imageButton3);

        dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentSwitch fc = (FragmentSwitch) getActivity();
                fc.replaceFragment(new MainFragment());
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentSwitch fc = (FragmentSwitch) getActivity();
                fc.replaceFragment(new SettingsFragment());
            }
        });

        this.sharedViewModel.getHumidity(0, ValuesFragment.this);
        this.sharedViewModel.getTemperature(0, ValuesFragment.this);

        return this.view;
    }

    public void filterList(){
        this.records = new ArrayList<>();

        if(this.hum && this.humList!=null) {
            for (Humidity hum : this.humList) {
                Date expiry = new Date(hum.getTimestamp()*1000);
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String strDate = dateFormat.format(expiry);
                this.records.add(0,new Records(strDate, hum.getPercentage(), "Humidade"));
            }
        }

        if(this.temp && this.tempList!=null) {
            for (Temperature temperature : this.tempList) {
                Date expiry = new Date(temperature.getTimestamp()*1000);
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String strDate = dateFormat.format(expiry);
                this.records.add(0,new Records(strDate, temperature.getPercentage(), "Temperatura"));
            }
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Collections.sort(this.records, Comparator.comparing(s -> LocalDateTime.parse(s.getAtualizado(), formatter)));
        Collections.reverse(this.records);

        adapter.setNotesList(this.records);
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onCompleteH(ArrayList<Humidity> response) {
        this.humList = response;
        filterList();

    }

    @Override
    public void onCompleteT(ArrayList<Temperature> response) {
        this.tempList = response;
        filterList();
    }

    @Override
    public void onCompleteV(double value, String type) {

    }
}