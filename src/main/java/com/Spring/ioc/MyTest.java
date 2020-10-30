package com.Spring.ioc;

import java.util.stream.Stream;

public class MyTest {

    public static void main(String[] args){
        UserController userController = new UserController();

        UserService userService = new UserService();
        Class<? extends UserController> clazz =  userController.getClass();

        Stream.of(clazz.getDeclaredFields()).forEach(field -> {

            String name = field.getName();
            Autowired autowired = field.getAnnotation(Autowired.class);


            if (autowired != null){
                field.setAccessible(true);

                Class<?> type = field.getType();

                try {
                    Object o = type.newInstance();
                    field.set(userController,o);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });

        System.out.println(userController.getUserService());


        System.out.println(userService);
    }
}
