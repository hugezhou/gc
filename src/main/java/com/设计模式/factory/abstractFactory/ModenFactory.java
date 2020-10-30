package com.设计模式.factory.abstractFactory;

public class ModenFactory extends AbatractFactory {
    @Override
    Food createFood() {
        return new Bread();
    }

    @Override
    Vechel createVechel() {
        return new Car();
    }

    @Override
    Weapen createWeapen() {
        return new AK47();
    }
}
