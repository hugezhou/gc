package com.设计模式.strategy;

public class Dog implements Compatable<Dog> {

    private int food;


    public Dog(int food) {
        this.food = food;
    }

    @Override
    public int CompateTo(Dog god) {

        if (this.food < god.food) return -1;
        else if (this.food > god.food) return 1;
        else return 0;
    }


    @Override
    public String toString() {
        return "Dog{" +
                "food=" + food +
                '}';
    }
}
