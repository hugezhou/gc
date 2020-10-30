package com.设计模式.factory.abstractFactory;


public class Main {

    public static void main(String[] args){


        AbatractFactory factory = new ModenFactory();

        Vechel vechel = factory.createVechel();
        vechel.go();
        Weapen weapen = factory.createWeapen();
        weapen.shoot();
        Food food = factory.createFood();
        food.printName();
    }
}
