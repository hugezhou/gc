package com.设计模式.strategy;


import java.util.*;

public class main {


    public static void main(String[] args){

        Cat[] a = {new Cat(5,5),new Cat(4,4),new Cat(3,3)};


//        Dog[] factory = { new Dog(4), new Dog(2),new Dog(3)};

//
//        Sorter<Cat> sorter = new Sorter();
//
//        sorter.sort(a,new CatWeightComparator());
//
//        System.out.println(Arrays.toString(a));



        List<Map<String,String>> maps = new LinkedList<>();


        Map<String,String> map3  = new HashMap<>();

        map3.put("num","3");

        Map<String,String> map2  = new HashMap<>();

        map2.put("num","2");

        Map<String,String> map1  = new HashMap<>();

        map1.put("num","1");

        Map<String,String> map0  = new HashMap<>();

        map0.put("num","0");


        maps.add(map3);
        maps.add(map2);
        maps.add(map1);
        maps.add(map0);

        System.out.println(maps);


        for (int i = 0; i < maps.size() - 1; i++){

            int minPos = i;

            for (int j = i + 1; j < maps.size();j++){

                minPos = compateTo(maps.get(j),maps.get(minPos)) == -1 ? j : minPos;
            }

            swap(maps,i,minPos);


        }

        System.out.println(maps);




    }

    public static void swap(List<Map<String,String>> arr, int i , int j){
        Map<String,String> temp = arr.get(i);
        arr.set(i,arr.get(j));
        arr.set(j,temp);
    }

    public static int compateTo(Map<String,String> o1, Map<String,String> o2) {
        Integer i1 = Integer.valueOf(o1.get("num"));
        Integer i2 = Integer.valueOf(o2.get("num"));
        if (i1 < i2) return -1;
        else if (i1 > i2) return 1;
        else return 0;
    }
}
