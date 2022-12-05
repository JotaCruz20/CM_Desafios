package com.example.desafio3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements FragmentSwitch, AsyncTask.Callback{
    SharedViewModel model;

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment, fragment.toString());
        fragmentTransaction.addToBackStack(fragment.toString());
        fragmentTransaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        model = new ViewModelProvider(this).get(SharedViewModel.class);
        DB db = new DB(this);
        model.setDB(db);
        model.createTreshValue("temp", 40.5, MainActivity.this);
        model.createTreshValue("hum", 70.5, MainActivity.this);
        model.getTreshValue("temp", MainActivity.this);
        model.getTreshValue("hum", MainActivity.this);

        if (savedInstanceState == null) {
            this.replaceFragment(new MainFragment());
        }
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
            model.setTreshTemp(value);
        }
        else{
            model.setTreshHum(value);
        }
    }
}