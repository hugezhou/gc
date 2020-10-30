package com.设计模式.strategy;

public class Cat implements Compatable<Cat> {

    int weight;

    int heigh;


    public Cat(int weight, int heigh) {
        this.weight = weight;
        this.heigh = heigh;
    }

    @Override
    public String toString() {
        return "Cat{" +
                "weight=" + weight +
                ", heigh=" + heigh +
                '}';
    }

    @Override
    public int CompateTo(Cat cat) {


        if (this.weight < cat.weight) return -1;
        else if (this.weight > cat.weight) return 1;
        else return 0;

    }
}
