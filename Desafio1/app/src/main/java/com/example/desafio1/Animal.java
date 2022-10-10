package com.example.desafio1;

public class Animal {
    private String name,owner;
    private int age,id;

    public Animal(String name, String owner, int age, int id){
        this.age=age;
        this.id=id;
        this.name=name;
        this.owner=owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
