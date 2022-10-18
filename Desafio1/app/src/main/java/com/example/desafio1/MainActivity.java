package com.example.desafio1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements FragmentSwitch{


    @Override
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView2, fragment, fragment.toString());
        fragmentTransaction.addToBackStack(fragment.toString());
        fragmentTransaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedViewModel model = new ViewModelProvider(this).get(SharedViewModel.class);
        model.addAnimal(new Animal("Rino","Manel",50,R.drawable.rhino));
        model.addAnimal(new Animal("Frog","Zé",10,R.drawable.frog));
        model.addAnimal(new Animal("Snail","Alberto",15,R.drawable.snail));

        if (savedInstanceState == null) {
            this.replaceFragment(new FragmentSelection());
        }
    }
}