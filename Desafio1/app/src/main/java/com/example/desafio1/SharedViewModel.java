package com.example.desafio1;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class SharedViewModel extends ViewModel {
    private final SavedStateHandle state;
    private MutableLiveData<ArrayList<Animal>> animals = new MutableLiveData<>();

    public SharedViewModel(SavedStateHandle state) {
        this.state = state;
    }


    public ArrayList<Animal> getAnimals() {
        return this.animals.getValue();
    }

    public void changeAnimal(String name, int age, int pos){
        ArrayList<Animal> animalsValue = this.animals.getValue();
        Animal animal = animalsValue.get(pos);
        animalsValue.remove(pos);
        animal.setName(name);
        animal.setAge(age);
        animalsValue.add(pos,animal);
        this.animals.setValue(animalsValue);
    }

    public void addAnimal(Animal animal){
        if(this.animals.getValue() == null){
            this.animals.setValue(new ArrayList<Animal>());
        }

        ArrayList<Animal> animalsValue = this.animals.getValue();
        animalsValue.add(animal);
        this.animals.setValue(animalsValue);
    }

    public Animal getSelectedAnimal(int pos){
        ArrayList<Animal> animalsValue = this.animals.getValue();
        return animalsValue.get(pos);
    }
}
