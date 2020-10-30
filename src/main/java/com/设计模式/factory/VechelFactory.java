package com.设计模式.factory;

/**
 * 简单工厂可扩展性不好
 */
public class VechelFactory {

    public Car createCar(){
        return new Car();
    }

    public Plane createPlane(){
        return new Plane();
    }
}
