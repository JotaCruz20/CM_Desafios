package com.example.desafios2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements FragmentSwitch{

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

        SharedViewModel model = new ViewModelProvider(this).get(SharedViewModel.class);
        // SO PARA TESTAR DAR DELETE A ISTO DEPOIS
        DB db = new DB(this);
        db.createRecords("TESTE","TESTE!123");
        db.createRecords("ABC","TESTE123");
        db.createRecords("PAULO","TESTE!1234567");
        //DAR DELETE

        if (savedInstanceState == null) {
            this.replaceFragment(new Fragment1());
        }
    }
}