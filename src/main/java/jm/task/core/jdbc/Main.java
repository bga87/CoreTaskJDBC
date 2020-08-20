package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        // локальный класс, инкапсулирующий данные о пользователе
        class UserData {
            private String name;
            private String lastName;
            private byte age;

            public UserData(String name, String lastName, byte age) {
                this.name = name;
                this.lastName = lastName;
                this.age = age;
            }
        }

        UserData[] inputData = {
                new UserData("Иван", "Иванов", (byte) 25),
                new UserData("Петр", "Петров", (byte) 41),
                new UserData("Сидор", "Сидоров", (byte) 54),
                new UserData("Николай", "Николаев", (byte) 15)
        };
        UserService userService = new UserServiceImpl();

        System.out.println("Создание таблицы пользователей");
        userService.createUsersTable();

        System.out.println("\nДобавление пользователей в базу данных:");
        for (UserData uData : inputData) {
            userService.saveUser(uData.name, uData.lastName, uData.age);
            System.out.printf("User с именем – '%s %s' добавлен в базу данных%n", uData.name, uData.lastName);
        }

        System.out.println("\nПолучение всех пользователей из базы данных:");
        for (User user : userService.getAllUsers()) {
            System.out.println(user);
        }

        System.out.println("\nОчистка таблицы пользователей");
        userService.cleanUsersTable();

        System.out.println("\nУдаление таблицы пользователей");
        userService.dropUsersTable();
    }
}
