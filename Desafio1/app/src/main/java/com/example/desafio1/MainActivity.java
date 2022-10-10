package com.example.desafio1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedViewModel model = new ViewModelProvider(this).get(SharedViewModel.class);
        model.addAnimal(new Animal("Rino","Manel",50,R.drawable.rhino));
        model.addAnimal(new Animal("Frog","Zé",10,R.drawable.frog));
        model.addAnimal(new Animal("Snail","Alberto",15,R.drawable.snail));

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragmentContainerView2, FragmentSelection.class, null)
                    .commit();
        }
    }
}