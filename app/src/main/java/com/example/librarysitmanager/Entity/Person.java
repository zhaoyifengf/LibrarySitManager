package com.example.librarysitmanager.Entity;

public class Person {
    private String name;
    private String say;

    public Person(String name, String say) {
        this.name = name;
        this.say = say;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSay(String say) {
        this.say = say;
    }

    public String getSay() {
        return say;
    }
}
