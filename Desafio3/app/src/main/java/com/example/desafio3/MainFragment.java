package com.example.desafio3;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment implements AsyncTask.Callback {
    private SharedViewModel sharedViewModel;
    private View view;
    private boolean temp,hum,ledOnOff;
    private LineChart chart;
    private long definedFilter;
    private NotificationBroadcast notificationBroadcast;
    private List<Humidity> humList;
    private List<Temperature> tempList;

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
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

        this.hum = true;
        this.temp = true;
        this.definedFilter = 0;

        final Observer<String> topicObserverTemp = newMsg -> {
            String[] arrOfStr = newMsg.toString().split("\\|", 2);
            String epoch = arrOfStr[0];
            String value = arrOfStr[1];

            if(Double.parseDouble(value) > this.sharedViewModel.getTreshTemp()){
                this.notificationBroadcast.sendNotification(getContext(),"NIVEL DE HUMIDADE ACIMA DO LIMITE");
                Toast.makeText(getContext(),"NIVEL DE TEMPERATURA ACIMA DO LIMITE", Toast.LENGTH_SHORT).show();
            }
            this.sharedViewModel.createTempRecord(Long.parseLong(epoch), Double.parseDouble(value), MainFragment.this);
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
            this.sharedViewModel.createHumRecord(Long.parseLong(epoch), Double.parseDouble(value), MainFragment.this);
        };

        sharedViewModel.getNewFromTopicHum().observe(this, topicObserverHum);

        this.sharedViewModel.connection();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.view = inflater.inflate(R.layout.fragment_mainfragment, container, false);

        Button led = this.view.findViewById(R.id.ledOnOff);

        led.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mandar not para ligar

                String msg = "";

                if(!ledOnOff){
                    led.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.ledon));
                    led.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_check_circle_outline_24,0,0,0);
                    msg = "ledON";
                }
                else{
                    led.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.ledoff));
                    led.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_not_interested_24,0,0,0);
                    msg = "ledOFF";
                }

                ledOnOff=!ledOnOff;

                sharedViewModel.changeLed(sharedViewModel.getHelper(),msg,2);
            }
        });

        Button day = this.view.findViewById(R.id.day);
        Button day3 = this.view.findViewById(R.id.day3);
        Button week = this.view.findViewById(R.id.week);
        Button month = this.view.findViewById(R.id.month);
        Button month6 = this.view.findViewById(R.id.month6);

        day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                day.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.filterbuttonselected));
                day3.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.filterbutton));
                week.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.filterbutton));
                month.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.filterbutton));
                month6.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.filterbutton));

                LocalDate date = LocalDate.now().minusDays(1);
                ZoneId zoneId = ZoneId.systemDefault();
                long newEpoch = date.atStartOfDay(zoneId).toEpochSecond();

                sharedViewModel.getHumidity(newEpoch, MainFragment.this);
                sharedViewModel.getTemperature(newEpoch, MainFragment.this);
                definedFilter = newEpoch;
            }
        });

        day3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                day.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.filterbutton));
                day3.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.filterbuttonselected));
                week.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.filterbutton));
                month.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.filterbutton));
                month6.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.filterbutton));

                LocalDate date = LocalDate.now().minusDays(3);
                ZoneId zoneId = ZoneId.systemDefault();
                long newEpoch = date.atStartOfDay(zoneId).toEpochSecond();

                sharedViewModel.getHumidity(newEpoch, MainFragment.this);
                sharedViewModel.getTemperature(newEpoch, MainFragment.this);
                definedFilter = newEpoch;
            }
        });

        week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                day.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.filterbutton));
                day3.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.filterbutton));
                week.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.filterbuttonselected));
                month.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.filterbutton));
                month6.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.filterbutton));

                LocalDate date = LocalDate.now().minusDays(7);
                ZoneId zoneId = ZoneId.systemDefault();
                long newEpoch = date.atStartOfDay(zoneId).toEpochSecond();

                sharedViewModel.getHumidity(newEpoch, MainFragment.this);
                sharedViewModel.getTemperature(newEpoch, MainFragment.this);
                definedFilter = newEpoch;
            }
        });

        month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                day.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.filterbutton));
                day3.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.filterbutton));
                week.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.filterbutton));
                month.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.filterbuttonselected));
                month6.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.filterbutton));

                LocalDate date = LocalDate.now().minusMonths(1);
                ZoneId zoneId = ZoneId.systemDefault();
                long newEpoch = date.atStartOfDay(zoneId).toEpochSecond();

                sharedViewModel.getHumidity(newEpoch, MainFragment.this);
                sharedViewModel.getTemperature(newEpoch, MainFragment.this);
                definedFilter = newEpoch;
            }
        });

        month6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                day.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.filterbutton));
                day3.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.filterbutton));
                week.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.filterbutton));
                month.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.filterbutton));
                month6.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.filterbuttonselected));

                LocalDate date = LocalDate.now().minusMonths(6);
                ZoneId zoneId = ZoneId.systemDefault();
                long newEpoch = date.atStartOfDay(zoneId).toEpochSecond();

                sharedViewModel.getHumidity(newEpoch, MainFragment.this);
                sharedViewModel.getTemperature(newEpoch, MainFragment.this);
                definedFilter = newEpoch;
            }
        });

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
                sharedViewModel.changeTemp(sharedViewModel.getHelper(), 2);
                changeGraphData();
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
                sharedViewModel.changeHum(sharedViewModel.getHelper(), 2);
                changeGraphData();
            }
        });

        this.chart = (LineChart) this.view.findViewById(R.id.chart);

        //INITIATE CHART
        sharedViewModel.getHumidity(definedFilter, MainFragment.this);
        sharedViewModel.getTemperature(definedFilter, MainFragment.this);

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

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentSwitch fc = (FragmentSwitch) getActivity();
                fc.replaceFragment(new SettingsFragment());
            }
        });

        return view;
    }

    void changeGraphData(){
        List<ILineDataSet> lines = new ArrayList<ILineDataSet>();
        if(this.hum && this.humList!=null) {
            List<Entry> entries = new ArrayList<Entry>();
            for (Humidity hum : this.humList) {
                entries.add(new Entry(hum.getTimestamp(), (float) hum.getPercentage()));
            }

            LineDataSet dataSet = new LineDataSet(entries, "Humidity");
            dataSet.setColor(Color.RED);
            dataSet.setCircleColor(Color.RED);
            dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            dataSet.setDrawFilled(true);
            dataSet.setFillColor(ContextCompat.getColor(this.view.getContext(), R.color.red));
            lines.add(dataSet);
        }

        if(this.temp && this.tempList!=null) {
            List<Entry> entries2 = new ArrayList<Entry>();
            for (Temperature hum : this.tempList) {
                entries2.add(new Entry(hum.getTimestamp(), (float) hum.getPercentage()));
            }

            LineDataSet dataSet2 = new LineDataSet(entries2, "Temperature");
            dataSet2.setColor(Color.BLUE);
            dataSet2.setCircleColor(Color.BLUE);
            dataSet2.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            dataSet2.setDrawFilled(true);
            dataSet2.setFillColor(ContextCompat.getColor(this.view.getContext(), R.color.blue));
             lines.add(dataSet2);
        }

        LineData data = new LineData(lines);
        chart.setData(data);
        chart.notifyDataSetChanged();
        chart.invalidate();
        //chart.getXAxis().setValueFormatter(new LineChartXAxisValueFormatter());
    }


    @Override
    public void onComplete() {
        this.sharedViewModel.getTemperature(this.definedFilter, MainFragment.this);
        this.sharedViewModel.getHumidity(this.definedFilter, MainFragment.this);
    }

    @Override
    public void onCompleteH(ArrayList<Humidity> response) {
        this.humList = response;
        this.changeGraphData();

    }

    @Override
    public void onCompleteT(ArrayList<Temperature> response) {
        this.tempList = response;
        this.changeGraphData();
    }

    @Override
    public void onCompleteV(double value, String type) {

    }
}